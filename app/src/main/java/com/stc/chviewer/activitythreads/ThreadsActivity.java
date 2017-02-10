package com.stc.chviewer.activitythreads;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.stc.chviewer.PlayerActivity;
import com.stc.chviewer.R;
import com.stc.chviewer.activitythreads.model.PlayableItem;
import com.stc.chviewer.activitythreads.model.ThreadItemsPlaylist;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static com.stc.chviewer.Constants.ACTION_PLAY_LIST;
import static com.stc.chviewer.Constants.BOARD_TITLE_EXTRA;
import static com.stc.chviewer.Constants.COLUMNS_LANDSCAPE;
import static com.stc.chviewer.Constants.COLUMNS_PORTRAIT;
import static com.stc.chviewer.Constants.ITEMS_LIST_EXTRA;
import static com.stc.chviewer.Constants.ITEM_INDEX_EXTRA;
import static com.stc.chviewer.Constants.THREAD_EXTRA;

public class ThreadsActivity extends AppCompatActivity implements ThreadsContract.View, PlayableItemFragment.OnListFragmentInteractionListener, PlaylistItemFragment.OnListFragmentInteractionListener {
    private static final String TAG = "ThreadSelectActivity";
    private static final int REQUEST_PLAY = 244;
    private static final String PLAYABLE_FRAGMENT_TAG = "PLAYABLE_FRAGMENT_TAG";
    private static final String PLAYLIST_FRAGMENT_TAG = "PLAYLIST_FRAGMENT_TAG";

    ProgressBar progressBar;

    Button buttonRetry;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ThreadsContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threads);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(GONE);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        buttonRetry=(Button) findViewById(R.id.buttonRetry);
        buttonRetry.setVisibility(GONE);
        buttonRetry.setOnClickListener(v -> start());
        start();
    }
    private void start(){
        new ThreadsPresenter(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public String getBoard() {
        if(getIntent()==null || !getIntent().getExtras().containsKey(BOARD_TITLE_EXTRA)) return null;
        return getIntent().getStringExtra(BOARD_TITLE_EXTRA);
    }

    @Override
    public void showBaseInfo(List<ThreadItemsPlaylist> threads, boolean sawError) {
        if (sawError) {
            showError("ERROR");
            return;
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        PlaylistItemFragment playlistItemFragment = PlaylistItemFragment.newInstance((ArrayList<ThreadItemsPlaylist>) threads);
        transaction.replace(R.id.container, playlistItemFragment, PLAYLIST_FRAGMENT_TAG);
        transaction.commit();


    }

    @Override
    public void startPlayer(String board, ThreadItemsPlaylist threadItemsPlaylist, PlayableItem[] playableItems, int itemIndex) {
        Intent intent=new Intent(this, PlayerActivity.class);
        intent.setAction(ACTION_PLAY_LIST);
        intent.putExtra(ITEMS_LIST_EXTRA, playableItems);
        intent.putExtra(THREAD_EXTRA,threadItemsPlaylist );
        intent.putExtra(BOARD_TITLE_EXTRA, board);
        intent.putExtra(ITEM_INDEX_EXTRA, itemIndex);
        startActivityForResult(intent, REQUEST_PLAY);
    }

    @Override
    public void showError(String errorText) {
        Log.e(TAG, "showError: "+errorText );
        Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadThreadContent(ThreadItemsPlaylist thread, PlayableItem[] playableItems) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        PlayableItemFragment playableItemFragment = PlayableItemFragment.newInstance(
                isPortrait() ? COLUMNS_PORTRAIT : COLUMNS_LANDSCAPE,
                playableItems,
                0
        );
        transaction.replace(R.id.container, playableItemFragment, PLAYABLE_FRAGMENT_TAG);
        transaction.commit();
    }

    @Override
    public void setPresenter(ThreadsContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void onListFragmentInteraction(PlayableItem item) {
        presenter.requestStartPlayer(item.getThreadId(), item.getItemIndex());
    }

    @Override
    public void onListFragmentInteraction(ThreadItemsPlaylist item) {
        presenter.requestThreadContent(item.getThreadId());

    }
    private boolean isPortrait() {
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        return (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180);
    }
}
