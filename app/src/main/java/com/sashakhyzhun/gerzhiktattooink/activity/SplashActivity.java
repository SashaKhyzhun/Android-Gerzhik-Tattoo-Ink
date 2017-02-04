package com.sashakhyzhun.gerzhiktattooink.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.sashakhyzhun.gerzhiktattooink.R;
import com.sashakhyzhun.gerzhiktattooink.utils.SessionManager;

/**
 * Created by SashaKhyzhun on 1/30/17.
 */

public class SplashActivity extends AppCompatActivity {

    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SessionManager sessionManager = SessionManager.getInstance(getApplicationContext());
        if (sessionManager.isUserLoggedIn()) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(intent);
//            }
//        }, 500);


    }

}
