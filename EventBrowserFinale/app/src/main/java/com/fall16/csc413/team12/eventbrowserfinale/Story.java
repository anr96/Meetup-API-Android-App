package com.fall16.csc413.team12.eventbrowserfinale;

import android.widget.ImageView;

import java.util.UUID;

/**
 * Created by wgalan on 11/22/16.
 */

public class Story {

	private UUID mUuid;
	private String mName;
	private String mDescription;
	private String mStoryName;
	private ImageView mImageView;

	public Story(String storyName, String description){
		mStoryName = storyName;
		mDescription = description;
		mUuid = UUID.randomUUID();
	}


	/**
	 *
	 * @param jsonObject    {@link JSONObject} response, received in Volley success listener
	 * @return  list of movies
	 * @throws JSONException
	 */
	/*
	public static List<Movie> parseJson(JSONObject jsonObject) throws JSONException{
		List<Movie> movies = new ArrayList<>();
		// Check if the JSONObject has object with key "Search"
		if(jsonObject.has("Search")){
			// Get JSONArray from JSONObject
			JSONArray jsonArray = jsonObject.getJSONArray("Search");
			for(int i = 0; i < jsonArray.length(); i++){
				// Create new Movie object from each JSONObject in the JSONArray
				movies.add(new Movie(jsonArray.getJSONObject(i)));
			}
		}

		return movies;
	}
	*/

	/**
	 * <p>Class constructor</p>
	 * <p>Sample Movie JSONObject</p>
	 * <pre>
	 * {
	 *  "Title": "Batman Begins",
	 *  "Year": "2005",
	 *  "imdbID": "tt0372784",
	 *  "Type": "movie",
	 *  "Poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BNTM3OTc0MzM2OV5BMl5BanBnXkFtZTYwNzUwMTI3._V1_SX300.jpg"
	 * }
	 * </pre>
	 * @param jsonObject    {@link JSONObject} from each item in the search result
	 * @throws JSONException     when parser fails to parse the given JSON
	 */
	/*
	private Movie(JSONObject jsonObject) throws JSONException {
		if(jsonObject.has("Title")) this.setTitle(jsonObject.getString("Title"));
		if(jsonObject.has("Year")) this.setYear(jsonObject.getString("Year"));
		if(jsonObject.has("imdbID")) this.setImdbId(jsonObject.getString("imdbID"));
		if(jsonObject.has("Type")) this.setType(jsonObject.getString("Type"));
		if(jsonObject.has("Poster")) this.setPosterUrl(jsonObject.getString("Poster"));
	}
	*/


	public UUID getUuid() {return mUuid;}

	public String getName() {return mName;}

	public void setName(String name) {mName = name;}

	public String getDescription() {return mDescription;}

	public void setDescription(String description) {mDescription = description;}

	public String getStoryName() {return mStoryName;}

	public void setStoryName(String storyName) {mStoryName = storyName;}

	public ImageView getImageView() {return mImageView;}

	public void setImageView(ImageView imageView) {mImageView = imageView;}
}
