package com.stc.chviewer.activitythreads;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
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
import static com.stc.chviewer.Constants.SETTINGS;
import static com.stc.chviewer.Constants.SETTINGS_KEY_SHOW_HELP;
import static com.stc.chviewer.Constants.THREAD_EXTRA;

public class ThreadsActivity extends AppCompatActivity implements ThreadsContract.View, PlayableItemFragment.OnListFragmentInteractionListener, PlaylistItemFragment.OnListFragmentInteractionListener {
    private static final String TAG = "ThreadSelectActivity";
    private static final int REQUEST_PLAY = 244;
    private static final String PLAYABLE_FRAGMENT_TAG = "PLAYABLE_FRAGMENT_TAG";
    private static final String PLAYLIST_FRAGMENT_TAG = "PLAYLIST_FRAGMENT_TAG";
    private static final int MAX_LENGTH = 1000;

    ProgressBar progressBar;

    Button buttonRetry;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ThreadsContract.Presenter presenter;
    SearchView searchView;

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

    private void showSearch() {
        if(searchView==null) searchView=new SearchView(this);
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.requestSearchThreadContent(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(Gravity.END);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(searchView.isShown())toolbar.addView(searchView, layoutParams);

            }
        });
    }

    @Override
    public void startPlayer(String board, ThreadItemsPlaylist threadItemsPlaylist, PlayableItem[] playableItems, int itemIndex) {
        Intent intent=new Intent(this, PlayerActivity.class);

        if(playableItems.length>MAX_LENGTH){
            Log.w(TAG, "startPlayer: max length exceeded");
            PlayableItem[] shorterList =new PlayableItem[MAX_LENGTH];
            for (int i = 0; i < MAX_LENGTH; i++) {
                shorterList[i]=playableItems[i];
                intent.putExtra(ITEMS_LIST_EXTRA, shorterList);
            }
        }else {
            intent.putExtra(ITEMS_LIST_EXTRA, playableItems);
        }
        intent.setAction(ACTION_PLAY_LIST);
        intent.putExtra(THREAD_EXTRA,threadItemsPlaylist );
        intent.putExtra(BOARD_TITLE_EXTRA, board);
        intent.putExtra(ITEM_INDEX_EXTRA, itemIndex);
        startActivityForResult(intent, REQUEST_PLAY);
    }

    @Override
    public void showBaseInfo(List<ThreadItemsPlaylist> threads, boolean sawError) {
        if (sawError) {
            showError("ERROR");
            return;
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        PlaylistItemFragment playlistItemFragment = PlaylistItemFragment.newInstance((ArrayList<ThreadItemsPlaylist>) threads);
        transaction.replace(R.id.container, playlistItemFragment, PLAYLIST_FRAGMENT_TAG).addToBackStack(null);
        transaction.commit();
        showSearch();
        updateTitle(getBoard());
    }

    @Override
    public void loadThreadContent(ThreadItemsPlaylist thread, PlayableItem[] playableItems) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        PlayableItemFragment playableItemFragment = PlayableItemFragment.newInstance(
                isPortrait() ? COLUMNS_PORTRAIT : COLUMNS_LANDSCAPE,
                playableItems,
                0
        );
        transaction.replace(R.id.container, playableItemFragment, PLAYABLE_FRAGMENT_TAG).addToBackStack(null);
        transaction.commit();
        toolbar.removeView(searchView);
        updateTitle(thread.getThreadTitle());
        showHelpToastIfNeeded();

    }


    private void showHelpToastIfNeeded(){
        if(shouldShowHelpToast()){
            Toast.makeText(this, "Select item to start playback. All threads will be included in playlist automatically", Toast.LENGTH_SHORT).show();
            getSharedPreferences(SETTINGS,MODE_PRIVATE ).edit().putBoolean(SETTINGS_KEY_SHOW_HELP, true).apply();
        }
    }
    private boolean shouldShowHelpToast(){
        return getSharedPreferences(SETTINGS,MODE_PRIVATE ).getBoolean(SETTINGS_KEY_SHOW_HELP, false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: entrycount="+getFragmentManager().getBackStackEntryCount());
        if(getFragmentManager().getBackStackEntryCount()<1 || getFragmentManager().findFragmentByTag(PLAYABLE_FRAGMENT_TAG)==null) {
            updateTitle(getBoard());
            showSearch();
        }
    }

    private void updateTitle(String title){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setTitle(title);
            }
        });
    }

    @Override
    public void showError(String errorText) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "showError: "+errorText );
                Toast.makeText(ThreadsActivity.this, errorText, Toast.LENGTH_SHORT).show();
            }
        });
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
        presenter.requestGetThreadContent(item.getThreadId());

    }
    private boolean isPortrait() {
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        return (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180);
    }
}
