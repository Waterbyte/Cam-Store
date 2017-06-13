package sagar.mehar.camstore.util;

import android.app.Activity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import sagar.mehar.camstore.MainActivity;

/**
 * Created by Mountain on 09-06-2017.
 */

public class Paste {
    private final WeakReference<Activity> activity;
    public Paste(final Activity activity)
    {
        this.activity = new WeakReference<>(activity);
        pasteFunction();
    }
    private void pasteFunction()
    {
        final Activity activity = this.activity.get();
        ArrayList<File> PasteFileList=((MainActivity)activity).getCutFileList();
        for(File f:PasteFileList)
        {
            File f2=new File(((MainActivity) activity).getCurrentPath()+File.separator+f.getName());
            f.renameTo(f2);
        }
        ((MainActivity) activity).setMenuType(1);
        activity.invalidateOptionsMenu();
        ((MainActivity) activity).display(((MainActivity) activity).getCurrentPath());
        ((MainActivity)activity).callBroadCast(((MainActivity)activity).getFixedPath());
    }

}
