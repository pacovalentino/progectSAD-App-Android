package api;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.restdemo.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import api.interfaces.ServerCallback;
import entity.Patient;

public class GetStructuresByRegionApi {
    protected static final String URL = "http://10.0.2.2:8000/api/get-structures-by-region/";
    protected static final int METHOD = Request.Method.GET;

    public static void call(
            Context context,
            final String urlComplete,
            final String token,
            final ServerCallback serverCallback
            ) {
        Response.ErrorListener errorListener = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                serverCallback.onFail(volleyError);
            }
        };

        Response.Listener<String> listener = new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                try {
                    serverCallback.onSuccess(new JSONObject(s));
                } catch (JSONException e) {
                    serverCallback.onSuccess(new JSONObject());
                }
            }
        };

        final StringRequest request = new StringRequest(METHOD, URL + urlComplete, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("authorization", "Bearer " + token);
                params.put("Accept", "application/json");
                return params;
            }
        };
        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }
}
