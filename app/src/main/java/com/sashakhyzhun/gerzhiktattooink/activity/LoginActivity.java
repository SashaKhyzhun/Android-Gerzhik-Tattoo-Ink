package com.sashakhyzhun.gerzhiktattooink.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sashakhyzhun.gerzhiktattooink.R;
import com.sashakhyzhun.gerzhiktattooink.utils.SessionManager;
import com.sashakhyzhun.locationhelper.MyLocationHelper;

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
    private static final int RC_SIGN_IN = 228;
    private String userEmail, userPicture, userName, userID;
    private LoginButton btnLoginFacebook;
    private SignInButton btnLoginGoogle;
    private Button btnLoginAnonymous;

    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private AccessTokenTracker accessTokenTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);


        MyLocationHelper locationHelper = new MyLocationHelper(this);
        locationHelper.askLocationPermission();

//        /************************ for API >= 23 *************************/
//        ActivityCompat.requestPermissions(this, new String[]{
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//        }, 1);
//        /****************************************************************/

        btnLoginFacebook = (LoginButton) findViewById(R.id.button_login_facebook);
        btnLoginGoogle = (SignInButton) findViewById(R.id.button_login_google);
        btnLoginAnonymous = (Button) findViewById(R.id.button_login_anonymous);


        callbackManager = CallbackManager.Factory.create();
        btnLoginFacebook.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends"));


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, connectionResult -> {})
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customizing G+ button
        btnLoginGoogle.setSize(SignInButton.SIZE_STANDARD);
        btnLoginGoogle.setScopes(gso.getScopeArray());

        btnLoginFacebook.setOnClickListener(this);
        btnLoginGoogle.setOnClickListener(this);
        btnLoginAnonymous.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login_facebook:
                btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(LoginActivity.this, "Yo! Logged in!", Toast.LENGTH_SHORT).show();
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                                (object, response) -> registerUserWithFacebook(object));

                        Bundle b = new Bundle();
                        b.putString("fields", "id,email,first_name,last_name,picture.type(large)");
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
                break;

            case R.id.button_login_google:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.button_login_anonymous:
                Toast.makeText(this, "ANON", Toast.LENGTH_SHORT).show();
                registerLikeAsAnonymous();
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            registerUserWithGoogle(result);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void registerUserWithFacebook(JSONObject user) {
        try {
            userEmail = user.getString("email");
            userID = user.getString("id");
            userName = user.getString("first_name") + " " + user.getString("last_name");
            try {
                URL picture = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
                userPicture = picture.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }

        SessionManager session = SessionManager.getInstance(getApplicationContext());
        session.createUserLoginSession(userName, userEmail, userID, userPicture, "facebook");

        startActivity(new Intent(this, SplashActivity.class));
        Log.i(TAG, "onCompleted: userName    : " + userName);
        Log.i(TAG, "onCompleted: userEmail   : " + userEmail);
        Log.i(TAG, "onCompleted: userID      : " + userID);
        Log.i(TAG, "onCompleted: userPicture : " + userPicture);
    }

    private void registerUserWithGoogle(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            if (acct != null ) {
                userPicture = (acct.getPhotoUrl() != null) ? acct.getPhotoUrl().toString() : "";
                userName = acct.getDisplayName();
                userEmail = acct.getEmail();
                userID = acct.getId();
            }


            SessionManager session = SessionManager.getInstance(getApplicationContext());
            session.createUserLoginSession(userName, userEmail, userID, userPicture, "google");

            Log.i(TAG, "onCompleted: userName    : " + userName);
            Log.i(TAG, "onCompleted: userEmail   : " + userEmail);
            Log.i(TAG, "onCompleted: userID      : " + userID);
            Log.i(TAG, "onCompleted: userPicture : " + userPicture);

            startActivity(new Intent(this, SplashActivity.class));
        }

    }

    private void registerLikeAsAnonymous() {
        String userName = "", userEmail = "", userID = "", userPicture = "";
        SessionManager session = SessionManager.getInstance(getApplicationContext());
        session.createUserLoginSession(userName, userEmail, userID, userPicture, "anonymous");

        startActivity(new Intent(this, SplashActivity.class));
    }


}
