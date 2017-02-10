package com.stc.chviewer.activitythreads;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stc.chviewer.R;
import com.stc.chviewer.activitythreads.model.PlayableItem;


public class PlayableItemFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_PLAYABLE_ITEMS = "ARG_PLAYABLE_ITEMS";
    private static final String ARG_SELECTED_INDEX = "ARG_SELECTED_INDEX";
    private static final String ARG_THREAD_ID = "ARG_THREAD_ID";
    private int mColumnCount = 1;
    private String threadId=null;
    PlayableItem[] list;
    private OnListFragmentInteractionListener mListener;
    public PlayableItemFragment() {
    }

    public static PlayableItemFragment newInstance(int columnCount, PlayableItem[] playableItems, int selectedItem) {
        PlayableItemFragment fragment = new PlayableItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putParcelableArray(ARG_PLAYABLE_ITEMS, playableItems);
        args.putInt(ARG_SELECTED_INDEX, selectedItem);
        if(playableItems.length>0)
            args.putString(ARG_THREAD_ID, playableItems[0].getThreadId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            threadId=getArguments().getString(ARG_THREAD_ID);
            list= (PlayableItem[]) getArguments().getParcelableArray(ARG_PLAYABLE_ITEMS);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playableitem_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            recyclerView.setAdapter(new MyPlayableItemRecyclerViewAdapter(list, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(PlayableItem item);
    }

}
