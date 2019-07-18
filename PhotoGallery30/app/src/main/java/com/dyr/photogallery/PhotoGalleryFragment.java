package com.dyr.photogallery;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;

import java.util.ArrayList;

public class PhotoGalleryFragment extends VisibleFragment {
    private static final String TAG = "PhotoGalleryFragment";
    private GridView mGridView;
    private ArrayList<GalleryItem> mItems;
    ThumbnailDownloader<ImageView> mThumbnailThread;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        updateItems();

        mThumbnailThread = new ThumbnailDownloader<ImageView>(new Handler());
        mThumbnailThread.setListener(new ThumbnailDownloader.Listener<ImageView>() {
            @Override
            public void onThumbnailDownloaded(ImageView imageView, Bitmap thumbnail) {
                if(isVisible()){
                    imageView.setImageBitmap(thumbnail);
                }
            }
        });
        mThumbnailThread.start();
        mThumbnailThread.getLooper();

        Log.i(TAG, "Background thread started");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_photo_gallery, parent, false);
        mGridView = v.findViewById(R.id.gridView);
        setupAdapter();
        return v;
    }

    @TargetApi(11)
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_photo_gallery, menu);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            //Pull out the search View
            MenuItem searchItem = menu.findItem(R.id.menu_item_search);
            SearchView searchView = (SearchView) searchItem.getActionView();

            //Get the data from out searchable.xml as a SearchableInfo
            SearchManager searchManger = (SearchManager)getActivity()
                    .getSystemService(Context.SEARCH_SERVICE);
            ComponentName name = getActivity().getComponentName();
            SearchableInfo searchInfo = searchManger.getSearchableInfo(name);

            searchView.setSearchableInfo(searchInfo);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);

        MenuItem toggleItem = menu.findItem(R.id.menu_item_toggle_polling);
        if(PollService.isServiceAlarmOn(getActivity())){
            toggleItem.setTitle(R.string.stop_polling);
        }else{
            toggleItem.setTitle(R.string.start_polling);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_search:
                getActivity().onSearchRequested();
                return true;
            case R.id.menu_item_clear:
                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit().putString(FlickerFetchr.PREF_SEARCH_QUERY, null)
                        .commit();
                updateItems();
                return true;
            case R.id.menu_item_toggle_polling:
                boolean shouldStartAlarm = !PollService.isServiceAlarmOn(getActivity());
                PollService.setServiceAlarm(getActivity(), shouldStartAlarm);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                    getActivity().invalidateOptionsMenu();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mThumbnailThread.stop();
        Log.i(TAG,"Background thread destroyed");
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mThumbnailThread.clearQueue();
    }

    void setupAdapter(){
        if(getActivity() == null || mGridView == null) return;

        if(mItems != null){
            mGridView.setAdapter(new GalleryItemAdapter(mItems));
        }else{
            mGridView.setAdapter(null);
        }
    }

    public void updateItems(){
        new FetchItemTask().execute();
    }

    private class FetchItemTask extends AsyncTask<Void, Void, ArrayList<GalleryItem>>{
        @Override
        protected ArrayList<GalleryItem> doInBackground(Void... params){
            Activity activity = getActivity();
            if(activity == null){
                return new ArrayList<GalleryItem>();
            }

            String query = PreferenceManager.getDefaultSharedPreferences(activity)
                    .getString(FlickerFetchr.PREF_SEARCH_QUERY, null);
            if (query != null){
                return new FlickerFetchr().search(query);
            }else {
                return new FlickerFetchr().fetchItems();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<GalleryItem> items){
            mItems = items;
            setupAdapter();
        }
    }

    public class GalleryItemAdapter extends ArrayAdapter<GalleryItem>{
        public GalleryItemAdapter(ArrayList<GalleryItem> items){
            super(getActivity(),0 ,items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = getActivity().getLayoutInflater().
                        inflate(R.layout.gallery_item, parent, false);
            }

            ImageView imageView = (ImageView)convertView.findViewById(R.id.gallery_item_imageView);
            imageView.setImageResource(R.drawable.brain_up_close);

            GalleryItem item = getItem(position);
            mThumbnailThread.queueThumbnail(imageView, item.getUrl());

            return imageView;
        }
    }
}
