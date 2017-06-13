package sagar.mehar.camstore.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

import sagar.mehar.camstore.MainActivity;


public class Sharing {
    private Context context;

    public Sharing(Context context)          //public constructor to access it outside package
    {
        this.context=context;
        share();
    }
    private void share()
    {
     ArrayList<File> temp=((MainActivity)context).getGridAdapter().getSelected(); //selected items from gridadapter
        ArrayList<Uri> imageUris = new ArrayList<>();                            //image uri arraylist
        for (int i = 0; i < temp.size(); i++) {
            imageUris.add(Uri.fromFile(temp.get(i)));
        }
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        shareIntent.setType("*/*");
        context.startActivity(Intent.createChooser(shareIntent, "Share Data via..."));
    }

}
