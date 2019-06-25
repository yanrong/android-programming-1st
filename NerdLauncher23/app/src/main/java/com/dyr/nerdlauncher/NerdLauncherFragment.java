package com.dyr.nerdlauncher;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NerdLauncherFragment extends ListFragment {

    private static final String TAG = "NerdLauncherFragment";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(i, 0);
        Log.i(TAG, "i've found " + activities.size() + " activities");

        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo o1, ResolveInfo o2) {
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(o1.loadLabel(pm).toString(),
                        o2.loadLabel(pm).toString());
            }
        });

        ArrayAdapter<ResolveInfo> adapter = new ArrayAdapter<ResolveInfo>(
                getActivity(), android.R.layout.simple_list_item_1, activities
        ){
            public View getView(int pos, View convertView, ViewGroup parent){
                PackageManager pm = getActivity().getPackageManager();
                View v = super.getView(pos, convertView, parent);
                //Documentation says that simple_list_item_1 is a TextView
                //so cast it so that you can set its text value
                TextView tv = (TextView)v;
                ResolveInfo info = getItem(pos);
                tv.setText(info.loadLabel(pm));
                return v;
            }
        };

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id){
       ResolveInfo resolveInfo = (ResolveInfo)l.getAdapter().getItem(pos);
       ActivityInfo info = resolveInfo.activityInfo;

       if(info == null) return;
       Intent i = new Intent(Intent.ACTION_MAIN);
       i.setClassName(info.applicationInfo.packageName, info.name);
    }
}
