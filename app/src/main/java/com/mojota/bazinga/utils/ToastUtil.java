package com.mojota.bazinga.utils;

import android.widget.Toast;

import com.mojota.bazinga.MyApplication;

/**
 * Created by mojota on 15-8-4.
 */
public class ToastUtil {
    public static void showToast(String str) {
        Toast.makeText(MyApplication.getContext(), str, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(int res) {
        Toast.makeText(MyApplication.getContext(), res, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(String str) {
        Toast.makeText(MyApplication.getContext(), str, Toast.LENGTH_LONG).show();
    }

    public static void showLongToast(int res) {
        Toast.makeText(MyApplication.getContext(), res, Toast.LENGTH_LONG).show();
    }
}
