package com.mojota.bazinga.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mojota.bazinga.R;
import com.mojota.bazinga.ToolBarActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpActivity extends ToolBarActivity implements View.OnClickListener {

    private static final String TAG = "HttpActivity";
    private Button mBtHurlc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);
        mBtHurlc = (Button) findViewById(R.id.bt_hurlc);
        mBtHurlc.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_hurlc:
                urlconnectionRequest("http://www.bangcommunity.com/");
                break;
        }
    }

    private void urlconnectionRequest(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    // 新建一个URL对象
                    URL newUrl = new URL(url);
                    // 打开一个HttpURLConnection连接
                    HttpURLConnection conn = (HttpURLConnection)newUrl.openConnection();
                    // 设置连接超时时间
                    conn.setConnectTimeout(10000);
                    //设置从主机读取数据超时
                    conn.setReadTimeout(10000);
                    // 设置请求方法
                    conn.setRequestMethod("POST");
                    // Post请求必须设置允许输出 默认false
                    conn.setDoOutput(true);
                    // 配置是否保持连接,
                    conn.setRequestProperty("Connection", "keep-alive");
                    // 配置请求体的Content-Type
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

                    // 向服务器写数据
                    String data = "username=135700&accountType=1";
                    OutputStream out = conn.getOutputStream();
                    out.write(data.getBytes());
                    out.flush();
                    out.close();

                    // 判断请求是否成功
                    if (conn.getResponseCode() == 200) {
                        // 获取返回的数据
                        String result = streamToString(conn.getInputStream());
                        Log.e(TAG, "请求成功，result:" + result);
                    } else {
                        Log.e(TAG, "请求失败" + conn.getResponseCode());
                    }
                    // 关闭连接
                    conn.disconnect();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
     */
    public String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
}
