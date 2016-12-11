package com.fall16.csc413.team12.eventbrowserfinale;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by AmandaNikkole on 11/27/16.
 */

public class MeetUpListFragment extends Fragment implements SearchView.OnQueryTextListener,
		GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

	private static final String TAG = "MeetUpListFragment";

	// The desired interval for location updates. Inexact. Updates may be more or less frequent.
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

	// The fastest rate for active location updates. Exact
	public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
			UPDATE_INTERVAL_IN_MILLISECONDS / 2;

	// Keys for storing activity state in the Bundle.
	protected final static String REQUESTING_LOCATION_UPDATES_KEY =
			"requesting-location-updates-key";
	protected final static String LOCATION_KEY = "location-key";
	protected final static String LAST_UPDATED_TIME_STRING_KEY =
			"last-updated-time-string-key";

	// Provides the entry point to Google Play services.
	private GoogleApiClient mGoogleApiClient;

	// Stores parameters for requests to the FusedLocationProviderApi.
	private LocationRequest mLocationRequest;

	// Represents a geographical location.
	protected Location mCurrentLocation;

	public static double mLatitude;
	private double mLongitude;

	// Tracks the status of the location updates request.
	protected Boolean mRequestingLocationUpdates = true;

	// Time when the location was updated represented as a String.
	protected String mLastUpdateTime;

	// int required to ask permission for location
	private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;

	private RecyclerView mStoryRecyclerView;
	private StoryAdapter mAdapter;
	//JsonController controller;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_meet_up_list, container, false);

		// Create the toolbar for this fragment
		Toolbar toolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
		AppCompatActivity activity = (AppCompatActivity) getActivity();
		activity.setSupportActionBar(toolbar);

		// Hides the title defined in manifest.xml as app_name from Toolbar
		((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

		mStoryRecyclerView = (RecyclerView) view.findViewById(R.id.story_recycler_view);
		mStoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		mRequestingLocationUpdates = false;
		mLastUpdateTime = "";

		// Update values using data stored in the Bundle.
		updateValuesFromBundle(savedInstanceState);

		// Create an instance of GoogleAPIClient.
		buildGoogleApiClient();

		// Required for SearchView implementation
		setHasOptionsMenu(true);

		updateUI();

		/*
		controller = new JsonController(
				new JsonController.OnResponseListener() {
					@Override
					public void onSuccess(List<Movie> movies) {
						if(movies.size() > 0) {
							textView.setVisibility(View.GONE);
							recyclerView.setVisibility(View.VISIBLE);
							recyclerView.invalidate();
							adapter.updateDataSet(movies);
							recyclerView.setAdapter(adapter);
						}
					}

					@Override
					public void onFailure(String errorMessage) {
						textView.setVisibility(View.VISIBLE);
						textView.setText("Failed to retrieve data");
						Toast.makeText(MainActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
					}
				});
				*/

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.menu_main, menu);

		MenuItem searchMenuItem = menu.findItem(R.id.search);
		searchMenuItem.expandActionView();
		MenuItemCompat.setOnActionExpandListener(searchMenuItem,
				new MenuItemCompat.OnActionExpandListener() {
					@Override
					public boolean onMenuItemActionExpand(MenuItem item) {
						return true;
					}

					@Override
					public boolean onMenuItemActionCollapse(MenuItem item) {
						// Refresh the list when pressing back button on Search widget
						updateUI();
						return true;
					}
				});

		// Associate searchable configuration with the SearchView
		SearchManager searchManager =
				(SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

		// Use the AppCompat version of SearchView (the one in the support.v7 package) and
		// define it in menu_main.xml with app:actionViewClass instead of android:actionViewClass
		SearchView searchView =
				(SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
		searchView.setSearchableInfo(searchManager.
				getSearchableInfo(getActivity().getComponentName()));
		searchView.setOnQueryTextListener(this);
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		//Log.d(TAG, "QueryTextSubmit: " + query);
		//mAdapter.getFilter().filter(query);
		//updateUI();

		/*
		if(query.length() > 1) {
			controller.cancelAllRequests();
			controller.sendRequest(query);
			return false;
		} else {
			Toast.makeText(MainActivity.this, "Must provide more than one character", Toast.LENGTH_SHORT).show();
			recyclerView.setVisibility(View.GONE);
			textView.setVisibility(View.VISIBLE);
			textView.setText("Must provide more than one character to search");
			return true;
		}
		*/
		return false;
	}

	// Call filter based on single character change
	@Override
	public boolean onQueryTextChange(String newText) {
		mStoryRecyclerView.setVisibility(View.VISIBLE);
		mAdapter.getFilter().filter(newText);

		/*
		if(newText.length() > 1) {
			controller.cancelAllRequests();
			controller.sendRequest(newText);
		} else if(newText.equals("")) {
			recyclerView.setVisibility(View.GONE);
			textView.setVisibility(View.VISIBLE);
		}
		*/
		return true;
	}

	private void updateValuesFromBundle(Bundle savedInstanceState) {
		Log.i(TAG, "Updating values from bundle");
		if (savedInstanceState != null) {
			// Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
			// the Start Updates and Stop Updates buttons are correctly enabled or disabled.
			if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
				mRequestingLocationUpdates = savedInstanceState.getBoolean(
						REQUESTING_LOCATION_UPDATES_KEY);
			}

			// Update the value of mCurrentLocation from the Bundle and update the UI to show the
			// correct latitude and longitude.
			if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
				// Since LOCATION_KEY was found in the Bundle, we can be sure that mCurrentLocation
				// is not null.
				mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
			}

			// Update the value of mLastUpdateTime from the Bundle and update the UI.
			if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
				mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
			}
		}
	}

	// Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
	protected synchronized void buildGoogleApiClient() {

		Log.i(TAG, "Building GoogleApiClient");

		mGoogleApiClient = new GoogleApiClient.Builder(App.getContext())
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();

		createLocationRequest();
	}

	protected void createLocationRequest() {

		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	}

	// Requests location updates from the FusedLocationApi.
	protected void startLocationUpdates() {

		// Check for location permissions
		if (ContextCompat.checkSelfPermission(App.getContext(),
				android.Manifest.permission.ACCESS_COARSE_LOCATION )
				!= PackageManager.PERMISSION_GRANTED ) {

			ActivityCompat.requestPermissions(getActivity(), new String[] {
							android.Manifest.permission.ACCESS_COARSE_LOCATION },
					MY_PERMISSION_ACCESS_COARSE_LOCATION);
		}

		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, this);
	}

	// Removes location updates from the FusedLocationApi.
	protected void stopLocationUpdates() {
		// It is a good practice to remove location requests when the activity is in a paused or
		// stopped state. Doing so helps battery performance and is especially
		// recommended in applications that request frequent location updates.

		LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
	}

	@Override
	public void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	public void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {

		Log.i(TAG, "Connected to GoogleApiClient");

		// Check for location permissions
		if (ContextCompat.checkSelfPermission(App.getContext(),
				android.Manifest.permission.ACCESS_COARSE_LOCATION )
				!= PackageManager.PERMISSION_GRANTED ) {

			ActivityCompat.requestPermissions(getActivity(), new String[] {
					android.Manifest.permission.ACCESS_COARSE_LOCATION },
					MY_PERMISSION_ACCESS_COARSE_LOCATION);
		}

		if (mCurrentLocation == null) {
			mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
			mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

			mLatitude = mCurrentLocation.getLatitude();
			Log.i(TAG, "Latitude is: " + mLatitude);
			mLongitude = mCurrentLocation.getLongitude();
			Log.i(TAG, "Longitude is: " + mLongitude);

			/*
			Intent in = new Intent(App.getContext(), JsonRequest.class);
			in.putExtra("lat", mLatitude);
			in.putExtra("long", mLongitude);
			startActivity(in);
			*/
		}

		if (mRequestingLocationUpdates) {
			startLocationUpdates();
		}
	}

	@Override
	public void onConnectionSuspended(int i) {
		Log.i(TAG, "Connection Suspended");
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
	}

	@Override
	public void onLocationChanged(Location location) {
		mCurrentLocation = location;
		mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

		mLatitude = mCurrentLocation.getLatitude();
		Log.i(TAG, "Location Changed. New Latitude is: " + mLatitude);
		mLongitude = mCurrentLocation.getLongitude();
		Log.i(TAG, "Location Changed. New Longitude is: " + mLongitude);
	}

	// Stores activity data in the Bundle.
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
		savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
		savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
		super.onSaveInstanceState(savedInstanceState);
	}

    // ViewHolder holds onto a View
    private class StoryHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private TextView mNameTextView;
        private TextView mDescriptionTextView;
        private TextView mStoryNameTextView;
        private ImageView mImageView;
        private Story mStory;

        public StoryHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);

            mNameTextView = (TextView) itemView.findViewById(R.id.list_item_card_name);
            mStoryNameTextView = (TextView) itemView.findViewById(R.id.list_item_story_name);

            mDescriptionTextView =
					(TextView) itemView.findViewById(R.id.list_item_story_description);
            mImageView = (ImageView) itemView.findViewById(R.id.list_item_story_card);
        }

        
        public void bindStory(Story story){
            mStory = story;
            mNameTextView.setText(mStory.getName());
            mDescriptionTextView.setText(mStory.getDescription());
            mStoryNameTextView.setText(mStory.getStoryName());
            mImageView.setImageResource(R.drawable.shrek);
        }

        @Override
        public void onClick(View v){
            Intent intent = MeetUpDetailsActivity.newIntent(getActivity(), mStory.getUuid());
			startActivity(intent);

        }
    }


    private class StoryAdapter extends RecyclerView.Adapter<StoryHolder> implements Filterable {

		private List<Story> mStories;
		private List<Story> mStoriesCopy;
		private Filter mStoryFilter;

        public StoryAdapter(List<Story> stories){

			mStories = stories;
			mStoriesCopy = stories;
        }

        @Override
        public StoryHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_meet_up,
                    parent, false);
            return new StoryHolder(view);
        }

        @Override
        public void onBindViewHolder(StoryHolder holder, int position){
            holder.mStoryNameTextView.setText(mStories.get(position).getStoryName());
            Story story = mStories.get(position);
            holder.bindStory(story);
        }

        @Override
        public int getItemCount(){
			return mStories.size();
        }


		@Override
		public Filter getFilter() {
			if (mStoryFilter == null) {
				mStoryFilter = new StoryFilter();
			}
			return mStoryFilter;
		}

		private class StoryFilter extends Filter {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();

				// We implement the filter logic here
				if (constraint == null || constraint.length() == 0) {
					// No filter implemented we return all the list
					results.values = mStories;
					results.count = mStories.size();

				}
				else {
					// We perform filtering operation
					List<Story> filteredStories = new ArrayList<Story>();

					for (Story s : mStoriesCopy) {
						if (s.getStoryName().toUpperCase().startsWith(constraint.
								toString().toUpperCase())) {
							filteredStories.add(s);
						}
					}

					results.values = filteredStories;
					results.count = filteredStories.size();
				}
				return results;
			}

			@Override
			protected void publishResults (CharSequence constraint, FilterResults results) {
				mStories = (List<Story>) results.values;
				notifyDataSetChanged();
			}
		}
    }

    private void updateUI(){
        StoryLab storyLab = StoryLab.get(getActivity());
        List<Story> stories = storyLab.getStories();

        mAdapter = new StoryAdapter(stories);
        mStoryRecyclerView.setAdapter(mAdapter);
    }
}
