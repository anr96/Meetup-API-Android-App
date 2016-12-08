package com.fall16.csc413.team12.eventbrowserfinale;

import android.support.v4.app.Fragment;

/**
 * Created by AmandaNikkole on 11/27/16.
 */

public class StoryListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new StoryListFragment();
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
