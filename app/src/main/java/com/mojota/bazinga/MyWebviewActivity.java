package com.mojota.bazinga;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.mojota.bazinga.model.MenuInfo;
import com.mojota.bazinga.view.MenuPopupWindow;
import com.mojota.bazinga.view.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamie on 17-6-29.
 */
public class MyWebviewActivity extends AppCompatActivity implements View.OnClickListener, OnMenuItemClickListener {

    private Button mBtMenu;
    private MenuPopupWindow mMenuPop;
    private ViewGroup mLayoutMenu;
    private List<MenuInfo> mMenuList = new ArrayList<MenuInfo>();
    private View mViewBg;
    private WebView mWebView;
    private WebSettings mWebSetting;
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywebview);

        mToolBar = (Toolbar) findViewById(R.id.toolbar_header);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebSetting = mWebView.getSettings();
        mWebSetting.setSupportZoom(true);
        mWebView.loadUrl("http://news.163.com");

        mBtMenu = (Button) findViewById(R.id.bt_menu);
        mLayoutMenu = (ViewGroup) findViewById(R.id.layout_menu);
        mViewBg = findViewById(R.id.view_bg);
        mBtMenu.setOnClickListener(this);
        setMenuList();
        mMenuPop = new MenuPopupWindow(this, mMenuList);
        mMenuPop.setOnMenuItemClickListener(this);
        mMenuPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                showBg(false);
            }
        });
    }

    /**
     * 显示或关闭半透明遮照
     */
    private void showBg(boolean show) {
        if (show) {
            mViewBg.setVisibility(View.VISIBLE);
        } else {
            mViewBg.setVisibility(View.GONE);
        }

    }

    /**
     * 设置菜单选项
     */
    private void setMenuList() {
        for (int i = 0; i < 18; i++) {
            MenuInfo menuInfo = new MenuInfo();
            menuInfo.setMenuId(i);
            menuInfo.setMenuName("菜单" + i);
            menuInfo.setMenuResId(R.mipmap.ic_nav);
            mMenuList.add(menuInfo);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        mMenuPop.show(mLayoutMenu);
        showBg(true);
    }

    @Override
    public void onMenuItemClick(int position, MenuInfo menuInfo) {
        Toast.makeText(this, menuInfo.getMenuName(), Toast.LENGTH_SHORT).show();
        // TODO 这里处理每个菜单的点击
//        switch (menuInfo.getMenuId()) {
//
//        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
