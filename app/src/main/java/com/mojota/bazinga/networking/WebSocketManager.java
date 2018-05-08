package com.mojota.bazinga.networking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mojota.bazinga.MyApplication;
import com.mojota.bazinga.utils.GlobalUtils;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * WebSocket即时通讯管理类
 * Created by wangjing on 18-4-9.
 */
public class WebSocketManager {
    private static final String TAG = "WebSocketManager";
    private static final int OPEN = 101;
    private static final int MESSAGE = 102;
    private static final int CLOSE = 103;
    private static final int ERROR = 104;
    private static final int SECOND = 1000;//1秒
    private static final int RETRY_COUNT = 5; //重试次数

    private Context mContext;

    private int mRetryCount = RETRY_COUNT;
    private WebSocketClient mWebSocketClient; // webSocket客户端
    private WebSocketListener mWebSocketListener;
    private String mUrl;

    public interface WebSocketListener {
        void onOpen();

        void onMessage(String message);

        void onClose(int code, String reason);

        void onError(String reason);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OPEN:
                    mRetryCount = RETRY_COUNT;
                    if (mWebSocketListener != null) {
                        mWebSocketListener.onOpen();
                    }
                    break;
                case MESSAGE:
                    String message = (String) msg.obj;
                    if (mWebSocketListener != null) {
                        mWebSocketListener.onMessage(message);
                    }
                    break;
                case CLOSE:
                    reconnect();
                    if (mWebSocketListener != null) {
                        int code = msg.arg1;
                        String reason = (String) msg.obj;
                        mWebSocketListener.onClose(code, reason);
                    }
                    break;
                case ERROR:
                    reconnect();
                    if (mWebSocketListener != null) {
                        String reason = (String) msg.obj;
                        mWebSocketListener.onError(reason);
                    }
                    break;
            }
        }
    };


    private BroadcastReceiver mNetworkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 网络状态发生变化，则尝试重连
            if (GlobalUtils.isNetworkAvailable(context)) {
                mRetryCount = RETRY_COUNT;
                reconnect();
            } else {
                Log.d(TAG, "尝试重连,网络不可用");
            }
        }
    };

    public WebSocketManager(Context context) {
        mContext = context;
        registerNetworkReceiver();
    }

    private void registerNetworkReceiver() {
        if (mContext != null) {
            IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            mContext.registerReceiver(mNetworkStateReceiver, intentFilter);
        }
    }


    public void setWebSocketListener(WebSocketListener webSocketListener) {
        this.mWebSocketListener = webSocketListener;
    }


    public void initWebSocket(String url) {
        mUrl = url;
        try {
            mWebSocketClient = new WebSocketClient(new URI(mUrl)) {

                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d(TAG, "WebSocket已连接到服务器");
                    Message msg = mHandler.obtainMessage();
                    msg.what = OPEN;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onMessage(String message) {
                    Log.d(TAG, "WebSocket收到消息:" + message);
                    Message msg = mHandler.obtainMessage();
                    msg.what = MESSAGE;
                    msg.obj = message;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d(TAG, "WebSocket连接关闭:" + code + ";" + reason);
                    Message msg = mHandler.obtainMessage();
                    msg.what = CLOSE;
                    msg.arg1 = code;
                    msg.obj = reason;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onError(Exception ex) {
                    Log.d(TAG, "WebSocket连接异常:" + ex);
                    Message msg = mHandler.obtainMessage();
                    msg.what = ERROR;
                    mHandler.sendMessage(msg);
                }
            };
            if (GlobalUtils.isNetworkAvailable(MyApplication.getContext())) {
                Log.d(TAG, "WebSocket开始连接");
                mWebSocketClient.connect();
            } else {
                Log.d(TAG, "WebSocket开始连接,网络不可用");
            }
        } catch (Exception e) {
            Log.d(TAG, "WebSocket初始化连接失败");
            e.printStackTrace();
        }
    }


    /**
     * 发送消息，返回是否连接正常并且已send
     */
    public boolean sendMsg(String msg) {
        boolean isSend = false;
        try {
            if (mWebSocketClient != null && mWebSocketClient.isOpen()) {
                mWebSocketClient.send(msg);
                isSend = true;
            } else {
                if (mRetryCount == 0) {
                    mRetryCount = RETRY_COUNT;
                }
                Message m = mHandler.obtainMessage();
                m.what = CLOSE;
                m.obj = "未连接，请重试";
                mHandler.sendMessage(m);
            }
        } catch (Exception e) {
            Message m = mHandler.obtainMessage();
            m.what = ERROR;
            m.obj = "发送异常，请重试";
            mHandler.sendMessage(m);
            e.printStackTrace();
        }
        Log.d(TAG, "发送：" + isSend + "；发送内容：" + msg);
        return isSend;
    }

    /**
     * 重连
     * 连接已经关闭才可以重连
     */
    private void reconnect() {
        try {
            if (mRetryCount > 0 && GlobalUtils.isNetworkAvailable(mContext)) {
                if (mWebSocketClient != null && mWebSocketClient.isClosed()) {
                    Log.d(TAG, "WebSocket尝试重连,剩余次数:" + mRetryCount);
                    mWebSocketClient.reconnect();
                    mRetryCount--;
                }
            } else {
                Log.d(TAG, "网络不可用,剩余重连次数:" + mRetryCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        mHandler.removeCallbacksAndMessages(null);
        if (mContext != null && mNetworkStateReceiver != null) {
            mContext.unregisterReceiver(mNetworkStateReceiver);
        }
        if (mWebSocketClient != null) {
            mWebSocketClient.getConnection().close();
            mWebSocketClient.close();
            mWebSocketClient = null;
        }
        this.mWebSocketListener = null;
    }

}