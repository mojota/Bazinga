package com.mojota.bazinga.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mojota.bazinga.R;
import com.mojota.bazinga.ToolBarActivity;
import com.mojota.bazinga.networking.DownloadRequest;
import com.mojota.bazinga.utils.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class HttpActivity extends ToolBarActivity implements View.OnClickListener {

    private static final String TAG = "HttpActivity";
    private Button mBtHurlc;
    private Button mBtBrokenTransfer;
    private Button mBtDownload;
    private int mTotalLength;
    private int mWriteCount;
    private ProgressDialog mProgressDlg;
    private DownloadRequest mDownloadRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);
        mBtHurlc = (Button) findViewById(R.id.bt_hurlc);
        mBtBrokenTransfer = (Button) findViewById(R.id.bt_broken_transfer);
        mBtDownload = (Button) findViewById(R.id.bt_download);
        mBtHurlc.setOnClickListener(this);
        mBtBrokenTransfer.setOnClickListener(this);
        mBtDownload.setOnClickListener(this);
        mProgressDlg = new ProgressDialog(this);
        mProgressDlg.setTitle("下载中...");
        mProgressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDlg.setMax(100);
        mProgressDlg.setIndeterminate(false);
        mProgressDlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mDownloadRequest != null) {
                    mDownloadRequest.stopDownload();
                }
            }
        });
        mDownloadRequest = new DownloadRequest();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_hurlc:
                urlconnectionRequest("http://www.bangcommunity.com/");
                break;
            case R.id.bt_broken_transfer:
//                resumeBrokenTransfer("http://im.bangcommunity.com:9000/app-download/bbchat.apk");
                resumeBrokenTransfer("https://uim.bangcommunity.com/video_explore/big_buck_bunny"
                        + ".mp4");
                break;
            case R.id.bt_download:
                downloadWithProgress();
                break;
        }
    }

    private void downloadWithProgress() {
        mProgressDlg.show();
        mDownloadRequest.setStateListener(new DownloadRequest.DownloadStateListener() {
            @Override
            public void onProgress(int progress) {
                mProgressDlg.setProgress(progress);
            }

            @Override
            public void onSuccess() {
                ToastUtil.showToast("下载成功");
                mProgressDlg.dismiss();
            }

            @Override
            public void onFailed() {
                ToastUtil.showToast("下载失败");
                mProgressDlg.dismiss();
            }
        });
        mDownloadRequest.startDownload("https://uim.bangcommunity" +
                ".com/video_explore/big_buck_bunny.mp4", getFileFolder()+getFileName(""));
    }

    private void urlconnectionRequest(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 新建一个URL对象
                    URL newUrl = new URL(url);
                    // 打开一个HttpURLConnection连接
                    HttpURLConnection conn = (HttpURLConnection) newUrl.openConnection();
                    // 设置连接超时时间
                    conn.setConnectTimeout(10000);
                    //设置从主机读取数据超时
                    conn.setReadTimeout(10000);
                    // 设置请求方法,默认是GET
                    conn.setRequestMethod("POST");
                    // Post请求必须设置开启请求体 默认false
                    conn.setDoOutput(true);
                    // 配置是否保持连接,
                    conn.setRequestProperty("Connection", "keep-alive");
                    // 配置请求体的Content-Type
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; "
                            + "charset=UTF-8");
//                    conn.setRequestProperty("Content-type", "application/octet-stream;
// charset=UTF-8");

                    // 向服务器写请求数据(body)，如果是上传文件，则此处的请求体为具体文件的字节数组
                    String data = "username=135700&accountType=1";
                    OutputStream out = conn.getOutputStream();
                    out.write(data.getBytes());
                    out.flush();
                    out.close();

                    // 判断请求是否成功
                    if (conn.getResponseCode() == 200) {
                        // 获取返回的数据
                        String result = streamToString(conn.getInputStream());
                        Log.e(TAG, "请求成功，result:" + result);
                    } else {
                        Log.e(TAG, "请求失败" + conn.getResponseCode());
                    }
                    // 关闭连接
                    conn.disconnect();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
     */
    public String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    /**
     * multipart/form-data上传
     */
    public static void uploadFile(File file, String RequestURL) {
        String BOUNDARY = UUID.randomUUID().toString();  //边界标识,随机生成
        String LINE_END = "\r\n";
        String PREFIX = "--";
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10 * 1000);
            conn.setConnectTimeout(10 * 1000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset", "utf-8");
            conn.setRequestProperty("Content-type", "multipart/form-data;boundary=" + BOUNDARY);

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            StringBuffer sb = new StringBuffer();
            // 文本
            sb.append(PREFIX + BOUNDARY + LINE_END);
            sb.append("Content-Disposition: form-data; name=\"data\"" + LINE_END + LINE_END);
            sb.append("hello android!" + LINE_END); //内容

            // 文件
            sb.append(PREFIX + BOUNDARY + LINE_END);
            sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName
                    () + "\"" + LINE_END);
            sb.append("Content-Type: application/octet-stream; charset=utf-8" + LINE_END +
                    LINE_END);
            dos.write(sb.toString().getBytes());
            InputStream is = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = is.read(bytes)) != -1) {
                dos.write(bytes, 0, len);
            }
            is.close();
            dos.write(LINE_END.getBytes());
            // 结尾
            dos.write((PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes());
            dos.flush();
            int code = conn.getResponseCode();
            Log.e(TAG, "response code:" + code);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 断点续传
     */
    private void resumeBrokenTransfer(final String urlStr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream fos = null;
                InputStream in = null;
//                RandomAccessFile raf = null;
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Charset", "UTF-8");

                    if (mWriteCount > 0 && mWriteCount < mTotalLength) {
                        conn.setRequestProperty("Range", "bytes=" + mWriteCount + "-"); // 设置range头
                        Log.d(TAG, "Range:bytes=" + mWriteCount + "-");
                    }

                    int responseCode = conn.getResponseCode();
                    File file = new File(getFileFolder(), getFileName(urlStr));
                    if (responseCode == 200 || responseCode == 206) {
                        Log.d(TAG, conn.getHeaderFields().toString());
                        if (responseCode == 200) {
                            if (file.exists()) {
                                file.delete();
                            }
                            mTotalLength = conn.getContentLength();
                            mWriteCount = 0;
                        }
                        fos = new FileOutputStream(file, true); // true表示增量写文件
//                        raf= new RandomAccessFile(file,"rw");
// RandomAccessFile类功能丰富，支持跳到文件任意位置读写数据，故也可使用此类写入文件。
//                        raf.seek(mWriteCount);
                        int size = 1024;
                        in = conn.getInputStream();
                        byte[] buffer = new byte[size];
                        int count;
                        while ((count = in.read(buffer)) != -1) {
                            fos.write(buffer, 0, count);
//                            raf.write(buffer, 0 ,count);
                            mWriteCount = mWriteCount + count; // 记录已下载字节数
                            Log.d(TAG, "count:" + String.valueOf(count) + ",writeCount:" + String
                                    .valueOf(mWriteCount));
                        }
                        if (mTotalLength == mWriteCount) {
                            Log.d(TAG, "下载完成");
                        }
                        Log.d(TAG, "writeCount:" + String.valueOf(mWriteCount));
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
//                    if (raf!= null) {
//                        try {
//                            raf.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
                }
            }
        }).start();
    }

    private String getFileName(String urlStr) {
        String fileName = "downloadFile";
        if (!TextUtils.isEmpty(urlStr)) {
            String[] temps = urlStr.split("/");
            fileName = temps[temps.length - 1];
        }
        return fileName;
    }


    /**
     * 获取文件夹路径
     */
    public static File getFileFolder() {
        File fileFolder = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdcard = Environment.getExternalStorageDirectory();
            fileFolder = new File(sdcard, "bazinga");
            if (!fileFolder.exists()) {
                fileFolder.mkdirs();
            }
        }
        return fileFolder;
    }
}
