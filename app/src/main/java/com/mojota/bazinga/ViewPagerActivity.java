package com.mojota.bazinga;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.mojota.bazinga.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends ToolBarActivity {

    private ViewPager mVp;
    private PagerAdapter mVpAdapter;
    private List<Integer> mList;
    private RelativeLayout mLayoutVp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        mLayoutVp = (RelativeLayout)findViewById(R.id.layout_vp);
        mVp = (ViewPager)findViewById(R.id.vp);
//        mList = new ArrayList<Integer>();
//        mList.add(R.mipmap.ic_pic_20);
//        mList.add(R.mipmap.ic_pic_21);
//        mList.add(R.mipmap.ic_pic_22);
//        mList.add(R.mipmap.ic_pic_23);
        mList = getList();
        mVpAdapter = new VpAdapter(this, mList);
        mVp.setOffscreenPageLimit(2);
        mVp.setPageMargin(getResources().getDimensionPixelSize(R.dimen.vp_padding));
        mVp.setPageTransformer(false, new ViewPagerTransformer());
        mLayoutVp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mVp.dispatchTouchEvent(event);
            }
        });
        mVp.setAdapter(mVpAdapter);
    }

    public List getList() {
        List list = new ArrayList();
        for (int i = 0; i < Constants.PICS.length; i++) {
            list.add(Constants.PICS[i]);
        }
        return list;
    }
}
