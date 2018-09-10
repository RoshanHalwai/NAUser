package com.kirtanlabs.nammaapartments.home.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.home.fragments.ApartmentServicesHome;
import com.kirtanlabs.nammaapartments.home.fragments.SocietyServicesHome;
import com.kirtanlabs.nammaapartments.navigationdrawer.UserProfile;
import com.kirtanlabs.nammaapartments.navigationdrawer.help.activities.NammaApartmentsHelp;
import com.kirtanlabs.nammaapartments.navigationdrawer.myguards.activities.MyGuardsActivity;
import com.kirtanlabs.nammaapartments.navigationdrawer.myvehicles.activities.MyVehiclesActivity;
import com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.activities.MyPaymentsActivity;
import com.kirtanlabs.nammaapartments.navigationdrawer.noticeboard.activities.NoticeBoard;
import com.kirtanlabs.nammaapartments.navigationdrawer.settings.NammaApartmentSettings;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.mysweethome.MySweetHome;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartments.userpojo.UserFlatDetails;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.util.Objects;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_DEVICE_TYPE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_DEVICE_VERSION;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_OTHER_DETAILS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_TOKENID;
import static com.kirtanlabs.nammaapartments.utilities.Constants.LOGGED_IN;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NAMMA_APARTMENTS_PREFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.USER_UID;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class NammaApartmentsHome extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private SmoothActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private Dialog dialog;
    private DatabaseReference userReference;
    private NavigationView navigationView;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_nahome_navigation_drawer;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.app_name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Since this is Namma Apartments Home Screen we wouldn't want the users to go back to OTP screen,
          hence hiding the back button from the Title Bar*/
        hideBackButton();

        initNavigationDrawer();
        checkSharedPreferences();
        setApplicationContextData();
        initViewPager();
    }

    /* ------------------------------------------------------------- *
     * Overriding OnNavigationItemSelected Listener Method
     * ------------------------------------------------------------- */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        /* Handle navigation view item clicks here.*/
        switch (item.getItemId()) {
            case R.id.nav_myProfile: {
                toggle.runWhenIdle(() -> {
                    Intent intent = new Intent(NammaApartmentsHome.this, UserProfile.class);
                    startActivity(intent);
                });
                drawer.closeDrawer(GravityCompat.START);
                break;
            }

            case R.id.nav_myWallet: {
                toggle.runWhenIdle(() -> {
                    Intent intent = new Intent(NammaApartmentsHome.this, MyPaymentsActivity.class);
                    startActivity(intent);
                });
                drawer.closeDrawer(GravityCompat.START);
                break;
            }

            case R.id.nav_myFamilyMembers: {
                toggle.runWhenIdle(() -> {
                    Intent mySweetHomeIntent = new Intent(NammaApartmentsHome.this, MySweetHome.class);
                    mySweetHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mySweetHomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mySweetHomeIntent);
                });
                drawer.closeDrawer(GravityCompat.START);
                break;
            }

            case R.id.nav_myVehicles: {
                toggle.runWhenIdle(() -> {
                    Intent intent = new Intent(NammaApartmentsHome.this, MyVehiclesActivity.class);
                    startActivity(intent);
                });
                drawer.closeDrawer(GravityCompat.START);
                break;
            }

            case R.id.nav_myGuards: {
                toggle.runWhenIdle(() -> {
                    Intent intent = new Intent(NammaApartmentsHome.this, MyGuardsActivity.class);
                    startActivity(intent);
                });
                drawer.closeDrawer(GravityCompat.START);
                break;
            }

            case R.id.nav_myNoticeBoard: {
                toggle.runWhenIdle(() -> {
                    Intent noticeBoardIntent = new Intent(NammaApartmentsHome.this, NoticeBoard.class);
                    startActivity(noticeBoardIntent);
                });
                drawer.closeDrawer(GravityCompat.START);
                break;
            }

            case R.id.nav_appSettings: {
                toggle.runWhenIdle(() -> {
                    Intent settingsIntent = new Intent(NammaApartmentsHome.this, NammaApartmentSettings.class);
                    startActivity(settingsIntent);
                });
                drawer.closeDrawer(GravityCompat.START);
                break;
            }

            case R.id.nav_help: {
                toggle.runWhenIdle(() -> {
                    Intent helpIntent = new Intent(NammaApartmentsHome.this, NammaApartmentsHelp.class);
                    startActivity(helpIntent);
                });
                drawer.closeDrawer(GravityCompat.START);
                break;
            }

            case R.id.nav_rateUs: {
                toggle.runWhenIdle(() -> {
                    showRateUsDialog();
                    dialog.show();
                });
                drawer.closeDrawer(GravityCompat.START);
                break;
            }

        }
        return false;
    }

    /* ------------------------------------------------------------- *
     * Overriding onBackPressed Method
     * ------------------------------------------------------------- */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This dialog gets invoked when user clicks on rateUs button.
     */
    private void showRateUsDialog() {
        /*Creating and Inflating the layout for rate us dialog */
        dialog = new Dialog(NammaApartmentsHome.this);
        dialog.setContentView(R.layout.layout_rate_us_dialog);

        /*Getting Id's for all the views*/
        TextView textRateDialogTitle = dialog.findViewById(R.id.textRateDialogTitle);
        TextView textRateDialogMessage = dialog.findViewById(R.id.textRateDialogMessage);
        Button buttonSubmit = dialog.findViewById(R.id.buttonSubmit);
        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

        /*Setting Fonts for all the views*/
        textRateDialogTitle.setTypeface(setLatoBoldFont(this));
        textRateDialogMessage.setTypeface(setLatoRegularFont(this));
        buttonSubmit.setTypeface(setLatoRegularFont(this));
        buttonCancel.setTypeface(setLatoRegularFont(this));

        /*Setting OnClick Listeners to the views*/
        /*TODO:To Change The Url after successful registration of our app in PlayStore */
        buttonSubmit.setOnClickListener(v -> redirectUsersToPlayStore());
        buttonCancel.setOnClickListener(v -> dialog.cancel());
    }

    /**
     * This method gets invoked to redirect users to playStore Link
     */
    private void redirectUsersToPlayStore() {
        // getPackageName() from Context or Activity object
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void initNavigationDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        toggle = new SmoothActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Creates shared preferences and loads USER-UID if User has logged in for the first time. Else
     * if the user has already logged
     */
    private void checkSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(NAMMA_APARTMENTS_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        if (sharedPreferences.getBoolean(LOGGED_IN, false)) {
            String userUid = sharedPreferences.getString(USER_UID, null);
            userReference = PRIVATE_USERS_REFERENCE.child(Objects.requireNonNull(userUid));
        } else {
            userReference = PRIVATE_USERS_REFERENCE
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            editor = sharedPreferences.edit();
            editor.putBoolean(LOGGED_IN, true);
            editor.putString(USER_UID, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            editor.apply();
        }
        /*Storing User's Mobile API level in firebase under (users->private->userUid->otherDetails->deviceVersion)*/
        userReference.child(FIREBASE_CHILD_OTHER_DETAILS).child(FIREBASE_CHILD_DEVICE_VERSION).setValue(Build.VERSION.SDK_INT);

        /*Storing User's Mobile OS Type(Android) in firebase under (users->private->userUid->otherDetails->deviceType)*/
        userReference.child(FIREBASE_CHILD_OTHER_DETAILS).child(FIREBASE_CHILD_DEVICE_TYPE).setValue(Constants.ANDROID);

        /*Generating token id for Family Member/Friend on launch of Home Screen, and making sure a refreshed token is generated when
         * user logs in from a different device*/
        String token_id = FirebaseInstanceId.getInstance().getToken();
        DatabaseReference userTokenIdReference = PRIVATE_USERS_REFERENCE.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        userTokenIdReference.child(FIREBASE_CHILD_TOKENID).setValue(token_id);
    }

    /**
     * Sets the Application Context data so user can use it through out the application
     */
    private void setApplicationContextData() {
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NammaApartmentUser nammaApartmentUser = dataSnapshot.getValue(NammaApartmentUser.class);
                ((NammaApartmentsGlobal) getApplicationContext()).setNammaApartmentUser(nammaApartmentUser);
                addNavigationHeaderContent(Objects.requireNonNull(nammaApartmentUser));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Add Society Name, Block and Apartment Number of current User
     *
     * @param nammaApartmentUser contains data of current user
     */
    private void addNavigationHeaderContent(NammaApartmentUser nammaApartmentUser) {
        View v = navigationView.getHeaderView(0);
        TextView textSocietyName = v.findViewById(R.id.textSocietyName);
        TextView textBlockAndFlat = v.findViewById(R.id.textBlockAndFlat);
        textSocietyName.setTypeface(setLatoRegularFont(NammaApartmentsHome.this));
        textBlockAndFlat.setTypeface(setLatoRegularFont(NammaApartmentsHome.this));
        UserFlatDetails userFlatDetails = nammaApartmentUser.getFlatDetails();
        String societyName = userFlatDetails.getSocietyName();
        String apartmentName = userFlatDetails.getApartmentName();
        String flatNumber = userFlatDetails.getFlatNumber();
        String societyAndFlat = apartmentName.concat(", ").concat(flatNumber);
        textSocietyName.setText(societyName);
        textBlockAndFlat.setText(societyAndFlat);
    }

    private void initViewPager() {
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = findViewById(R.id.container);
        TabLayout tabLayout = findViewById(R.id.tabs);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    /* ------------------------------------------------------------- *
     * SectionPagerAdapter Class
     * ------------------------------------------------------------- */

    class SectionsPagerAdapter extends FragmentPagerAdapter {

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /* ------------------------------------------------------------- *
         * Overriding FragmentPagerAdapter Objects
         * ------------------------------------------------------------- */

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SocietyServicesHome();
                case 1:
                    return new ApartmentServicesHome();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {

        private Runnable runnable;

        SmoothActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
                runnable.run();
                runnable = null;
            }
        }

        void runWhenIdle(Runnable runnable) {
            this.runnable = runnable;
        }
    }

}
