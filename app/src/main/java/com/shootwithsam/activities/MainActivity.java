package com.shootwithsam.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.shootwithsam.APIURLS;
import com.shootwithsam.R;
import com.shootwithsam.application.MyApplication;
import com.shootwithsam.fragments.AllLeadsFragment;
import com.shootwithsam.fragments.NotificationFragment;
import com.shootwithsam.utils.NetworkCheckUtility;
import com.shootwithsam.utils.SamSharedPreferences;
import com.shootwithsam.utils.SecuredNetworkUtility;
import com.shootwithsam.utils.ShowToastMessage;
import com.shootwithsam.widgets.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private static Context mContext;
    private static TextView mTextViewFirstname;
    private static TextView mTextViewLastName;
    private static TextView mTextViewUserEmail;
    private static String mStrFirstName = "";
    private static String mStrMiddleName = "";
    private static String mStrLastName = "";
    private static String mStrUserProfileUrL = "";
    private static String mStrUserId = "";
    private static String mStrUserEmail = "";
    private static JSONObject user_data;
    //  private ImageView mImageViewProfile;
    private ProgressBar mProgressBar;
    private JSONObject userData;
    private int REQUEST_CODE = 1000;
    private com.shootwithsam.widgets.CircularImageView mImageProfilePic;

    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;

        imageLoader = MyApplication.getImageLoader();
        displayImageOptions = MyApplication.getDisplayOptions();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    /*    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (!SamSharedPreferences.getIsUserLoggedIn(mContext)) {
            startActivity(new Intent(mContext, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
        try {
            userData = SamSharedPreferences.getUserInfo(mContext);
            Log.d(TAG, userData.toString());
            if (userData.length() <= 0) {
                Log.d(TAG, "No user data available");
            } else {
                mStrFirstName = userData.optString("first_name", "");
                mStrMiddleName = userData.optString("middle_name", "");
                mStrLastName = userData.optString("last_name", "");
                mStrUserId = userData.optString("user_id", "");
                mStrUserProfileUrL = userData.optString("profile_photo", "");
                mStrUserEmail = userData.optString("email_id", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mImageProfilePic = (CircularImageView) navigationView.getHeaderView(0).findViewById(R.id.imageViewProfileDrawer);
        mTextViewFirstname = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewDrawerFirstName);
        // mTextViewLastName = (TextView)navigationView.getHeaderView(0).findViewById(R.id.textViewDrawerLastName);
        mTextViewUserEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewDrawerUserEmail);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBarLoad);

        mTextViewFirstname.setText(mStrFirstName + mStrMiddleName + mStrLastName);
        mTextViewUserEmail.setText(mStrUserEmail);

//        if (mStrUserProfileUrL.toLowerCase().contains("http")) {
//            imageLoader.displayImage(mStrUserProfileUrL, mImageProfilePic, displayImageOptions, new SimpleImageLoadingListener() {
//                @Override
//                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                    if (loadedImage != null)
//                        mImageProfilePic.setImageBitmap(loadedImage);
//                    else
//                        Toast.makeText(mContext, "Profile photo not found", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }

        mImageProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
                finish();
            }
        });

        //  get UserProfile Details
        if (NetworkCheckUtility.isNetworkConnectionAvailable(mContext)) {
            if (!mStrUserId.equalsIgnoreCase(""))
                new GetProfileAsyncTask().execute();
            else {
                ShowToastMessage.showToast(mContext, "User not have logged in. Please login again.");
                SamSharedPreferences.setUserInfo(mContext, new JSONObject());
                SamSharedPreferences.setIsUserLoggedIn(mContext, false);
                startActivity(new Intent(mContext, LoginActivity.class));
                finish();
            }
        } else
            ShowToastMessage.noInternetToast(mContext);

        //  Load Default dashboard Fragment
        UpdateFragment(getResources().getString(R.string.strAllLeadLabel));

        //SharePreference Update profile data function
        UpdateProfileData();
    }

    public static void UpdateProfileData() {
        // Main code
        user_data = SamSharedPreferences.getUserInfo(mContext);
        mStrUserId = user_data.optString("user_id", "");
        try {
            mStrFirstName = user_data.getString("first_name");
            mStrMiddleName = user_data.getString("middle_name");
            mStrLastName = user_data.getString("last_name");
            mStrUserEmail = user_data.getString("email_id");
            mStrUserProfileUrL = user_data.optString("profile_photo");

            mTextViewFirstname.setText(mStrFirstName + " " + mStrMiddleName + " " + mStrLastName);
            mTextViewUserEmail.setText(mStrUserEmail);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GetProfile();
        UpdateProfileData();
    }
    private void GetProfile() {
        if (NetworkCheckUtility.isNetworkConnectionAvailable(mContext))
            new GetProfileAsyncTask().execute();
        else
            ShowToastMessage.noInternetToast(mContext);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                GetProfile();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Do you want to exit?");
            alertDialogBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            });
            alertDialogBuilder.setNegativeButton("Cancel", null);
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    //Logout User
    private void logoutUser() {
        //  Ask for close application
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setMessage(R.string.strLogoutMessage);
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton(getResources().getString(R.string.strLogoutLabel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!NetworkCheckUtility.isNetworkConnectionAvailable(mContext))
                    ShowToastMessage.noInternetToast(mContext);
                else {
//                    new AsyncUpdateLoginStatus().execute();
                    ShowToastMessage.showToast(mContext, "Log out successfully");
                   // Toast.makeText(mContext, "Log out successfully", Toast.LENGTH_SHORT).show();
                    SamSharedPreferences.setUserInfo(mContext, new JSONObject());
                    SamSharedPreferences.setIsUserLoggedIn(mContext, false);
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        }).create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//        Toast.makeText(mContext, "Item ID ", Toast.LENGTH_SHORT).show();

        /*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else*/
        if (id == R.id.nav_share) {

        } /*else if (id == R.id.nav_send) {

        }*/ else if (id == R.id.nav_logout) {
            logoutUser();
        } else if (id == R.id.nav_changePwd) {
            Intent intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
            Log.d(TAG, "ChangePwd");
            startActivity(intent);
        } else if (id == R.id.nav_userProfile) {
            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_allLead) {
            UpdateFragment(getResources().getString(R.string.strAllLeadLabel));
            Log.d(TAG, "AllLead");
        } else if (id == R.id.nav_notification) {
            Log.d(TAG, "notify");
            UpdateFragment(getResources().getString(R.string.strNotificationLabel));

        }
        //calling the method displayselectedscreen and passing the id of selected menu
        //  displaySelectedScreen(item.getItemId());
        //make this method blank
        // return true;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void UpdateFragment(String strFragmentToOpen) {
        try {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            Fragment fragment = null;

            if (strFragmentToOpen.equalsIgnoreCase(getResources().getString(R.string.strAllLeadLabel))) {
                fragment = new AllLeadsFragment();
            } else if (strFragmentToOpen.equals(getResources().getString(R.string.strNotificationLabel))) {
                fragment = new NotificationFragment();
            }

            if (fragment != null) {
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //My Code Fragment
 /*  private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_allLead:
                fragment = new AllLeadsFragment();
                break;
            case R.id.nav_notification:
                fragment=new NotificationFragment();

        }
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
*/

    /**
     * Get Updated USer info from Database and saved to SharedPreferences
     */
    private class GetProfileAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                JSONObject data = new JSONObject();
                data.put("action", APIURLS.str_user_profile_detail);
                data.put("user_id", mStrUserId);
                response = SecuredNetworkUtility.sendPostData(APIURLS.str_base_url, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (mProgressBar.getVisibility() == View.VISIBLE)
                    mProgressBar.setVisibility(View.GONE);

                if (s != null) {
                    JSONObject result = new JSONObject(s);
                    if (result.length() > 0) {
                        if (result.has("success")) {
                            if (result.optBoolean("success", false)) {
                                if (result.optJSONArray("details").length() > 0) {
                                    JSONObject profileData = result.optJSONArray("details").optJSONObject(0);
                                    SamSharedPreferences.setUserInfo(mContext, profileData);
                                    SamSharedPreferences.setIsUserLoggedIn(mContext, true);

                                    //  Update Username and Email in drawer
                                    mStrFirstName = profileData.optString("first_name", "");
                                    Log.d("firstname", mStrFirstName);
                                    mStrMiddleName = profileData.optString("middle_name", "");
                                    Log.d("middlename", mStrMiddleName);
                                    mStrLastName = profileData.optString("last_name", " ");
                                    Log.d("lastname", mStrLastName);
                                    mStrUserEmail = profileData.optString("email_id", " ");
                                    Log.d("email", mStrUserEmail);
                                    mStrUserId = profileData.optString("user_id", "");
                                    Log.d("userid", mStrUserId);
                                    mStrUserProfileUrL = profileData.optString("profile_photo", "");
                                    Log.d("photo", mStrUserProfileUrL);

                                    mTextViewFirstname.setText(mStrFirstName + " " + mStrMiddleName + " " + mStrLastName);
                                    mTextViewUserEmail.setText(mStrUserEmail);
                                    // mImageProfilePic.setImageURI(Uri.parse(mStrUserProfileUrL));
                                    userData = SamSharedPreferences.getUserInfo(mContext);
                                    Log.d(TAG, userData.toString());

                                    if (mStrUserProfileUrL.toLowerCase().contains("http")) {
                                        if (NetworkCheckUtility.isNetworkConnectionAvailable(mContext)) {
                                            imageLoader.displayImage(mStrUserProfileUrL, mImageProfilePic, displayImageOptions, new SimpleImageLoadingListener() {
                                                @Override
                                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                                    if (loadedImage != null)
                                                        mImageProfilePic.setImageBitmap(loadedImage);
                                                    else
                                                        Toast.makeText(mContext, "Profile photo not found", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    } else
                                        Toast.makeText(mContext, "Profile photo url invalid", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
