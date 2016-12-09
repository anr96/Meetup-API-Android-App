package com.fall16.csc413.team12.eventbrowserfinale;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.UUID;

/**
 * Created by AmandaNikkole on 11/27/16.
 */

public class EventBrowserFragment extends Fragment {
    private Story mStory;
    private TextView mNameField;
    private TextView mDescriptionField;
    private ImageView mImageView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID storyId = (UUID) getActivity().getIntent()
                .getSerializableExtra(EventBrowserActivity.EXTRA_STORY_ID);
        mStory = StoryLab.get(getActivity()).getStory(storyId);

    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        //explicitly inflate the fragment's view
        View v = inflater.inflate(R.layout.fragment_event_browser, container, false);
        mNameField = (TextView) v.findViewById(R.id.story_name);
        mNameField.setText(mStory.getStoryName());
        mDescriptionField = (TextView) v.findViewById(R.id.story_description);
        mDescriptionField.setText(mStory.getDescription());
        mImageView = (ImageView) v.findViewById(R.id.story_card);
        mImageView.setImageResource(R.drawable.shrek);

        return v;
    }

}
