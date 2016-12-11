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
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AmandaNikkole on 11/27/16.
 */

public class MeetUpListFragment extends Fragment implements SearchView.OnQueryTextListener,
		GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private RecyclerView mStoryRecyclerView;
    private StoryAdapter mAdapter;
	//JsonController controller;

	/**
	 * Provides the entry point to Google Play services.
	 */
	private GoogleApiClient mGoogleApiClient;

	/**
	 * Represents a geographical location.
	 */
	private Location mLastLocation;
	private double mLatitude;
	private double mLongitude;

	private static final String TAG = "MeetUpListFragment";

	// int required to ask permission for location
	private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		// Create an instance of GoogleAPIClient.
		buildGoogleApiClient();
	}

	@Override
<<<<<<< HEAD:EventBrowserFinale/app/src/main/java/com/fall16/csc413/team12/eventbrowserfinale/StoryListFragment.java
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_story_list, container, false);
=======
	public void onConnected(Bundle connectionHint) {

		if (ContextCompat.checkSelfPermission(App.getContext(),
				android.Manifest.permission.ACCESS_COARSE_LOCATION )
				!= PackageManager.PERMISSION_GRANTED ) {

			ActivityCompat.requestPermissions(getActivity(), new String[] {
					android.Manifest.permission.ACCESS_COARSE_LOCATION },
					MY_PERMISSION_ACCESS_COARSE_LOCATION);
		}

		mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
				mGoogleApiClient);
		if (mLastLocation != null) {
			mLatitude = mLastLocation.getLatitude();
			Log.i(TAG, "Latitude is: " + mLatitude);
			mLongitude = mLastLocation.getLongitude();
			Log.i(TAG, "Longitude is: " + mLongitude);

			//mLatitudeTextView.setText(String.valueOf(mLastLocation.getLatitude()));
			//mLongitudeTextView.setText(String.valueOf(mLastLocation.getLongitude()));
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_meet_up_list, container, false);
>>>>>>> origin:EventBrowserFinale/app/src/main/java/com/fall16/csc413/team12/eventbrowserfinale/MeetUpListFragment.java

		// Create the toolbar for this fragment
		Toolbar toolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
		AppCompatActivity activity = (AppCompatActivity) getActivity();
		activity.setSupportActionBar(toolbar);

		// Hides the title defined in manifest.xml as app_name from Toolbar
		((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

		mStoryRecyclerView = (RecyclerView) view.findViewById(R.id.story_recycler_view);
		mStoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		updateUI();

		// Required for SearchView implementation
		//setHasOptionsMenu(true);

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

	/**
	 * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
	 */
	protected synchronized void buildGoogleApiClient() {

		if (mGoogleApiClient == null) {
			mGoogleApiClient = new GoogleApiClient.Builder(App.getContext())
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.addApi(LocationServices.API)
					.build();
		}
	}

	protected void createLocationRequest() {
		LocationRequest mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(10000);
		mLocationRequest.setFastestInterval(5000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
		// Provides a simple way of getting a device's location and is well suited for
		// applications that do not require a fine-grained location and that do not need location
		// updates. Gets the best and most recent location currently available, which may be null
		// in rare cases when a location is not available.

		// Check for location permissions
		if (ContextCompat.checkSelfPermission(App.getContext(),
				android.Manifest.permission.ACCESS_COARSE_LOCATION )
				!= PackageManager.PERMISSION_GRANTED ) {

			ActivityCompat.requestPermissions(getActivity(), new String[] {
					android.Manifest.permission.ACCESS_COARSE_LOCATION },
					MY_PERMISSION_ACCESS_COARSE_LOCATION);
		}

		getLocation();

		if (mLastLocation != null) {
			mLatitude = mLastLocation.getLatitude();
			Log.i(TAG, "Latitude is: " + mLatitude);
			mLongitude = mLastLocation.getLongitude();
			Log.i(TAG, "Longitude is: " + mLongitude);
		}
		else {
			Toast.makeText(getActivity(), R.string.no_location_detected, Toast.LENGTH_LONG).show();
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

    // ViewHolder holds onto a View
    private class StoryHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private TextView mNameTextView;
        private TextView mDescriptionTextView;
        private TextView mStoryNameTextView;
        private ImageView mImageView;
        private Story mStory;



        public StoryHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);

            mNameTextView = (TextView) itemView.findViewById(R.id.list_item_card_name);
            mStoryNameTextView = (TextView) itemView.findViewById(R.id.list_item_story_name);

            mDescriptionTextView = (TextView) itemView.findViewById(R.id.list_item_story_description);
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
