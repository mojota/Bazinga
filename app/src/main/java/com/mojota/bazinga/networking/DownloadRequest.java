package com.mojota.bazinga.networking;

import android.os.AsyncTask;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 支持带进度的下载
 * 大文件的下载请使用此类
 * Created by wangjing on 18-2-9.
 */

public class DownloadRequest {

    private static final long INTERVAL = 1000; // 更新进度的间隔时间
    // 当下载同一文件时，尽量使用同一个实例下载，需要同时下载不同的文件时，new出新的实例下载，一般还是单线程的下载较为常见
    private static DownloadRequest mInstance;

    private DownloadTask mDownloadTask;
    private DownloadStateListener mStateListener;

    public synchronized static DownloadRequest getInstance() {
        if (mInstance == null) {
            mInstance = new DownloadRequest();
        }
        return mInstance;
    }


    public interface DownloadStateListener {
        void onProgress(int progress);

        void onSuccess();

        void onFailed();
    }


    public void setStateListener(DownloadStateListener stateListener) {
        mStateListener = stateListener;
    }

    /**
     * 开始下载
     */
    public void startDownload(String url, String filename) {
        Log.d("DownloadRequest", "url:" + url + " ,filename:" + filename);
        stopDownload();
        mDownloadTask = new DownloadTask();
        mDownloadTask.execute(url, filename);
    }


    /**
     * 停止下载
     */
    public void stopDownload() {
        if (mDownloadTask != null) {
            mDownloadTask.cancel(true);
            mDownloadTask = null;
        }
    }

    class DownloadTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String urlStr = params[0];
            String filename = params[1];
            InputStream in = null;
            FileOutputStream fos = null;
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(20000);
                conn.setReadTimeout(20000);
                conn.setUseCaches(false);
                conn.setRequestMethod("GET");
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    int length = conn.getContentLength();
                    in = conn.getInputStream();
                    fos = new FileOutputStream(filename);
                    byte[] buffer = new byte[1024];
                    long readCount = 0;
                    int count;
                    long updateTime = 0;
                    while ((count = in.read(buffer)) != -1) {
                        fos.write(buffer, 0, count);
                        readCount += count;
                        if (length > 0 && System.currentTimeMillis() - updateTime >= INTERVAL) {
                            // 一定时间更新一次进度,默认是1秒
                            updateTime = System.currentTimeMillis();
                            publishProgress((int) (readCount * 100 / length));
                        }
                    }
                    return true;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d("DownloadRequest", "progress:" + values[0]);
            if (mStateListener != null) {
                mStateListener.onProgress(values[0]);
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (mStateListener != null) {
                if (success) {
                    Log.d("DownloadRequest", "下载成功");
                    mStateListener.onSuccess();
                } else {
                    Log.d("DownloadRequest", "下载失败");
                    mStateListener.onFailed();
                }
            }
        }
    }

}
