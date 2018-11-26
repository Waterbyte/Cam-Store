package sagar.mehar.camstore.utils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.camerakit.CameraKitView;

import java.io.File;
import java.io.FileOutputStream;

import sagar.mehar.camstore.R;


/**
 * Created by Mountain on 08-11-18.
 */

public class CameraActivity extends Activity {


    private CameraKitView cameraKitView;
    private ImageButton clickImage;
    private String currentPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);
        currentPath = getIntent().getStringExtra(Constants.CURRENT_PATH);
        cameraKitView = findViewById(R.id.camera);
        clickImage = findViewById(R.id.clickImage);
        setupListeners();
    }

    private void setupListeners() {

        clickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, byte[] photo) {
                        File savedPhoto = new File(currentPath, "photo.jpg");
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
