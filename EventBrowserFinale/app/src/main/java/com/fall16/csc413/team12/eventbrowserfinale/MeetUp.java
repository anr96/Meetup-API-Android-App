package com.fall16.csc413.team12.eventbrowserfinale;

/**
 * Created by AmandaNikkole on 12/10/16.
 */
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
    private UUID meetUpId;
    private String name;
    private String link;
    private String description;
    //private String city;
    private String noAttendees;
    private String pictureURL;


    /**
     *
     * @param jsonObject    {@link JSONObject} response, received in Volley success listener
     * @return  list of meetups
     * @throws JSONException
     */
    public static List<MeetUp> parseJson(JSONObject jsonObject) throws JSONException{
        List<MeetUp> meetUps = new ArrayList<>();
        // Check if the JSONObject has object with key "#", varies 0 and on
//        //TODO .has(??) 0 only gets first object
//        for(int i = 0; i < 10; i++) {
//            if (jsonObject.has(Integer.toString(i))) {
//        if(jsonObject.has("0")){
//                // Get JSONArray from JSONObject
//                //JSONArray jsonArray = jsonObject.getJSONArray(Integer.toString(i));
//                JSONArray jsonArray = jsonObject.getJSONArray("0");
//                for (int j = 0; j < jsonArray.length(); j++) {
//                    // Create new MeetUp object from each JSONObject in the JSONArray
//                    meetUps.add(new MeetUp(jsonArray.getJSONObject(j)));
//                }
//            }
        JSONArray jsonArray = jsonObject.getJSONArray("");
        for (int i = 0; i< jsonArray.length();i++){
            meetUps.add(new MeetUp((jsonArray.getJSONObject(i))));
        }

        return meetUps;
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
       // if(jsonObject.has("city")) this.setCity(jsonObject.getString("city"));
        if(jsonObject.has("members")) this.setNoAttendees(jsonObject.getString("members"));

        /*
        //TODO validate the logic
        if(jsonObject.has("photo")) {
            if(jsonObject.has("photo_link"))
                this.setPictureURL(jsonObject.getString("photo_link"));
        }
        */
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

    public UUID getMeetUpId() {
        return meetUpId;
    }

    public void setMeetUpId(String stringMeetUpId) {
        meetUpId = UUID.fromString(stringMeetUpId);
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

//    public String getCity() {
//        return city;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }
}
