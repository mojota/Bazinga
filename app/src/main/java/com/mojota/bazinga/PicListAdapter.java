package com.mojota.bazinga;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mojota.bazinga.utils.Constants;

/**
 * Created by mojota on 15-10-16.
 */
public class PicListAdapter extends RecyclerView.Adapter<PicListAdapter.ViewHolder> {
    private final Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPic;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPic = (ImageView) itemView.findViewById(R.id.iv_pic);
        }
    }

    public PicListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ivPic.setImageResource(Constants.PICS[position]);
    }

    @Override
    public int getItemCount() {
        return Constants.PICS.length;
    }

}
