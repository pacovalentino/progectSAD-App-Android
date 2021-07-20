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

public class GetLastReservationByPatientEmailApi {
    protected static final String URL = "http://51.183.34.19:57017/api/get-last-reservation-by-patient-email/";
    protected static final int METHOD = Request.Method.GET;

    public static void call(
            Context context,
            final String email,
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

        final StringRequest request = new StringRequest(METHOD, URL + email, listener, errorListener) {
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
