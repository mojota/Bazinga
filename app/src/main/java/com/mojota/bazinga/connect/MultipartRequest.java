package com.mojota.bazinga.connect;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by jamie on 16-7-31.
 */
public class MultipartRequest extends Request<String> {

    private final String TAG = getClass().getSimpleName();
    private final Response.Listener<String> mListener;
    private final Map<String, File> mFileMap;
    private Map<String, String> mStrMap;
    private MultipartEntity mEntity = new MultipartEntity();

    public MultipartRequest(int method, String url, Map<String, String> strMap, Map<String,
            File> fileMap, Response.Listener<String> listener, Response.ErrorListener
                                    errorListener) {
        super(method, url, errorListener);
        mListener = listener;
        mStrMap = strMap;
        mFileMap = fileMap;
        setMultipartBody();
    }

    public void setMultipartBody() {
        if (mFileMap != null) {
            for (Map.Entry<String, File> fileEntry : mFileMap.entrySet()) {
                mEntity.addPart(fileEntry.getKey(), new FileBody(fileEntry.getValue()));
            }
        }
        if (mStrMap != null) {
            for (Map.Entry<String, String> strEnty : mStrMap.entrySet()) {
                try {
                    mEntity.addPart(strEnty.getKey(), new StringBody(strEnty.getValue(), Charset
                            .forName("UTF-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public String getBodyContentType() {
        Log.d(TAG,"getBodyContentType:"+mEntity.getContentType().getValue());
        return mEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Log.d(TAG,"getBody");
        try {
            mEntity.writeTo(bos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bos.toByteArray();
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);

    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String string = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(string, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }


}
