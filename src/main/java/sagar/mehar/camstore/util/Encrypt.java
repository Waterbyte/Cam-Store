package sagar.mehar.camstore.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

public class Encrypt {
    private final WeakReference<Activity> activity;
    private String text2;
    private String text1;
    private String uniq;
    private final ArrayList<String> listFilestoExp = new ArrayList<>();
    private final ArrayList<String> listFilestoExp2 = new ArrayList<>();
    public Encrypt(final Activity activity) {
        this.activity = new WeakReference<>(activity);
        fetchData();
    }
    private Activity getActivity()
    {
        return this.activity.get();
    }
    private ArrayList<String> listFilestoExplore(File file) {
        if (file.isDirectory()) {
            for (String child : file.list())
                listFilestoExplore(new File(file, child));
        } else {
            if (file.getName().endsWith(".cslock")) {
                listFilestoExp.clear();
                return listFilestoExp;
            } else {
                listFilestoExp.add(file.getAbsolutePath());
            }
        }
        return listFilestoExp;
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
              listFilestoExp2.addAll(listFilestoExplore(f));
            }
            String[] temp = listFilestoExp2.toArray(new String[listFilestoExp2.size()]);
            if (temp.length == 0) {
                Toast.makeText(activity, "There are Encrypted Files inside!!! Please Decrypt them first Or Folder is empty!!!", Toast.LENGTH_SHORT).show();
            } else {
                EncryptDialog(temp);
            }
        }
    }


    private void EncryptDialog(final String[] temp)
    {
        final Activity activity = getActivity();
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View promptView = layoutInflater.inflate(R.layout.encryptit, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setIcon(R.drawable.menu_save);
        alertDialogBuilder.setTitle("Enter your Password");
        alertDialogBuilder.setMessage("Enter a Strong Password!!!");
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.firstpass);
        final EditText editText2 = (EditText) promptView.findViewById(R.id.secondpass);
        final TextView textView = (TextView) promptView.findViewById(R.id.thirdpass);
        text1 = editText.getText().toString();
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uniq = text1;
                new EncryptUtil(activity).execute(temp);
                dialog.dismiss();
                text1 = text2 = null;

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
        alert.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);


        editText2.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                text1 = editText.getText().toString();
                text2 = editText2.getText().toString();

                if (text1.equals(text2) && text2.length() > 0) {
                    textView.setText(R.string.Passmatch);
                    alert.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    textView.setText(R.string.Passunmatch);
                    alert.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }
        });


    }


    class EncryptUtil extends AsyncTask<String, Void, Boolean> {
        final ProgressDialog pdialog;
        int encryptflag = 0;

        public EncryptUtil(Context context) {
            pdialog = new ProgressDialog(context);
        }


        @Override
        protected void onPreExecute() {
            pdialog.setIndeterminate(true);
            pdialog.setCancelable(false);
            pdialog.setTitle("Encrypting Files");
            pdialog.setMessage("Please wait. Encryption Takes Time...");
            pdialog.show();
        }


        protected Boolean doInBackground(String... data) {
            try {
                for (String aData : data) {
                    File fil = new File(aData);
                    File fil2 = new File(aData + ".cslock");
                    char[] c = uniq.toCharArray();
                    FileInputStream fin = new FileInputStream(fil);
                    FileOutputStream fon = new FileOutputStream(fil2);
                    AES.encrypt(256, c, fin, fon);
                    fil.delete();
                    fon.flush();
                    fon.close();
                    fin.close();
                }
            } catch (Exception e) {
                encryptflag = 1;
            }
            return !isCancelled();
        }


        protected void onPostExecute(Boolean result) {

            if (pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if (encryptflag == 1) {
                encryptflag = 0;
                Toast.makeText(activity.get().getApplicationContext(), "You can't Encrypt it...   :)", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity.get().getApplicationContext(), "Files Encrypted", Toast.LENGTH_SHORT).show();
            }

            final Activity activity=getActivity();
            ((MainActivity) activity).isShow(false);
            ((MainActivity)activity).getGridAdapter().notifyDataSetChanged();
            activity.invalidateOptionsMenu();
            ((MainActivity)activity).display(((MainActivity)activity).getCurrentPath());
            ((MainActivity)activity).callBroadCast(((MainActivity)activity).getCurrentPath());
            uniq=null;
        }
    }
}
