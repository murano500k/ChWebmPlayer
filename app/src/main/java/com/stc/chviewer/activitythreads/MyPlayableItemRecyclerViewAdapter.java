package com.stc.chviewer.activitythreads;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.stc.chviewer.PabloPicasso;
import com.stc.chviewer.R;
import com.stc.chviewer.activitythreads.model.PlayableItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyPlayableItemRecyclerViewAdapter extends RecyclerView.Adapter<MyPlayableItemRecyclerViewAdapter.ViewHolder> {

    private final List<PlayableItem> mValues;
    private final PlayableItemFragment.OnListFragmentInteractionListener  mListener;

    public MyPlayableItemRecyclerViewAdapter(PlayableItem[] items, PlayableItemFragment.OnListFragmentInteractionListener listener) {
        mValues = new ArrayList<PlayableItem>();
        Collections.addAll(mValues, items);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_playableitem_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        PabloPicasso.with(holder.mThumbView.getContext()).load(holder.mItem.getThumbUrl()).into(holder.mThumbView);
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
        public final ImageView mThumbView;
        public PlayableItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mThumbView = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }
}
