package sagar.mehar.camstore.utils;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.camerakit.CameraKit;
import com.camerakit.CameraKitView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import sagar.mehar.camstore.R;


/**
 * Created by Mountain on 08-11-18.
 */

public class CameraActivity extends Activity {


    private CameraKitView cameraKitView;
    private ImageButton clickImage, reverseCamera, reverseFlash;
    private String currentPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);
        currentPath = getIntent().getStringExtra(Constants.CURRENT_PATH);
        cameraKitView = findViewById(R.id.camera);
        cameraKitView.setFlash(CameraKit.FLASH_OFF);
        clickImage = findViewById(R.id.clickImage);
        reverseCamera = findViewById(R.id.reverseCamera);
        reverseFlash = findViewById(R.id.reverseFlash);
        setupListeners();
    }

    private void setupListeners() {

        clickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, byte[] photo) {
                        CharSequence s = DateFormat.format("MM-dd-yy hh-mm-ss", new Date().getTime());
                        File savedPhoto = new File(currentPath, s.toString() + " cam.jpg");
                        try {
                            FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
                            outputStream.write(photo);
                            outputStream.close();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                            Log.e("CKDemo", "Exception in photo callback");
                        }
                    }
                });
            }
        });


        reverseCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cameraKitView.getFacing() == CameraKit.FACING_BACK)
                    cameraKitView.setFacing(CameraKit.FACING_FRONT);
                else
                    cameraKitView.setFacing(CameraKit.FACING_BACK);
            }
        });

        reverseFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cameraKitView.getFlash() == CameraKit.FLASH_OFF) {
                    reverseFlash.setImageResource(R.drawable.ic_action_flash);
                    cameraKitView.setFlash(CameraKit.FLASH_ON);
                } else {
                    reverseFlash.setImageResource(R.drawable.ic_action_flash_reverse);
                    cameraKitView.setFlash(CameraKit.FLASH_OFF);
                }


            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
