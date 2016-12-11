package com.fall16.csc413.team12.eventbrowserfinale;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.fall16.csc413.team12.eventbrowserfinale.MeetUpListFragment.mLatitude;

/**
 * Created by wgalan on 12/7/16.
 */


public class JsonRequest extends Request<List<Story>> {

	// Success listener implemented in controller
	private Response.Listener<List<Story>> successListener;

	private static final String TAG = "JsonRequest";

	double lat = mLatitude;

	public JsonRequest( int method,
						String url,
						Response.Listener<List<Story>> successListener,
						Response.ErrorListener errorListener) {
		super(method, url, errorListener);
		this.successListener = successListener;
	}

	@Override
	protected Response<List<Story>> parseNetworkResponse(NetworkResponse response) {
		// Convert byte[] data received in the response to String
		String jsonString = new String(response.data);
		List<Story> stories;
		JSONObject jsonObject;
		Log.i(this.getClass().getName(), jsonString);
		// Try to convert JsonString to list of movies
		try {
			// Convert JsonString to JSONObject
			jsonObject = new JSONObject(jsonString);
			// Get list of movies from received JSON
			movies = Movie.parseJson(jsonObject);
		}
		// in case of exception, return volley error
		catch (JSONException e) {
			e.printStackTrace();
			// return new volley error with message
			return Response.error(new VolleyError("Failed to process the request"));
		}
		// return list of movies
		return Response.success(movies, getCacheEntry());
	}

	@Override
	protected void deliverResponse(List<Story> movies) {
		successListener.onResponse(movies);
	}
}
