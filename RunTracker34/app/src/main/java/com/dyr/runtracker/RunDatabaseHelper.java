package com.dyr.runtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.widget.CursorAdapter;

import java.util.Date;

public class RunDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "runs.sqlite";
    private static final int  VERSION =1;
    private static final String TABLE_RUN = "run";
    private static final String COLUMN_RUN_ID = "_id";
    private static final String COLUMN_RUN_START_DATE = "start_date";

    private static final String TABLE_LOCATION = "location";
    private static final String COLUMN_LOCATION_LATITUDE = "latitude";
    private static final String COLUMN_LOCATION_LONGITUDE = "longitude";
    private static final String COLUMN_LOCATION_ALTITUDE = "altitude";
    private static final String COLUMN_LOCATION_TIMESTAMP = "timestamp";
    private static final String COLUMN_LOCATION_PROVIDER = "provider";
    private static final String COLUMN_LOCATION_RUN_ID = "run_id";
    public RunDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
       //Create the run table
        db.execSQL("create table run ( _id integer primary key autoincrement, start_date integer)");
        //create the location table
        db.execSQL("create table location (timestamp integer, latitude real, longitude real, altitude real, " +
                " provider varchar(10), run_id integer references run(_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertRun(Run run){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_RUN_START_DATE, run.getStartDate().getTime());
        return getWritableDatabase().insert(TABLE_RUN, null, cv);
    }

    public long insertLocation(long runId, Location location){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LOCATION_LATITUDE, location.getLatitude());
        cv.put(COLUMN_LOCATION_LONGITUDE, location.getLongitude());
        cv.put(COLUMN_LOCATION_ALTITUDE, location.getAltitude());
        cv.put(COLUMN_LOCATION_TIMESTAMP, location.getTime());
        cv.put(COLUMN_LOCATION_PROVIDER, location.getProvider());
        cv.put(COLUMN_LOCATION_RUN_ID, runId);

        return getWritableDatabase().insert(TABLE_LOCATION, null, cv);
    }

    public RunCursor queryRuns(){
        Cursor wrapped = getReadableDatabase().query(TABLE_RUN, null, null, null,
                null, null, COLUMN_RUN_START_DATE + " asc");

        return new RunCursor(wrapped);
    }

    public RunCursor queryRuns(long id){
        Cursor wrapped = getReadableDatabase().query(TABLE_RUN, null, COLUMN_RUN_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, "1");

        return new RunCursor(wrapped);
    }

    public LocationCursor queryLastLocationForRun(long runId){
        Cursor wrapped = getReadableDatabase().query(TABLE_LOCATION,
                null, //all column
                COLUMN_LOCATION_RUN_ID + "=?", // limit to the given run
                new String[]{String.valueOf(runId)},
                null, null,
                COLUMN_LOCATION_TIMESTAMP + " desc",//order by latest first
                "1" );

        return new LocationCursor(wrapped);
    }
    /**
     * * A convenience class to wrap to cursor that returns rows from the "run"
     * * Table, the(@linke getRun} method will give you a Run instance representing
     * * The current row.
     * */
    public static class RunCursor extends CursorWrapper{

        /**
         * Creates a cursor wrapper.
         *
         * @param cursor The underlying cursor to wrap.
         */
        public RunCursor(Cursor cursor) {
            super(cursor);
        }

        /**
         * Returns a Run object configured for the current now
         * or null if the current row is invalid.
         * */
        public Run getRun(){
            if(isBeforeFirst() || isAfterLast()){
                return null;
            }

            Run run = new Run();
            long runId = getLong(getColumnIndex(COLUMN_RUN_ID));
            run.setId(runId);
            long startDate = getLong(getColumnIndex(COLUMN_RUN_START_DATE));
            run.setStartDate(new Date(startDate));

            return run;
        }
    }

    public static class LocationCursor extends CursorWrapper {
        public LocationCursor(Cursor c){
            super(c);
        }

        public Location getLocation(){
            if (isBeforeFirst() || isAfterLast()){
                return null;
            }
            //First get the provider out so you can use the constructor
            String provider = getString(getColumnIndex(COLUMN_LOCATION_PROVIDER));
            Location loc = new Location(provider);
            //Populate the remaining properties
            loc.setLatitude(getDouble(getColumnIndex(COLUMN_LOCATION_LONGITUDE)));
            loc.setAltitude(getDouble(getColumnIndex(COLUMN_LOCATION_ALTITUDE)));
            loc.setLatitude(getDouble(getColumnIndex(COLUMN_LOCATION_LATITUDE)));
            loc.setTime(getLong(getColumnIndex(COLUMN_LOCATION_TIMESTAMP)));

            return loc;
        }
    }
}
