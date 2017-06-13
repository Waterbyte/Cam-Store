package sagar.mehar.camstore.util;

import android.app.Activity;
import android.widget.Toast;

import java.io.File;
import java.lang.ref.WeakReference;

import sagar.mehar.camstore.MainActivity;

/**
 * Created by Mountain on 09-06-2017.
 */

public class Unhide {

    private final WeakReference<Activity> activity;

    public Unhide(Activity activity)
    {
        this.activity= new WeakReference<>(activity);
        unhideUtil();
    }
    private Activity getActivity()
    {
        return this.activity.get();
    }
    private void unhideUtil()
    {
        int count=0;
        final Activity activity=getActivity();
        String path=((MainActivity)activity).getCurrentPath();
        File f=new File(path);
        File[] chosenFiles=f.listFiles();

        for(File file:chosenFiles)
        {
            if(file.isDirectory()&&file.getName().startsWith(".")&&!file.getName().contains("thumb"))
            {
               File destination=new File(path,file.getName().substring(1,file.getName().length()));
                file.renameTo(destination);
                count++;
            }
        }
        if(count==0)
        {
            Toast.makeText(activity, "No Hidden Folders", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(activity,"Unhidden Successfully",Toast.LENGTH_SHORT).show();
            ((MainActivity)activity).getGridAdapter().notifyDataSetChanged();
            ((MainActivity)activity).display(((MainActivity)activity).getCurrentPath());
            ((MainActivity)activity).callBroadCast(path);
        }
    }
}
