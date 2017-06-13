package sagar.mehar.camstore.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.app.AlertDialog;
import android.widget.Toast;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import sagar.mehar.camstore.MainActivity;
import sagar.mehar.camstore.R;

/**
 * Created by Mountain on 27-12-2016.
 */

public class Delete {


    private final WeakReference<Activity> activity;
    private String path="";
    public Delete(final Activity activity,String path) {
        this.activity = new WeakReference<>(activity);
        DeleteDialog();
        this.path=path;
    }
    private void DeleteDialog() {
        final Activity activity = this.activity.get();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setIcon(R.drawable.menu_save);
        builder1.setTitle(R.string.Deletebutton);
        builder1.setMessage(R.string.deleteconfirm);
        builder1.setCancelable(true);
        builder1.setPositiveButton(R.string.Deletebutton,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            new DeleteUtil(activity).execute();
                        }catch (Exception e)
                        {
                            Toast.makeText(activity,"Failure",Toast.LENGTH_SHORT).show();
                        }
                        dialog.cancel();
                    }
                }).setNegativeButton(
                R.string.Cancelbut,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ((MainActivity)activity).isShow(false);
                        ((MainActivity)activity).getGridAdapter().notifyDataSetChanged();
                        activity.invalidateOptionsMenu();
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }



 class DeleteUtil extends AsyncTask               //inner class for deleting asynchronously
 {
     private final WeakReference<Activity> activity;
     ArrayList<File> temp;

     public DeleteUtil(final Activity activity) {
         this.activity = new WeakReference<>(activity);
     }

     public void deleteDir(File file) {
         if (file.isDirectory())
             for (String child : file.list())
                 deleteDir(new File(file, child));
         file.delete();
     }


     @Override
     protected void onPreExecute() {
         final Activity activity = this.activity.get();
         temp=((MainActivity)activity).getGridAdapter().getSelected();
         super.onPreExecute();
     }

     @Override
     protected Object doInBackground(Object[] params) {
         for(int i=0;i<temp.size();i++)
             deleteDir(temp.get(i));
         return null;
     }

     @Override
     protected void onPostExecute(Object o) {
         final Activity activity=this.activity.get();
         ((MainActivity) activity).isShow(false);
         ((MainActivity)activity).getGridAdapter().notifyDataSetChanged();
         activity.invalidateOptionsMenu();
         ((MainActivity)activity).display(path);
         ((MainActivity)activity).callBroadCast(path);
         super.onPostExecute(o);
     }
 }

}
