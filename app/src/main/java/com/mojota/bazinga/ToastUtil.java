package com.mojota.bazinga;

import android.widget.Toast;

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
}
