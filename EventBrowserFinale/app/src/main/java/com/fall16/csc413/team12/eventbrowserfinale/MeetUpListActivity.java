package com.fall16.csc413.team12.eventbrowserfinale;

import android.support.v4.app.Fragment;

/**
 * Created by AmandaNikkole on 11/27/16.
 */

public class MeetUpListActivity extends SingleFragmentActivity {

	private static final String TAG = "MeetUpListActivity";

	@Override
    protected Fragment createFragment() {
        return new MeetUpListFragment();
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
