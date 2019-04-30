package com.mojota.bazinga;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
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
    private Toolbar mToolBar;
    private EditText mEtUrl;
    private Button mBtGo;
    private InputMethodManager mInputManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywebview);

        mToolBar = (Toolbar) findViewById(R.id.toolbar_header);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEtUrl = findViewById(R.id.et_url);
        mBtGo = findViewById(R.id.bt_go);
        mBtGo.setOnClickListener(this);
        mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSetting = mWebView.getSettings();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setAllowUniversalAccessFromFileURLs(true);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setAllowFileAccess(true);
        webSetting.setAppCacheEnabled(false);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setDisplayZoomControls(false);
//        webSetting.setSupportZoom(false);
//        webSetting.setBuiltInZoomControls(false);
//        webSetting.setDisplayZoomControls(false);

//        webSetting.setUserAgentString("Mozilla/5.0");

        mWebView.setWebViewClient(new MyWebViewClient());

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

    private void closeKeyboard(){
        mInputManager.hideSoftInputFromWindow(mEtUrl.getWindowToken(),0);
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
        switch (v.getId()){
            case R.id.bt_go:
                closeKeyboard();
                if (TextUtils.isEmpty(mEtUrl.getText())){
                    mWebView.loadUrl("http://news.163.com");
                } else {
                    mWebView.loadUrl(mEtUrl.getText().toString());
                }
                break;
            case R.id.bt_menu:
                mMenuPop.show(mLayoutMenu);
                showBg(true);
                break;
        }
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
            mEtUrl.setText(url);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String
                failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // 即使证书有问题也继续加载
        }
    }
}
