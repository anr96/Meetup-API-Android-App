package com.fall16.csc413.team12.eventbrowserfinale;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class MeetUpDetailsActivity extends SingleFragmentActivity {

	public static final String EVENTBROSWER_MEET_UP_ID = "eventbroswer.meet_up_id";

	public static Intent newIntent(Context packageContext, String id) {
		Intent intent = new Intent(packageContext, MeetUpDetailsActivity.class);
		intent.putExtra(EVENTBROSWER_MEET_UP_ID, id);
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
