package com.example.dyr.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private ArrayList<Crime> mCrimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);

        setContentView(mViewPager);
*/
        setContentView(R.layout.activity_crime_pager);
        mViewPager = findViewById(R.id.crime_view_pager);

        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fm = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int i) {
                Crime crime = mCrimes.get(i);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        mViewPager.setOffscreenPageLimit(5);

        final UUID crimeId = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        for(int i = 0; i< mCrimes.size(); i++){
            if(mCrimes.get(i).getId().equals(crimeId)){
                mViewPager.setCurrentItem(i); //select current pick as viewpager content
                setTitle(mCrimes.get(i).getTitle());
                break;
            }
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            public void onPageScrollStateChanged(int state){ }
            public void onPageScrolled(int pos, float posOffset, int posOffsetPixels){ }

            @Override
            public void onPageSelected(int i) {
                Crime crime = mCrimes.get(i);
                if(crime.getTitle() != null){
                    setTitle(crime.getTitle());
                }
            }
        });
    }
}
