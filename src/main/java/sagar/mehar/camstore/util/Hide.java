package sagar.mehar.camstore.util;

import android.app.Activity;
import android.widget.Toast;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import sagar.mehar.camstore.MainActivity;

/**
 * Created by Mountain on 09-06-2017.
 */

public class Hide {
    private final WeakReference<Activity> activity;

    public Hide(Activity activity)
    {
        this.activity= new WeakReference<>(activity);
        hideUtil();
    }
    private Activity getActivity()
    {
        return this.activity.get();
    }
    private void hideUtil()
    {
        final Activity activity = getActivity();
        final ArrayList<File> selectedFileList=((MainActivity)activity).getGridAdapter().getSelected();

        if(selectedFileList.size()==0)
        {
            Toast.makeText(activity,"Select Folders to Hide",Toast.LENGTH_SHORT).show();
        }
        else
        {
            for(File f:selectedFileList)
            {
                if(f.isDirectory()) {
                    File dstFile = new File(f.getParent(), "." + f.getName());
                    try {
                        f.renameTo(dstFile);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(activity,"Error",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        ((MainActivity) activity).isShow(false);
        ((MainActivity)activity).getGridAdapter().notifyDataSetChanged();
        activity.invalidateOptionsMenu();
        ((MainActivity)activity).display(((MainActivity)activity).getCurrentPath());
        ((MainActivity)activity).callBroadCast(((MainActivity)activity).getCurrentPath());
    }

}
