package com.fall16.csc413.team12.eventbrowserfinale;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;

import java.util.List;

/**
 * Created by wgalan on 12/7/16.
 */

/*
public class JsonController {

	private final int TAG = 100;

	private OnResponseListener responseListener;

	public JsonController(OnResponseListener responseListener) {
		this.responseListener = responseListener;
	}

	// Adds request to volley request queue
	public void sendRequest(String query){

		// Request Method
		int method = Request.Method.GET;

		// Url with GET parameters
		String url = "http://www.omdbapi.com/?s=" + Uri.encode(query) + "&t=movie";

		// Create new request using JsonRequest
		JsonRequest request
				= new JsonRequest(
				method,
				url,
				new Response.Listener<List<Story>>() {
					@Override
					public void onResponse(List<Story> movies) {
						responseListener.onSuccess(movies);
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
		void onSuccess(List<Story> movies);
		void onFailure(String errorMessage);
	}

}
*/
