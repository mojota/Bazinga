package com.mojota.bazinga;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mojota.bazinga.networking.WebSocketManager;
import com.mojota.bazinga.utils.ToastUtil;

import java.net.URISyntaxException;


/**
 * A simple {@link Fragment} subclass.
 */
public class FiveFragment extends Fragment implements View.OnClickListener, WebSocketManager
        .WebSocketListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText mEtSend;
    private Button mBtSend;
    private Button mBtClose;
    private TextView mTvReceive;
    private WebSocketManager mWebSocketMgr;
    private Button mBtRestart;


    public static FiveFragment newInstance(String param1, String param2) {
        FiveFragment fragment = new FiveFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FiveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebSocketMgr = new WebSocketManager(getActivity());
        mWebSocketMgr.initWebSocket("ws://10.5.215.7:18105/websocket?token=safd");
        mWebSocketMgr.setWebSocketListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_five, container, false);
        mEtSend = (EditText) view.findViewById(R.id.et_send);
        mBtRestart = (Button) view.findViewById(R.id.bt_restart);
        mBtSend = (Button) view.findViewById(R.id.bt_send);
        mBtClose = (Button) view.findViewById(R.id.bt_close);
        mBtRestart.setOnClickListener(this);
        mBtSend.setOnClickListener(this);
        mBtClose.setOnClickListener(this);
        mTvReceive = (TextView) view.findViewById(R.id.tv_receive);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_restart:
                mWebSocketMgr.reconnect();
                break;
            case R.id.bt_send:
                if (!TextUtils.isEmpty(mEtSend.getText())) {
                    sendMsg(mEtSend.getText().toString());
                }
                break;
            case R.id.bt_close:
                mWebSocketMgr.close();
                break;
        }
    }

    private void sendMsg(String text) {
        mWebSocketMgr.sendMsg(text);
    }

    @Override
    public void onOpen() {
        ToastUtil.showToast("WebSocket已连接到服务器");

    }

    @Override
    public void onMessage(String message) {
        mTvReceive.setText(message);
    }

    @Override
    public void onClose(int code, String reason) {
        ToastUtil.showToast("WebSocket连接关闭:" + code + ":" + reason);

    }

    @Override
    public void onError(String reason) {
        ToastUtil.showToast("WebSocket连接异常:" + reason);
    }
}
