/*
package com.stc.chviewer.choosesamples;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;


public final class SampleAdapter extends BaseExpandableListAdapter {

	private final Context context;
	private final List<MySampleChooserPresenter.SampleGroup> sampleGroups;

	public SampleAdapter(Context context, List<MySampleChooserPresenter.SampleGroup> sampleGroups) {
		this.context = context;
		this.sampleGroups = sampleGroups;
	}

	@Override
	public MySampleChooserPresenter.Sample getChild(int groupPosition, int childPosition) {
		return getGroup(groupPosition).samples.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
	                         View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent,
					false);
		}
		((TextView) view).setText(getChild(groupPosition, childPosition).name);
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return getGroup(groupPosition).samples.size();
	}

	@Override
	public MySampleChooserPresenter.SampleGroup getGroup(int groupPosition) {
		return sampleGroups.get(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
	                         ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(android.R.layout.simple_expandable_list_item_1,
					parent, false);
		}
		((TextView) view).setText(getGroup(groupPosition).title);
		return view;
	}

	@Override
	public int getGroupCount() {
		return sampleGroups.size();
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}



*/
