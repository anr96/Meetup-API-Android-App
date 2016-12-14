package com.fall16.csc413.team12.eventbrowserfinale;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.UUID;

public class MeetUpDetailsActivity extends SingleFragmentActivity {

	public static final String EVENTBROSWER_MEET_UP_ID = "eventbroswer.meet_up_id";
	private static final String TAG = "MeetUpDetailsActivity";

	public static Intent newIntent(Context packageContext, UUID storyId) {
		Intent intent = new Intent(packageContext, MeetUpDetailsActivity.class);
		intent.putExtra(EVENTBROSWER_MEET_UP_ID, storyId);
		return intent;
	}

	@Override
	protected Fragment createFragment() {
		return new MeetUpDetailsFragment();
	}


	@Override
	public boolean onQueryTextSubmit(String query) {
		System.out.println("search query submit");
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		System.out.println("tap");
		return false;
	}
}
