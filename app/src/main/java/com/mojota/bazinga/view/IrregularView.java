
package com.mojota.bazinga.view;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mojota.bazinga.R;
import com.mojota.bazinga.utils.DisplayUtil;

/**
 * textview自适应长度排列的view
 */
public class IrregularView extends LinearLayout {

    private final int DIVIDER_WIDTH = getResources().getDimensionPixelSize(R.dimen.divider_spacing) * 2; // 间隔宽度
    private Context mContext;
    private int mTotalWidth;
    private int mRemainderWidth; // 剩余宽度

    public IrregularView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public IrregularView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mContext = context;
        mTotalWidth = DisplayUtil.getScreenWidth(context)
                - getResources().getDimensionPixelSize(R.dimen.margin_spacing) * 2;
        mRemainderWidth = mTotalWidth;
    }

    /*
     * 一行不够显示则增加下一行
     */
    public void fillView(List<String> list) {
        LinearLayout childLayout = new LinearLayout(mContext);
        for (int i = 0; i < list.size(); i++) {
            View itemView = createItemView(list, i);
            if (itemView != null) {
                itemView.measure(0, 0);
                mRemainderWidth = mRemainderWidth - itemView.getMeasuredWidth() - DIVIDER_WIDTH;
                if (mRemainderWidth > 0) {
                    childLayout.addView(itemView);
                } else {
                    addView(childLayout);
                    mRemainderWidth = mTotalWidth - itemView.getMeasuredWidth() - DIVIDER_WIDTH;
                    childLayout = new LinearLayout(mContext);
                    childLayout.addView(itemView);
                }
            }
        }
        addView(childLayout);
    }

    private View createItemView(List<String> list, int position) {
        if (TextUtils.isEmpty(list.get(position))) {
            return null;
        }
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_cardtype_view, null);
        CardView cvCardType = (CardView) itemView.findViewById(R.id.cv_cardtype);
        TextView tvCardType = (TextView) itemView.findViewById(R.id.tv_cardtype);
        cvCardType.setCardBackgroundColor(getColor(position));
        tvCardType.setText(list.get(position));
        return itemView;
    }

    private int getColor(int position) {
        if (position % 2 == 0) {
            return Color.parseColor("#61D2FE");
        } else {
            return Color.parseColor("#FF4966");
        }
    }
}
