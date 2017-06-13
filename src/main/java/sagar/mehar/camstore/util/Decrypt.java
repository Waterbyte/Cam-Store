package sagar.mehar.camstore.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import sagar.mehar.camstore.MainActivity;
import sagar.mehar.camstore.R;

/**
 * Created by Mountain on 09-06-2017.
 */

public class Decrypt {

    private final WeakReference<Activity> activity;
    private String untext;
    private String uniq2;
    private final ArrayList<String> FilestoExp = new ArrayList<>();
    private final ArrayList<String> FilestoExp2 = new ArrayList<>();

    public Decrypt(final Activity activity) {
        this.activity = new WeakReference<>(activity);
        fetchData();
    }
    private Activity getActivity()
    {
        return this.activity.get();
    }


    private ArrayList<String> FilestoExplore(File file) {


        if (file.isDirectory()) {
            for (String child : file.list())
                FilestoExplore(new File(file, child));
        } else {
            if (file.getName().endsWith(".cslock")) {
                FilestoExp.add(file.getAbsolutePath());
            }
        }
        return FilestoExp;
    }

    private void fetchData()
    {
        final Activity activity = getActivity();
        final ArrayList<File> selectedFileList=((MainActivity)activity).getGridAdapter().getSelected();
        if(selectedFileList.size()==0)
        {
            Toast.makeText(activity,"No File Selected",Toast.LENGTH_SHORT).show();
        }
        else
        {
            for(File f:selectedFileList)
            {
                FilestoExp2.addAll(FilestoExplore(f));
            }
            String[] temp = FilestoExp2.toArray(new String[FilestoExp2.size()]);
            if (temp.length == 0) {
                Toast.makeText(activity, "There are Encrypted Files inside!!! Please Decrypt them first Or Folder is empty!!!", Toast.LENGTH_SHORT).show();
            } else {
                DecryptDialog(temp);
            }
        }
    }

    private void DecryptDialog(final String[] temp)
    {
        final Activity activity=getActivity();
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View promptView = layoutInflater.inflate(R.layout.decryptit, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setIcon(R.drawable.menu_save);
        alertDialogBuilder.setTitle("Enter your Password");
        alertDialogBuilder.setView(promptView);

        final EditText unedit = (EditText) promptView.findViewById(R.id.unpass1);

        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                untext = unedit.getText().toString();
                uniq2 = untext;
                new DecryptUtil(activity).execute(temp);
                dialog.dismiss();
                untext = null;

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                dialog.dismiss();
            }
        });


        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    class DecryptUtil extends AsyncTask<String, Void, Boolean> {
        int flag = 0;
        final ProgressDialog pdialog;

        public DecryptUtil(Context context) {
            pdialog = new ProgressDialog(context);
        }


        @Override
        protected void onPreExecute() {
            pdialog.setIndeterminate(true);
            pdialog.setCancelable(false);
            pdialog.setTitle("Decrypting Files");
            pdialog.setMessage("Please Wait. Decryption Takes Time...");
            pdialog.show();
        }


        protected Boolean doInBackground(String... data) {
            try {

                for (String aData : data) {
                    File fil = new File(aData);
                    int a = aData.lastIndexOf('.');
                    String s = aData.substring(0, a);
                    File fil2 = new File(s);
                    char[] c = uniq2.toCharArray();
                    FileInputStream fin = new FileInputStream(fil);
                    FileOutputStream fon = new FileOutputStream(fil2);
                    try {
                        AES.decrypt(c, fin, fon);
                    } catch (AES.InvalidPasswordException e) {
                        flag = 2;
                        fil2.delete();
                    }
                    if (flag == 0) {
                        fil.delete();
                    }
                    fon.flush();
                    fon.close();
                    fin.close();

                }
            } catch (Exception e) {
                flag = 1;
            }
            return !isCancelled();
        }


        protected void onPostExecute(Boolean result) {
            if (pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if (flag == 1) {
                Toast.makeText(getActivity(), "Operation Unsuccessful!!!", Toast.LENGTH_SHORT).show();
            } else if (flag == 2) {
                Toast.makeText(getActivity(), "Wrong Password...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Files Decrypted", Toast.LENGTH_SHORT).show();
            }
            final Activity activity=getActivity();
            ((MainActivity) activity).isShow(false);
            ((MainActivity)activity).getGridAdapter().notifyDataSetChanged();
            activity.invalidateOptionsMenu();
            ((MainActivity)activity).display(((MainActivity)activity).getCurrentPath());
            ((MainActivity)activity).callBroadCast(((MainActivity)activity).getCurrentPath());
            uniq2 = null;

        }
    }

}
