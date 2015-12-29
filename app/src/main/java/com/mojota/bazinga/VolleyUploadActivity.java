package com.mojota.bazinga;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class VolleyUploadActivity extends ToolBarActivity implements View.OnClickListener {

    private Button mBtUpload;
    private ImageView mPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley_upload);
        mPic = (ImageView) findViewById(R.id.iv_pic);
        mBtUpload = (Button) findViewById(R.id.bt_upload);
        mBtUpload.setOnClickListener(this);
        initData();
    }

    private void initData() {
//        mPic.set
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_upload:
                uploadPic();
                break;
            default:
                break;
        }
    }

    private void uploadPic() {

    }
}
