package com.stc.chviewer.threadselect;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stc.chviewer.PabloPicasso;
import com.stc.chviewer.R;
import com.stc.chviewer.retro.model.ChThread;
import com.stc.chviewer.retro.model.File;

import java.util.List;

import static com.stc.chviewer.Constants.BASE_URL_2CH;

/**
 * Created by artem on 2/8/17.
 */

public class ChThreadAdapter extends RecyclerView.Adapter<ChThreadAdapter.ChThreadViewHolder> {
    private final OnItemClickListener listener;
    private List<ChThread> list;
    public interface OnItemClickListener {
        void onItemClick(long threadId);
    }



    ChThreadAdapter(List<ChThread> list, OnItemClickListener listener) {
        this.list = list;
        this.listener=listener;
    }



    @Override
    public ChThreadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout view = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(
                R.layout.thread, parent, false);
        return new ChThreadViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ChThreadViewHolder viewHolder, int position) {
        final ChThread thread = list.get(position);
        if(thread.getFiles()!=null && thread.getFiles().get(0)!=null){
            File file = thread.getFiles().get(0);
            String path = file.getPath();
            if(path.contains("jpg") || path.contains("png")){
                PabloPicasso.with(viewHolder.image.getContext()).load(BASE_URL_2CH + path).fit().into(viewHolder.image);
            }
        }
        viewHolder.title.setText(thread.getSubject());
       // viewHolder.text.setText(thread.getComment());
        viewHolder.date.setText(thread.getDate());
        viewHolder.postCount.setText(thread.getPostsCount()+" posts");
        viewHolder.filesCount.setText(thread.getFilesCount()+" files");
        viewHolder.id=Long.parseLong(thread.getNum());
        viewHolder.listener=this.listener;

    }



    @Override
    public int getItemViewType(int position) {
        return R.layout.thread;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static class ChThreadViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView text;
        TextView date;
        TextView postCount;
        TextView filesCount;
        OnItemClickListener listener;
        long id;

        ChThreadViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(id);
                }
            });

            title = (TextView) itemView.findViewById(R.id.title);
            text = (TextView) itemView.findViewById(R.id.text);
            date = (TextView) itemView.findViewById(R.id.date);
            postCount = (TextView) itemView.findViewById(R.id.posts_count);
            filesCount = (TextView) itemView.findViewById(R.id.files_count);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }



}
