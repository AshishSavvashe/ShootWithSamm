package com.shootwithsam.activities;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shootwithsam.APIURLS;
import com.shootwithsam.R;
import com.shootwithsam.utils.NetworkCheckUtility;
import com.shootwithsam.utils.SamSharedPreferences;
import com.shootwithsam.utils.SecuredNetworkUtility;
import com.shootwithsam.utils.ShowToastMessage;

import org.json.JSONObject;

import static com.shootwithsam.R.id.layoutChangePwdBtns;

public class ChangePasswordActivity extends AppCompatActivity {
    private final String TAG = "ChangePasswordActivity";

    private Context mContext;
    private EditText mEditTextOldPwd;
    private EditText mEditTextNewPwd;
    private EditText mEditTextConfirmPwd;
    private ImageView mImgShowOldPwd;
    private ImageView mImgShowPwd;
    private ImageView mImgShowConfirmPwd;
    private ProgressBar mPrrogressBar;
    private LinearLayout mLayoutOldPwdForm;
    private LinearLayout mLayoutChangePwdBtns;

    private Button mBtnChange;
    private Button mBtnCancel;

    private String strOldPwd = "";
    private String strNewPwd = "";
    private String strConfirmPwd = "";
    private String strRegisterUserId = "";

    private boolean isOldPwdValid = false;
    private boolean isPwdValid = false;
    private boolean isConfirmPwdValid = false;
    private boolean isOldPasswordShow = true;
    private boolean isPasswordShow = true;
    private boolean isConfirmPasswordShow = true;

    private LinearLayout mLayoutFieldsMandatory;

    private String strParentCall = "";
    private String strEmail = "";


    /* private Context mContext;
    private LinearLayout mLayoutBtns;
    private EditText mEditTextOldPwd;
    private EditText mEditTextNewPwd;
    private EditText mEditTextConfirmPwd;
    private ImageView mImgShowPwd;
    private ImageView mImgShowConfirmPwd;
    private ProgressBar mPrrogressBar;

    private Button mBtnChange;
    private Button mBtnChangeCancel;

    private String strRegisterUserId = "";
    private String strOldPwd = "";
    private String strNewPwd = "";
    private String strConfirmPwd = "";


    private boolean isOldValid = false;
    private boolean isPwdValid = false;
    private boolean isConfirmPwdValid = false;
    private boolean isPasswordShow = true;
    private boolean isConfirmPasswordShow = true;

    private LinearLayout mLayoutFieldsMandatory;
    private String strParentCall = "";
    private String strEmail = "";
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        try {

            mContext = ChangePasswordActivity.this;

            strRegisterUserId = getIntent().getStringExtra("user_id");
            strOldPwd = getIntent().getStringExtra("old_password");

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            ActionBar toolbar = getSupportActionBar();
            toolbar.setTitle(getString(R.string.strChangePasswordLabel));
         /*    toolbar.setHomeButtonEnabled(true);
            toolbar.setDisplayHomeAsUpEnabled(true);
            toolbar.setDisplayShowHomeEnabled(true);*/

           /* JSONObject userinfo = SamSharedPreferences.getUserInfo(mContext);
            if (userinfo != null) {
                strRegisterUserId = userinfo.getString("user_id");
            }*/

            strRegisterUserId = SamSharedPreferences.getUserId(mContext);

            mLayoutOldPwdForm = (LinearLayout) findViewById(R.id.layoutOldPwd);
            mLayoutChangePwdBtns = (LinearLayout) findViewById(R.id.layoutChangePwdBtns);
            mEditTextOldPwd = (EditText) findViewById(R.id.editTextOldPwd);
            mEditTextNewPwd = (EditText) findViewById(R.id.editTextNewPwd);
            mEditTextConfirmPwd = (EditText) findViewById(R.id.editTextNewConfirmPwd);
            mBtnChange = (Button) findViewById(R.id.btnChangePwdSubmit);
            mBtnCancel = (Button) findViewById(R.id.btnChangeCancel);
            mImgShowOldPwd = (ImageView) findViewById(R.id.imgViewHideResetOldPwd);
            mImgShowPwd = (ImageView) findViewById(R.id.imgViewHideResetPwd);
            mImgShowConfirmPwd = (ImageView) findViewById(R.id.imgViewHideResetCPwd);
            mPrrogressBar = (ProgressBar) findViewById(R.id.progressBarLoad);
            mLayoutFieldsMandatory = (LinearLayout) findViewById(R.id.layoutAllEmptyFields);

          /*  mLayoutBtns = (LinearLayout) findViewById(R.id.layoutChangePwdBtns);
            mEditTextOldPwd = (EditText) findViewById(R.id.editTextOldPwd);
            mEditTextNewPwd = (EditText) findViewById(R.id.editTextNewPwd);
            mEditTextConfirmPwd = (EditText) findViewById(R.id.editTextNewConfirmPwd);
            mBtnChange = (Button) findViewById(R.id.btnChangePwdSubmit);
            mBtnChangeCancel = (Button) findViewById(R.id.btnChangeCancel);
            mImgShowPwd = (ImageView) findViewById(R.id.imgViewHideResetPwd);
            mImgShowConfirmPwd = (ImageView) findViewById(R.id.imgViewHideResetCPwd);
            mPrrogressBar = (ProgressBar) findViewById(R.id.progressBarLoad);
            mLayoutFieldsMandatory = (LinearLayout) findViewById(R.id.layoutAllEmptyFields);*/

           /* mImgShowPwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mEditTextNewPwd.getText().toString().equalsIgnoreCase("")) {
                        if (!isPasswordShow) {
                            isPasswordShow = true;
                            mEditTextNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            mImgShowPwd.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_show));
                        } else {
                            isPasswordShow = false;
                            mEditTextNewPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            mImgShowPwd.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_hide));
                        }
                    }
                    mEditTextNewPwd.requestFocus();
                    mEditTextNewPwd.setSelection(mEditTextNewPwd.getText().toString().length());
                }
            });
            mImgShowConfirmPwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mEditTextConfirmPwd.getText().toString().equalsIgnoreCase("")) {
                        if (!isConfirmPasswordShow) {
                            isConfirmPasswordShow = true;
                            mEditTextConfirmPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            mImgShowConfirmPwd.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_show));
                        } else {
                            isConfirmPasswordShow = false;
                            mEditTextConfirmPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            mImgShowConfirmPwd.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_hide));
                        }
                    }
                    mEditTextConfirmPwd.requestFocus();
                    mEditTextConfirmPwd.setSelection(mEditTextConfirmPwd.getText().toString().length());
                }
            });
            mEditTextNewPwd.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mLayoutFieldsMandatory.getVisibility() == View.VISIBLE)
                        mLayoutFieldsMandatory.setVisibility(View.GONE);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        if (mEditTextNewPwd.getText().toString().equalsIgnoreCase("")) {
                            if (!isPasswordShow) {
                                isPasswordShow = true;
                                mImgShowPwd.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_show));
                                mEditTextNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            mEditTextConfirmPwd.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mLayoutFieldsMandatory.getVisibility() == View.VISIBLE)
                        mLayoutFieldsMandatory.setVisibility(View.GONE);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        if (mEditTextConfirmPwd.getText().toString().equalsIgnoreCase("")) {
                            if (!isConfirmPasswordShow) {
                                isConfirmPasswordShow = true;
                                mImgShowConfirmPwd.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_show));
                                mEditTextConfirmPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            mBtnChangeCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  *//*  startActivity(new Intent(mContext, MyProfileFragment.class));
                    finish();*//*
                    finish();
                }
            });
            mBtnChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        strOldPwd = mEditTextOldPwd.getText().toString().trim();
                        strNewPwd = mEditTextNewPwd.getText().toString().trim();
                        strConfirmPwd = mEditTextConfirmPwd.getText().toString().trim();

                        if (strOldPwd.equalsIgnoreCase("") && strNewPwd.equalsIgnoreCase("") && strConfirmPwd.equalsIgnoreCase("")) {
                            mEditTextOldPwd.setError("Please Enter Old Password");
                            mEditTextNewPwd.setError("Please Enter New Password");
                            mEditTextConfirmPwd.setError("Please Enter New Confirm Password");

                            mLayoutFieldsMandatory.setVisibility(View.VISIBLE);

                            mEditTextOldPwd.requestFocus();
                            isOldValid = false;
                            isPwdValid = false;
                            isConfirmPwdValid = false;
                        } else {
                            if (strOldPwd.equalsIgnoreCase("")) {
                                mEditTextOldPwd.setError("Please Enter Old Password");
                                mEditTextOldPwd.requestFocus();
                                isOldValid = false;
                            } else if (strOldPwd.length() < 6) {
                                mEditTextOldPwd.setError("Password should be minimum of 6 characters");
                                mEditTextOldPwd.requestFocus();
                                isOldValid = false;
                            } else {
                                isOldValid = true;
                            }

                            if (strNewPwd.equalsIgnoreCase("")) {
                                mEditTextNewPwd.setError("Please Enter New Password");
                                mEditTextNewPwd.requestFocus();
                                isPwdValid = false;
                            } else if (strNewPwd.length() < 6) {
                                mEditTextNewPwd.setError("Password should be minimum of 6 characters");
                                mEditTextNewPwd.requestFocus();
                                isPwdValid = false;
                            } else
                                isPwdValid = true;

                            if (strConfirmPwd.equalsIgnoreCase("")) {
                                mEditTextConfirmPwd.setError("Please Enter Confirm New Password");
                                mEditTextConfirmPwd.requestFocus();
                                isConfirmPwdValid = false;
                            } else if (!strConfirmPwd.equalsIgnoreCase(strNewPwd)) {
                                mEditTextConfirmPwd.setError("Passwords do not match");
                                mEditTextConfirmPwd.requestFocus();
                                isConfirmPwdValid = false;
                            } else
                                isConfirmPwdValid = true;

                            if (!isOldValid)
                                mEditTextOldPwd.requestFocus();
                            else if (!isPwdValid)
                                mEditTextNewPwd.requestFocus();
                            else if (!isConfirmPwdValid)
                                mEditTextConfirmPwd.requestFocus();

                            if (isOldValid && isPwdValid && isConfirmPwdValid) {
                                if (NetworkCheckUtility.isNetworkConnectionAvailable(mContext)) {
                                    new ChangePasswordAsyncTask().execute();
                                } else
                                    ShowToastMessage.noInternetToast(mContext);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }*/
            mImgShowOldPwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mEditTextOldPwd.getText().toString().equalsIgnoreCase("")) {
                        if (!isOldPasswordShow) {
                            isOldPasswordShow = true;
                            mEditTextOldPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            mImgShowOldPwd.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_show));
                        } else {
                            isOldPasswordShow = false;
                            mEditTextOldPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            mImgShowOldPwd.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_hide));
                        }
                    }
                    mEditTextOldPwd.requestFocus();
                    mEditTextOldPwd.setSelection(mEditTextOldPwd.getText().toString().length());
                }
            });
            mImgShowPwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mEditTextNewPwd.getText().toString().equalsIgnoreCase("")) {
                        if (!isPasswordShow) {
                            isPasswordShow = true;
                            mEditTextNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            mImgShowPwd.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_show));
                        } else {
                            isPasswordShow = false;
                            mEditTextNewPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            mImgShowPwd.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_hide));
                        }
                    }
                    mEditTextNewPwd.requestFocus();
                    mEditTextNewPwd.setSelection(mEditTextNewPwd.getText().toString().length());
                }
            });

            mImgShowConfirmPwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mEditTextConfirmPwd.getText().toString().equalsIgnoreCase("")) {
                        if (!isConfirmPasswordShow) {
                            isConfirmPasswordShow = true;
                            mEditTextConfirmPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            mImgShowConfirmPwd.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_show));
                        } else {
                            isConfirmPasswordShow = false;
                            mEditTextConfirmPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            mImgShowConfirmPwd.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_hide));
                        }
                    }
                    mEditTextConfirmPwd.requestFocus();
                    mEditTextConfirmPwd.setSelection(mEditTextConfirmPwd.getText().toString().length());
                }
            });
            mEditTextNewPwd.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mLayoutFieldsMandatory.getVisibility() == View.VISIBLE)
                        mLayoutFieldsMandatory.setVisibility(View.GONE);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        if (mEditTextNewPwd.getText().toString().equalsIgnoreCase("")) {
                            if (!isPasswordShow) {
                                isPasswordShow = true;
                                mImgShowPwd.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_show));
                                mEditTextNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            mEditTextConfirmPwd.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mLayoutFieldsMandatory.getVisibility() == View.VISIBLE)
                        mLayoutFieldsMandatory.setVisibility(View.GONE);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        if (mEditTextConfirmPwd.getText().toString().equalsIgnoreCase("")) {
                            if (!isConfirmPasswordShow) {
                                isConfirmPasswordShow = true;
                                mImgShowConfirmPwd.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_show));
                                mEditTextConfirmPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            mEditTextOldPwd.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mLayoutFieldsMandatory.getVisibility() == View.VISIBLE)
                        mLayoutFieldsMandatory.setVisibility(View.GONE);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            mBtnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            mBtnChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        strOldPwd = mEditTextOldPwd.getText().toString().trim();
                        strNewPwd = mEditTextNewPwd.getText().toString().trim();
                        strConfirmPwd = mEditTextConfirmPwd.getText().toString().trim();

                        if (strOldPwd.equalsIgnoreCase("") && strNewPwd.equalsIgnoreCase("") && strConfirmPwd.equalsIgnoreCase("")) {
                            mEditTextOldPwd.setError("Please Enter Old Password");
                            mEditTextNewPwd.setError("Please Enter New Password");
                            mEditTextConfirmPwd.setError("Please Enter New Confirm Password");

                            mLayoutFieldsMandatory.setVisibility(View.VISIBLE);

                            mEditTextOldPwd.requestFocus();
                            isOldPwdValid = false;
                            isPwdValid = false;
                            isConfirmPwdValid = false;
                        } else {
                            if (strOldPwd.equalsIgnoreCase("")) {
                                mEditTextOldPwd.setError("Please Enter Old Password");
                                mEditTextOldPwd.requestFocus();
                                isOldPwdValid = false;
                            } else if (strOldPwd.length() < 6) {
                                mEditTextOldPwd.setError("Password should be minimum of 6 characters");
                                mEditTextOldPwd.requestFocus();
                                isOldPwdValid = false;
                            } else {
                                isOldPwdValid = true;
                            }

                            if (strNewPwd.equalsIgnoreCase("")) {
                                mEditTextNewPwd.setError("Please Enter New Password");
                                mEditTextNewPwd.requestFocus();
                                isPwdValid = false;
                            } else if (strNewPwd.length() < 6) {
                                mEditTextNewPwd.setError("Password should be minimum of 6 characters");
                                mEditTextNewPwd.requestFocus();
                                isPwdValid = false;
                            } else
                                isPwdValid = true;

                            if (strConfirmPwd.equalsIgnoreCase("")) {
                                mEditTextConfirmPwd.setError("Please Enter Confirm New Password");
                                mEditTextConfirmPwd.requestFocus();
                                isConfirmPwdValid = false;
                            } else if (!strConfirmPwd.equalsIgnoreCase(strNewPwd)) {
                                mEditTextConfirmPwd.setError("Passwords do not match");
                                mEditTextConfirmPwd.requestFocus();
                                isConfirmPwdValid = false;
                            } else
                                isConfirmPwdValid = true;

                            if (!isOldPwdValid)
                                mEditTextOldPwd.requestFocus();
                            else if (!isPwdValid)
                                mEditTextNewPwd.requestFocus();
                            else if (!isConfirmPwdValid)
                                mEditTextConfirmPwd.requestFocus();

                            if (isOldPwdValid && isPwdValid && isConfirmPwdValid) {
                                if (NetworkCheckUtility.isNetworkConnectionAvailable(mContext)) {
                                    new ChangePasswordAsyncTask().execute();
                                } else
                                    ShowToastMessage.noInternetToast(mContext);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
          /*  mBtnChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ClearErrors();

                        String strUnique = "";

                        strOldPwd = mEditTextOldPwd.getText().toString().trim();
                        strNewPwd = mEditTextNewPwd.getText().toString().trim();
                        strConfirmPwd = mEditTextConfirmPwd.getText().toString().trim();

                            strUnique = strOldPwd;

                        if (strUnique.equalsIgnoreCase("") && strNewPwd.equalsIgnoreCase("") && strConfirmPwd.equalsIgnoreCase("")) {

                                mEditTextOldPwd.setError("Please Enter Old Password");
                                mEditTextOldPwd.requestFocus();
                                isOldPwdValid = false;

                            mEditTextNewPwd.setError("Please Enter New Password");
                            mEditTextConfirmPwd.setError("Please Enter New Confirm Password");

                            mLayoutFieldsMandatory.setVisibility(View.VISIBLE);

                            isPwdValid = false;
                            isConfirmPwdValid = false;
                        } else {
                                if (strOldPwd.equalsIgnoreCase("")) {
                                    mEditTextNewPwd.setError("Please Enter Old Password");
                                    mEditTextNewPwd.requestFocus();
                                    isOldPwdValid = false;
                                } else {
                                    isOldPwdValid = true;
                                }

                            if (strNewPwd.equalsIgnoreCase("")) {
                                mEditTextNewPwd.setError("Please Enter New Password");
                                mEditTextNewPwd.requestFocus();
                                isPwdValid = false;
                            } else if (strNewPwd.length() < 6) {
                                mEditTextNewPwd.setError("Password should be minimum of 6 characters");
                                mEditTextNewPwd.requestFocus();
                                isPwdValid = false;
                            } *//*else if (!strNewPwd.equalsIgnoreCase("")) {
                                boolean hasSpecial = !strNewPwd.matches("[A-Za-z0-9 ]*");
                                boolean hasUpperCase = !strNewPwd.equals(strNewPwd.toLowerCase());
                                boolean hasLowerCase = !strNewPwd.equals(strNewPwd.toUpperCase());
                                boolean hasDigit = strNewPwd.matches(".*[0-9].*");

                                if (strNewPwd.contains("?") || strNewPwd.contains("\\") || strNewPwd.contains("/")) {
                                    mEditTextNewPwd.setError("Password can't contain any of the character from (?, /, \\)");
                                    mEditTextNewPwd.requestFocus();
                                    isPwdValid = false;
                                } else if (!hasSpecial) {
                                    mEditTextNewPwd.setError("Enter more Secured Password");
                                    mEditTextNewPwd.requestFocus();
                                    isPwdValid = false;
                                } *//**//*else if (!hasUpperCase) {
                                    mEditTextNewPwd.setError("Password should have atleast 1 Capital letter");
                                    mEditTextNewPwd.requestFocus();
                                    isPwdValid = false;
                                }*//**//* else if (!hasDigit) {
                                    mEditTextNewPwd.setError("Enter more Secured Password");
                                    mEditTextNewPwd.requestFocus();
                                    isPwdValid = false;
                                } else { //if (hasDigit && hasUpperCase && hasSpecial) {
                                    isPwdValid = true;
                                }
                            }*//*

                            if (strConfirmPwd.equalsIgnoreCase("")) {
                                mEditTextConfirmPwd.setError("Please Enter Confirm New Password");
                                mEditTextConfirmPwd.requestFocus();
                                isConfirmPwdValid = false;
                            } else if (!strConfirmPwd.equalsIgnoreCase("")) {
                                boolean hasSpecial = !strConfirmPwd.matches("[A-Za-z0-9]*");
                                boolean hasUpperCase = !strConfirmPwd.equals(strConfirmPwd.toLowerCase());
                                boolean hasLowerCase = !strConfirmPwd.equals(strConfirmPwd.toUpperCase());
                                boolean hasDigit = strConfirmPwd.matches(".*[0-9].*");

                                if (strConfirmPwd.contains("?") || strConfirmPwd.contains("\\") || strConfirmPwd.contains("/")) {
                                    mEditTextConfirmPwd.setError("Password can't contain any of the character from (?, /, \\)");
                                    mEditTextConfirmPwd.requestFocus();
                                    isConfirmPwdValid = false;
                                } else if (!hasSpecial) {
                                    mEditTextConfirmPwd.setError("Enter more Secured Password");
                                    mEditTextConfirmPwd.requestFocus();
                                    isConfirmPwdValid = false;
                                }*//* else if (!hasUpperCase) {
                                    mEditTextConfirmPwd.setError("Password should have atleast 1 Capital letter");
                                    mEditTextConfirmPwd.requestFocus();
                                    isConfirmPwdValid = false;
                                } *//*else if (!hasDigit) {
                                    mEditTextConfirmPwd.setError("Enter more Secured Password");
                                    mEditTextConfirmPwd.requestFocus();
                                    isConfirmPwdValid = false;
                                } else if (!strNewPwd.equals(strConfirmPwd)) {
                                    mEditTextConfirmPwd.setError("Passwords do not match");
                                    mEditTextConfirmPwd.requestFocus();
                                    isConfirmPwdValid = false;
                                } else
                                    isConfirmPwdValid = true;
                            }

                                if (!isOldPwdValid)
                                    mEditTextOldPwd.requestFocus();
                                else if (!isPwdValid)
                                    mEditTextNewPwd.requestFocus();
                                else if (!isConfirmPwdValid)
                                    mEditTextConfirmPwd.requestFocus();

                                if (isOldPwdValid && isPwdValid && isConfirmPwdValid) {
                                    if (NetworkCheckUtility.isNetworkConnectionAvailable(mContext)) {
                                        new ChangePasswordAsyncTask().execute();
                                    } else
                                        ShowToastMessage.noInternetToast(mContext);
                                }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CancelDialog();
    }

    private void CancelDialog() {
        try {
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            dialog.setMessage("Do you really want to cancel?");
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SignInIntent() {
//        Intent intent = new Intent(mContext, LoginActivity.class);
//        startActivity(intent);
        // finish();

        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        ComponentName cn = intent.getComponent();
//        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                Intent.FLAG_ACTIVITY_CLEAR_TASK |
//                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void LogOutUserDetails() {
        SamSharedPreferences.setIsUserLoggedIn(mContext, false);
        SamSharedPreferences.setUserInfo(mContext, new JSONObject());
        startActivity(new Intent(mContext, LoginActivity.class));
        finish();
    }

    private void ClearErrors() {
        mEditTextOldPwd.setError(null);
        mEditTextNewPwd.setError(null);
        mEditTextConfirmPwd.setError(null);
    }
    //Asyn Task for Change password
    private class ChangePasswordAsyncTask extends AsyncTask<String, String, String> {
        private String response = "";

        @Override
        protected void onPreExecute() {
            mPrrogressBar.setVisibility(View.VISIBLE);
           mLayoutChangePwdBtns.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                JSONObject loginData = new JSONObject();

                loginData.put("action", APIURLS.str_reset_franchisee_user_using_password);
                loginData.put("user_id", strRegisterUserId);
                loginData.put("oldpassword", strOldPwd);
                loginData.put("newpassword", strNewPwd);
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
                mPrrogressBar.setVisibility(View.GONE);
                mLayoutChangePwdBtns.setVisibility(View.VISIBLE);

                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("success")) {
                        if (jsonObject.getBoolean("success")) {
                            if (jsonObject.getBoolean("success")) {

                                ShowToastMessage.showToast(mContext, "Congratulations! Password Changed Successfully.");

                                //     SamSharedPreferences.setForgotPasswordCreated(mContext, false, "");
                                //  new CheckCredentialsTask().execute();

                                SamSharedPreferences.setIsUserLoggedIn(mContext, false);
                                SamSharedPreferences.setUserInfo(mContext, new JSONObject());
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }  else
                                ShowToastMessage.showToast(mContext, getResources().getString(R.string.strFailureMessage));

                        } else
                            ShowToastMessage.showToast(mContext, "Please enter correct old password.");
                    } else
                        ShowToastMessage.showToast(mContext, getResources().getString(R.string.strFailureMessage));
                }
                 else
                    ShowToastMessage.showToast(mContext, getResources().getString(R.string.strFailureMessage));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

   /* private class CheckCredentialsTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            mPrrogressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                JSONObject data = new JSONObject();
                data.put("action", APIURLS.str_reset_franchisee_user_using_password);
                data.put("user_id", strRegisterUserId);
                data.put("oldpassword", strOldPwd);
                data.put("newpassword", strNewPwd);
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
                mPrrogressBar.setVisibility(View.GONE);

                if (s != null) {

                    JSONObject result = new JSONObject(s);
                    if (result.has("success")) {
                        if (result.optBoolean("success", false)) {

                            JSONObject user_data = result.optJSONObject("details");
                            SamSharedPreferences.setUserInfo(mContext, user_data);
                            SamSharedPreferences.setIsUserLoggedIn(mContext, true);
                            SamSharedPreferences.updateUserLoginDetails(mContext, strEmail, strNewPwd);
                            SamSharedPreferences.setLoginRemember(mContext, "no");
                            SignInIntent();
                        } else {
                            if (result.has("Errorcode"))
                                ShowToastMessage.showToast(mContext, result.optString("Errorcode"));
                            else {
                                ShowToastMessage.showToast(mContext, getResources().getString(R.string.strLoginFailMessage));
                                SignInIntent();
                            }
                        }
                    } else {
                        ShowToastMessage.showToast(mContext, getResources().getString(R.string.strFailureMessage));
                        SignInIntent();
                    }
                } else {
                    ShowToastMessage.showToast(mContext, getResources().getString(R.string.strFailureMessage));
                    SignInIntent();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    */
 /*  private class CheckCredentialsTask extends AsyncTask<String, String, String> {

       @Override
       protected void onPreExecute() {
           mPrrogressBar.setVisibility(View.VISIBLE);
           mLayoutChangePwdBtns.setVisibility(View.GONE);
       }

       @Override
       protected String doInBackground(String... params) {
           String response = "";
           try {
               JSONObject data = new JSONObject();
               data.put("action", APIURLS.str_reset_franchisee_user_using_password);
               data.put("user_id", strRegisterUserId);
               data.put("oldpassword", strOldPwd);
               data.put("newpassword", strNewPwd);
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
               mPrrogressBar.setVisibility(View.GONE);
               mLayoutChangePwdBtns.setVisibility(View.VISIBLE);

               if (s != null) {

                   JSONObject result = new JSONObject(s);
                   if (result.has("success")) {
                       if (result.optBoolean("success", false)) {
//                            ShowToastMessage.showToast(mContext, getResources().getString(R.string.strLoginSuccessMessage));

                           JSONObject user_data = result.optJSONObject("details");
                           SamSharedPreferences.setUserInfo(mContext, user_data);
                           SamSharedPreferences.setIsUserLoggedIn(mContext, true);
                           SamSharedPreferences.updateUserLoginDetails(mContext, strEmail, strNewPwd);
                           SamSharedPreferences.setLoginRemember(mContext, "no");

                           startActivity(new Intent(mContext, MainActivity.class));
                           finish();
                       } else {
                           if (result.has("Errorcode"))
                               ShowToastMessage.showToast(mContext, result.optString("Errorcode"));
                           else {
                               ShowToastMessage.showToast(mContext, getResources().getString(R.string.strLoginFailMessage));
                               SignInIntent();
                           }
                       }
                   } else {
                       ShowToastMessage.showToast(mContext, getResources().getString(R.string.strFailureMessage));
                       SignInIntent();
                   }
               } else {
                   ShowToastMessage.showToast(mContext, getResources().getString(R.string.strFailureMessage));
                   SignInIntent();
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   }
*/
}
