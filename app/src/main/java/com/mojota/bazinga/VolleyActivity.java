package com.mojota.bazinga;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by mojota on 15-7-31.
 */
public class VolleyActivity extends AppCompatActivity {
    private TextView mTvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
        mTvResult = (TextView) findViewById(R.id.tv_result);
    }

    public void onGetStringClick(View view) {

    }
}
