package com.mojota.bazinga;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by mojota on 15-8-4.
 */
public class MyApplication extends Application {
    private static RequestQueue mRequestQueue;
    private static Context mContext;

    public static Context getContext() {
        return getContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    /**
     * 创建一个RequestQueue对象,网络请求队列,全局一个就够了
     * @return
     */
    public static RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

}
