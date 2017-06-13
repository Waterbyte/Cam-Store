package sagar.mehar.camstore;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.doctoror.particlesdrawable.ParticlesDrawable;
import com.github.clans.fab.FloatingActionButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import sagar.mehar.camstore.adapter.GridAdapter;
import sagar.mehar.camstore.util.CreateFolder;
import sagar.mehar.camstore.util.Cut;
import sagar.mehar.camstore.util.Decrypt;
import sagar.mehar.camstore.util.Delete;
import sagar.mehar.camstore.util.Encrypt;
import sagar.mehar.camstore.util.FileComparator;
import sagar.mehar.camstore.util.Hide;
import sagar.mehar.camstore.util.Paste;
import sagar.mehar.camstore.util.Rename;
import sagar.mehar.camstore.util.Sharing;
import sagar.mehar.camstore.util.Unhide;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {


    private FloatingActionButton camBut;
    private FloatingActionButton vidBut;
    private FloatingActionButton addfoldBut;
    private String fixedPath = "";
    private boolean isShow = false;
    private GridAdapter gridAdapter;
    private String currentPath;
    private final static int CAMERA_RQ = 6969;
    private int MenuType = 1;
    private ArrayList<File> cutFileList;
    private final ParticlesDrawable mDrawable = new ParticlesDrawable();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        MenuItem deleteAll = menu.findItem(R.id.deletemenu);
        MenuItem renameAll = menu.findItem(R.id.renamemenu);
        MenuItem shareAll = menu.findItem(R.id.sharemenu);
        MenuItem cutAll = menu.findItem(R.id.cutmenu);
        MenuItem selectAll = menu.findItem(R.id.selectallmenu);
        MenuItem pasteAll = menu.findItem(R.id.pastemenu);
        MenuItem cancelPaste = menu.findItem(R.id.cancelPaste);
        // MenuItem encryptAll=menu.findItem(R.id.action_encrypt);
        // MenuItem decryptAll=menu.findItem(R.id.action_decrypt);
        // MenuItem hideAll=menu.findItem(R.id.action_hide);
        // MenuItem unhideAll=menu.findItem(R.id.action_unhide);

        if (isShow) {
            try {
                ActionBar actionBar = getSupportActionBar();
                actionBar.setTitle("");
            } catch (Exception e) {
                Log.e("title", "title problem");
            }
            if (MenuType == 1) {
                deleteAll.setVisible(true);
                renameAll.setVisible(true);
                shareAll.setVisible(true);
                cutAll.setVisible(true);
                selectAll.setVisible(true);
            }
        } else if (MenuType == 2) {
            pasteAll.setVisible(true);
            cancelPaste.setVisible(true);
        } else {
            try {
                ActionBar actionBar = getSupportActionBar();
                File f = new File(currentPath);
                actionBar.setTitle("CamStore   " + f.getName());
            } catch (Exception e) {
                Log.e("title", "title problem");
            }
        }

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.selectallmenu:
                gridAdapter.setSelectedAll();
                break;
            case R.id.sharemenu:
                new Sharing(this);
                break;
            case R.id.renamemenu:
                new Rename(MainActivity.this);
                break;
            case R.id.deletemenu:
                new Delete(MainActivity.this, currentPath);
                break;
            case R.id.cutmenu:
                new Cut(MainActivity.this);
                break;
            case R.id.pastemenu:
                new Paste(MainActivity.this);
                break;
            case R.id.cancelPaste: {
                setMenuType(1);
                invalidateOptionsMenu();
            }
            break;
            case R.id.action_encrypt: {
             try{new Encrypt(MainActivity.this);}
             catch (Exception e)
             {
                 Toast.makeText(MainActivity.this,"Exception Occured",Toast.LENGTH_SHORT).show();
             }
            }
                break;
            case R.id.action_decrypt:
            {
                try {
                    new Decrypt(MainActivity.this);
                }catch (Exception e)
                {
                    Toast.makeText(MainActivity.this,"Exception Occured",Toast.LENGTH_SHORT).show();
                }
            }
                break;
            case R.id.action_hide: {
                try {
                    new Hide(MainActivity.this);
                } catch (Exception e)
                {
                    Toast.makeText(MainActivity.this,"Exception Occured",Toast.LENGTH_SHORT).show();
                }
            }
                break;
            case R.id.action_unhide: {
                try {
                    new Unhide(MainActivity.this);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Exception Occured", Toast.LENGTH_SHORT).show();
                }
            }
                break;
            case R.id.rateIt: {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                try {
                    intent.setData(Uri.parse("market://details?id=sagar.mehar.camstore"));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "Problem Occured!!!", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }


        return super.onOptionsItemSelected(item);
    }

    public void setMenuType(int value) {
        MenuType = value;
    }

    public void setCutFileList(ArrayList<File> fileList) {
        cutFileList = fileList;
    }

    public ArrayList<File> getCutFileList() {
        return cutFileList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawable.setLineColor(Color.MAGENTA);
        findViewById(R.id.gridView).setBackground(mDrawable);
        FloatingActionButtonFunction();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(options)

                .build();
        ImageLoader.getInstance().init(config);

        try {
            currentPath = fixedPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath();
        } catch (Exception e) {
            Toast.makeText(this, "Cannot Access your Storage Media", Toast.LENGTH_SHORT).show();
        }
        permissions();
    }

    public void permissions()
    {
        String[] permission={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA
                , Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, permission)) {
            display(fixedPath);
        }
        else
        {
        EasyPermissions.requestPermissions(MainActivity.this, "We need to display your pics...",100,permission);
    }
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Toast.makeText(this,"Granted Permisssions...",Toast.LENGTH_SHORT).show();
        display(fixedPath);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this,"Permissions are required for App's Normal Working...",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onStart() {
        mDrawable.start();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mDrawable.stop();
        super.onStop();
    }

    private void FloatingActionButtonFunction()
    {
        camBut=(FloatingActionButton)findViewById(R.id.camBut);
        vidBut=(FloatingActionButton)findViewById(R.id.vidBut);
        addfoldBut=(FloatingActionButton)findViewById(R.id.addfoldBut);

        camBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeStillShots();
            }
        });

        vidBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeVideo();
            }
        });

        addfoldBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreateFolder().dirDialog(currentPath,MainActivity.this);
            }
        });
    }

    private void takeStillShots()
    {
        new MaterialCamera(MainActivity.this)
                .saveDir(currentPath)
                .allowRetry(true)
                .videoPreferredHeight(480)
                .stillShot().qualityProfile(MaterialCamera.QUALITY_HIGH)
                .start(CAMERA_RQ);
    }
    private void takeVideo()
    {
        new MaterialCamera(MainActivity.this)                               // Constructor takes an Activity
                .allowRetry(true)                                  // Whether or not 'Retry' is visible during playback
                .autoSubmit(false)                                 // Whether or not user is allowed to playback videos after recording. This can affect other things, discussed in the next section.
                .saveDir(currentPath)                               // The folder recorded videos are saved to
                .defaultToFrontFacing(false)                       // Whether or not the camera will initially show the front facing camera
                .retryExits(false)                                 // If true, the 'Retry' button in the playback screen will exit the camera instead of going back to the recorder
                .restartTimerOnRetry(false)                        // If true, the countdown timer is reset to 0 when the user taps 'Retry' in playback
                .continueTimerInPlayback(false)                    // If true, the countdown timer will continue to go down during playback, rather than pausing.
                .qualityProfile(MaterialCamera.QUALITY_HIGH)       // Sets a quality profile, manually setting bit rates or frame rates with other settings will overwrite individual quality profile settings
                .videoPreferredHeight(480)                         // Sets a preferred height for the recorded video output.
                .videoPreferredAspect(4f / 3f)                     // Sets a preferred aspect ratio for the recorded video output.
                .showPortraitWarning(false)
                .start(CAMERA_RQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Received recording or error from MaterialCamera
        if (requestCode == CAMERA_RQ) {
            if (resultCode == RESULT_OK) {
                (MainActivity.this).display(currentPath);
                this.callBroadCast(getCurrentPath());
            } else if (data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void callBroadCast(String pathscan) {
        if (Build.VERSION.SDK_INT >= 14) {

            MediaScannerConnection.scanFile(this, new String[]{pathscan}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {

                }
            });
        } else {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" +pathscan)));
        }
    }


    public  void display(String path)
    {
        try {
            currentPath=path;
            File folderX = new File(path);
            ActionBar actionBar=getSupportActionBar();
            actionBar.setTitle("CamStore   "+folderX.getName());
            File[] folderXCHILD = folderX.listFiles();
            Arrays.sort(folderXCHILD, new FileComparator());
            folderXCHILD=removeInv(folderXCHILD);
            GridView gridView = (GridView)findViewById(R.id.gridView);
            gridAdapter = new GridAdapter(this, folderXCHILD);
            gridView.setAdapter(gridAdapter);

        }
        catch (Exception e)
        {
            Toast.makeText(this, "Cannot display the Content.", Toast.LENGTH_SHORT).show();
        }

    }




public boolean isShow()
{
    return isShow;
}
    public void isShow(boolean value)
    {
        isShow=value;
    }
    public void setcurrentPath(String path)
    {
        currentPath=path;
    }
    public String getFixedPath(){return fixedPath;}
    public String getCurrentPath()
    {
        return currentPath;
    }
    public GridAdapter getGridAdapter(){return gridAdapter;}

    @Override
    public void onBackPressed() {
        if(isShow)
        {
            isShow=false;
            gridAdapter.notifyDataSetChanged();
            invalidateOptionsMenu();
        }
        else {

            FragmentManager Manager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = Manager.beginTransaction();
            Fragment fragment = Manager.findFragmentByTag("tag");
            if (fragment != null && fragment.isVisible())
                fragmentTransaction.remove(fragment).commit();
            else if(currentPath.equals(fixedPath))
            {
                super.onBackPressed();
            }
                else
            {
                display(currentPath.substring(0,currentPath.lastIndexOf('/')));
            }
        }
    }

    private static File[] removeInv(File[] folderXCHILD)
    {
        ArrayList<File> visfolderXChild= new ArrayList<>();          //this step remove all the files starting with .
        int vis=0;
        for(File f:folderXCHILD)
        {
            if(f.getName().startsWith("."))
            {}
            else {
                visfolderXChild.add(f);
                ++vis;
            }
        }
        Iterator<File> it=visfolderXChild.iterator();
        File[] folderXChildvis=new File[vis];
        int ivis=0;
        while(it.hasNext())
        {
            folderXChildvis[ivis]=it.next();
            ivis++;
        }
        return folderXChildvis;
    }


}
