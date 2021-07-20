package api;

import android.content.Context;

import com.android.volley.AuthFailureError;
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

import api.interfaces.ServerCallback;

public class LoginApi {
    protected static final String URL = "http://51.183.34.19:57017/api/login";
    protected static final int METHOD = Request.Method.POST;

    public static void call(final Context context, final String email, final String password, final ServerCallback callback) {
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
                }
            }
        };

        StringRequest request = new StringRequest(METHOD, URL, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("email", email);
                parameters.put("password", password);

                return parameters;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                return params;
            }
        };
        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }
}
