package com.stc.chviewer.activitythreads;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stc.chviewer.R;
import com.stc.chviewer.activitythreads.model.ThreadItemsPlaylist;

import java.util.List;

public class MyPlaylistRecyclerViewAdapter extends RecyclerView.Adapter<MyPlaylistRecyclerViewAdapter.ViewHolder> {

    private final List<ThreadItemsPlaylist> mValues;
    private final PlaylistItemFragment.OnListFragmentInteractionListener mListener;

    public MyPlaylistRecyclerViewAdapter(List<ThreadItemsPlaylist> items, PlaylistItemFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_threaditemsplaylist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        String text = mValues.get(position).getThreadTitle();
        if(text!=null && text.contains("playable:")) holder.mView.setBackgroundColor(holder.mView.getResources().getColor(R.color.colorHighlighted));
        else holder.mView.setBackgroundColor(holder.mView.getResources().getColor(R.color.colorEmpty));
        holder.mTextView.setText(mValues.get(position).getThreadTitle());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTextView;
        public ThreadItemsPlaylist mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTextView = (TextView) view.findViewById(R.id.text);
        }
    }
}
