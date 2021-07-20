package api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import api.payload.RegisterPayload;
import api.interfaces.ServerCallback;

public class RegisterApi {
    protected final static String URL = "http://51.183.34.19:57017/api/register";
    protected final static int METHOD = Request.Method.POST;

    public static void call(final Context context, final RegisterPayload payload, final ServerCallback callback) {
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onFail(volleyError);
            }
        };

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    callback.onSuccess(new JSONObject(s));
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onSuccess(new JSONObject());
                }
            }
        };

        StringRequest request = new StringRequest(METHOD, URL, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                return payload.toHashMap();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Accept", "application/json");
                return params;
            }
        };
        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }
}
