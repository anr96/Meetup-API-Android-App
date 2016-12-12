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

	private String meetUpId;
    private String name;
    private String link;
    private String description;
    private String noAttendees;
    private String pictureURL;


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

		if(jsonObject.has("id")) this.setMeetUpId(jsonObject.getString("id"));
        if(jsonObject.has("name")) this.setName(jsonObject.getString("name"));
        if(jsonObject.has("link")) this.setLink(jsonObject.getString("link"));
        if(jsonObject.has("description")) this.setDescription(jsonObject.getString("description"));
        if(jsonObject.has("members")) this.setNoAttendees(jsonObject.getString("members"));

		if(jsonObject.has("organizer")) {
			JSONObject jsonOrganizer = jsonObject.getJSONObject("organizer");
			if(jsonOrganizer.has("photo")) {
				JSONObject jsonPhoto = jsonOrganizer.getJSONObject("photo");
				this.setPictureURL(jsonPhoto.getString("photo_link"));
			}
		}
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

	public String getMeetUpId() {
		return meetUpId;
	}

	public void setMeetUpId(String stringMeetUpId) {
		meetUpId = stringMeetUpId;
	}
    public String getNoAttendees() {
        return noAttendees;
    }

    public void setNoAttendees(String noAttendees) {
        this.noAttendees = noAttendees;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
