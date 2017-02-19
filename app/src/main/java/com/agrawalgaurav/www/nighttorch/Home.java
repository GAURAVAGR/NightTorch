package com.agrawalgaurav.www.nighttorch;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import static android.content.pm.PackageManager.*;
import static java.security.AccessController.getContext;

public class Home extends Activity {
    Button torch;
    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
     Parameters params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
          torch = (Button) findViewById(R.id.torch);


            String permission = Manifest.permission.CAMERA;
            if (PERMISSION_GRANTED != ContextCompat.checkSelfPermission(Home.this, permission)){
     /*           if(!ActivityCompat.shouldShowRequestPermissionRationale(Home.this, permission)){
                    requestPermissions(new String[]{permission}),
                            1);
                }*/
            }


        MobileAds.initialize(getApplicationContext(), "ca-app-pub-2777383527093660/9714220039");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        AdView mAdView2 = (AdView) findViewById(R.id.adView2);
        AdRequest adRequest2 = new AdRequest.Builder().build();


        mAdView.loadAd(adRequest);

        mAdView2.loadAd(adRequest2);

    /*    AdView adView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(TEST_DEVICE_ID)
                .build();
        adView.loadAd(adRequest);*/


       Boolean hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(FEATURE_CAMERA_FLASH);

        if (!hasFlash) {
            // device doesn't support flash
            // Show alert message and close the application
            AlertDialog alert = new AlertDialog.Builder(Home.this)
                    .create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            alert.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                }
            });
            alert.show();
            return;
        }



            getCamera();



        // displaying button image
//        toggleButtonImage();


        // Switch button click event to toggle flash on/off
        torch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isFlashOn) {
//                    toggleButtonImage();
                    turnOffFlash();
                    torch.setText("ON");
                } else {
                    // turn on flashtoggleButtonImage();
//                    toggleButtonImage();
                    torch.setText("OFF");
                    turnOnFlash();

                }
            }
        });





    }



    private void getCamera(){
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                AlertDialog alert = new AlertDialog.Builder(Home.this)
                        .create();
                alert.setTitle("Error");
                alert.setMessage("Sorry, Either your device doesn't have camera or please allow permission to access camera");
                alert.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(   DialogInterface dialog, int which) {
                        // closing the application
                        finish();
                    }
                });
                alert.show();

            }
        }
    }

    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }


            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;

            // changing button/switch image

        }

    }

    private void toggleButtonImage() {
        if(torch.getText().toString().trim().equals("ON")) {
            torch.setText("OFF");
        }else if(torch.getText().toString().trim().equals("OFF")) {
            torch.setText("ON");
        }

    }


    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            // play sound


            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;

            // changing button/switch image

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // on pause turn off the flash
        turnOffFlash();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // on resume turn on the flash
        if(hasFlash)
            turnOnFlash();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // on starting the app get the camera params
        getCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // on stop release the camera
        if (camera != null) {

            camera = null;
        }
    }

}
