package com.mojota.bazinga.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * 在大神的基础上修改的下拉控件
 */
public class PullScrollView extends RelativeLayout {

    private Context context;

    private Scroller mScroller;//view滑动计算器

    //getScaledTouchSlop是一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件。
    private int mTouchSlop;

    private View mHeaderView;
    private ScrollView mContentView;

    private int startY;
    private PullState state = PullState.REST;
    private int mHeaderHeight = 0;
    private Onscrolllistener mOnScrollListener;

    enum PullState {
        REST, ON_REFRESH
    }

    public interface Onscrolllistener {
        void onScrollChanged(int l, int t, int oldl, int oldt);
        void onRefreshing();
    }

    public PullScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(context);
    }

    public PullScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    public PullScrollView(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public void setOnScrollListener(Onscrolllistener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    private void init(Context context) {

        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mScroller = new Scroller(context, new DecelerateInterpolator());
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 2) {
            throw new RuntimeException("子View只能有两个");
        }
        mHeaderView = (View) getChildAt(0);
        mContentView = (ScrollView) getChildAt(1);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getY();
                int delayY = moveY - startY;
                if ((getTopPosition() && delayY > 0 || getScrollY() < 0) && Math.abs(delayY) >
                        mTouchSlop) {
                    ev.setAction(MotionEvent.ACTION_DOWN);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int delayY = (int) (event.getY() - startY);
                if (getTopPosition() && delayY > 0 || getScrollY() < 0) {
                    pullMove((int) (-delayY * 0.8));
                }
                startY = (int) event.getY();

                return true;
            case MotionEvent.ACTION_UP:
                int scrollY = Math.abs(getScrollY());
                if (scrollY < mHeaderHeight * 9/10) {
                    returnView();
                } else {
                    int dy = mHeaderHeight - scrollY;
                    expandView(dy);
                    if (mOnScrollListener != null) {
                        mOnScrollListener.onRefreshing();
                    }
                }
                break;
        }
        return true;
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }


    /**
     * 回到0,0
     */
    public void returnView() {
        restView(-getScrollY());
    }

    /**
     * 展开header
     */
    public void expandView(int dy) {
        restView(-dy);
    }

    private void restView(int dy) {
        mScroller.startScroll(0, getScrollY(), 0, dy);
        postInvalidate();
    }


    private void pullMove(int delay) {
        if (getScrollY() <= 0 && (getScrollY() + delay) <= 0) {
            scrollBy(0, delay);
        } else {
            scrollTo(0, 0);
        }
    }

    /**
     * 判断
     *
     * @return 判断contentView的偏移量
     */
    private boolean getTopPosition() {
        if (mContentView.getScrollY() <= 0) {
            return true;
        }
        return false;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mHeaderHeight = getHeaderViewHeight();
        mHeaderView.layout(l, -mHeaderHeight, r, t);
        mContentView.layout(l, 0, r, b);
    }


    /**
     * 获得head头部的高度
     *
     * @return
     */
    public int getHeaderViewHeight() {
        return mHeaderView.getMeasuredHeight();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollListener != null){
            mOnScrollListener.onScrollChanged(l, t, oldl, oldt);
        }
    }
}
