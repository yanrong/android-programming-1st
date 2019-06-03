package com.dyr.app.criminalintent;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Uri photoUri;

    private static final String TAG = "CriminalFragment";
    public static final String EXTRA_CRIME_ID = "com.dyr.app.criminalintent.crime_id";
    public static final String LOCATION = Environment.getExternalStorageDirectory() + "/Crime/";
    private static final String DIALOG_DATE = "date";
    private static final String DIALOG_TIME = "time";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int TAKE_PHOTO = 2;
    public static final int CROP_PHOTO = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume(){
        super.onResume();
        CrimeLab.get(getActivity()).saveCrimes();
    }

    //@TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, parent, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityName(getActivity()) != null) {
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        mPhotoView = (ImageView) v.findViewById(R.id.crime_ImageView);
        mTitleField = (EditText) v.findViewById(R.id.crime_title);

        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before,
                                      int count) {
                mCrime.setTitle(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count,
                                          int after) {
                // This space intentionally left blank
            }

            public void afterTextChanged(Editable c) {
                // This one too
            }
        });

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        mTimeButton = (Button) v.findViewById(R.id.crime_time);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                TimePickerFragment dialog = TimePickerFragment
                        .newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(fm, DIALOG_TIME);
            }
        });

        updateDateAndTime();

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mCrime.setSolved(isChecked);
                    }
                });

        mPhotoButton = (ImageButton)v.findViewById(R.id.crime_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int checkCameraPermission = ContextCompat.checkSelfPermission(
                            getActivity(), Manifest.permission.CAMERA);
                    int checkStoragePermission = ContextCompat.checkSelfPermission(
                           getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                    );

                    if ((checkCameraPermission != PackageManager.PERMISSION_GRANTED) &&
                            (checkStoragePermission != PackageManager.PERMISSION_GRANTED)) {
                        ActivityCompat.requestPermissions(
                                getActivity(),
                                new String[]{
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                }
                                , TAKE_PHOTO);

                    } else {
                        try {
                            //open camera
                            getCamera();
                        } catch (Exception e) {
                            Log.e(TAG, "Error in open camera");
                        }
                    }
                }
            }
        });

        PackageManager pmanger = getActivity().getPackageManager();
        boolean hasCamera = pmanger.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
                pmanger.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
                Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD ||
                Camera.getNumberOfCameras() > 0 ;

        if(!hasCamera){
            mPhotoButton.setEnabled(false);
        }
        return v;
    }

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDateAndTime();
        } else if (requestCode == REQUEST_TIME) {
            Date date = (Date) data
                    .getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setDate(date);
            updateDateAndTime();
        } else  if(requestCode == TAKE_PHOTO){
            corpPhoto(photoUri);
        } else  if(requestCode == CROP_PHOTO){
            Bitmap bitmap = data.getParcelableExtra("data");
            String id = mCrime.getId().toString();
            FileOpt.doSaveFile(getActivity(), "Crime_" + id );
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,
                                           int[] grantResults) {
        if (requestCode == TAKE_PHOTO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    getCamera();
                } catch (Exception e) {
                    Log.e(TAG, "error", e);
                }
            } else {
                Toast.makeText(getActivity(), "ungranted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateDateAndTime() {
        Date d = mCrime.getDate();
        CharSequence c = DateFormat.format("EEEE, MMM dd, yyyy", d);
        CharSequence t = DateFormat.format("h:mm a", d);
        mDateButton.setText(c);
        mTimeButton.setText(t);
    }

    private void getCamera(){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        String s = mCrime.getId().toString();
        //default storage format is jpg
        File outputImage = FileOpt.doSaveFile(getActivity(), "Crime" + s);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            photoUri = FileProvider.getUriForFile(getActivity(),
                    "com.dyr.app.criminalintent" ,outputImage );
        } else {
            photoUri = Uri.fromFile(outputImage);
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, TAKE_PHOTO);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    protected void corpPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.setDataAndType(uri, "image/*");

        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 50);
        intent.putExtra("outputY", 80);

        intent.putExtra("return-data", true);

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, CROP_PHOTO);
    }

    private void showPhoto(String path){
        BitmapDrawable b = null;
        if(path != null){
            b = PictureUtils.getScaleDrawable(getActivity(), path);
        }
        mPhotoView.setImageDrawable(b);
    }
}
