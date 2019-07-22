package com.dyr.runtracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class RunFragment extends Fragment {
    private Button mStartButton, mStopButton;
    private TextView mStartTextView, mLatitudeTextView, mLongitudeTextView,
            mAltitudeTextView, mDurationtTextView;
    private RunManager mRunManger;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mRunManger = RunManager.get(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_run, container,false);
        mStartTextView      = (TextView) v.findViewById(R.id.run_startedTextView);
        mLatitudeTextView   = (TextView) v.findViewById(R.id.run_latitudeTextView);
        mLongitudeTextView  = (TextView) v.findViewById(R.id.run_longitudeTextView);
        mAltitudeTextView   = (TextView) v.findViewById(R.id.run_altitudeTextView);
        mDurationtTextView  = (TextView) v.findViewById(R.id.run_durationTextView);

        mStartButton    = (Button) v.findViewById(R.id.run_startButton);
        mStopButton     = (Button) v.findViewById(R.id.run_stopButton);

        mStartButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mRunManger.startLocationUpdates();
                updateUI();
            }
        });
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRunManger.stopLocationUpdates();
                updateUI();
            }
        });
        return v;
    }

    private void updateUI(){
        boolean started = mRunManger.isTrackingRun();
        mStartButton.setEnabled(!started);
        mStopButton.setEnabled(started);
    }
}
