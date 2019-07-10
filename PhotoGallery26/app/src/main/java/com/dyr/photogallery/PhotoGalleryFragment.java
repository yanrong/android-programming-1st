package com.dyr.photogallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = "PhotoGalleryFragment";
    private GridView mGridView;
    private ArrayList<GalleryItem> mItems;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_photo_gallery, parent, false);
        mGridView = v.findViewById(R.id.gridView);
        setupAdapter();
        return v;
    }

    void setupAdapter(){
        if(getActivity() == null || mGridView == null) return;

        if(mItems != null){
            mGridView.setAdapter(new ArrayAdapter<GalleryItem>(getActivity(),
                    android.R.layout.simple_gallery_item));
        }else{
            mGridView.setAdapter(null);
        }
    }

    private class FetchItemTask extends AsyncTask<Void, Void, ArrayList<GalleryItem>>{
        @Override
        protected ArrayList<GalleryItem> doInBackground(Void... params){
            return new FlickerFetchr().fetchItems();
        }
        @Override
        protected void onPostExecute(ArrayList<GalleryItem> items){
            mItems = items;
            setupAdapter();
        }
    }
}
