/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stc.chviewer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.DecoderInitializationException;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.DebugTextViewHelper;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Util;
import com.stc.chviewer.activitythreads.model.PlayableItem;
import com.stc.chviewer.activitythreads.model.ThreadItemsPlaylist;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_MEDIA_NEXT;
import static com.stc.chviewer.Constants.ACTION_PLAY_LIST;
import static com.stc.chviewer.Constants.BOARD_TITLE_EXTRA;
import static com.stc.chviewer.Constants.ITEMS_LIST_EXTRA;
import static com.stc.chviewer.Constants.ITEM_INDEX_EXTRA;
import static com.stc.chviewer.Constants.THREAD_EXTRA;

public class PlayerActivity extends Activity implements OnClickListener, ExoPlayer.EventListener,
    PlaybackControlView.VisibilityListener {

  public static final String TAG = "PlayerActivity";



  private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
  private static final CookieManager DEFAULT_COOKIE_MANAGER;
  static {
    DEFAULT_COOKIE_MANAGER = new CookieManager();
    DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
  }

  private Handler mainHandler;
  private Timeline.Window window;
  private EventLogger eventLogger;
  private SimpleExoPlayerView simpleExoPlayerView;
  private LinearLayout debugRootView;
  private TextView debugTextView;
  private Button retryButton;

  private DataSource.Factory mediaDataSourceFactory;
  private SimpleExoPlayer player;
  private DefaultTrackSelector trackSelector;
  private TrackSelectionHelper trackSelectionHelper;
  private DebugTextViewHelper debugViewHelper;
  private boolean playerNeedsSource;

  private boolean shouldAutoPlay;
  private boolean isTimelineStatic;
  private int playerWindow;
  private long playerPosition;
	MediaSource[] mediaSources;
  PlayableItem[] playableItems;
  ThreadItemsPlaylist thread;
  String board;
  int requestedItemIndex;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    shouldAutoPlay = true;
    mediaDataSourceFactory = buildDataSourceFactory(true);
    mainHandler = new Handler();
    window = new Timeline.Window();
    if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
      CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
    }

    setContentView(R.layout.player_activity);
    View rootView = findViewById(R.id.root);
    rootView.setOnClickListener(this);
    debugRootView = (LinearLayout) findViewById(R.id.controls_root);
    debugTextView = (TextView) findViewById(R.id.debug_text_view);
    retryButton = (Button) findViewById(R.id.retry_button);
	  debugTextView.setOnClickListener(this);
    retryButton.setOnClickListener(this);

    simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
    simpleExoPlayerView.setControllerVisibilityListener(this);
    simpleExoPlayerView.requestFocus();
	  simpleExoPlayerView.setControllerShowTimeoutMs(1000);
  }

  @Override
  public void onNewIntent(Intent intent) {
    releasePlayer();
    isTimelineStatic = false;
    setIntent(intent);
  }

  @Override
  public void onStart() {
    super.onStart();
    if (Util.SDK_INT > 23) {
      initPlayerFromIntent();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    if ((Util.SDK_INT <= 23 || player == null)) {
      initPlayerFromIntent();
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (Util.SDK_INT <= 23) {
      releasePlayer();
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (Util.SDK_INT > 23) {
      releasePlayer();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      initPlayerFromIntent();
    } else {
      showToast(R.string.storage_permission_denied);
      finish();
    }
  }

  @Override
  public boolean dispatchKeyEvent(KeyEvent event) {
    // Show the controls on any key event.
    simpleExoPlayerView.showController();
    // If the event was not handled then see if the player view can handle it as a media key event.
    return super.dispatchKeyEvent(event) || simpleExoPlayerView.dispatchMediaKeyEvent(event);
  }

  // OnClickListener methods

  @Override
  public void onClick(View view) {
    if (view == retryButton) {
      initPlayerFromIntent();
    } else if (view == debugTextView) {
	    Log.d(TAG, "onClick: next pressed");
	    KeyEvent eventNext = new KeyEvent(ACTION_DOWN, KEYCODE_MEDIA_NEXT);
	    simpleExoPlayerView.dispatchMediaKeyEvent(eventNext);
    } else if (view.getParent() == debugRootView) {
      MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
      if (mappedTrackInfo != null) {
        trackSelectionHelper.showSelectionDialog(this, ((Button) view).getText(),
            trackSelector.getCurrentMappedTrackInfo(), (int) view.getTag());
      }
    }
  }

  // PlaybackControlView.VisibilityListener implementation

  @Override
  public void onVisibilityChange(int visibility) {
	  Log.d(TAG, "onVisibilityChange: "+visibility);
	  debugRootView.setVisibility(visibility);
  }

  // Internal methods
  private void initPlayerFromIntent() {
    Intent intent = getIntent();
    String action = intent.getAction();
    if (ACTION_PLAY_LIST.equals(action)) {
      playableItems = (PlayableItem[]) intent.getParcelableArrayExtra(ITEMS_LIST_EXTRA);
      thread = intent.getParcelableExtra(THREAD_EXTRA);
      board = intent.getStringExtra(BOARD_TITLE_EXTRA);
      int index = intent.getIntExtra(ITEM_INDEX_EXTRA, 0);
      initializePlayer(playableItems,index, false);


    } else {
      showToast(getString(R.string.unexpected_intent_action, action));
      return;
    }
  }

  private void initializePlayer(PlayableItem[] items, int index,  boolean playerNeedsSource ) {
    if (player == null) {
      TrackSelection.Factory videoTrackSelectionFactory =
          new AdaptiveVideoTrackSelection.Factory(BANDWIDTH_METER);
      trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
      trackSelectionHelper = new TrackSelectionHelper(trackSelector, videoTrackSelectionFactory);
      player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, new DefaultLoadControl());
      player.addListener(this);
      eventLogger = new EventLogger(trackSelector);
      player.addListener(eventLogger);
      player.setAudioDebugListener(eventLogger);
      player.setVideoDebugListener(eventLogger);
      player.setId3Output(eventLogger);

      simpleExoPlayerView.setPlayer(player);
      if (isTimelineStatic) {
        if (playerPosition == C.TIME_UNSET) {
          player.seekToDefaultPosition(playerWindow);
        } else {
          player.seekTo(playerWindow, playerPosition);
        }
      }
      player.setPlayWhenReady(shouldAutoPlay);
      debugViewHelper = new DebugTextViewHelper(player, debugTextView);
      debugViewHelper.start();
      playerNeedsSource = true;
    }
    if (playerNeedsSource) {
      mediaSources = new MediaSource[items.length];
      for (int i = index; i < items.length; i++) {
        mediaSources[i] = buildMediaSource(Uri.parse(items[i].getWebmUrl()));
      }
      MediaSource mediaSource = mediaSources.length == 1 ? mediaSources[0]
          : new ConcatenatingMediaSource(mediaSources);

      player.prepare(mediaSource, !isTimelineStatic, !isTimelineStatic);
      updateButtonVisibilities();
    }
  }

  private MediaSource buildMediaSource(Uri uri) {
    return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
            mainHandler, eventLogger);
  }
  private void releasePlayer() {
    if (player != null) {
      debugViewHelper.stop();
      debugViewHelper = null;
      shouldAutoPlay = player.getPlayWhenReady();
      playerWindow = player.getCurrentWindowIndex();
      playerPosition = C.TIME_UNSET;
      Timeline timeline = player.getCurrentTimeline();
      if (!timeline.isEmpty() && timeline.getWindow(playerWindow, window).isSeekable) {
        playerPosition = player.getCurrentPosition();
      }
      player.release();
      player = null;
      trackSelector = null;
      trackSelectionHelper = null;
      eventLogger = null;
    }
  }
  private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
    return ((DemoApplication) getApplication())
        .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
  }

  @Override
  public void onLoadingChanged(boolean isLoading) {
    // Do nothing.
  }

  @Override
  public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
    if (playbackState == ExoPlayer.STATE_ENDED) {
      showControls();
    }
    updateButtonVisibilities();
  }

  @Override
  public void onPositionDiscontinuity() {
    Log.d(TAG, "onPositionDiscontinuity: ");
    Toast.makeText(this, "onPositionDiscontinuity", Toast.LENGTH_SHORT).show();
    setResult(RESULT_OK );
    finish();
  }




  @Override
  public void onTimelineChanged(Timeline timeline, Object manifest) {
    isTimelineStatic = !timeline.isEmpty()
        && !timeline.getWindow(timeline.getWindowCount() - 1, window).isDynamic;
  }

  @Override
  public void onPlayerError(ExoPlaybackException e) {
    String errorString = null;
    if (e.type == ExoPlaybackException.TYPE_RENDERER) {
      Exception cause = e.getRendererException();
      if (cause instanceof DecoderInitializationException) {
        // Special case for decoder initialization failures.
        DecoderInitializationException decoderInitializationException =
            (DecoderInitializationException) cause;
        if (decoderInitializationException.decoderName == null) {
          if (decoderInitializationException.getCause() instanceof DecoderQueryException) {
            errorString = getString(R.string.error_querying_decoders);
          } else if (decoderInitializationException.secureDecoderRequired) {
            errorString = getString(R.string.error_no_secure_decoder,
                decoderInitializationException.mimeType);
          } else {
            errorString = getString(R.string.error_no_decoder,
                decoderInitializationException.mimeType);
          }
        } else {
          errorString = getString(R.string.error_instantiating_decoder,
              decoderInitializationException.decoderName);
        }
      }
    }
    if (errorString != null) {
      showToast(errorString);
    }
    playerNeedsSource = true;
    updateButtonVisibilities();
    showControls();
  }

  @Override
  public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    updateButtonVisibilities();
    MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
    if (mappedTrackInfo != null) {

      if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_VIDEO)
          == MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
        showToast(R.string.error_unsupported_video);
      }
      if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_AUDIO)
          == MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
        showToast(R.string.error_unsupported_audio);
      }
    }
  }

  // User controls

  private void updateButtonVisibilities() {
    debugRootView.removeAllViews();
    retryButton.setVisibility(playerNeedsSource ? View.VISIBLE : View.GONE);
    debugRootView.addView(retryButton);

    if (player == null) {
      return;
    }

    MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
    if (mappedTrackInfo == null) {
      return;
    }

    for (int i = 0; i < mappedTrackInfo.length; i++) {
      TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
      if (trackGroups.length != 0) {
        Button button = new Button(this);
        int label;
        switch (player.getRendererType(i)) {
          case C.TRACK_TYPE_AUDIO:
            label = R.string.audio;
            break;
          case C.TRACK_TYPE_VIDEO:
            label = R.string.video;
            break;
          case C.TRACK_TYPE_TEXT:
            label = R.string.text;
            break;
          default:
            continue;
        }
        button.setText(label);
        button.setTag(i);
        button.setOnClickListener(this);
        debugRootView.addView(button, debugRootView.getChildCount() - 1);
      }
    }
  }

  private void showControls() {
    debugRootView.setVisibility(View.VISIBLE);
  }

  private void showToast(int messageId) {
    showToast(getString(messageId));
  }

  private void showToast(String message) {
    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  }

}
