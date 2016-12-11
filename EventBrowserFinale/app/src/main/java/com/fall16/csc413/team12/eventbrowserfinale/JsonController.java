package com.fall16.csc413.team12.eventbrowserfinale;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonRequest;
import com.fall16.csc413.team12.eventbrowserfinale.JsonRequest;

import java.util.List;

/**
 * Created by wgalan on 12/7/16.
 */


public class JsonController {

    private final int TAG = 100;

    private OnResponseListener responseListener;

    public JsonController(OnResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    // Adds request to volley request queue
    public void sendRequest(String query){

//        String[] location = query.split(" ");
//        String longitude = location[0];
//        String latitude = location[1];

         String longitude = "0.1278";
         String latitude = "51.5074";

        //TODO latitude and longitude = 51.507351 & -0.127758
        // Request Method
        int method = Request.Method.GET;

        // Url with GET parameters
        String url = "https://api.meetup.com/find/groups?&sign=" +
                "true&photo-host=public&lon=" + longitude +
                "&lat=" + latitude + "&page=10&desc=" +
                Uri.encode(query) + "&key=103066133866724c245743e5b397b6c";

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
    // There is no way to control the request already processed
    public void cancelAllRequests() {
        VolleySingleton.getInstance(App.getContext()).cancelAllRequests(TAG);
    }

    // Interface to communicate between {@link android.app.Activity} and {@link JsonRequest}
    public interface OnResponseListener {
        void onSuccess(List<MeetUp> meetUps);
        void onFailure(String errorMessage);
    }

}

