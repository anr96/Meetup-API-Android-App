package com.fall16.csc413.team12.eventbrowserfinale;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
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
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by AmandaNikkole on 11/27/16.
 */

/*
public class MeetUpListFragment extends Fragment implements SearchView.OnQueryTextListener,
		GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
		LocationListener, ResultCallback<LocationSettingsResult> { */
public class MeetUpListFragment extends Fragment implements SearchView.OnQueryTextListener,
		GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
		LocationListener {

	private static final String TAG = "MeetUpListFragment";
	public static final String PREFS_NAME = "MyPrefsFile";

	// int required to ask permission for location
	private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 11;

	public static final int API_LEVEL = 24;

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

	public static final double DEFAULT_LATITUDE = 37.774929;
	public static final double DEFAULT_LONGITUDE = -122.419416;

	// Constant used in the location settings dialog.
	//protected static final int REQUEST_CHECK_SETTINGS = 0x1;

	// Provides the entry point to Google Play services.
	private GoogleApiClient mGoogleApiClient;

	// Stores parameters for requests to the FusedLocationProviderApi.
	private LocationRequest mLocationRequest;

	// Stores the types of location services the client is interested in using. Used for checking
	// settings to determine if the device has optimal location settings.
	//protected LocationSettingsRequest mLocationSettingsRequest;

	// Represents a geographical location.
	protected Location mCurrentLocation;

	public static double mLatitude;
	private double mLongitude;

	// Tracks the status of the location updates request.
	protected Boolean mRequestingLocationUpdates = true;

	// Time when the location was updated represented as a String.
	protected String mLastUpdateTime;

	private RecyclerView mMeetUpRecyclerView;
	private MeetUpAdapter mAdapter;
	JsonController mController;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

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

		mMeetUpRecyclerView = (RecyclerView) view.findViewById(R.id.story_recycler_view);
        mMeetUpRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		mLastUpdateTime = "";

		// Update values using data stored in the Bundle.
		updateValuesFromBundle(savedInstanceState);

		// Create an instance of GoogleAPIClient.
		buildGoogleApiClient();

		//buildLocationSettingsRequest();

		//checkLocationSettings();

		// Required for SearchView implementation
		setHasOptionsMenu(true);

		mController = new JsonController(
				new JsonController.OnResponseListener() {
					@Override
					public void onSuccess(List<MeetUp> meetUps) {
						if(meetUps.size() > 0) {
							mMeetUpRecyclerView.setVisibility(View.VISIBLE);
							mMeetUpRecyclerView.invalidate();
							mAdapter.updateDataSet(meetUps);
							mMeetUpRecyclerView.setAdapter(mAdapter);
						}
					}

					@Override
					public void onFailure(String errorMessage) {
						Toast.makeText(getContext(), "Failed to retrieve data",
								Toast.LENGTH_SHORT).show();
					}
				});

		// Request data from MeetUp API
		mController.sendRequest();

		updateUI();

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.menu_main, menu);

		MenuItem searchMenuItem = menu.findItem(R.id.search);
		//searchMenuItem.expandActionView();
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
		searchView.clearFocus();
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		Log.d(TAG, "QueryTextSubmit: " + query);
		return false;
	}

	// Call filter based on single character change
	@Override
	public boolean onQueryTextChange(String newText) {
		Log.d(TAG, "QueryTextChange: " + newText);
		mMeetUpRecyclerView.setVisibility(View.VISIBLE);
		mAdapter.getFilter().filter(newText);

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

	/*
	// Used for checking if a device has the needed location settings.
	protected void buildLocationSettingsRequest() {
		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
		builder.addLocationRequest(mLocationRequest);
		mLocationSettingsRequest = builder.build();
	}
	*/

	/*
	// Check if the device's location settings are adequate for the app's needs
	protected void checkLocationSettings() {
		PendingResult<LocationSettingsResult> result =
				LocationServices.SettingsApi.checkLocationSettings(
						mGoogleApiClient,
						mLocationSettingsRequest
				);
		result.setResultCallback(this);
	}
	*/

	/*
	// Determines if location settings are adequate. If they are not, begins the process of
	// presenting a location settings dialog to the user.
	@Override
	public void onResult(LocationSettingsResult locationSettingsResult) {
		final Status status = locationSettingsResult.getStatus();
		switch (status.getStatusCode()) {
			case LocationSettingsStatusCodes.SUCCESS:
				Log.i(TAG, "All location settings are satisfied.");
				startLocationUpdates();
				break;
			case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
				Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
						"upgrade location settings ");

				try {
					// Show the dialog by calling startResolutionForResult(), and check the result
					// in onActivityResult().
					status.startResolutionForResult(MeetUpListActivity.this,
							REQUEST_CHECK_SETTINGS);
				} catch (IntentSender.SendIntentException e) {
					Log.i(TAG, "PendingIntent unable to execute request.");
				}
				break;
			case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
				Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
						"not created.");
				break;
		}
	}
	*/

	/*
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			// Check for the integer request code originally supplied to startResolutionForResult().
			case REQUEST_CHECK_SETTINGS:
				switch (resultCode) {
					case Activity.RESULT_OK:
						Log.i(TAG, "User agreed to make required location settings changes.");
						startLocationUpdates();
						break;
					case Activity.RESULT_CANCELED:
						Log.i(TAG, "User chose not to make required location settings changes.");
						break;
				}
				break;
		}
	}
	*/

	// Requests location updates from the FusedLocationApi.
	protected void startLocationUpdates() {

		// Check for location permissions
		if (ContextCompat.checkSelfPermission(App.getContext(),
				android.Manifest.permission.ACCESS_FINE_LOCATION )
				!= PackageManager.PERMISSION_GRANTED ) {

			ActivityCompat.requestPermissions(getActivity(), new String[] {
							android.Manifest.permission.ACCESS_FINE_LOCATION },
					MY_PERMISSION_ACCESS_FINE_LOCATION);
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

	@Override
	public void onResume() {
		super.onResume();

		if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
			startLocationUpdates();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		// Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
		if (mGoogleApiClient.isConnected()) {
			stopLocationUpdates();
		}
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
				android.Manifest.permission.ACCESS_FINE_LOCATION )
				!= PackageManager.PERMISSION_GRANTED ) {

			ActivityCompat.requestPermissions(getActivity(), new String[] {
							android.Manifest.permission.ACCESS_FINE_LOCATION },
					MY_PERMISSION_ACCESS_FINE_LOCATION);
		}
		else {
			mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
			//if (mCurrentLocation == null) {
			if (mCurrentLocation != null) {
				Log.i(TAG, "mCurrentLocation was not null");
				mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

				mLatitude = mCurrentLocation.getLatitude();
				Log.i(TAG, "Latitude is: " + mLatitude);
				mLongitude = mCurrentLocation.getLongitude();
				Log.i(TAG, "Longitude is: " + mLongitude);

				// We need an Editor object to make preference changes.
				// All objects are from android.context.Context
				SharedPreferences settings = App.getContext().getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("Latitude", String.valueOf(mLatitude));
				editor.putString("Longitude", String.valueOf(mLongitude));
				// Commit the edits!
				editor.apply();
			}
			else {
				Log.i(TAG, "mCurrentLocation was null");

				mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

				mLatitude = DEFAULT_LATITUDE;
				Log.i(TAG, "Latitude is: " + mLatitude);
				mLongitude = DEFAULT_LONGITUDE;
				Log.i(TAG, "Longitude is: " + mLongitude);

				// We need an Editor object to make preference changes.
				// All objects are from android.context.Context
				SharedPreferences settings = App.getContext().getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("Latitude", String.valueOf(mLatitude));
				editor.putString("Longitude", String.valueOf(mLongitude));
				// Commit the edits!
				editor.apply();
			}
		}

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions,
										   int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSION_ACCESS_FINE_LOCATION:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.
						PERMISSION_GRANTED) {
					//continueYourTask
					if (ContextCompat.checkSelfPermission(App.getContext(),
							android.Manifest.permission.ACCESS_FINE_LOCATION )
							!= PackageManager.PERMISSION_GRANTED ) {
						mCurrentLocation = LocationServices.FusedLocationApi.
								getLastLocation(mGoogleApiClient);
					}
					if (mCurrentLocation != null) {
						Log.i(TAG, "Permissions Requested. mCurrentLocation was not null");
						mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

						mLatitude = mCurrentLocation.getLatitude();
						Log.i(TAG, "Latitude is: " + mLatitude);
						mLongitude = mCurrentLocation.getLongitude();
						Log.i(TAG, "Longitude is: " + mLongitude);

						// We need an Editor object to make preference changes.
						// All objects are from android.context.Context
						SharedPreferences settings = App.getContext().getSharedPreferences
								(PREFS_NAME, 0);
						SharedPreferences.Editor editor = settings.edit();
						editor.putString("Latitude", String.valueOf(mLatitude));
						editor.putString("Longitude", String.valueOf(mLongitude));
						// Commit the edits!
						editor.apply();
					}
					else {
						Log.i(TAG, "Permissions Requests. mCurrentLocation was null");

						mLatitude = DEFAULT_LATITUDE;
						Log.i(TAG, "Latitude is: " + mLatitude);
						mLongitude = DEFAULT_LONGITUDE;
						Log.i(TAG, "Longitude is: " + mLongitude);

						// We need an Editor object to make preference changes.
						// All objects are from android.context.Context
						SharedPreferences settings = App.getContext().getSharedPreferences
								(PREFS_NAME, 0);
						SharedPreferences.Editor editor = settings.edit();
						editor.putString("Latitude", String.valueOf(mLatitude));
						editor.putString("Longitude", String.valueOf(mLongitude));
						// Commit the edits!
						editor.apply();
					}
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

	/**
 	* Recycler View Adapter
 	* */
    // ViewHolder holds onto a View
    private class MeetUpHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private TextView mNameTextView;
		private TextView mNumberMembersTextView;
        private TextView mDescriptionTextView;
		private NetworkImageView mImageView;
        private TextView mLinkTextView;


        private MeetUp mMeetUp;

        public MeetUpHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);

			mNameTextView = (TextView) itemView.findViewById(R.id.list_meet_up_name);
            mLinkTextView = (TextView) itemView.findViewById(R.id.list_meet_up_link);
			mLinkTextView.setClickable(true);

			mNumberMembersTextView = (TextView) itemView.findViewById(R.id.list_meet_up_members);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.list_meet_up_description);
            mImageView = (NetworkImageView) itemView.findViewById(R.id.nivPhoto);
        }

		void setName(String name) {
			String n = "Name:\n" + name;
			mNameTextView.setText(n);
		}

		void setPhotoUrl(String photoUrl){
			ImageLoader imageLoader = VolleySingleton.getInstance(App.getContext()).getImageLoader();
			mImageView.setImageUrl(photoUrl, imageLoader);
		}

        public void bindStory(MeetUp meetUp){
            mMeetUp = meetUp;
			ImageLoader imageLoader = VolleySingleton.getInstance(App.getContext()).getImageLoader();

            mNameTextView.setText(mMeetUp.getGroupName());
			mImageView.setImageUrl(mMeetUp.getGroupPhotoLinkURL(), imageLoader);

			// Remove HTML tags from Description String
			if (Build.VERSION.SDK_INT >= API_LEVEL) {
				mDescriptionTextView.setText(Html.fromHtml(mMeetUp.getGroupDescription(),
						Html.FROM_HTML_MODE_LEGACY));
			} else {
				mDescriptionTextView.setText(Html.fromHtml(mMeetUp.getGroupDescription()));
			}

			mLinkTextView.setClickable(true);
			mLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());

			if (Build.VERSION.SDK_INT >= API_LEVEL) {
				mLinkTextView.setText(Html.fromHtml(mMeetUp.getGroupVisitSiteLink(),
						Html.FROM_HTML_MODE_LEGACY));
			}
			else {
				mLinkTextView.setText(Html.fromHtml(mMeetUp.getGroupDescription()));
			}
        }

        @Override
        public void onClick(View v){
            Intent intent = MeetUpDetailsActivity.newIntent(getActivity(),
					mMeetUp.getGroupMeetUpId());
			startActivity(intent);

        }
    }

	/**
 	* Adapter begins here which calls the MeetUpHolder (CardView Holder)
 	* */
    private class MeetUpAdapter extends RecyclerView.Adapter<MeetUpHolder> implements Filterable {

		private List<MeetUp> mMeetUpList;
		private List<MeetUp> mMeetUpListCopy;
		private Filter mMeetUpFilter;
		//private OnClickListener listener;

        public MeetUpAdapter(List<MeetUp> meetUpList){

			mMeetUpList = meetUpList;
			mMeetUpListCopy = meetUpList;
        }

        @Override
        public MeetUpHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_meet_up,
                    parent, false);

            return new MeetUpHolder(view);
        }

        @Override
        public void onBindViewHolder(MeetUpHolder holder, int position){

			holder.mNameTextView.setText(mMeetUpList.get(position).getGroupName());
			holder.mDescriptionTextView.setText(mMeetUpList.get(position).getGroupDescription());
			holder.setPhotoUrl(mMeetUpList.get(position).getGroupPhotoLinkURL());
			holder.mNumberMembersTextView.setText(mMeetUpList.get(position).
					getNumberOfGroupMembers());
			MeetUp meetUp = mMeetUpList.get(position);
			holder.bindStory(meetUp);

        }

        @Override
        public int getItemCount(){
			return mMeetUpList.size();
        }

		/**
		 * Removes older data from MeetUpList and updates it.
		 * Once the data is updated, notifies RecyclerViewAdapter.
		 * @param modelList list of meetups
		 */
		public void updateDataSet(List<MeetUp> modelList) {
			this.mMeetUpList.clear();
			this.mMeetUpList.addAll(modelList);
			notifyDataSetChanged();
		}

		@Override
		public Filter getFilter() {
			if (mMeetUpFilter == null) {
				mMeetUpFilter = new MeetUpFilter();
			}
			return mMeetUpFilter;
		}


		private class MeetUpFilter extends Filter {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();

				// We implement the filter logic here
				if (constraint == null || constraint.length() == 0) {
					// No filter implemented we return all the list
					results.values = mMeetUpList;
					results.count = mMeetUpList.size();

				}
				else {
					// We perform filtering operation
					List<MeetUp> filteredMeetUps = new ArrayList<MeetUp>();

					for (MeetUp m : mMeetUpListCopy) {
						if (m.getGroupName().toUpperCase().startsWith(constraint.
								toString().toUpperCase())) {
							filteredMeetUps.add(m);
						}
					}

					results.values = filteredMeetUps;
					results.count = filteredMeetUps.size();
				}
				return results;
			}

			@Override
			protected void publishResults (CharSequence constraint, FilterResults results) {
				mMeetUpList = (List<MeetUp>) results.values;
				notifyDataSetChanged();
			}
		}
    }

    private void updateUI(){
        MeetUpLab meetUpLab = MeetUpLab.get(getActivity());
        List<MeetUp> meetUps = meetUpLab.getMeetUps();

        mAdapter = new MeetUpAdapter(meetUps);
        mMeetUpRecyclerView.setAdapter(mAdapter);
    }
}
