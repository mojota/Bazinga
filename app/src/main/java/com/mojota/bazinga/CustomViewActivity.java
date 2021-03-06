package com.mojota.bazinga;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mojota.bazinga.view.TopBar;

/**
 * Created by mojota on 16-7-8.
 */
public class CustomViewActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private TopBar mTopBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        Log.d(TAG, "onCreate");
        mTopBar = (TopBar) findViewById(R.id.top_bar);
        mTopBar.setOnTopBarClickListener(new TopBar.TopBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }
        });
    }
}
