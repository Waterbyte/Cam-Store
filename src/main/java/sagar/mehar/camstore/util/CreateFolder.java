package sagar.mehar.camstore.util;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

import sagar.mehar.camstore.MainActivity;
import sagar.mehar.camstore.R;

/**
 * Created by Mountain on 04-12-2016.
 */

public class CreateFolder {
    private String newName;
    public void dirDialog(final String path, final Context context)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.renamedialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("New Folder");
        alertDialogBuilder.setView(promptView);
        final EditText editText = (EditText) promptView.findViewById(R.id.rename_query);

        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 newName=editText.getText().toString();
                 if(createDir(path,newName))
                 {
                     ((MainActivity) context).display(path);
                 }
                else
                 {
                     Toast.makeText(context,"There was an exception while creating the Folder.",Toast.LENGTH_SHORT).show();
                 }
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }


    private static boolean createDir(String path, String name) {
        File folder = new File(path, name);
        return !folder.exists() && folder.mkdir();

    }

}
