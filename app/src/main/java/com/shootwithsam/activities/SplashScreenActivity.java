package com.shootwithsam.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.shootwithsam.R;
import com.shootwithsam.utils.SamSharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int MY_PERMISSIONS_REQUEST_CODE = 0;
    private static Context applicationContext;
    private String currentVersion;
    private SharedPreferences mSharedPrefs;
    private boolean isFirstInstall = true;

    public static Context getContext() {
        return applicationContext;
    }

    //    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        try {
            applicationContext = SplashScreenActivity.this;
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

            mSharedPrefs = getSharedPreferences("InstallPrefs", Context.MODE_PRIVATE);
            isFirstInstall = mSharedPrefs.getBoolean("firstInstall", true);

            /**
             *  Check for version code.
             *  If its >=23 means its Marshmalllow device or up version device then externaly ask permission user to enable it.
             *  Otherwise start app normally
             */
            int versionName = Build.VERSION.SDK_INT;
            Log.d("Splash", "Version : " + versionName);
            if (versionName >= 23) {
                checkPermissions();
            } else {
                startThread();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.N)

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(SplashScreenActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreenActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                askForPermission();
            } else {
                askForPermission();
            }
        } else if (ContextCompat.checkSelfPermission(SplashScreenActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreenActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                askForPermission();
            } else {
                askForPermission();
            }
        } else if (ContextCompat.checkSelfPermission(SplashScreenActivity.this,
                Manifest.permission.INSTALL_SHORTCUT) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreenActivity.this,
                    Manifest.permission.INSTALL_SHORTCUT)) {
                askForPermission();
            } else {
                askForPermission();
            }
        } else
            startThread();

    }

    //    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.N)

    private void askForPermission() {
        ActivityCompat
                .requestPermissions(this,
                        new String[]{Manifest.permission.INSTALL_SHORTCUT, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CODE);
    }


    private void showPermissionSettings() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashScreenActivity.this);
        alertDialog.setMessage(
                "It seems that you have disabled some permissions for this application. To use this application enable required permissions. ");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                finish();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        }).create().show();
    }


    @SuppressLint({"NewApi", "Override"})
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(Manifest.permission.INSTALL_SHORTCUT, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                if (perms.get(Manifest.permission.INSTALL_SHORTCUT) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(SplashScreenActivity.this, "All required permission granted", Toast.LENGTH_SHORT).show();
                    startThread();
                } else {
                    showPermissionSettings();
                }
            }
            break;
        }
    }

    private void startMainIntent() {
        try {
            if (isFirstInstall) {
                addShortcut();
            }
            final Intent in = new Intent(SplashScreenActivity.this, LoginActivity.class);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(in);
                    finish();
                }
            }, 3000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addShortcut() {
        //Adding shortcut for MainActivity
        //on Home screen
        Intent shortcutIntent = new Intent(getApplicationContext(),
                SplashScreenActivity.class);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        // addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                        R.drawable.logo));

        addIntent
                .setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        addIntent.putExtra("duplicate", false);  //may it's already there so don't duplicate
        getApplicationContext().sendBroadcast(addIntent);
    }

    /**
     * Start thread for login
     */
    private void startThread() {

        try {
            boolean strFirstTime = SamSharedPreferences.getFirstTimeUse(applicationContext);
            if (strFirstTime) {

                SamSharedPreferences.setFirstTimeUse(applicationContext);
                startMainIntent();
            } else {
                startMainIntent();
//                new GetVersionCodeAsyncTask().execute();
            }
        } catch (SecurityException exp) {
            exp.printStackTrace();
        }
    }
}
