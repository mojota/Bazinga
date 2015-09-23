
package com.mojota.bazinga.connect;

import java.util.Map;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.mojota.bazinga.MyApplication;

public class VolleyUtil {

    private static RequestQueue mRequestQueue;

    /**
     * 创建一个RequestQueue对象,网络请求队列,全局一个就够了
     */
    private static RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(MyApplication.getContext());
        }
        return mRequestQueue;
    }

    public static <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    /**
     * url拼接参数
     */
    public static String addParams(String url, Map<String, String> map) {
        StringBuilder strUrl = new StringBuilder(url);
        try {
            if (map != null) {
                strUrl.append("?");
                for (Map.Entry<String, String> entrySet : map.entrySet()) {
                    strUrl.append(URLEncoder.encode(entrySet.getKey(), "UTF-8"));
                    strUrl.append("=");
                    if (TextUtils.isEmpty(entrySet.getValue())) {
                        strUrl.append("");
                    } else {
                        strUrl.append(URLEncoder.encode(entrySet.getValue(), "UTF-8"));
                    }
                    strUrl.append("&");
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return strUrl.toString();
    }

}
