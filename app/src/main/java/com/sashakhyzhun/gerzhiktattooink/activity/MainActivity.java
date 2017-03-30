package com.sashakhyzhun.gerzhiktattooink.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sashakhyzhun.gerzhiktattooink.R;
import com.sashakhyzhun.gerzhiktattooink.fragments.NewsFragment;
import com.sashakhyzhun.gerzhiktattooink.fragments.AboutMeFragment;
import com.sashakhyzhun.gerzhiktattooink.fragments.ContactMeFragment;
import com.sashakhyzhun.gerzhiktattooink.fragments.FindMyOfficeFragment;
import com.sashakhyzhun.gerzhiktattooink.fragments.PrivacyPolicyFragment;
import com.sashakhyzhun.gerzhiktattooink.fragments.SettingsFragment;
import com.sashakhyzhun.gerzhiktattooink.fragments.TermsFragment;
import com.sashakhyzhun.gerzhiktattooink.utils.CircleTransform;
import com.sashakhyzhun.gerzhiktattooink.utils.SessionManager;
import com.sashakhyzhun.locationhelper.GPSTracker;
import com.sashakhyzhun.locationhelper.LocationChecker;
import com.sashakhyzhun.locationhelper.LocationUtil;

import static com.sashakhyzhun.gerzhiktattooink.utils.Constants.TAG_NEWS;
import static com.sashakhyzhun.gerzhiktattooink.utils.Constants.TAG_FIND_MY_OFFICE;
import static com.sashakhyzhun.gerzhiktattooink.utils.Constants.TAG_CONTACT_US;
import static com.sashakhyzhun.gerzhiktattooink.utils.Constants.TAG_ABOUT_ME;
import static com.sashakhyzhun.gerzhiktattooink.utils.Constants.TAG_PRIVACY_POLICY;
import static com.sashakhyzhun.gerzhiktattooink.utils.Constants.TAG_SETTINGS;
import static com.sashakhyzhun.gerzhiktattooink.utils.Constants.TAG_TERMS;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String CURRENT_TAG = TAG_NEWS;
    public static int navItemIndex = 0;
    private NavigationView navigationView;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private String[] activityTitles;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SessionManager session = SessionManager.getInstance(getApplicationContext());

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                InputMethodManager input = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (getWindow().getCurrentFocus() != null)
                    input.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        };
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        drawerToggle.getDrawerArrowDrawable().setColor(Color.WHITE);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeader = navigationView.getHeaderView(0);
        TextView  drawerName  = (TextView)  navHeader.findViewById(R.id.text_view_user_name);
        TextView  drawerEmail = (TextView)  navHeader.findViewById(R.id.text_view_user_email);
        ImageView drawerImage = (ImageView) navHeader.findViewById(R.id.image_view_user_photo);

        // load nav menu header data
        drawerName.setText(session.getUserName());
        drawerEmail.setText(session.getUserEmail());
        Glide.with(this).load(session.getUserPathToPic())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(drawerImage);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            LocationChecker checker = new LocationChecker(getApplicationContext());
            checker.enableDailyLocationCheck(2, 27, 9999);
            Snackbar.make(view, "Setup daily location checker, want to cancel?", Snackbar.LENGTH_LONG).setAction("Action", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checker.disableDailyLocationCheck(9999);
                    Toast.makeText(MainActivity.this, "Successfully canceled", Toast.LENGTH_SHORT).show();
                }
            }).show();
        });

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_NEWS;
            loadCurrentFragment();
        }
    }


    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadCurrentFragment() {
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        //navigationView.getMenu().getItem(navItemIndex);   // selecting appropriate nav menu item
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(activityTitles[navItemIndex]); // set toolbar title
        }
        // if user select the current navigation menu again, just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable runnable = () -> {
            // update the main content by replacing fragments
            Fragment fragment = getHomeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        };

        mHandler = new Handler();
        mHandler.post(runnable); // If 'runnable' is not null, then add to the message queue
        drawer.closeDrawers();   // Closing drawer on item click
        invalidateOptionsMenu(); // refresh toolbar menu
        toggleFab();             // show or hide the fab button
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            //Replacing the main content with ContentFragment Which is our Inbox View;
            case R.id.nav_home:
                navItemIndex = 0;
                CURRENT_TAG = TAG_NEWS;
                break;
            case R.id.nav_find_my_office:
                navItemIndex = 1;
                CURRENT_TAG = TAG_FIND_MY_OFFICE;
                break;
            case R.id.nav_about_me:
                navItemIndex = 2;
                CURRENT_TAG = TAG_ABOUT_ME;
                break;
            case R.id.nav_contact_us:
                navItemIndex = 3;
                CURRENT_TAG = TAG_CONTACT_US;
                break;
            case R.id.nav_settings:
                navItemIndex = 4;
                CURRENT_TAG = TAG_SETTINGS;
                break;
            case R.id.nav_terms:
                navItemIndex = 5;
                CURRENT_TAG = TAG_TERMS;
                break;
            case R.id.nav_privacy_policy:
                navItemIndex = 6;
                CURRENT_TAG = TAG_PRIVACY_POLICY;
                break;
            default:
                navItemIndex = 0;
                CURRENT_TAG = TAG_NEWS;
        }


        loadCurrentFragment();

        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        if (navItemIndex != 0) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_NEWS;
            loadCurrentFragment();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sync_app) {
            startActivity(new Intent(this, SplashActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0: return new NewsFragment();
            case 1: return new FindMyOfficeFragment();
            case 2: return new AboutMeFragment();
            case 3: return new ContactMeFragment();
            case 4: return new SettingsFragment();
            case 5: return new TermsFragment();
            case 6: return new PrivacyPolicyFragment();
            default:return new NewsFragment();
        }
    }


    private void toggleFab() {
        if (navItemIndex == 0) {
            fab.show();
        } else {
            fab.hide();
        }
    }


}