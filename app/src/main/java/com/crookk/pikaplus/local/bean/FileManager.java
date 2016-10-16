package com.crookk.pikaplus.local.bean;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import org.androidannotations.annotations.EBean;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

@EBean
public class FileManager {

    public static final String TAG = FileManager.class.getSimpleName();
    public static String CACHE_URL;

    public void init() {

        CACHE_URL = Environment.getExternalStorageDirectory().getPath() + "/PikaCatch";

        try {

            File folder = new File(CACHE_URL);

            if (!folder.exists()) {
                boolean created = folder.mkdirs();
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public File saveBitmap(Bitmap bitmap) {

        File root = new File(CACHE_URL);

        Date date = new Date();

        String name = "Snapshot_" + date.getTime() + ".png";

        File file = new File(root, name);

        if (!file.exists()) {
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                return null;
            }
        }

        return file;
    }
}
