/*
 * Copyright 2016, The Android Open Source Project
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
 *//*


package com.stc.chviewer.threadselect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.stc.chviewer.PlayerActivity;
import com.stc.chviewer.R;
import com.stc.chviewer.retro.model.ChThread;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static com.stc.chviewer.Constants.BOARD_TITLE_EXTRA;
import static com.stc.chviewer.Constants.EXTRA_THREAD_ID;
import static com.stc.chviewer.PlayerActivity.ACTION_VIEW_LIST;
import static com.stc.chviewer.PlayerActivity.EXTENSION_LIST_EXTRA;
import static com.stc.chviewer.PlayerActivity.URI_LIST_EXTRA;

*/
/**
 * Show statistics for tasks.
 *//*

public class ThreadSelectActivity extends AppCompatActivity implements ThreadSelectContract.View {


	private static final String TAG = "ThreadSelectActivity";
	private static final int REQUEST_PLAY = 244;
	ProgressBar progressBar;
	Button buttonRetry;
	@BindView(R.id.threads_list)
	RecyclerView listThreads;
	ChThreadAdapter adapter;


	@BindView(R.id.toolbar)
	Toolbar toolbar;

	private ThreadSelectContract.Presenter presenter;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_select);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
		progressBar = (ProgressBar) findViewById(R.id.progress);
		progressBar.setVisibility(GONE);
		progressBar.setProgress(0);
		progressBar.setMax(100);
		buttonRetry=(Button) findViewById(R.id.buttonRetry);
		buttonRetry.setVisibility(View.GONE);
		buttonRetry.setOnClickListener(v -> loadData());
		loadData();
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==REQUEST_PLAY && resultCode==RESULT_OK){

		}
	}

	private void loadData() {
		buttonRetry.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		new ThreadSelectPresenter(this);
		presenter.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}



	@Override
	public void updateProgress(int percent) {
		if(percent<1 || percent>99) progressBar.setVisibility(GONE);
		else {
			progressBar.setProgress(percent);
			progressBar.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void showThreads(List<ChThread> threads, boolean sawError) {
		Log.d(TAG, "showThreads: error="+sawError);
		progressBar.setVisibility(GONE);
		if(sawError) {
			Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
			buttonRetry.setVisibility(View.VISIBLE);
		}else{
			adapter=new ChThreadAdapter(threads, new ChThreadAdapter.OnItemClickListener() {
				@Override
				public void onItemClick(long threadId) {
					onThreadSelected(threadId);
				}
			});
			RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
			listThreads.setLayoutManager(mLayoutManager);
			listThreads.setItemAnimator(new DefaultItemAnimator());
			listThreads.setAdapter(adapter);
		}
	}

	@Override
	public void onThreadSelected(long threadNum) {
		Log.d(TAG, "ThreadSelected: "+ threadNum);
		Toast.makeText(this, "ThreadSelected: "+ threadNum, Toast.LENGTH_SHORT).show();
		this.threadId=threadNum;
		presenter.getUris(threadNum);
	}

	@Override
	public void showError(String text) {
		Log.e(TAG, "showError: "+text );

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(ThreadSelectActivity.this, "ERROR: "+text, Toast.LENGTH_SHORT).show();
			}
		});	}

	@Override
	public void startPlayer(String boardName, String threadTitle, String[] uris, String[] extentions) {
		Intent intent=new Intent(this, PlayerActivity.class);
		intent.setAction(ACTION_VIEW_LIST);
		intent.putExtra(URI_LIST_EXTRA, uris);
		intent.putExtra(EXTENSION_LIST_EXTRA,extentions );
		intent.putExtra(BOARD_TITLE_EXTRA, boardName);
		intent.putExtra(EXTRA_THREAD_ID, threadTitle);
		startActivityForResult(intent, REQUEST_PLAY);
	}


	@Override
	public String getBoard() {
		Intent intent=getIntent();
		if(intent==null)return null;
		return intent.getStringExtra(BOARD_TITLE_EXTRA);
	}


	@Override
	public void setPresenter(ThreadSelectContract.Presenter presenter) {
		this.presenter=presenter;
	}

}
*/
