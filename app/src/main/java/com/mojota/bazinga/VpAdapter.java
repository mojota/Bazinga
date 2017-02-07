package com.mojota.bazinga;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by jamie on 16-12-16.
 */
public class VpAdapter extends PagerAdapter {
    private final Context mContext;
    private final List<Integer> mList;

    public VpAdapter(Context context, List<Integer> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View convertView = View.inflate(mContext, R.layout.item_vp, null);
        ImageView ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
        int res = mList.get(position);
        ivPic.setImageResource(res);
        container.addView(convertView);
        return convertView;
    }
}
