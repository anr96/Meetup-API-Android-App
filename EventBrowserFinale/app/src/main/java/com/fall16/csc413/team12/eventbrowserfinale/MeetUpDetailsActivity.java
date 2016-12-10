package com.fall16.csc413.team12.eventbrowserfinale;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class MeetUpDetailsActivity extends SingleFragmentActivity {

	public static final String EXTRA_STORY_ID = "eventbroswer.story_id";

	public static Intent newIntent(Context packageContext, UUID storyId) {
		Intent intent = new Intent(packageContext, MeetUpDetailsActivity.class);
		intent.putExtra(EXTRA_STORY_ID, storyId);
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
