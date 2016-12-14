package com.fall16.csc413.team12.eventbrowserfinale;

import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonRequest;
import com.fall16.csc413.team12.eventbrowserfinale.JsonRequest;

import java.util.List;

import static com.fall16.csc413.team12.eventbrowserfinale.MeetUpListFragment.PREFS_NAME;

/**
 * Created by wgalan on 12/7/16.
 */


public class JsonController {

    private final int TAG = 100;

	// Personal API Key for MeetUP
	private static final String API_KEY = "16742d314d494331274718b366222c";

	SharedPreferences settings = App.getContext().getSharedPreferences(PREFS_NAME, 0);
	private String Latitude = settings.getString("Latitude", "");
	private String Longitude = settings.getString("Longitude", "");

    private OnResponseListener responseListener;

    public JsonController(OnResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    // Adds request to volley request queue
	public void sendRequest() {

        // Request Method
        int method = Request.Method.GET;


        // Url with GET parameters
        String url = "https://api.meetup.com/find/groups?&sign=" +
                "true&photo-host=public&lon=" + Longitude +
                "&lat=" + Latitude + "&page=10&" + "&key=" + API_KEY;
        

        // Create new request using JsonRequest
        JsonRequest request
                = new JsonRequest(
                method,
                url,
                //done with request, let you know if it works with 1/2 listeners
                new Response.Listener<List<MeetUp>>() {
                    @Override
                    public void onResponse(List<MeetUp> meetUps) {
                        responseListener.onSuccess(meetUps); //seen in main activity
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        responseListener.onFailure(error.getMessage());
                    }
                }
        );

        // Add tag to request
        request.setTag(TAG);

        // Get RequestQueue from VolleySingleton
        VolleySingleton.getInstance(App.getContext()).addToRequestQueue(request);
    }

    // Cancels all request pending in request queue
    public void cancelAllRequests() {
        VolleySingleton.getInstance(App.getContext()).cancelAllRequests(TAG);
    }

    // Interface to communicate between {@link android.app.Activity} and {@link JsonRequest}
    public interface OnResponseListener {
        void onSuccess(List<MeetUp> meetUps);
        void onFailure(String errorMessage);
    }

}

