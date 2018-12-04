package sagar.mehar.camstore.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Mountain on 08-11-18.
 */

public class CommonUtility {


    public static boolean createDir(String path, String name) {
        try {
            File folder = new File(path, name);
            return !folder.exists() && folder.mkdir();
        } catch (Exception e) {
            return false;
        }
    }

    public static void openImage(Context context, Uri uri) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //must provide
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "image/*");
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Could not find app to open image", Toast.LENGTH_SHORT).show();
        }
    }

    public static void deleteDir(File file) {
        if (file.isDirectory())
            for (String child : file.list())
                deleteDir(new File(file, child));
        file.delete();
    }

}