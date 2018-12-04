package sagar.mehar.camstore.utils;

import android.content.Context;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import sagar.mehar.camstore.ExplorerFragment;
import sagar.mehar.camstore.MainActivity;
import sagar.mehar.camstore.R;
import sagar.mehar.camstore.async.DeleteFiles;

public class ActionBarCallback implements ActionMode.Callback {

    Context context;
    ExplorerFragment explorerFragment;

    public ActionBarCallback(Context context) {
        this.context = context;
        explorerFragment = ((MainActivity) context).getFragment();
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        actionMode.getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        ArrayList<String> selectedFiles = explorerFragment.getSelectedItems();
        switch (menuItem.getItemId()) {
            case R.id.sharemenu:
                break;
            case R.id.renamemenu:
                break;
            case R.id.deletemenu:
                new DeleteFiles(context, selectedFiles).execute();
                break;
            case R.id.action_encrypt:
                break;
            case R.id.action_decrypt:
                break;
            case R.id.rateIt:
                break;


        }

        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        if (explorerFragment != null) {
            explorerFragment.stopMultiSelect();
        }
    }
}
