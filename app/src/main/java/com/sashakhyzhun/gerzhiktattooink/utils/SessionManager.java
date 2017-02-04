package com.sashakhyzhun.gerzhiktattooink.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.facebook.login.LoginManager;
import com.sashakhyzhun.gerzhiktattooink.activity.SplashActivity;

import java.util.HashMap;

/**
 * Created by SashaKhyzhun on 1/31/17.
 */

public class SessionManager {

    private static SessionManager instance = null;

    public static final String KEY_ID = "id";                     // User ID from login method
    public static final String KEY_NAME = "name";                 // User name
    public static final String KEY_EMAIL = "email";               // Email address
    public static final String KEY_PICTURE = "picture";           // User picture path

    private static final String PREFER_NAME = "PreferName";       // Shared pref file name
    private static final String IS_USER_LOGIN = "IsUserLoggedIn"; // All Shared Preferences Keys

    private int PRIVATE_MODE = 0;                                 // Shared pref mode
    private SharedPreferences pref;                               // Shared Preferences reference
    private SharedPreferences.Editor editor;                      // Editor reference for SP
    private Context context;                                      // Context

    /**
     * Standard constructor to access this manager
     * @param context - simple context from class when we had created session manager.
     */
    private SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }

    public void createUserLoginSession(String name, String email, String id, String picture) {
        editor.putBoolean(IS_USER_LOGIN, true); // Storing login value as TRUE
        editor.putString(KEY_ID,    id);        // Storing id in pref
        editor.putString(KEY_NAME,    name);    // Storing name in pref
        editor.putString(KEY_EMAIL,   email);   // Storing email in pref
        editor.putString(KEY_PICTURE, picture); // Storing picture in pref
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_ID,      pref.getString(KEY_ID,      ""));
        user.put(KEY_NAME,    pref.getString(KEY_NAME,    ""));
        user.put(KEY_EMAIL,   pref.getString(KEY_EMAIL,   ""));
        user.put(KEY_PICTURE, pref.getString(KEY_PICTURE, ""));
        return user;
    }


    public void setUserPathToPic(String newPathToPic) {
        editor.putString(KEY_PICTURE, newPathToPic);
        editor.commit();
    }

    public void setUserName(String newName) {
        editor.putString(KEY_NAME, newName);
        editor.commit();
    }

    public String getUserID() {
        return pref.getString("id", "");
    }

    public String getUserName() {
        return pref.getString("name", "");
    }

    public String getUserEmail() {
        return pref.getString("email", "");
    }

    public String getUserPathToPic() {
        return pref.getString("picture", "");
    }

    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }


    public boolean checkLogin() {
        // Check login status
        if (!this.isUserLoggedIn()) {
            Intent i = new Intent(context, SplashActivity.class); // because is not logged-in
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Closing all the Activities from stack
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add new Flag to start new Activity
            context.startActivity(i);
            return true;
        }
        return false;
    }

    public void logoutUser() {
        LoginManager.getInstance().logOut();
        editor.clear();  // Clearing all user data from Shared Preferences
        editor.commit();

        Intent i = new Intent(context, SplashActivity.class); // After logout redirect
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);           // Closing all the Activities
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);            // Add new Flag to start new Activity
        context.startActivity(i);                             // Staring Login Activity
    }


}
