package com.dyr.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.dyr.app.criminalintent.R;

public class CrimeListActivity extends SingleFragmentActivity
        implements CrimeListFragment.Callbacks ,CrimeFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
    @Override
    protected  int getLayoutResId(){
        return R.layout.activity_twopanel;
    }

    public void onCrimeSelected(Crime c){
        if(findViewById(R.id.detailFragmentContainer) == null){
            Intent i = new Intent(this, CrimePagerActivity.class);
            i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
            startActivity(i);
        }else{
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            Fragment oldDetail = fm.findFragmentById(R.id.detailFragmentContainer);
            Fragment newDetail = CrimeFragment.newInstance(c.getId());

            if(oldDetail != null){
                ft.remove(oldDetail);
            }

            ft.add(R.id.detailFragmentContainer, newDetail);
            ft.commit();
        }
    }

    public void onUpdateCrime(Crime c){
        FragmentManager fm = getSupportFragmentManager();
        CrimeListFragment listFragment = (CrimeListFragment) fm.findFragmentById(R.id.fragmentContainer);
        listFragment.updateUi();
    }

}
