/*
package com.stc.chviewer.choosesamples;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.stc.chviewer.R;
import java.util.List;

import static android.view.View.GONE;

public class MySampleChooserActivity extends Activity implements MySampleChooserContract.View {


	private static final String TAG = "SampleChooserActivity";
	ProgressBar progressBar;
	Button buttonRetry;
	private MySampleChooserContract.Presenter presenter;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sample_chooser_activity);
		progressBar = (ProgressBar) findViewById(R.id.progress);
		progressBar.setVisibility(GONE);
		progressBar.setProgress(0);
		progressBar.setMax(100);
		buttonRetry=(Button) findViewById(R.id.buttonRetry);
		buttonRetry.setVisibility(View.GONE);
		buttonRetry.setOnClickListener(v -> loadData());
		loadData();
    }

	private void loadData() {
		buttonRetry.setVisibility(View.GONE);
		MySampleChooserPresenter presenter=new MySampleChooserPresenter(this);
		presenter.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public void setPresenter(MySampleChooserContract.Presenter presenter) {
		this.presenter=presenter;
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
	public void showThreads(final List<MySampleChooserPresenter.SampleGroup> groups, boolean sawError) {
		if (sawError || groups==null || groups.size()==0) {
			Toast.makeText(getApplicationContext(), R.string.sample_list_load_error, Toast.LENGTH_LONG)
					.show();
			buttonRetry.setVisibility(View.VISIBLE);

			return;
		}
		ExpandableListView sampleList = (ExpandableListView) findViewById(R.id.sample_list);
		sampleList.setAdapter(new SampleAdapter(this, groups));
		sampleList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View view, int groupPosition,
			                            int childPosition, long id) {
				onThreadSelected(groups.get(groupPosition),
						groups.get(groupPosition).samples.get(childPosition));
				return true;
			}
		});
	}

	@Override
	public void onThreadSelected(MySampleChooserPresenter.SampleGroup sampleGroup,
								 MySampleChooserPresenter.Sample sample) {

		startActivity(sample.buildIntent(this));

	}
}
*/
