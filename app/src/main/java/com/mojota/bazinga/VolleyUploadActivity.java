package com.mojota.bazinga;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mojota.bazinga.networking.MultipartRequest;
import com.mojota.bazinga.networking.VolleyUtil;
import com.mojota.bazinga.utils.ToastUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class VolleyUploadActivity extends ToolBarActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_FOR_PICK_IMAGE = 100;
    private String TAG = getClass().getSimpleName();
    private Button mBtUpload;
    private ImageView mIvPic;
    private TextView mTvPicName;
    private String mImagePath;
    private String mImageName;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_volley_upload);
        mIvPic = (ImageView) findViewById(R.id.iv_pic);
        mIvPic.setOnClickListener(this);
        mTvPicName = (TextView)findViewById(R.id.tv_pic_name);
        mBtUpload = (Button) findViewById(R.id.bt_upload);
        mBtUpload.setOnClickListener(this);
        initData();
    }

    private void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pic:
                selectPic();
                break;
            case R.id.bt_upload:
                uploadPic();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_FOR_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    mImageUri = data.getData();
                    mImagePath = getRealPath(mImageUri);
                    Log.d(TAG, "uri.getScheme:" + mImageUri.getScheme());
                    Log.d(TAG, "uri.getPath:" + mImageUri.getPath());
                    Log.d(TAG, "getRealPath:" + mImagePath);
                    Log.d(TAG, "uri.toString:" + mImageUri.toString());
                    mImageName = getImageName(mImagePath);
                    Log.d(TAG, "mImageName:" + mImageName);
                    mIvPic.setImageURI(mImageUri);
                    mTvPicName.setText(mImagePath);
                    break;
                }
        }
    }

    private String getRealPath(Uri uri) {
        String path = "";
        String[] proj = new String[]{MediaStore.Images.ImageColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        if (cursor !=null){
            if (cursor.moveToFirst()){
                path= cursor.getString(cursor.getColumnIndex(proj[0]));
            }
            cursor.close();
        }
        return path;
    }

    private String getImageName(String imagePath) {
        String name = imagePath;
        int index = imagePath.lastIndexOf("/");
        if (index != -1) {
            name = imagePath.substring(index + 1);
        }
        return name;
    }

    private void selectPic() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE_FOR_PICK_IMAGE);
    }

    private void uploadPic() {
        if (TextUtils.isEmpty(mImagePath)) {
            return;
        }
        Map<String, String> strMap = new HashMap<String, String>();
        Map<String, File> fileMap = new HashMap<String, File>();
        String url = "http://up-z1.qiniu.com";
        strMap.put("token", getToken());
        strMap.put("key", mImageName);
        strMap.put("scope", "mojota");
        fileMap.put("file", new File(mImagePath));
        MultipartRequest request = new MultipartRequest(Request.Method.POST, url, strMap, fileMap,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d(TAG, "response:" + s);
                        ToastUtil.showToast("success");
                        mImagePath = "";
                        mTvPicName.setText(mImagePath);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "onErrorResponse:" + volleyError.getMessage());
                ToastUtil.showToast(volleyError.getMessage());
            }
        });
        VolleyUtil.addToRequestQueue(request);
    }

    public String getToken() {
        String token = "HLKwn2P8bRI45y2Z61_LvBEOPN4FXX-vH97-xIDA:CkbNDwzGZDjoeo6VE_aR6Yg3pz8" +
                "=:eyJzY29wZSI6Im1vam90YSIsImRlYWRsaW5lIjoxNDcwMTEyNDYxfQ==";
        return token;
    }
}
