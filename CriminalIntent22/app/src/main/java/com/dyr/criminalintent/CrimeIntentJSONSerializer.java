package com.dyr.criminalintent;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public class CrimeIntentJSONSerializer {
    private Context mContext;
    private String mFilename;

    public CrimeIntentJSONSerializer(Context c, String f) {
        mContext = c;
        mFilename = f;
    }

    public void saveCrimes(ArrayList<Crime> crimes)
            throws JSONException, IOException {
        JSONArray array = new JSONArray();
        for (Crime c : crimes)
            array.put(c.toJSON());

        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    public ArrayList<Crime> loadCrimes() throws IOException, JSONException {
        ArrayList<Crime> crimes = new ArrayList<Crime>();
        BufferedReader reader = null;
        try {
            //open file and
            InputStream jin = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(jin));
            StringBuilder jsonString = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
                    .nextValue();
            for (int i = 0; i < array.length(); i++) {
                crimes.add(new Crime(array.getJSONObject(i)));
            }

        } catch (FileNotFoundException e) {
            //ignore it
        } finally {
            if (reader != null) reader.close();
        }

        return crimes;
    }
}
