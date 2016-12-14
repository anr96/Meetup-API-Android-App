package com.fall16.csc413.team12.eventbrowserfinale;

import android.os.Build;
import android.os.Bundle;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by AmandaNikkole on 11/27/16.
 */

public class MeetUpDetailsFragment extends Fragment {
    private MeetUp mMeetUp;
    private TextView mNameField;
    private TextView mDescriptionField;
	private TextView mNumberOfGroupMembersField;
    private NetworkImageView mImageView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        String meetUpId = (String) getActivity().getIntent()
                .getSerializableExtra(MeetUpDetailsActivity.EVENTBROSWER_MEET_UP_ID);
        mMeetUp = MeetUpLab.get(getActivity()).getMeetUp(meetUpId);

    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        ImageLoader imageLoader = VolleySingleton.getInstance(App.getContext()).getImageLoader();
        final int API_LEVEL = 24;

        //explicitly inflate the fragment's view
        View v = inflater.inflate(R.layout.fragment_meet_up_details, container, false);
        mNameField = (TextView) v.findViewById(R.id.meetup_name);
        mNameField.setText(mMeetUp.getGroupName());
        mDescriptionField = (TextView) v.findViewById(R.id.meetup_description);
        // Remove HTML tags from Description String
        if (Build.VERSION.SDK_INT >= API_LEVEL) {
            mDescriptionField.setText(Html.fromHtml(mMeetUp.getGroupDescription(),
                    Html.FROM_HTML_MODE_LEGACY));
        } else {
            mDescriptionField.setText(Html.fromHtml(mMeetUp.getGroupDescription()));
        }
        mNumberOfGroupMembersField = (TextView) v.findViewById(R.id.meetup_members);
        mNumberOfGroupMembersField.setText("Current Members: " + mMeetUp.getNumberOfGroupMembers());
        mImageView = (NetworkImageView) v.findViewById(R.id.meetup_photo);
        mImageView.setImageUrl(mMeetUp.getGroupPhotoLinkURL(), imageLoader);

        return v;
    }

}
