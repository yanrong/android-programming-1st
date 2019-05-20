package com.example.dyr.criminalintent;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CrimeListFragment extends ListFragment {

    private static final String TAG = "CrimeListFragment";
    private ArrayList<Crime> mCrimes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.crime_title_label);
        mCrimes = CrimeLab.get(getActivity()).getCrimes();
        /*
        //default create adapter by constructor
        ArrayAdapter<Crime> adapter = new ArrayAdapter<Crime>(getActivity(),
                android.R.layout.simple_list_item_1, mCrimes);
        */

        CrimeAdapter adapter = new CrimeAdapter(mCrimes);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        //Crime c = (Crime)(getListAdapter()).getItem(position);
        Crime c= ((CrimeAdapter)getListAdapter()).getItem(position);
        Log.d(TAG,c.getTitle() + " was clicked");
    }

    //customize adapter to get data from base
    private class CrimeAdapter extends ArrayAdapter<Crime>{
        private List<Crime> mCrimes;

        public CrimeAdapter(ArrayList<Crime> crimes){
            //call parent
            super(getActivity(), 0 , crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
             //If we haven't a view , inflate one
            if(convertView == null){
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_crime, null);
            }

            //configuration a new view for this Crime
            Crime c = getItem(position);

            TextView titleTextView = (TextView) convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(c.getTitle());

            TextView dateTextView = (TextView) convertView.findViewById(R.id.crime_list_item_dateTextView);
            dateTextView.setText(c.getDate().toString());

            CheckBox solvedCheckBox = (CheckBox) convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(c.isSolved());

            return convertView;
        }


    }
}
