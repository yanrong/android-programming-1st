package com.dyr.app.criminalintent;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CrimeListFragment extends ListFragment {
    private ArrayList<Crime> mCrimes;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.crimes_title);
        mCrimes = CrimeLab.get(getActivity()).getCrimes();

        CrimeAdapter adapter = new CrimeAdapter(mCrimes);
        setListAdapter(adapter);

        setRetainInstance(true);
        mSubtitleVisible = false;
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.crime_list_fragment, parent, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (mSubtitleVisible) {
                ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.subtitle);
            }
        }

        ListView listView = (ListView) v.findViewById(android.R.id.list);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // float context menu
            registerForContextMenu(listView);
        }else{
            //contextual action bar
           listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
           listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
               @Override
               public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                   //ignore it
               }

               @Override
               public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                   MenuInflater  inflater = actionMode.getMenuInflater();
                   inflater.inflate(R.menu.crime_list_item_context, menu);
                   return true;
               }

               @Override
               public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                   return false;
               }

               @Override
               public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                   switch (menuItem.getItemId()){
                       case R.id.menu_item_delete_crime:
                           CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
                           CrimeLab crimeLab = CrimeLab.get(getActivity());

                           for(int i = adapter.getCount() - 1; i >= 0; i--){
                               if(getListView().isItemChecked(i)){
                                   crimeLab.deleteCrime(adapter.getItem(i));
                               }
                           }

                           actionMode.finish();
                           adapter.notifyDataSetChanged();
                           return  true;

                       default:
                           return false;
                   }
               }

               @Override
               public void onDestroyActionMode(ActionMode actionMode) {

               }
           });
        }
        return v;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Crime c = ((CrimeAdapter) getListAdapter()).getItem(position);

        // Start CrimeActivity
        Intent i = new Intent(getActivity(), CrimePagerActivity.class);
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
        startActivity(i);
    }

    private class CrimeAdapter extends ArrayAdapter<Crime> {
        public CrimeAdapter(ArrayList<Crime> crimes) {
            super(getActivity(), 0, crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // If we weren't given a view, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.list_item_crime, null);
            }
            // Configure the view for this Crime
            Crime c = getItem(position);
            TextView titleTextView = (TextView) convertView
                    .findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(c.getTitle());
            TextView dateTextView = (TextView) convertView
                    .findViewById(R.id.crime_list_item_dateTextView);
            dateTextView.setText(c.getDate().toString());
            CheckBox solvedCheckBox = (CheckBox) convertView
                    .findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(c.isSolved());
            return convertView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible && showSubtitle != null) {
            showSubtitle.setTitle(R.string.hide_subtitle);
        }

        Button addACrimeButton = (Button) getActivity().findViewById(android.R.id.empty);
        addACrimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent i = new Intent(getActivity(), CrimePagerActivity.class);
                i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
                startActivityForResult(i, 0);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent i = new Intent(getActivity(), CrimePagerActivity.class);
                i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
                startActivityForResult(i, 0);
                return true;
            case R.id.menu_item_show_subtitle:
                if (((ActionBarActivity) getActivity()).getSupportActionBar().getSubtitle() == null) {
                    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.subtitle);
                    mSubtitleVisible = true;
                    item.setTitle(R.string.hide_subtitle);
                } else {
                    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(null);
                    mSubtitleVisible = false;
                    item.setTitle(R.string.show_subtitle);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v, ContextMenu.ContextMenuInfo info){
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
       AdapterView.AdapterContextMenuInfo info =
               (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
       int position = info.position;
       CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
       Crime crime = adapter.getItem(position);

       switch (item.getItemId()) {
           case R.id.menu_item_delete_crime:
               CrimeLab.get(getActivity()).deleteCrime(crime);
               adapter.notifyDataSetChanged();
               return true;
       }

       return super.onContextItemSelected(item);
    }
}
