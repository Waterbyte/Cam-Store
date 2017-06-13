package sagar.mehar.camstore.util;

import android.app.Activity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import sagar.mehar.camstore.MainActivity;

/**
 * Created by Mountain on 28-12-2016.
 */

public class Cut {
    private final WeakReference<Activity> activity;
    public Cut(final Activity activity)
    {
        this.activity = new WeakReference<>(activity);
        cutFunction();
    }

    private void cutFunction()
    {
        final Activity activity = this.activity.get();
        ArrayList<File> cutFileList=((MainActivity)activity).getGridAdapter().getSelected();
        ((MainActivity)activity).setCutFileList(cutFileList);
        ((MainActivity)activity).setMenuType(2);
        activity.invalidateOptionsMenu();
        ((MainActivity)activity).isShow(false);
        ((MainActivity)activity).getGridAdapter().notifyDataSetChanged();
    }


}
