//
//package com.mojota.bazinga.utils.qrcode;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.text.Spannable;
//import android.text.SpannableStringBuilder;
//import android.text.style.ClickableSpan;
//import android.text.style.URLSpan;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.TextView;
//
//public class QRCodeInfoActivity extends BaseActivity {
//
//    private TextView tvInfo;
//    private String info;
//    private Toolbar mToolbar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.cloudread_activity_qrcodeinfo);
//        getInitData();
//        mToolbar = (Toolbar) findViewById(R.id.toolbar_header);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle(R.string.text_qrcode);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        tvInfo = (TextView) findViewById(R.id.textview_qrcode_info_msg);
//        tvInfo.setText(info);
//
//        CharSequence text = tvInfo.getText();
//        if (text instanceof Spannable) {
//            int end = text.length();
//            Spannable sp = (Spannable) tvInfo.getText();
//            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
//            SpannableStringBuilder style = new SpannableStringBuilder(text);
//            style.clearSpans(); // should clear old spans
//            for (URLSpan url : urls) {
//                MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
//                style.setSpan(myURLSpan, sp.getSpanStart(url), sp.getSpanEnd(url),
//                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//            }
//            tvInfo.setText(style);
//        }
//    }
//
//    private void getInitData() {
//        Intent intent = getIntent();
//        info = intent.getStringExtra("info");
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    // 修改链接的默认点击事件
//    private class MyURLSpan extends ClickableSpan {
//
//        private String mUrl;
//
//        MyURLSpan(String url) {
//            mUrl = url;
//        }
//
//        @Override
//        public void onClick(View widget) {
//            showDialog(mUrl);
//        }
//    }
//
//    // 打开浏览器
//    private void startBrowser(String url) {
//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.VIEW");
//        Uri content_url = Uri.parse(url);
//        intent.setData(content_url);
//        startActivity(intent);
//    }
//
//    // 弹出对话框,确认是否打开链接
//    private void showDialog(final String url) {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(QRCodeInfoActivity.this);
//        dialog.setTitle("链接跳转");
//        dialog.setMessage("是否打开" + url + "链接?");
//        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startBrowser(url);
//            }
//        });
//        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        dialog.create().show();
//    }
//
//}
