package sagar.mehar.camstore.utils;

import android.content.Context;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import sagar.mehar.camstore.ExplorerFragment;
import sagar.mehar.camstore.MainActivity;
import sagar.mehar.camstore.R;

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
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        if (explorerFragment != null) {
            explorerFragment.stopMultiSelect();
        }
    }
}
