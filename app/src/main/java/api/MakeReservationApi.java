package api;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import entity.Patient;
import api.interfaces.ServerCallback;

public class MakeReservationApi {
    protected static final String URL = "http://10.0.2.2:8000/api/reservation";
    protected static final int METHOD = Request.Method.POST;

    public static void call(
            Context context,
            final Patient patient,
            final String date,
            final String structureId,
            final String token,
            final ServerCallback serverCallback
            ) {
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                serverCallback.onFail(volleyError);
            }
        };

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    serverCallback.onSuccess(new JSONObject(s));
                } catch (JSONException e) {
                    serverCallback.onSuccess(new JSONObject());
                }
            }
        };

        final StringRequest request = new StringRequest(METHOD, URL, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("patient_id", patient.getId());
                parameters.put("date", date);
                parameters.put("structure_id", structureId);
                return parameters;
            }

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
