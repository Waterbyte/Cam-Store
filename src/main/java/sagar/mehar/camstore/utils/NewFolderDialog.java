package sagar.mehar.camstore.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import sagar.mehar.camstore.MainActivity;
import sagar.mehar.camstore.R;

public class NewFolderDialog {
    private Context localContext;
    private String currentPath;

    public NewFolderDialog(Context localContext, String currentPath) {
        this.localContext = localContext;
        this.currentPath = currentPath;
        show();
    }

    private void show() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(localContext,R.style.MyDialogTheme);
        LayoutInflater layoutInflater = ((MainActivity) localContext).getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.newfolder, null);
        builder.setView(dialogView);

        final EditText folderName = dialogView.findViewById(R.id.folderNameEdit);

        final AlertDialog alertDialog = builder.create();
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CommonUtility.createDir(currentPath, folderName.getText().toString().trim());
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

}
