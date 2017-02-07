package com.mojota.bazinga;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by jamie on 16-12-16.
 */
public class ViewPagerTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.75f;
    private static final float MIN_ALPHA = 0.5f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        if (position < -1) { // [-∞,-1)
            // 这一页已经是最左边的屏幕页
            view.setAlpha(0.8f);

        } else if (position <= 1) { // [-1,1]
            // 修改默认的滑动过渡效果为缩放效果
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }

            // 开始根据缩放系数进行变化 (在 MIN_SCALE 和 1 之间变化)
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            // 根据大小（缩放系数）变化化透明度实现淡化页面效果
            view.setAlpha(MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                            (1 - MIN_SCALE) * (1 - MIN_ALPHA));
//            view.setAlpha(1);

        } else { // (1,+∞ ]
            // 这一页已经是最右边的屏幕页
            view.setAlpha(0.8f);
        }
    }

}
