
package com.mojota.bazinga.connect;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonObjectRequest extends Request<JSONObject> {
    private Map<String, String> mMap;
    private final Listener<JSONObject> mListener;
    
    public JsonObjectRequest(int method, String url, Map<String, String> map,
            Listener<JSONObject> listener,
            ErrorListener errorListener) {
        super(method, url, errorListener);
        mMap = map;
        mListener = listener;
    }
	
    /**
     * 默认get请求
     */
    public JsonObjectRequest(String url, Map<String, String> map,
            Listener<JSONObject> listener,
            ErrorListener errorListener) {
        this(Method.GET, VolleyUtil.addParams(url, map), null, listener, errorListener);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mMap;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}
