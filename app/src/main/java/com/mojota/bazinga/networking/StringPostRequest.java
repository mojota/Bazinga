package com.mojota.bazinga.networking;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jamie
*/
public class StringPostRequest extends Request<String> {
	private Map<String, String> mMap;
	private final Listener<String> mListener;
	private Map<String, String> mHeaderMap;

	public StringPostRequest(String url, Map<String, String> map, Listener<String> listener,
                             ErrorListener errorlistener) {
		super(Method.POST, url, errorlistener);
		mListener = listener;
 		mMap = treatMap(map);

		// 设置超时20秒
		setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	}

	public StringPostRequest(String url, Map<String, String> headMap, Map<String, String> map, Listener<String> listener,
                             ErrorListener errorlistener) {
		super(Method.POST, url, errorlistener);
		mListener = listener;
		mHeaderMap = treatMap(headMap);
		mMap = treatMap(map);

		// 设置超时20秒
		setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	}

	/**
	 * 当map的value为null时会报错，故value为null的置""
	 */
	private Map<String, String> treatMap(Map<String, String> map) {
		if(map == null) {
			return null;
		}
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getValue() == null) {
				map.put(entry.getKey(), "");
			}
		}
		return map;
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return mMap;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		if (mHeaderMap == null) {
			mHeaderMap = new HashMap<>();
		}
		return mHeaderMap;
	}
	
	@Override
	protected void deliverResponse(String response) {
		if (mListener != null) {
			mListener.onResponse(response);
		}
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		try {
			String responseStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(responseStr, HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return Response.error(new ParseError(e));
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.error(new ParseError(ex));
		}
	}
}
