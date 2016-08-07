package com.mojota.bazinga;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mojota.bazinga.utils.Constants;

import java.util.List;

/**
 * Created by mojota on 15-10-16.
 */
public class PicListAdapter extends RecyclerView.Adapter<PicListAdapter.ViewHolder> {
    private final Context mContext;
    private List<Integer> mPics;

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void setList(List list) {
        mPics = list;
    }

    public List<Integer> getList() {
        return mPics;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cvPic;
        ImageView ivPic;

        public ViewHolder(View itemView) {
            super(itemView);
            cvPic = (CardView) itemView.findViewById(R.id.cv_pic);
            ivPic = (ImageView) itemView.findViewById(R.id.iv_pic);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getAdapterPosition());
            }

        }
    }

    public PicListAdapter(Context context, List<Integer> pics) {
        mContext = context;
        mPics = pics;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_item,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.ivPic.setImageResource((int) mPics.get(position));
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), (int) mPics.get
                (position));

        Palette.Builder builder = Palette.from(bitmap);
        builder.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getDarkMutedSwatch();
                if (swatch != null) {
                    holder.cvPic.setCardBackgroundColor(swatch.getRgb());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPics.size();
    }

    public void removeItem(int position) {
        mPics.remove(position);
        notifyItemRemoved(position);
    }


    public void addItem(int position) {
        mPics.add(position, mPics.get(position));
        notifyItemInserted(position);
        notifyItemChanged(position + 1);
    }
}
