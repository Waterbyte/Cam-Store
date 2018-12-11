package sagar.mehar.camstore.async;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.util.ArrayList;

import sagar.mehar.camstore.MainActivity;
import sagar.mehar.camstore.utils.CommonUtility;

public class DeleteFiles extends AsyncTask<String, Void, Void> {
    private Context localContext;
    private ArrayList<String> selectedItems;

    public DeleteFiles(Context localContext, ArrayList<String> selectedItems) {
        this.localContext = localContext;
        this.selectedItems = selectedItems;
    }

    @Override
    protected Void doInBackground(String... strings) {
        for(String items: selectedItems){
            CommonUtility.deleteDir(new File(items));
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
