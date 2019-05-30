package com.dyr.mycamera;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileSave {

    public static File doSaveFile(Context c, String fileName) {
        String location = Environment.getExternalStorageDirectory() + "/Crime/";
        Log.e("===========FileSave", location);
        return saveFile(c, location, fileName);
    }

    public static File saveFile(Context c, String filePath, String fileName) {
        String newFileName = "";
        File fullFile = null;
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        newFileName = dateFormat.format(date) + fileName+".jpg";

        try {
            File dir = new File(filePath);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            fullFile = new File(filePath, newFileName);
            if (fullFile.exists()) {
                fullFile.delete();
            }
            fullFile.createNewFile();

        } catch (IOException e) {
            Log.e("Error" ,"create storage file",e);
        }

        return fullFile;
    }

}
