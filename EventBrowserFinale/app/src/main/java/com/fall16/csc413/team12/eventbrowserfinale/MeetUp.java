package com.fall16.csc413.team12.eventbrowserfinale;

/**
 * Created by AmandaNikkole on 12/10/16.
 */
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Model class for meetup
 * */
public class MeetUp {

	// Variables we are currently using
	private String groupName;
	private String groupDescription;
	private String numberOfGroupMembers;
	private String groupPhotoLinkURL;

	// Potential variables we may use
	private String groupMeetUpId;
    private String groupLink;

    /**
     *
     * @param jsonArray    {@link JSONArray} response, received in Volley success listener
     * @return  list of meetups
     * @throws JSONException
     */

	public static List<MeetUp> parseJson(JSONArray jsonArray) throws JSONException {

		List<MeetUp> meetUps = new ArrayList<>();

		try {
			// Parsing json array response, loop through each json object
			for (int i = 0; i < jsonArray.length(); i++) {
				// Create new MeetUp object from each JSONObject in the JSONArray
				JSONObject meetup = (JSONObject) jsonArray.get(i);
				meetUps.add(new MeetUp(meetup));
			}

		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(App.getContext(),
					"Error: " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}

		return  meetUps;
    }

    /**
     * <p>Class constructor</p>
     * <p>MeetUp JSONObject</p>
     * @param jsonObject    {@link JSONObject} from each item in the search result
     * @throws JSONException     when parser fails to parse the given JSON
     */
    private MeetUp(JSONObject jsonObject) throws JSONException {

		if(jsonObject.has("id")) this.setGroupMeetUpId(jsonObject.getString("id"));
        if(jsonObject.has("name")) this.setGroupName(jsonObject.getString("name"));
        if(jsonObject.has("link")) this.setGroupLink(jsonObject.getString("link"));
        if(jsonObject.has("description")) this.setGroupDescription(jsonObject.getString("description"));
        if(jsonObject.has("members")) this.setNumberOfGroupMembers(jsonObject.getString("members"));

		if(jsonObject.has("organizer")) {
			JSONObject jsonOrganizer = jsonObject.getJSONObject("organizer");
			if(jsonOrganizer.has("photo")) {
				JSONObject jsonPhoto = jsonOrganizer.getJSONObject("photo");
				this.setGroupPhotoLinkURL(jsonPhoto.getString("photo_link"));
			}
		}
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String name) {
        this.groupName = name;
    }

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String description) {
		this.groupDescription = description;
	}

	public String getNumberOfGroupMembers() {
		return numberOfGroupMembers;
	}

	public void setNumberOfGroupMembers(String numberOfMembers) {
		this.numberOfGroupMembers = numberOfMembers;
	}

    public String getGroupPhotoLinkURL() {
        return groupPhotoLinkURL;
    }

    public void setGroupPhotoLinkURL(String pictureURL) {
        this.groupPhotoLinkURL = pictureURL;
    }

	public String getGroupMeetUpId() {
		return groupMeetUpId;
	}

	public void setGroupMeetUpId(String stringMeetUpId) {
		groupMeetUpId = stringMeetUpId;
	}

    public String getGroupLink() {
        return groupLink;
    }

    public void setGroupLink(String link) {
        this.groupLink = link;
    }
}
