package com.dyr.app.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

public class Crime {

	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_SOLVED ="solved";
	private static final String JSON_DATE = "date";
	private static final String JSON_PHOTO_ORIGIN = "crime";
	private static final String JSON_PHOTO_CROP = "thumbnail";
	private static final String JSON_SUSPECT = "suspect";

	private UUID mId;
	private String mTitle;
	private Date mDate;
	private boolean mSolved;
	private Photo mOriginPhoto;
	private Photo mCropPhoto;
	private String mSuspect;

	public Crime() {
		// Generate unique identifier
		mId = UUID.randomUUID();
		mDate = new Date();
	}

	public JSONObject toJSON() throws JSONException{
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle);
		json.put(JSON_SOLVED, mSolved);
		json.put(JSON_DATE, mDate.getTime());
		if(mOriginPhoto != null){
			json.put(JSON_PHOTO_ORIGIN,mOriginPhoto.toJSON());
		}
		if(mCropPhoto != null){
			json.put(JSON_PHOTO_CROP,mCropPhoto.toJSON());
		}
		json.put(JSON_SUSPECT, mSuspect);
		return json;
	}

	public Crime(JSONObject json) throws JSONException{
		mId = UUID.fromString(json.getString(JSON_ID));
		if(json.has(JSON_TITLE)){
			mTitle = json.getString(JSON_TITLE);
		}
		mSolved = json.getBoolean(JSON_SOLVED);
		mDate = new Date(json.getLong(JSON_DATE));
		if(json.has(JSON_PHOTO_ORIGIN)){
			json.put(JSON_PHOTO_ORIGIN, mOriginPhoto.toJSON());
		}
		if(json.has(JSON_PHOTO_CROP)){
			json.put(JSON_PHOTO_CROP, mCropPhoto.toJSON());
		}

		if(json.has(JSON_SUSPECT)){
			mSuspect = json.getString(JSON_SUSPECT);
		}
	}

	public String getSuspect() {
		return mSuspect;
	}

	public void setSuspect(String Suspect) {
		mSuspect = Suspect;
	}

	public Photo getOriginPhoto(){
		return  mOriginPhoto;
	}

	public void setOriginPhoto(Photo p){
		mOriginPhoto = p;
	}

	public Photo getCropPhoto(){
		return  mCropPhoto;
	}

	public void setCropPhoto(Photo p){
		mCropPhoto = p;
	}
	@Override
	public String toString() {
		return mTitle;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public UUID getId() {
		return mId;
	}

	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public boolean isSolved() {
		return mSolved;
	}

	public void setSolved(boolean solved) {
		mSolved = solved;
	}

}
