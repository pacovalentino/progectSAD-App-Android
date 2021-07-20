package api.utils;

import android.util.Log;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class VolleyErrorHandler {
    public static StringBuilder getValidationToastMessage(JSONObject errorPayload) throws JSONException {
        StringBuilder toastError = new StringBuilder();
        JSONObject errors = new JSONObject(errorPayload.getString("errors"));
        Iterator<String> keys = errors.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            JSONArray subErrors = errors.getJSONArray(key);
            for (int i = 0; i < subErrors.length(); i++) {
                if (!toastError.toString().equals("")) {
                    toastError.append("\n");
                }
                toastError.append("- ").append(subErrors.getString(i));
                Log.e("error", subErrors.getString(i));
            }
        }
        return toastError;
    }

    public static StringBuilder getToastMessage(VolleyError volleyError) {
        try {
            String errorBody = new String(volleyError.networkResponse.data, StandardCharsets.UTF_8);
            errorBody = errorBody + "}";
            JSONObject errorPayload = new JSONObject(errorBody);
            String exceptionMessage = errorPayload.getString("message");
            if (exceptionMessage.equals("validation")) {
                return getValidationToastMessage(errorPayload);
            } else {
                return new StringBuilder(exceptionMessage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return new StringBuilder("Impossibile decifrare risposta del server");
        }
    }
}
