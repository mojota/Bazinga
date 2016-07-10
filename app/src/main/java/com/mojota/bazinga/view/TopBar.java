package com.mojota.bazinga.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mojota.bazinga.R;

/**
 * Created by mojota on 16-7-8.
 */
public class TopBar extends RelativeLayout {
    private Button mBtLeft;
    private TextView mTvTitle;
    private String mTitle;
    private float mTitleTextSize;
    private int mTitleTextColor;
    private int mLeftTextColor;
    private int mLeftBackground;
    private String mLeftText;
    private TopBarClickListener mListener;

    public interface TopBarClickListener{
        void onLeftClick();
    }
    public TopBar(Context context) {
        super(context);
    }

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        //故意使用xml布局文件摆放控件
        View view = LayoutInflater.from(context).inflate(R.layout.view_top_bar, null);
        mTvTitle = (TextView) view.findViewById(R.id.tv_topbar_title);
        mBtLeft = (Button) view.findViewById(R.id.bt_topbar_left);
        addView(view);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        mTitle = a.getString(R.styleable.TopBar_mainTitle);
        mTitleTextSize = a.getDimension(R.styleable.TopBar_mainTitleTextSize, 10);
        mTitleTextColor = a.getColor(R.styleable.TopBar_mainTitleTextColor, 0);
        mLeftTextColor = a.getColor(R.styleable.TopBar_leftTextColor, 0);
        mLeftBackground = a.getResourceId(R.styleable.TopBar_leftBackground,0);
        mLeftText = a.getString(R.styleable.TopBar_leftText);
        a.recycle();

        mTvTitle.setText(mTitle);
        mTvTitle.setTextSize(mTitleTextSize);
        mTvTitle.setTextColor(mTitleTextColor);
        mBtLeft.setText(mLeftText);
        mBtLeft.setBackgroundResource(mLeftBackground);
        mBtLeft.setTextColor(mLeftTextColor);

        mBtLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null){
                    mListener.onLeftClick();
                }
            }
        });

    }

    public void setOnTopBarClickListener(TopBarClickListener listener) {
        this.mListener = listener;
    }
}
