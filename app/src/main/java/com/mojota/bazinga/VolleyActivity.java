package com.mojota.bazinga;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.mojota.bazinga.connect.GsonRequest;
import com.mojota.bazinga.connect.VolleyUtil;
import com.mojota.bazinga.model.CookDetail;
import com.mojota.bazinga.utils.ToastUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mojota on 15-7-31.
 */
public class VolleyActivity extends AppCompatActivity {
    private static final String URL = "http://apis.juhe.cn/cook/queryid?key=b954e4feff60d14fe3d32538641c1b36&id=1001";
    private TextView mTvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        mTvResult.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    public void onGetStringClick(View view) {
        String url = "http://api.juheapi.com/japi/toh?key=9ca927c6a05534847004e94d7d850fdb&v=1.0&month=09&day=22";
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                mTvResult.setText(s);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showToast("error");
            }
        });
        VolleyUtil.addToRequestQueue(stringRequest);

    }

    public void onGetJsonClick(View view) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                mTvResult.setText(jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showToast("error");
            }
        });

        VolleyUtil.addToRequestQueue(jsonObjectRequest);
    }

    public void onPostJsonClick(View view) {
        String url = "http://apis.juhe.cn/cook/queryid";
        Map<String, String> params = new HashMap<String, String>();
        params.put("key", "b954e4feff60d14fe3d32538641c1b36");
        params.put("id", "1001");
        GsonRequest<CookDetail> request = new GsonRequest<CookDetail>(Request.Method.POST, url, params, CookDetail.class, new Response.Listener<CookDetail>() {
            @Override
            public void onResponse(CookDetail cookDetail) {
                mTvResult.setText(cookDetail.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showToast("error");
            }
        });

        VolleyUtil.addToRequestQueue(request);
    }
}
