package sagar.mehar.camstore.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import sagar.mehar.camstore.MainActivity;
import sagar.mehar.camstore.R;

/**
 * Created by Mountain on 26-12-2016.
 */

public class Rename{
    private final WeakReference<Activity> activity;
    private ArrayList<File> temp;
    public Rename(final Activity activity) {
        this.activity = new WeakReference<>(activity);
        renamefunc();
    }
  private void renamefunc()
  {

      final Activity activity = this.activity.get();
      temp=((MainActivity)activity).getGridAdapter().getSelected();
      if(temp.size()!=1)
      {
          Toast.makeText(activity, "Please select only one item!!!", Toast.LENGTH_SHORT).show();
      }
      else
      {
            LayoutInflater layoutInflater = LayoutInflater.from(activity);
            View promptView = layoutInflater.inflate(R.layout.renamedialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            alertDialogBuilder.setTitle(R.string.Renamebutton);
            alertDialogBuilder.setView(promptView);

            final EditText editText = (EditText) promptView.findViewById(R.id.rename_query);

          final File original=temp.get(0);
          final String extension=getExtension(original);                      // this step checks for filetype whether it is file or folder
          String filename;

          if(extension!=null)
          {
              filename=original.getName().substring(0,original.getName().lastIndexOf("."));
          }
          else
          {
              filename=original.getName();
          }

          editText.setText(filename);

           
            alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String newpath=original.getParent();               //parent path

                    File newFile;
                    if(extension!=null)
                    {
                        newFile=new File(newpath,editText.getText().toString()+"."+extension);   //add extension to file
                    }
                    else
                    {
                        newFile=new File(newpath,editText.getText().toString()); //folder so no extension
                    }

                    try {
                        temp.get(0).renameTo(newFile);                         //look for any exception
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(activity,"Failure",Toast.LENGTH_SHORT).show();
                    }
                    ((MainActivity) activity).isShow(false);
                    activity.invalidateOptionsMenu();
                    ((MainActivity)activity).callBroadCast(((MainActivity)activity).getCurrentPath());
                    ((MainActivity) activity).display(temp.get(0).getParent());   // display is called since the name of file is changed
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ((MainActivity) activity).isShow(false);
                            ((MainActivity) activity).display(temp.get(0).getParent());
                            activity.invalidateOptionsMenu();
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();

    }

  }
    private  String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i;
        if(s.contains(".")){
            i = s.lastIndexOf('.');
        }
        else
        {
            return null;
        }

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}
