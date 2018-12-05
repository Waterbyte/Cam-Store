package sagar.mehar.camstore.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import sagar.mehar.camstore.R;
import sagar.mehar.camstore.interfaces.ConfirmationInterface;

public class ConfirmationDialog {
    ConfirmationInterface confirmationInterface;
    private Context localContext;
    private String title, message,origin;

    public ConfirmationDialog(Context localContext, String origin, String title, String message) {
        this.localContext = localContext;
        this.title = title;
        this.message = message;
        this.confirmationInterface = (ConfirmationInterface) localContext;
        this.origin = origin;
        show();
    }

    private void show() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(localContext, R.style.MyDialogTheme);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                confirmationInterface.approved_confirm(origin);
            }
        });
        builder.setNegativeButton(R.string.Cancelbut, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                confirmationInterface.cancel_confirm(origin);
            }
        });
        builder.show();


    }

}
