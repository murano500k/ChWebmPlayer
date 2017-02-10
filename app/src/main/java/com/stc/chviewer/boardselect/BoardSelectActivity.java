package com.stc.chviewer.boardselect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.stc.chviewer.R;
import com.stc.chviewer.activitythreads.ThreadsActivity;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.stc.chviewer.Constants.ACTION_SHOW_THREADS;
import static com.stc.chviewer.Constants.BOARD_TITLE_EXTRA;
import static com.stc.chviewer.Constants.KEY_BOARD_NAME;
import static com.stc.chviewer.Constants.SETTINGS;
import static com.stc.chviewer.Constants.SETTINGS_KEY_SHOW_HELP;

public class BoardSelectActivity extends AppCompatActivity {
    private static final String TAG = "BoardSelectActivity";
    SearchView searchView;

    @BindView(R.id.board_list)
    ListView listView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    java.util.List<HashMap<java.lang.String, String>> data;

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
        data=new ArrayList<>();
        for(String s: getResources().getStringArray(R.array.board_codes)){
            HashMap<java.lang.String, String> map =new HashMap<>();
            map.put(KEY_BOARD_NAME, s);
            data.add(map);
        }
        String fromArray[]={KEY_BOARD_NAME};
        int to[]={android.R.id.text1};
        ListAdapter adapter=new SimpleAdapter( this,data, android.R.layout.simple_list_item_1, fromArray, to);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boardSelected(data.get(position).get(KEY_BOARD_NAME));
            }
        });
        listView.setAdapter(adapter);
    }
    private void boardSelected(String boardName){
        Log.d(TAG, "boardSelected: "+ boardName);
        Intent intent=new Intent(this, ThreadsActivity.class);
        intent.setAction(ACTION_SHOW_THREADS);
        intent.putExtra(BOARD_TITLE_EXTRA, boardName);
        startActivity(intent);

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


}

