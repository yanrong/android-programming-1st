package com.dyr.app.criminalintent;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileOpt {

    public static File createEmptyFile(Context c, String filePath, String fileName) {
        /*String newFileName = "";
        File fullFile = null;
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        newFileName = dateFormat.format(date) + fileName+".jpg";
        */
        File fullFile = null;
        String newFileName = fileName+".jpg";
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

    public static String getFile(String path,String fileName){
        File searchfile = null;
        File dir = new File(path);

        if (dir.exists()) {
            searchfile = new File(path, fileName+".jpg");
            if (searchfile.exists()) {
                return searchfile.getAbsolutePath();
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    public static void saveFile(Bitmap bitmap, String path, String name) {

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File thumbnailfile = new File(path, name +".jpg");
        if (thumbnailfile.exists()){
            thumbnailfile.delete();
        }

        try {
            thumbnailfile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(thumbnailfile);
            if (bitmap != null) {
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                        fileOutputStream)) {
                    fileOutputStream.flush();
                }
            }
        } catch (FileNotFoundException e) {
            thumbnailfile.delete();
            e.printStackTrace();
        } catch (IOException e) {
            thumbnailfile.delete();
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                    fileOutputStream = null;
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
