package com.stc.chviewer.boardselect;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.stc.chviewer.R;
import com.stc.chviewer.activitythreads.ThreadsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.stc.chviewer.Constants.ACTION_SHOW_THREADS;
import static com.stc.chviewer.Constants.BOARD_TITLE_EXTRA;

public class BoardSelectActivity extends AppCompatActivity implements BoardListFragment.OnListFragmentInteractionListener{
    private static final String TAG = "BoardSelectActivity";
    private static final String FRAGMENT_BOARD_LIST = "FRAGMENT_BOARD_LIST";
    SearchView searchView;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    String[] data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_select);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        searchView=new SearchView(this);
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                for(String s: getResources().getStringArray(R.array.board_codes)){
                    if(s.equals(query)){
                        boardSelected(s);
                        return true;
                    }
                }
                Toast.makeText(BoardSelectActivity.this, "Board not found", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(Gravity.END);
        toolbar.addView(searchView, layoutParams);
        showBoardsList();

    }
    private void showBoardsList(){
        Log.d(TAG, "showBoardsList:");
        data= getResources().getStringArray(R.array.board_codes);
        BoardListFragment boardListFragment = BoardListFragment.newInstance(data);

        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        transaction.add(R.id.container, boardListFragment, FRAGMENT_BOARD_LIST);

    }
    private void boardSelected(String boardName){
        Log.d(TAG, "boardSelected: "+ boardName);
        Intent intent=new Intent(this, ThreadsActivity.class);
        intent.setAction(ACTION_SHOW_THREADS);
        intent.putExtra(BOARD_TITLE_EXTRA, boardName);
        startActivity(intent);

    }


    @Override
    public void onListFragmentInteraction(String board) {
        boardSelected(board);
    }
}
