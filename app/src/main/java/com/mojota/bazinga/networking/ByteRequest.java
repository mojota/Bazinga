package com.mojota.bazinga.networking;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Map;

/**
 * 考虑到写文件应该在线程中处理，
 * 故只返回byte[]原始数据,后续根据自己需要写入文件或者转换其它类型
 * Created by jamie on 18-1-26.
 */
public class ByteRequest extends Request<byte[]> {
    private static final String TAG = "ByteRequest";

    private final Response.Listener<byte[]> mListener;
    private Map<String, String> mParams;

    /**
     * 注：method 传入Method.GET或者Method.POST
     * params若无传null
     */
    public ByteRequest(int method, String url, Map<String, String> params, Response
            .Listener<byte[]> listener, Response.ErrorListener errorlistener) {
        super(method, url, errorlistener);
        mListener = listener;
        mParams = treatMap(params);

        // 设置超时20秒
        setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /**
     * 当map的value为null时会报错，故value为null的置""
     */
    private Map<String, String> treatMap(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue() == null) {
                map.put(entry.getKey(), "");
            }
        }
        return map;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    protected void deliverResponse(byte[] response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

}