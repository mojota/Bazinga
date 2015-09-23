
package com.mojota.bazinga.connect;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class GsonRequest<T> extends Request<T> {
    private final Gson mGson;
    private Map<String, String> mMap;
    private final Listener<T> mListener;
    private Class<T> mClass;

    public GsonRequest(int method, String url, Map<String, String> map, Class<T> clazz,
            Listener<T> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        mMap = map;
        mListener = listener;
        mGson = new Gson();
        mClass = clazz;
    }

    /**
     * 默认get请求
     */
    public GsonRequest(String url, Map<String, String> map, Class<T> clazz,
            Listener<T> listener, ErrorListener errorListener) {
        this(Method.GET, VolleyUtil.addParams(url, map), null, clazz, listener, errorListener);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mMap;
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(mGson.fromJson(jsonString, mClass),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
