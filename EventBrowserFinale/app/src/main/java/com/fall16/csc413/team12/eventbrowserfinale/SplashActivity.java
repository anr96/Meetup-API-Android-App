package com.fall16.csc413.team12.eventbrowserfinale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by AmandaNikkole on 11/30/16.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, StoryListActivity.class);
        startActivity(intent);
        finish();
    }
}
