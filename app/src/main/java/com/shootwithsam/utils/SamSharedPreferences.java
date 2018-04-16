package com.shootwithsam.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

/**
 * Created by SwatiK on 20-08-2017.
 */
public class SamSharedPreferences {
    private static String strNotificationKey = "notifications";
    private static String strGCMIDKey = "GCMID";
    private static String strUserToken = "token";
    private static String strRememberMe = "isRememberMe";
    private static String strUserInfo = "user_info";
    private static String strIsLoggedIn = "isLoggedIn";

    private static SharedPreferences sharedPreferences;
    private static String strLastSyncDate = "lastSyncDate";
    private static String strIsFirstTimeUse = "isFirstTimeUse";
    private static String strIsSeen="is_seen";

    public static SharedPreferences initLMSharedPrefs(Context context) {
        try {
            return context.getSharedPreferences("Sam", Context.MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setUserInfo(Context context, JSONObject userBasicInfo) {
        try {
            sharedPreferences = initLMSharedPrefs(context);
            if (sharedPreferences != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(strUserInfo, userBasicInfo.toString());
                editor.commit();
                editor.apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public static JSONObject getUserInfo(Context context) {
        try {
            sharedPreferences = initLMSharedPrefs(context);
            if (sharedPreferences != null)
                return new JSONObject(sharedPreferences.getString(strUserInfo, ""));
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/
    public static JSONObject getUserInfo(Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            sharedPreferences = initLMSharedPrefs(context);
            if (sharedPreferences != null) {
                if (!sharedPreferences.getString(strUserInfo, "").trim().equalsIgnoreCase(""))
                    jsonObject = new JSONObject(sharedPreferences.getString(strUserInfo, ""));
            } else
                return new JSONObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static void updateUserLoginDetails(Context context, String username, String password) {
        sharedPreferences = initLMSharedPrefs(context);
        SharedPreferences.Editor userEditor = sharedPreferences.edit();
        userEditor.putString("Username", username);
        userEditor.putString("Password", password);
        userEditor.apply();
    }

    public static JSONObject getUserLoginDetails(Context context) {
        JSONObject loginDetails = new JSONObject();
        try {
            sharedPreferences = initLMSharedPrefs(context);
            if (sharedPreferences != null) {
                loginDetails.put("username", sharedPreferences.getString("Username", ""));
                loginDetails.put("password", sharedPreferences.getString("Password", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginDetails;
    }

    public static void updateUserProfileDetails(Context context, String firstname, String middlename, String lastname, String mobileno) {
        try {
            JSONObject user_data = getUserInfo(context);

            user_data.put("first_name", firstname);
            user_data.put("last_name", lastname);
            user_data.put("middle_name", middlename);
            user_data.put("mobile_no", mobileno);
            //user_data.put("profile_photo",profilephoto);
            setUserInfo(context, user_data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setUserRemember(Context context, String isRememberMe) {
        sharedPreferences = initLMSharedPrefs(context);
        SharedPreferences.Editor userEditor = sharedPreferences.edit();
        userEditor.putString(strRememberMe, isRememberMe);
        userEditor.apply();
    }

    public static String isRememberMe(Context context) {
        try {
            return initLMSharedPrefs(context).getString(strRememberMe, "");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setIsUserLoggedIn(Context context, Boolean isLoggedIn) {
        try {
            SharedPreferences.Editor editor = initLMSharedPrefs(context).edit();
            editor.putBoolean(strIsLoggedIn, isLoggedIn);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getIsUserLoggedIn(Context context) {
        try {
            return initLMSharedPrefs(context).getBoolean(strIsLoggedIn, false);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getNotificationsCount(Context context) {
        int count = 0;
        try {
            sharedPreferences = initLMSharedPrefs(context);
            count = sharedPreferences.getInt(strNotificationKey, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public static void setFirstTimeUse(Context context) {
        try {
            SharedPreferences.Editor editor = initLMSharedPrefs(context).edit();
            editor.putBoolean(strIsFirstTimeUse, false);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getFirstTimeUse(Context context) {
        try {
            return initLMSharedPrefs(context).getBoolean(strIsFirstTimeUse, true);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void setStrIsSeen(Context context) {
        try {
            SharedPreferences.Editor editor = initLMSharedPrefs(context).edit();
            editor.putBoolean(strIsSeen, false);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean getStrIsSeen(Context context) {
        try {
            return initLMSharedPrefs(context).getBoolean(strIsSeen, true);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /* public static void updateUserLoginDetails(Context context, String username, String password) {
         sharedPreferences = initLMSharedPrefs(context);
         SharedPreferences.Editor userEditor = sharedPreferences.edit();
         userEditor.putString("Username", username);
         userEditor.putString("Password", password);
         userEditor.apply();
     }*/
    public static void setForgotPasswordCreated(Context context, boolean flag, String email) {
        try {
            sharedPreferences = initLMSharedPrefs(context);
            if (sharedPreferences != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isForgotPasswordSet", flag);
                editor.putString("forgotPasswordEmail", email);
                editor.apply();
                editor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isForgotPasswordCreated(Context context) {
        boolean isSet = false;
        try {
            sharedPreferences = initLMSharedPrefs(context);
            if (sharedPreferences != null)
                isSet = sharedPreferences.getBoolean("isForgotPasswordSet", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSet;
    }

    public static void setLoginRemember(Context context, String isRememberMe) {
        sharedPreferences = initLMSharedPrefs(context);
        if (sharedPreferences != null) {
            SharedPreferences.Editor userEditor = sharedPreferences.edit();
            userEditor.putString(strRememberMe, isRememberMe);
            userEditor.commit();
            userEditor.apply();
        }
    }

    public static String getUserId(Context context) {
        String strUserId = "";
        try {
            sharedPreferences = initLMSharedPrefs(context);
            if (sharedPreferences != null) {
                JSONObject data = getUserInfo(context);
                if (data != null) {
                    strUserId = data.optString("user_id", "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strUserId;
    }
}
