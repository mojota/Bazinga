package com.mojota.bazinga.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.mojota.bazinga.R;
import com.mojota.bazinga.model.MenuInfo;

import java.util.List;

/**
 * Created by jamie on 17-6-29.
 */

public class MenuPopupWindow extends PopupWindow implements ViewPager.OnPageChangeListener,
        OnMenuItemClickListener {
    private Context mContext;
    private View mPopView;
    private ViewPager mVp;
    private List<MenuInfo> mMenuList;
    private MenuPagerAdapter mMenuPagerAdapter;
    private LinearLayout mLayoutIndicator;
    private OnMenuItemClickListener mOnMenuItemClickListener;


    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.mOnMenuItemClickListener = listener;
    }


    public MenuPopupWindow(Context context, List<MenuInfo> list) {
        super(context);
        mContext = context;
        mMenuList = list;
        initView();
    }

    public MenuPopupWindow(Context context, AttributeSet attrs, List<MenuInfo> list) {
        super(context, attrs);
        mContext = context;
        mMenuList = list;
        initView();
    }

    public MenuPopupWindow(Context context, AttributeSet attrs, int defStyleAttr, List<MenuInfo>
            list) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mMenuList = list;
        initView();
    }

    public MenuPopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes, List<MenuInfo> list) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        mMenuList = list;
        initView();
    }

    private void initView() {
        mPopView = LayoutInflater.from(mContext).inflate(R.layout.layout_memu_popupwindow, null);
        setContentView(mPopView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(mContext.getResources().getDimensionPixelSize(R.dimen.menu_height));
        setAnimationStyle(R.style.anim_menuPop);
        setBackgroundDrawable(new ColorDrawable(0x00000000));
        setOutsideTouchable(true);
        setFocusable(true);
        mVp = (ViewPager) mPopView.findViewById(R.id.vp);
        mLayoutIndicator = (LinearLayout) mPopView.findViewById(R.id.layout_indicator);
        mMenuPagerAdapter = new MenuPagerAdapter(mContext, mMenuList);
        setupIndicator(mMenuPagerAdapter);
        mMenuPagerAdapter.setOnMenuItemClickListener(this);
        mVp.setAdapter(mMenuPagerAdapter);
        mVp.addOnPageChangeListener(this);
    }

    private void setupIndicator(MenuPagerAdapter adapter) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.indicator_margin);
        params.rightMargin = mContext.getResources().getDimensionPixelSize(R.dimen
                .indicator_margin);
        mLayoutIndicator.removeAllViews();
        if (adapter != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                ImageView ivPoint = new ImageView(mContext);
                ivPoint.setBackgroundResource(R.drawable.menu_indicator_selector);
                if (i == 0) {
                    ivPoint.setEnabled(true);
                } else {
                    ivPoint.setEnabled(false);
                }
                mLayoutIndicator.addView(ivPoint, params);
            }
        }
    }

    public void show(View parentView) {
        showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onMenuItemClick(int position, MenuInfo menuInfo) {
        if (mOnMenuItemClickListener != null) {
            mOnMenuItemClickListener.onMenuItemClick(position, menuInfo);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < mLayoutIndicator.getChildCount(); i++)
            mLayoutIndicator.getChildAt(i).setEnabled(position == i);

    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    public class MenuPagerAdapter extends PagerAdapter {
        private static final int PAGE_COUNT = 8;
        private List<MenuInfo> menuList;
        private Context context;
        private OnMenuItemClickListener mOnMenuItemClickListener;


        public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
            this.mOnMenuItemClickListener = listener;
        }

        public MenuPagerAdapter(Context ctx, List<MenuInfo> list) {
            context = ctx;
            menuList = list;
        }

        @Override
        public int getCount() {
            if (menuList != null) {
                return (int) Math.ceil((double) menuList.size() / PAGE_COUNT);
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
            View view = LayoutInflater.from(context).inflate(R.layout.page_menu, null);
            WrapGridView gvMenu = (WrapGridView) view.findViewById(R.id.gv_menu);
            int start = position * PAGE_COUNT;
            int end = (position + 1) * PAGE_COUNT;
            if (end >= menuList.size()) {
                end = menuList.size();
            }
            List<MenuInfo> itemList = menuList.subList(start, end);
            MenuItemAdapter itemAdapter = new MenuItemAdapter(context, itemList);
            itemAdapter.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                @Override
                public void onMenuItemClick(int position, MenuInfo menuInfo) {
                    if (mOnMenuItemClickListener != null) {
                        mOnMenuItemClickListener.onMenuItemClick(position, menuInfo);
                    }
                }
            });
            gvMenu.setAdapter(itemAdapter);
            container.addView(view);
            return view;
        }
    }

    /**
     * 菜单条目
     */
    private class MenuItemAdapter extends BaseAdapter {
        private final List<MenuInfo> list;
        private Context context;
        public OnMenuItemClickListener mOnMenuItemClickListener;

        public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
            this.mOnMenuItemClickListener = listener;
        }

        public MenuItemAdapter(Context ctx, List<MenuInfo> itemList) {
            context = ctx;
            list = itemList;
        }

        @Override
        public int getCount() {
            if (list != null && list.size() > 0) {
                return list.size();
            }
            return 0;
        }

        @Override
        public MenuInfo getItem(int position) {
            if (list != null) {
                return list.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_menu, null);
                holder.layoutMenu = (ViewGroup) convertView.findViewById(R.id.layout_menu);
                holder.tvMenu = (TextView) convertView.findViewById(R.id.tv_menu);
                holder.ivMenu = (ImageView) convertView.findViewById(R.id.iv_menu);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final MenuInfo info = list.get(position);
            holder.tvMenu.setText(info.getMenuName());
            holder.ivMenu.setImageResource(info.getMenuResId());
            holder.layoutMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnMenuItemClickListener != null) {
                        mOnMenuItemClickListener.onMenuItemClick(position, info);
                    }
                }
            });
            return convertView;
        }

        class ViewHolder {
            public ViewGroup layoutMenu;
            public TextView tvMenu;
            public ImageView ivMenu;
        }
    }
}
