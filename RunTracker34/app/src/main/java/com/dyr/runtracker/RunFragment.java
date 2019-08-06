package com.dyr.runtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RunFragment extends Fragment {
    private Button mStartButton, mStopButton;
    private TextView mStartTextView, mLatitudeTextView, mLongitudeTextView,
            mAltitudeTextView, mDurationTextView;
    private RunManager mRunManger;
    private Location mLastLocation;
    private Run mRun;
    private static final String ARG_RUN_ID = "RUN_ID";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mRunManger = RunManager.get(getActivity());

        //Check for a Run Id as a argument, and find the run
        Bundle args = new Bundle();
        if(args != null){
            long runId = args.getLong(ARG_RUN_ID, -1);
            if(runId != -1){
                mRun = mRunManger.getRun(runId);
                mLastLocation = mRunManger.getLastLocationForRun(runId);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_run, container,false);
        mStartTextView      = (TextView) v.findViewById(R.id.run_startedTextView);
        mLatitudeTextView   = (TextView) v.findViewById(R.id.run_latitudeTextView);
        mLongitudeTextView  = (TextView) v.findViewById(R.id.run_longitudeTextView);
        mAltitudeTextView   = (TextView) v.findViewById(R.id.run_altitudeTextView);
        mDurationTextView  = (TextView) v.findViewById(R.id.run_durationTextView);

        mStartButton    = (Button) v.findViewById(R.id.run_startButton);
        mStopButton     = (Button) v.findViewById(R.id.run_stopButton);

        mStartButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mRun == null){
                    mRun = mRunManger.startNewRun();
                }else {
                    mRunManger.startTrackingRun(mRun);
                }
                updateUI();
            }
        });
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRunManger.stopRun();
                updateUI();
            }
        });
        return v;
    }

    @Override
    public void onStart(){
        super.onStart();
        getActivity().registerReceiver(mLocationReceiver, new IntentFilter(RunManager.ACTION_LOCATION));
    }

    @Override
    public void onStop(){
        getActivity().unregisterReceiver(mLocationReceiver);
        super.onStop();
    }

    private BroadcastReceiver mLocationReceiver = new LocationReceiver(){
        @Override
        protected  void onLocationReceived(Context context, Location loc){
            if(!mRunManger.isTrackingRun()){
                return;
            }
            mLastLocation = loc;
            if(isVisible()){
                updateUI();
            }
        }
        protected void onProviderEnableChanged(boolean enable){
            int toastText = enable ? R.string.gps_enabled : R.string.gps_disabled;
            Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG);
        }
    };

    public static RunFragment newInstance(long runId){
        Bundle args = new Bundle();
        args.putLong(ARG_RUN_ID, runId);
        RunFragment rf = new RunFragment();
        rf.setArguments(args);
        return rf;
    }

    private void updateUI(){
        boolean started = mRunManger.isTrackingRun();
        if(mRun != null){
            mStartTextView.setText(mRun.getStartDate().toString());
        }

        int durationSeconds = 0;
        if(mRun != null && mLastLocation != null){
            durationSeconds = mRun.getDurationSeconds(mLastLocation.getTime());
            mLatitudeTextView.setText(Double.toString(mLastLocation.getLatitude()));
            mLongitudeTextView.setText(Double.toString(mLastLocation.getLongitude()));
            mAltitudeTextView.setText(Double.toString(mLastLocation.getAltitude()));
        }

        mDurationTextView.setText(Run.formatDuration(durationSeconds));
        mStartButton.setEnabled(!started);
        mStopButton.setEnabled(started);
    }
}
