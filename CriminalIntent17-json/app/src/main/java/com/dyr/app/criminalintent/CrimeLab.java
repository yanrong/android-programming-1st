package com.dyr.app.criminalintent;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

public class CrimeLab {
    private static final String TAG = "CrimeLab";
    private static final String FileName = "crime.json";

    private static CrimeLab sCrimeLab;
    private Context mAppContext;
    private ArrayList<Crime> mCrimes;
    private CrimeIntentJSONSerializer mSerializer;

    private CrimeLab(Context appContext) {
        mAppContext = appContext;
        mSerializer = new CrimeIntentJSONSerializer(mAppContext, FileName);
        try{
            mCrimes = mSerializer.loadCrimes();
        }catch (Exception e){
            mCrimes = new ArrayList<Crime>();
            Log.e(TAG, "Error load crimes",e);
        }
    }

    public static CrimeLab get(Context c) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        }
        return sCrimeLab;
    }

    public void addCrime(Crime c) {
        mCrimes.add(c);
    }

    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime c : mCrimes) {
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }
    public boolean saveCrimes(){
        try{
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG, "save crimes ok");
            return true;
        }catch (Exception e){
            Log.d(TAG, "Error saving crimes", e);
            return false;
        }
    }

}
