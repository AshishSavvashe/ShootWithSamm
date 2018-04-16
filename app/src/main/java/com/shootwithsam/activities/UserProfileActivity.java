package com.shootwithsam.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.shootwithsam.APIURLS;
import com.shootwithsam.R;
import com.shootwithsam.application.MyApplication;
import com.shootwithsam.utils.NetworkCheckUtility;
import com.shootwithsam.utils.SamSharedPreferences;
import com.shootwithsam.utils.SecuredNetworkUtility;
import com.shootwithsam.utils.ShowToastMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = "UserProfileActivity";
    private static int RESULT_LOAD_IMAGE = 1;
    public SharedPreferences mUserSharedPreferences;
    private Context mContext;
    // private ImageView mImageViewProfilePic;
    private LinearLayout mLinearLayout;
    private EditText mEditTextFirstName;
    private EditText mEditTextMiddleName;
    private EditText mEditTextLastName;
    private TextView mEditTextEmail;
    private TextView mTextViewChangePhoto;
    private EditText mEditTextMobile;
    private Button mBtnCancel;
    private Button mBtnUpdate;
    private String strUserId = "";
    private String strFirstName = "";
    private String strMiddleName = "";
    private String strLastName = "";
    private String strEmail = "";
    private String strMobile = "";
    private String strAction = "";
    private String strUpdateProfile = "";
    private JSONObject user_data;
    private ProgressBar mProgressBar;
    private String selectedImagePath;
    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;
    private int REQUEST_CODE = 1000;
    private com.shootwithsam.widgets.CircularImageView mImageProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mContext = UserProfileActivity.this;
        imageLoader = MyApplication.getImageLoader();
        displayImageOptions = MyApplication.getDisplayOptions();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //set Title
        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle(getString(R.string.strUserDetailsLabel));
        toolbar.setDisplayHomeAsUpEnabled(true);

        strUserId = getIntent().getStringExtra("user_id");

        mUserSharedPreferences = SamSharedPreferences.initLMSharedPrefs(mContext);

        mLinearLayout = (LinearLayout) findViewById(R.id.lnrImage);
        mImageProfilePic = (com.shootwithsam.widgets.CircularImageView) findViewById(R.id.imageViewProfilePic);
        mTextViewChangePhoto = (TextView) findViewById(R.id.textViewChangePhoto);
        // mImageViewProfilePic = (ImageView) findViewById(R.id.imageViewProfilePic);
        mEditTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        mEditTextMiddleName = (EditText) findViewById(R.id.editTextMiddleName);
        mEditTextLastName = (EditText) findViewById(R.id.editTextLastName);
        mEditTextEmail = (TextView) findViewById(R.id.editTextEmail);
        mEditTextMobile = (EditText) findViewById(R.id.editTextMobile);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);
        mBtnUpdate = (Button) findViewById(R.id.btnUpdate);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBarLoad);

        //Function Profile Data save to SharedPreference
        UpdateProfileData();

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateCredentials();
            }
        });

       /* mImageViewProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.putExtra("crop", "true");
                i.putExtra("scale", true);
                i.putExtra("outputX", 150);
                i.putExtra("outputY", 150);
                i.putExtra("aspectX", 1);
                i.putExtra("aspectY", 1);
                i.putExtra("return-data", true);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            *//*  Intent intent = new Intent(UserProfileActivity.this,Gallery.class);
                startActivityForResult(intent,PICK_IMAGE_MULTIPLE);*//*
            }
        });*/
        mTextViewChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    private void openGallery() {
      /*  try {
            Intent galleryIntent = new Intent();
            galleryIntent.setType("image*//*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(galleryIntent, SELECT_PICTURE);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    /* @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
             Uri selectedImage = data.getData();
             String[] filePathColumn = { MediaStore.Images.Media.DATA };

             Cursor cursor = getContentResolver().query(selectedImage,
                     filePathColumn, null, null, null);
             cursor.moveToFirst();

             int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
             String picturePath = cursor.getString(columnIndex);
             cursor.close();

             CircularImageView imageView = (CircularImageView) findViewById(R.id.imageViewProfilePic);

             Bitmap bmp = null;
             try {
                 bmp = getBitmapFromUri(selectedImage);
                 new LoadProfileImageAsyncTask().execute();
             } catch (IOException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             }
             imageView.setImageBitmap(bmp);
         }

     }
 */
    @Override
    public void onResume() {
        super.onResume();
        GetPhoto();
    }

    private void GetPhoto() {
        if (NetworkCheckUtility.isNetworkConnectionAvailable(mContext))
            new UpdateProfilePhotoAsyncTask().execute();
        else
            ShowToastMessage.noInternetToast(mContext);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                GetPhoto();
            }
            try {
                if (requestCode == 1) {
                    if (data != null) {

                        Uri selectedImageUri = data.getData();
                        Log.d(TAG, selectedImageUri.toString());
                        selectedImagePath = getPath(selectedImageUri);

                        Log.d(TAG, selectedImagePath);
                        try {
                            Bitmap bitmap = getBitmapFromUri(selectedImageUri);
                            if (bitmap != null) {
                                mImageProfilePic.setImageBitmap(bitmap);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new LoadProfileImageAsyncTask().execute();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }


    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    //Updated Profile Data save to SharedPreference
    private void UpdateProfileData() {
        // Main code
        user_data = SamSharedPreferences.getUserInfo(mContext);
        strUserId = user_data.optString("user_id", "");
        try {
            strFirstName = user_data.getString("first_name");
            strMiddleName = user_data.getString("middle_name");
            strLastName = user_data.getString("last_name");
            strMobile = user_data.getString("mobile_no");
            strEmail = user_data.getString("email_id");
            //  strUpdateProfile=user_data.getString("profile_photo");
            final String url = user_data.getString("profile_photo");
            Log.d(TAG, "ProfilePic : " + url);


            mEditTextFirstName.setText(strFirstName);
            mEditTextMiddleName.setText(strMiddleName);
            mEditTextLastName.setText(strLastName);
            mEditTextEmail.setText(strEmail);
            mEditTextMobile.setText(strMobile);
            //   mImageProfilePic.setImageURI(Uri.parse(strUpdateProfile));

            if (url.toLowerCase().contains("http")) {
                imageLoader.displayImage(url, mImageProfilePic, displayImageOptions, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        if (loadedImage != null)
                            mImageProfilePic.setImageBitmap(loadedImage);
                        else
                            Toast.makeText(mContext, "Profile photo not found", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void validateCredentials() {
        mEditTextFirstName.setError(null);
        mEditTextLastName.setError(null);
        mEditTextMobile.setError(null);

        /*mEditTextFirstName.setLines(1);
        mEditTextFirstName.setSingleLine(true);
        mEditTextMiddleName.setLines(1);
        mEditTextMiddleName.setSingleLine(true);
        mEditTextLastName.setLines(1);
        mEditTextLastName.setSingleLine(true);*/

        strFirstName = mEditTextFirstName.getText().toString().trim();
        strMiddleName = mEditTextMiddleName.getText().toString().trim();
        strLastName = mEditTextLastName.getText().toString().trim();
        strEmail = mEditTextEmail.getText().toString().trim();
        strMobile = mEditTextMobile.getText().toString().trim();

        if (strFirstName.equalsIgnoreCase("")) {
            mEditTextFirstName.setError("First Name is empty");
            mEditTextFirstName.requestFocus();

        } else if (strFirstName.contains("0") || strFirstName.contains("1") || strFirstName.contains("2") || strFirstName.contains("3") || strFirstName.contains("4") || strFirstName.contains("5")
                || strFirstName.contains("6") || strFirstName.contains("7") || strFirstName.contains("8") || strFirstName.contains("9")) {
            mEditTextFirstName.setError("Number not allowed");
            mEditTextFirstName.requestFocus();
        } else if (strFirstName.contains("?") || strFirstName.contains("\\") || strFirstName.contains("/") || strFirstName.contains("#")
                || strFirstName.contains("@") || strFirstName.contains("!") || strFirstName.contains("$") || strFirstName.contains("%")
                || strFirstName.contains("^") || strFirstName.contains("&") || strFirstName.contains("*") || strFirstName.contains("(")
                || strFirstName.contains(")") || strFirstName.contains("_") || strFirstName.contains("-") || strFirstName.contains("+") || strFirstName.contains(".")
                || strFirstName.contains("<") || strFirstName.contains("<") || strFirstName.contains(":") || strFirstName.contains("'") || strFirstName.contains(";")) {
            mEditTextFirstName.setError("Special character not allowed");
            mEditTextFirstName.requestFocus();
        } else if (strFirstName.length() >= 20 || strFirstName.length() < 1) {
            mEditTextFirstName.setError("Maximum 20 characters can be entered");
            mEditTextFirstName.requestFocus();

        }/*else if (strMiddleName.equalsIgnoreCase("")) {
            mEditTextMiddleName.setError("Middle Name is empty");
            mEditTextMiddleName.requestFocus();

        }else if (strMiddleName.contains("0")||strMiddleName.contains("1")||strMiddleName.contains("2")||strMiddleName.contains("3")||strMiddleName.contains("4")||strMiddleName.contains("5")
                ||strMiddleName.contains("6")||strMiddleName.contains("7")||strMiddleName.contains("8")||strMiddleName.contains("9")){
            mEditTextMiddleName.setError("Middle Name can't contain any Number");
            mEditTextMiddleName.requestFocus();
        }
        else if (strMiddleName.contains("?") || strMiddleName.contains("\\") || strMiddleName.contains("/")) {
            mEditTextMiddleName.setError("Middle Name can't contain any special character");
            mEditTextMiddleName.requestFocus();
        }
        else if (strMiddleName.length() >= 20 || strMiddleName.length() < 1) {
            mEditTextMiddleName.setError("Maximum 20 characters can be entered");
            mEditTextMiddleName.requestFocus();

        }*/ else if (strLastName.equalsIgnoreCase("")) {
            mEditTextLastName.setError("Last Name is empty");
            mEditTextLastName.requestFocus();

        } else if (strLastName.contains("0") || strLastName.contains("1") || strLastName.contains("2") || strLastName.contains("3") || strLastName.contains("4") || strLastName.contains("5")
                || strLastName.contains("6") || strLastName.contains("7") || strLastName.contains("8") || strLastName.contains("9")) {
            mEditTextLastName.setError("Number not allowed");
            mEditTextLastName.requestFocus();
        } else if (strLastName.contains("?") || strLastName.contains("\\") || strLastName.contains("/") || strLastName.contains("#")
                || strLastName.contains("@") || strLastName.contains("!") || strLastName.contains("$") || strLastName.contains("%")
                || strLastName.contains("^") || strLastName.contains("&") || strLastName.contains("*") || strLastName.contains("(")
                || strLastName.contains(")") || strLastName.contains("_") || strLastName.contains("-") || strLastName.contains("+")
                || strLastName.contains(".") || strLastName.contains("<") || strLastName.contains("<") || strLastName.contains(":")
                || strLastName.contains("'") || strLastName.contains(";")) {
            mEditTextLastName.setError("Special character not allowed");
            mEditTextLastName.requestFocus();
        } else if (strLastName.length() >= 20 || strLastName.length() < 1) {
            mEditTextLastName.setError("Maximum 20 characters can be entered");
            mEditTextLastName.requestFocus();
        } else if (strMobile.equalsIgnoreCase("")) {
            mEditTextMobile.setError("Mobile Number is empty");
            mEditTextMobile.requestFocus();

        } else if (strMobile.length() > 10 || strMobile.length() < 10) {
            mEditTextMobile.setError("Mobile number should be of 10 digits");
            mEditTextMobile.requestFocus();
        } else
            CallSaveURL();

    }

    private void CallSaveURL() {
        if (NetworkCheckUtility.isNetworkConnectionAvailable(mContext)) {
            new ProfileSaveAsyncTask().execute();
        } else
            ShowToastMessage.noInternetToast(mContext);
    }

    private void updateProfileUrl(String profileURL) {
        try {
            Log.d(TAG, "newProfile : " + profileURL);

            //  Userinfo
            JSONObject userData = SamSharedPreferences.getUserInfo(mContext);
            userData.put("profile_photo", profileURL);

            //  Update shared prefs data
            SamSharedPreferences.setUserInfo(mContext, userData);

            UpdateProfileData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Async Task profile data to save in Db
    private class ProfileSaveAsyncTask extends AsyncTask<String, String, String> {
        String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                // String strNewUser = "1";
                JSONObject saveParams = new JSONObject();

                if (strAction.equalsIgnoreCase(strUpdateProfile)) {
                    saveParams.put("action", APIURLS.str_update_franchisee_user_profile);
                    saveParams.put("user_id", strUserId);
                    saveParams.put("first_name", strFirstName);
                    saveParams.put("middle_name", strMiddleName);
                    saveParams.put("last_name", strLastName);
                    saveParams.put("mobile_no", strMobile);

                }
                response = SecuredNetworkUtility.sendPostData(APIURLS.str_base_url, saveParams);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (mProgressBar.getVisibility() == View.VISIBLE)
                    mProgressBar.setVisibility(View.GONE);

                if (response != null) {
                    JSONObject resultData = new JSONObject(result);

                    if (resultData.length() == 0) {
                        ShowToastMessage.showToast(mContext, "Failed to update profile.");
                    } else {
                        boolean boolSuccess = resultData.getBoolean("success");
                        if (boolSuccess) {
                            if (strAction.equalsIgnoreCase(strUpdateProfile))
                                ShowToastMessage.showToast(mContext, "Profile Updated successfully");

                            SamSharedPreferences.updateUserProfileDetails(mContext, mEditTextFirstName.getText().toString(), mEditTextMiddleName.getText().toString(), mEditTextLastName.getText().toString(), mEditTextMobile.getText().toString());
                            UpdateProfileData();
                        } else {
                            String strMessage = resultData.optString("message");
                            if (!strMessage.equalsIgnoreCase(""))
                                ShowToastMessage.showToast(mContext, strMessage);
                        }
                    }
                } else
                    ShowToastMessage.showToast(mContext, "Something goes wrong. Please try again later");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    // Profile Photo Upload
    private class LoadProfileImageAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Void... params) {
            String imageString = "";
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bitmap1 = BitmapFactory.decodeFile(selectedImagePath);
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 40, baos);

                int bitmapByteCount;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    bitmapByteCount = bitmap1.getAllocationByteCount();
                } else {
                    bitmapByteCount = bitmap1.getRowBytes() * bitmap1.getHeight();
                }

                Log.d(TAG, "count : " + bitmapByteCount);

                byte[] imageBytes = baos.toByteArray();
                imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                Log.d(TAG, imageString);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return imageString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mProgressBar.getVisibility() == View.VISIBLE)
                mProgressBar.setVisibility(View.GONE);

            try {
                if (!s.equalsIgnoreCase("")) {
                    if (NetworkCheckUtility.isNetworkConnectionAvailable(mContext))
                        new UpdateProfilePhotoAsyncTask().execute(s);
                    else
                        ShowToastMessage.noInternetToast(mContext);
                } else {
                    ShowToastMessage.showToast(mContext, "Failed to change photo. Try again later");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class UpdateProfilePhotoAsyncTask extends AsyncTask<String, String, String> {
        String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String strImage = strings[0].trim();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("action", APIURLS.str_upload_profile_image);
                jsonObject.put("user_id", strUserId);
                jsonObject.put("string", strImage);

                response = SecuredNetworkUtility.sendPostData(APIURLS.str_base_url, jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mProgressBar.getVisibility() == View.VISIBLE)
                mProgressBar.setVisibility(View.GONE);
            try {
                if (s != null) {
                    JSONObject result = new JSONObject(s);
                    if (result.has("success") && result.optBoolean("success")) {
                        ShowToastMessage.showToast(mContext, "Profile photo updated successfully.");
                        //   Toast.makeText(mContext, "Profile photo updated successfully.", Toast.LENGTH_SHORT).show();
                        updateProfileUrl(result.optString("profile_url", ""));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
