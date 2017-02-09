package com.stc.chviewer.activitythreads;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.stc.chviewer.PlayerActivity;
import com.stc.chviewer.R;
import com.stc.chviewer.activitythreads.model.PlayableItem;
import com.stc.chviewer.activitythreads.model.ThreadItemsPlaylist;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static com.stc.chviewer.Constants.EXTRA_BOARD_NAME;
import static com.stc.chviewer.Constants.EXTRA_THREAD_ID;
import static com.stc.chviewer.Constants.EXTRA_THREAD_NAME;
import static com.stc.chviewer.PlayerActivity.ACTION_VIEW_LIST;
import static com.stc.chviewer.PlayerActivity.EXTENSION_LIST_EXTRA;
import static com.stc.chviewer.PlayerActivity.URI_LIST_EXTRA;

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
        if(getIntent()==null || !getIntent().getExtras().containsKey(EXTRA_BOARD_NAME)) return null;
        return getIntent().getStringExtra(EXTRA_BOARD_NAME);
    }

    @Override
    public void showBaseInfo(List<ThreadItemsPlaylist> threads, boolean sawError) {
        if (sawError) {
            showError("ERROR");
            return;
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        PlaylistItemFragment playlistItemFragment = new PlaylistItemFragment(this,threads);
        transaction.replace(R.id.container, playlistItemFragment, PLAYLIST_FRAGMENT_TAG);
        transaction.commit();


    }

    @Override
    public void startPlayer(String board, String threadId, String threadText, String[] uris,String[] extentions) {
        Intent intent=new Intent(this, PlayerActivity.class);
        intent.setAction(ACTION_VIEW_LIST);
        intent.putExtra(URI_LIST_EXTRA, uris);
        intent.putExtra(EXTENSION_LIST_EXTRA,extentions );
        intent.putExtra(EXTRA_BOARD_NAME, board);
        intent.putExtra(EXTRA_THREAD_ID, threadId);
        intent.putExtra(EXTRA_THREAD_NAME, threadText);
        startActivityForResult(intent, REQUEST_PLAY);

    }

    @Override
    public void showError(String errorText) {
        Log.e(TAG, "showError: "+errorText );
        Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadThreadContent(ThreadItemsPlaylist thread, List<PlayableItem> items) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        PlayableItemFragment playableItemFragment = new PlayableItemFragment(this,thread.getThreadId(), items);
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
}
