package api.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface ServerCallback{
    void onSuccess(JSONObject result);
    void onFail(VolleyError volleyError);
}
