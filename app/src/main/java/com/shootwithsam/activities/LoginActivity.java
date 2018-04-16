package com.shootwithsam.activities;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shootwithsam.APIURLS;
import com.shootwithsam.R;
import com.shootwithsam.fragments.AllLeadsFragment;
import com.shootwithsam.utils.NetworkCheckUtility;
import com.shootwithsam.utils.SamSharedPreferences;
import com.shootwithsam.utils.SecuredNetworkUtility;
import com.shootwithsam.utils.ShowToastMessage;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    public SharedPreferences mUserSharedPreferences;
    private EditText mEditTextUsername;
    private EditText mEditTextPassword;
    private Button mButtonLogin;
    private Button mBtnChangePassword;
    private CheckBox mChkBoxRememberMe;
    private ProgressBar mProgressBar;

    private String mStrUsername;
    private String mStrPassword;
    private String strFranchisee_id;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            mContext = LoginActivity.this;

            if (SamSharedPreferences.getIsUserLoggedIn(mContext)) {
               startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            }

            mUserSharedPreferences = SamSharedPreferences.initLMSharedPrefs(mContext);
            //  Hide keyboard when screen opens.
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            mEditTextUsername = (EditText) findViewById(R.id.editTextUsernameLogin);
            mEditTextPassword = (EditText) findViewById(R.id.editTextPasswordLogin);
            mButtonLogin = (Button) findViewById(R.id.buttonLogin);
            //  mBtnChangePassword=(Button)findViewById(R.id.btnChangePassword);
            mChkBoxRememberMe = (CheckBox) findViewById(R.id.checkboxRememberMe);
            mProgressBar = (ProgressBar) findViewById(R.id.progressBarLoad);

            if (!mUserSharedPreferences.getString("Username", "").equalsIgnoreCase("")) {
                mStrUsername = mUserSharedPreferences.getString("Username", "");
                mStrPassword = mUserSharedPreferences.getString("Password", "");
                mEditTextUsername.setText(mStrUsername);
                mEditTextPassword.setText(mStrPassword);
                mChkBoxRememberMe.setChecked(true);
            }
            mButtonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validateCredentials();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Validate Username and password
    private void validateCredentials() {
        mEditTextUsername.setError(null);

        mStrUsername = mEditTextUsername.getText().toString().trim();
        mStrUsername = mEditTextUsername.getText().toString();
        mStrPassword = mEditTextPassword.getText().toString();

        /*if (mStrUsername.equalsIgnoreCase("")) {
            mEditTextUsername.setError("Wrong User Name");
            mEditTextUsername.requestFocus();
        }*/

        if (!mStrUsername.equalsIgnoreCase("")) {

            if (!mStrPassword.equalsIgnoreCase("")) {

                if (!NetworkCheckUtility.isNetworkConnectionAvailable(mContext)) {
                    ShowToastMessage.noInternetToast(mContext);
                } else {
                    boolean digitsOnly = TextUtils.isDigitsOnly(mEditTextUsername.getText());
                    if (digitsOnly) {
                        if (mEditTextUsername.getText().toString().length() > 10 || mEditTextUsername.getText().toString().length() < 10) {
                            ShowToastMessage.showToast(mContext, getResources().getString(R.string.strUsernameFormatMessage));
                            // Toast.makeText(mContext, R.string.strWrongMobNumFormatMsssage, Toast.LENGTH_LONG).show();
                        } else {
                            if (NetworkCheckUtility.isNetworkConnectionAvailable(mContext))
                                new AsyncTaskLogin().execute();
                            else
                                ShowToastMessage.noInternetToast(mContext);
                        }
                    } else {
                        if (android.util.Patterns.EMAIL_ADDRESS.matcher(mStrUsername).matches()) {
                            if (NetworkCheckUtility.isNetworkConnectionAvailable(mContext))
                                new AsyncTaskLogin().execute();
                            else
                                ShowToastMessage.noInternetToast(mContext);
                        } else {
                            ShowToastMessage.showToast(mContext, getResources().getString(R.string.strUsernameFormatMessage));
                        }
                    }
                }
            } else {
                ShowToastMessage.showToast(mContext, getResources().getString(R.string.strPasswordEmptyMessage));
            }
        } else {
            ShowToastMessage.showToast(mContext, getResources().getString(R.string.strUsernameEmptyMessage));
        }
    }

    //Async Task for Login
    private class AsyncTaskLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String response = "";

            try {
//                String strUsername = params[0];
//                String strPassword = params[1];

                JSONObject loginData = new JSONObject();
                loginData.put("action", APIURLS.str_login_action);
                loginData.put("username", mStrUsername);
                loginData.put("password", mStrPassword);
                response = SecuredNetworkUtility.sendPostData(APIURLS.str_base_url, loginData);
            } catch (Exception io) {
                io.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            try {
                if (mProgressBar.getVisibility() == View.VISIBLE)
                    mProgressBar.setVisibility(View.GONE);

                if (result != null) {
                    JSONObject resultJsonObject = new JSONObject(result);

                    if (resultJsonObject.has("success")) {
                        if (resultJsonObject.getBoolean("success")) {

                            JSONObject userInfo = resultJsonObject.optJSONArray("details").optJSONObject(0);
                            if (userInfo.length() > 0) {
                                SamSharedPreferences.setUserInfo(mContext, userInfo);
                                ShowToastMessage.showToast(mContext, "Logged in successfully");

                                SamSharedPreferences.setIsUserLoggedIn(mContext, true);
                                if (mChkBoxRememberMe.isChecked()) {
                                    SamSharedPreferences.updateUserLoginDetails(mContext, mEditTextUsername.getText().toString(), mEditTextPassword.getText().toString());
                                } else
                                    SamSharedPreferences.updateUserLoginDetails(mContext, "", "");

                                startActivity(new Intent(mContext, MainActivity.class));
                                finish();
                            }
                        } else {
                            if (resultJsonObject.has("Errorcode"))
                                ShowToastMessage.showToast(mContext, resultJsonObject.optString("Errorcode"));
                            else
                                ShowToastMessage.showToast(mContext, getResources().getString(R.string.strWrongCredentialsMessage));
                        }
                    } else
                        ShowToastMessage.showToast(mContext, getResources().getString(R.string.strWrongCredentialsMessage));
                }

            } catch (Exception e) {
                e.printStackTrace();
                ShowToastMessage.showToast(mContext, "Some exception occured. Try again later");
            }
        }
    }
}