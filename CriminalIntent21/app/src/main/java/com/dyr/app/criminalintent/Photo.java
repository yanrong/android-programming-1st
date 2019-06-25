package com.dyr.app.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

public class Photo {
    private static final String JSON_FILENAME = "filename";
    private String mFileName;

    public Photo(String filename){
        mFileName = filename;
    }

    public Photo(JSONObject json) throws JSONException{
        mFileName = json.getString(JSON_FILENAME);
    }

    public JSONObject toJSON() throws  JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_FILENAME, mFileName);
        return json;
    }

    public String getFilename(){
        return mFileName;
    }
}
