package com.dyr.runtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class LocationReceiver extends BroadcastReceiver {
    private static final String TAG = "LocationReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        //If you have a location extra , use it
        Location loc = (Location) intent.getParcelableExtra(LocationManager.KEY_STATUS_CHANGED);
        if(loc != null){
            onLocationReceived(context, loc);
            return;
        }

        //If you get here, something else has changed
        if(intent.hasExtra(LocationManager.KEY_PROVIDER_ENABLED)){
            boolean enabled = intent.getBooleanExtra(LocationManager.KEY_PROVIDER_ENABLED, false);
            onProviderEnableChanged(enabled);
        }
    }

    protected void onLocationReceived(Context context, Location loc){
        Log.d(TAG, this+ " Got location from " + loc.getProvider() + ": " + loc.getLatitude() +
                ", " + loc.getLongitude());
    }

    protected void onProviderEnableChanged(boolean enable){
        Log.d(TAG, "Provider " + (enable ? "enabled" : "disabled"));
    }
}
