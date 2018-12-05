package sagar.mehar.camstore;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import sagar.mehar.camstore.adapters.GridAdapter;
import sagar.mehar.camstore.interfaces.ConfirmationInterface;
import sagar.mehar.camstore.utils.ActionBarCallback;
import sagar.mehar.camstore.utils.CameraActivity;
import sagar.mehar.camstore.utils.Constants;
import sagar.mehar.camstore.utils.NewFolderDialog;

/**
 * Created by Mountain on 04-11-18.
 */

public class MainActivity extends BaseAppCompatActivity implements EasyPermissions.PermissionCallbacks, GridAdapter.ItemClickListener, ConfirmationInterface {
    private final static String TAG = "Main Activity";
    Intent cameraIntent = null;
    private String homePath = null;
    private String currentPath = null;
    private FloatingActionButton camBut, addfoldBut;
    private ExplorerFragment explorerFragment = null;
    private ActionMode actionMode;
    private NewFolderDialog newFolderDialog;
    private Context localContext = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        FloatingActionButtonFunction();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "on Start called");
        permissions();  //fragment is visible now
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCurrentPath(currentPath);
    }

    @Override
    public void onBackPressed() {


        if (!currentPath.equalsIgnoreCase(homePath)) {
            setCurrentPath(currentPath.substring(0, currentPath.lastIndexOf("/")));
        } else {
            super.onBackPressed();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        homePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
        setFragment();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        permissions(); //ask permission again
    }

    public ExplorerFragment getFragment() {
        return explorerFragment;
    }

    private void setFragment() {
        FragmentManager fm = getSupportFragmentManager();
        explorerFragment = (ExplorerFragment) fm.findFragmentByTag(Constants.EXPLORER_FRAGMENT);
        if (explorerFragment == null) {
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            explorerFragment = new ExplorerFragment();
            fragmentTransaction.add(R.id.fragmentContainer, explorerFragment, Constants.EXPLORER_FRAGMENT);
            fragmentTransaction.commit();
            fm.executePendingTransactions();
            setCurrentPath(homePath);
        }
    }

    @AfterPermissionGranted(Constants.PERMSSIONS)
    public void permissions() {
        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
                , Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, permission)) {
            homePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
            setFragment();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_dialog), Constants.PERMSSIONS, permission);
        }
    }

    public void setCurrentPath(String path) {
        currentPath = path;
        explorerFragment.setDisplayPath(currentPath);
    }

    private void FloatingActionButtonFunction() {
        camBut = findViewById(R.id.camBut);

        addfoldBut = findViewById(R.id.addfoldBut);
        if (currentPath == homePath) {

            camBut.setVisibility(View.VISIBLE);
            addfoldBut.setVisibility(View.VISIBLE);
        } else {

            camBut.setVisibility(View.VISIBLE);
            addfoldBut.setVisibility(View.GONE);
        }

        camBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent = new Intent(MainActivity.this, CameraActivity.class);
                cameraIntent.putExtra(Constants.CURRENT_PATH, currentPath);
                startActivity(cameraIntent);
            }
        });

        addfoldBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFolderDialog = new NewFolderDialog(localContext, currentPath);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        explorerFragment.onClickListener(position);
    }

    @Override
    public boolean onItemLongClicked(View view, int position) {
        actionMode = MainActivity.this.startActionMode(new ActionBarCallback(MainActivity.this));
        return explorerFragment.onLongClickListener(position);
    }

    public ActionMode getActionMode() {
        return actionMode;
    }


    @Override
    public void approved_confirm(String dialogOrigin) {

    }

    @Override
    public void cancel_confirm(String dialogOrigin) {

    }
}
