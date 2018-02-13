package com.mojota.bazinga.networking;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 上传文件
 * Created by jamie on 18-1-26.
 */
public class UploadFileRequest extends Request<String> {
    private static final String TAG = "UploadFileRequest";

    private final Response.Listener<String> mListener;
    private String mContentType = "application/octet-stream; charset=UTF-8";
    private byte[] mBody;
    private Map<String, String> mHeaders = new HashMap<>();

    /**
     * 传入文件名
     */
    public UploadFileRequest(String url, String fileName, Response.Listener<String> listener,
                             Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        mBody = getFileByte(fileName);
        // 设置超时20秒
        setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /**
     * 传入文件的byte[]，用于没有文件名的情况
     */
    public UploadFileRequest(String url, byte[] fileByte, Response.Listener<String> listener,
                             Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        mBody = fileByte;
        // 设置超时20秒
        setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /**
     * 设置ContentType
     *
     * @param ct
     */
    public void setContentType(String ct) {
        mContentType = ct;
    }

    /**
     * 设置ContentEncoding
     *
     * @param ce
     */
    public void setContentEncoding(String ce) {
        try {
            mHeaders.put("Content-Encoding", ce);
        } catch (Throwable e) {

        }
    }

    /**
     * 设置header
     *
     * @param headers
     */
    public void setHeaders(Map<String, String> headers) {
        try {
            mHeaders.putAll(headers);
        } catch (Throwable e) {

        }
    }

    /**
     * 通过文件名获取byte[]
     */
    private byte[] getFileByte(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }
        ByteArrayOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            bos = new ByteArrayOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[1024];
            int readLength;
            while ((readLength = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, readLength);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Log.d("UploadFileRequest", " header " + mHeaders.toString());
        return mHeaders;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return mBody;
    }

    @Override
    public String getBodyContentType() {
        return mContentType;
    }

    @Override
    protected void deliverResponse(String response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String string = new String(response.data, HttpHeaderParser.parseCharset(response
                    .headers));
            return Response.success(string, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

}
