package com.sashakhyzhun.gerzhiktattooink.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.sashakhyzhun.gerzhiktattooink.R;
import com.sashakhyzhun.gerzhiktattooink.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by SashaKhyzhun on 1/30/17.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "LoginActivity";
    private String user_email, path_to_pic, name, fb_id;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        loginButton = (LoginButton)findViewById(R.id.button_login_facebook);

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends"));
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginActivity.this, "Yo! Logged in!", Toast.LENGTH_SHORT).show();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        registerUserWithFacebook(object);
                    }
                });
                Bundle b = new Bundle();
                b.putString("fields","id,email,first_name,last_name,picture.type(large)");
                request.setParameters(b);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void registerUserWithFacebook(JSONObject user) {
        try {
            user_email = user.getString("email");
            fb_id = user.getString("id");
            name = user.getString("first_name") + " " + user.getString("last_name");
            try {
                URL picture = new URL("https://graph.facebook.com/" + fb_id + "/picture?type=large");
                path_to_pic = picture.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }

//        if (path_to_pic != null) {
//            File profile = new File(getApplicationContext().getFilesDir().getPath(), "profile.jpg");
//        }

        SessionManager session = SessionManager.getInstance(getApplicationContext());
        session.createUserLoginSession(name, user_email, fb_id, path_to_pic);

        startActivity(new Intent(this, SplashActivity.class));
        Log.i(TAG, "onCompleted: user_name  : " + name);
        Log.i(TAG, "onCompleted: user_email : " + user_email);
        Log.i(TAG, "onCompleted: facebook_id: " + fb_id);
        Log.i(TAG, "onCompleted: path_to_pic: " + path_to_pic);
    }


}
