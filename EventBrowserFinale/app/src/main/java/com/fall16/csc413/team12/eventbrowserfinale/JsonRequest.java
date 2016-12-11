package com.fall16.csc413.team12.eventbrowserfinale;

import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.fall16.csc413.team12.eventbrowserfinale.MeetUpListFragment.PREFS_NAME;

/**
 * Created by wgalan on 12/7/16.
 */

public class JsonRequest extends Request<List<MeetUp>> {

    // Success listener implemented in controller
    private Response.Listener<List<MeetUp>> successListener;

	SharedPreferences settings = App.getContext().getSharedPreferences(PREFS_NAME, 0);
	private String LatString = settings.getString("Latitude", "");
	private String LongString = settings.getString("Longitude", "");

    /**
     * Class constructor
     * @param method            Request method
     * @param url               url to API
     * @param successListener   success listener
     * @param errorListener     failure listener
     */
    public JsonRequest( int method,
                        String url,
                        Response.Listener<List<MeetUp>> successListener,
                        Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.successListener = successListener;
    }

    @Override
    protected Response<List<MeetUp>> parseNetworkResponse(NetworkResponse response) {
        // Convert byte[] data received in the response to String
        String jsonString = new String(response.data);
        List<MeetUp> meetUps;
        JSONObject jsonObject;
        Log.i(this.getClass().getName(), jsonString);
        // Try to convert JsonString to list of meetups
        try {
            // Convert JsonString to JSONObject
            //W/System.err: TODO
            jsonObject = new JSONObject(jsonString);
            // Get list of meetups from received JSON
            meetUps = MeetUp.parseJson(jsonObject);
        }
        // in case of exception, return volley error
        catch (JSONException e) {
            e.printStackTrace();
            // return new volley error with message
            return Response.error(new VolleyError("Failed to process the request"));
        }
        // return list of movies
        return Response.success(meetUps, getCacheEntry());
    }

    @Override
    protected void deliverResponse(List<MeetUp> movies) {
        successListener.onResponse(movies);
    }
}
