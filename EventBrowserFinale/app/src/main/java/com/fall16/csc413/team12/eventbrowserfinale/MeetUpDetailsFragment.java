package com.fall16.csc413.team12.eventbrowserfinale;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.UUID;

/**
 * Created by AmandaNikkole on 11/27/16.
 */

public class MeetUpDetailsFragment extends Fragment {
    private MeetUp mMeetUp;
    private TextView mNameField;
    private TextView mDescriptionField;
	//private TextView mNumberOfGroupMembersField;
    private ImageView mImageView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //TODO fix EVENTBROSWER_MEET_UP_ID
        UUID meetUpId = (UUID) getActivity().getIntent()
                .getSerializableExtra(MeetUpDetailsActivity.EVENTBROSWER_MEET_UP_ID);
        mMeetUp = MeetUpLab.get(getActivity()).getMeetUp(meetUpId);

    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        //explicitly inflate the fragment's view
        View v = inflater.inflate(R.layout.fragment_meet_up_details, container, false);
        mNameField = (TextView) v.findViewById(R.id.story_name);
        mNameField.setText(mMeetUp.getGroupName());
        mDescriptionField = (TextView) v.findViewById(R.id.story_description);
        mDescriptionField.setText(mMeetUp.getGroupDescription());

		//TODO fix to appropriate fields
        mImageView = (ImageView) v.findViewById(R.id.story_card);
        mImageView.setImageResource(R.drawable.shrek);

        return v;
    }

}
