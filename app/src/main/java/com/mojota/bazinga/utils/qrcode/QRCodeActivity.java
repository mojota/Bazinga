/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mojota.bazinga.utils.qrcode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.mojota.bazinga.R;
import com.mojota.bazinga.utils.ToastUtil;
import com.mojota.bazinga.utils.qrcode.zxing.camera.CameraManager;
import com.mojota.bazinga.utils.qrcode.zxing.decode.DecodeThread;
import com.mojota.bazinga.utils.qrcode.zxing.utils.BeepManager;
import com.mojota.bazinga.utils.qrcode.zxing.utils.CaptureActivityHandler;
import com.mojota.bazinga.utils.qrcode.zxing.utils.InactivityTimer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Hashtable;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class QRCodeActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private static final String TAG = QRCodeActivity.class.getSimpleName();

    private static final int REQUEST_IMAGE = 100;

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;

    private SurfaceView scanPreview = null;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;

    private Rect mCropRect = null;

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    private boolean isHasSurface = false;

    private String mPhotoPath;

    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_qrcode);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_header);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("二维码");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
        scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.scan_line);

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT,
                0.9f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_qrcode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_photo:
                Intent innerIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(innerIntent, REQUEST_IMAGE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());

        handler = null;

        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(scanPreview.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            scanPreview.getHolder().addCallback(this);
        }

        inactivityTimer.onResume();
    }

    @Override
    public void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     * 
     * @param rawResult The contents of the barcode.
     * @param bundle The extras
     */
    public void handleDecode(Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();

        String result = recode(rawResult.getText());
        ToastUtil.showToast(result);
    }


    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("二维码");
        builder.setMessage("无法启动相机，请检查相机权限是否已开启!");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

//    public void restartPreviewAfterDelay(long delayMS) {
//        if (handler != null) {
//            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
//        }
//    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE:
                    // 获取选中图片的路径
                    Uri uri = data.getData();
                    String[] proj = {
                            MediaStore.Images.Media.DATA
                    };
                    Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
                    cursor.moveToFirst();
                    mPhotoPath = cursor.getString(cursor.getColumnIndex(proj[0]));
                    cursor.close();
                    Log.d(TAG, "mPhotoPath: " + mPhotoPath);

                    QrcodeTask qrcodeTask = new QrcodeTask();
                    qrcodeTask.execute();
            }
        }
    }

    class QrcodeTask extends AsyncTask<String, Integer, Result> {

        @Override
        protected Result doInBackground(String... params) {
            // 识别图片,返回结果
//             Bitmap picBitmap = BitmapFactory.decodeFile(mPhotoPath);
            Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
            hints.put(DecodeHintType.CHARACTER_SET, "ISO-8859-1");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true; // 先获取原大小
            Bitmap picBitmap = BitmapFactory.decodeFile(mPhotoPath, options);
            options.inJustDecodeBounds = false; // 获取新的大小
            int sampleSize = (int) (options.outHeight / (float) 200);
            if (sampleSize <= 0) {
                sampleSize = 1;
            }
            options.inSampleSize = sampleSize;
            picBitmap = BitmapFactory.decodeFile(mPhotoPath, options);
            int[] dataint = new int[picBitmap.getHeight() * picBitmap.getWidth()];
            picBitmap.getPixels(dataint, 0, picBitmap.getWidth(), 0, 0,
                    picBitmap.getWidth(), picBitmap.getHeight());
            RGBLuminanceSource source = new RGBLuminanceSource(picBitmap.getWidth(),
                    picBitmap.getHeight(), dataint);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            QRCodeReader reader = new QRCodeReader();
            Result result = null;
            try {
                result = reader.decode(bitmap, hints);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Result result) {
            if (handler == null) {
                handler = new CaptureActivityHandler(QRCodeActivity.this, cameraManager,
                        DecodeThread.ALL_MODE);
            }
            if (result != null) {
                if (handler != null) {
                    Message message = Message.obtain(handler, R.id.decode_succeeded,
                            result);
                    Bundle bundle = new Bundle();
                    message.setData(bundle);
                    message.sendToTarget();
                }
            } else {
                if (handler != null) {
                    Message message = Message.obtain(handler, R.id.decode_failed);
                    message.sendToTarget();
                    ToastUtil.showToast(R.string.qrcode_not_found);
                }
            }
        }
    }

    /**
     * 中文乱码处理
     * 
     * @return
     */
    private String recode(String str) {
        String formart = "";

        try {
            boolean ISO = Charset.forName("ISO-8859-1").newEncoder().canEncode(str);
            if (ISO) {
                formart = new String(str.getBytes("ISO-8859-1"), "GB2312");
            } else {
                formart = str;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return formart;
    }
}
