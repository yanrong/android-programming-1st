package com.dyr.mycamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileSave {

    public static String doSaveFile(Context c, String fileName, Bitmap bitmap) {
        String location = Environment.getExternalStorageDirectory() + "/Crime/";
        return onSaveFile(c, location, fileName, bitmap);
    }

    public static String onSaveFile(Context c, String filePath, String fileName, Bitmap bitmap) {
        byte[] bytes = bitmapToBytes(bitmap);
        return saveFile(c, filePath, fileName, bytes);
    }

    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, writer);
        return writer.toByteArray();
    }

    public static String saveFile(Context c, String filePath, String fileName, byte[] bytes) {
        String newFileName = "";
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        newFileName = dateFormat.format(date) + fileName+".jpg";

        FileOutputStream writer = null;
        //String dateFolder = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
        //        .format(new Date());
        try {
            //String suffix = "";
            if (filePath == null || filePath.trim().length() == 0) {
                filePath = Environment.getExternalStorageDirectory() + "/Crime/";
            }

            File file = new File(filePath);

            if (!file.exists()) {
                file.mkdirs();
            }

            File fullFile = new File(filePath, newFileName);
            //fileFullName = fullFile.getPath();
            writer = new FileOutputStream(new File(filePath, newFileName));
            writer.write(bytes);

        } catch (Exception e) {
            newFileName = "";
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    newFileName = "";
                }
            }
        }
        return newFileName;
    }
}
