package com.kirtanlabs.nammaapartments.nammaapartmentshome;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentshome.navigationdrawer.NammaApartmentsHelp;
import com.kirtanlabs.nammaapartments.nammaapartmentshome.navigationdrawer.UserProfile;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mysweethome.MySweetHome;
import com.kirtanlabs.nammaapartments.onboarding.login.SignIn;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.util.Objects;

import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;
import static com.kirtanlabs.nammaapartments.NammaApartmentsGlobal.userUID;

public class NammaApartmentsHome extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */
    /*Root Layout for Navigation Drawer*/
    private DrawerLayout drawer;
    private Dialog dialog;
    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /*At this point new user and existing user would have their records in firebase and hence we store
         * the values to NammaApartmentsGlobal*/
        DatabaseReference userReference = Constants.PRIVATE_USERS_REFERENCE
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NammaApartmentUser nammaApartmentUser = dataSnapshot.getValue(NammaApartmentUser.class);
                ((NammaApartmentsGlobal) getApplicationContext()).setNammaApartmentUser(nammaApartmentUser);

                /*Storing user token_id in firebase so Guard can send notification*/
                String token_id = FirebaseInstanceId.getInstance().getToken();
                PRIVATE_USERS_REFERENCE.child(userUID).child("tokenId").setValue(token_id);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = findViewById(R.id.container);
        TabLayout tabLayout = findViewById(R.id.tabs);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_myProfile) {
            Intent intent = new Intent(NammaApartmentsHome.this, UserProfile.class);
            startActivity(intent);
        }
        if (id == R.id.nav_myFamilyMembers) {
            Intent mySweetHomeIntent = new Intent(NammaApartmentsHome.this, MySweetHome.class);
            mySweetHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mySweetHomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mySweetHomeIntent);
        }
        if (id == R.id.nav_appSettings) {
            //TODO:To implement this Functionality later
        }
        if (id == R.id.nav_help) {
            Intent helpIntent = new Intent(NammaApartmentsHome.this, NammaApartmentsHelp.class);
            startActivity(helpIntent);
        }
        if (id == R.id.nav_rateUs) {
            openRateDialog();
            dialog.show();
        }
        if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(NammaApartmentsHome.this, SignIn.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This dialog gets invoked when user clicks on rateUs button.
     */
    private void openRateDialog() {
        dialog = new Dialog(NammaApartmentsHome.this);
        dialog.setContentView(R.layout.layout_rate_us_dialog);
        TextView textRateDialog = dialog.findViewById(R.id.textRateDialog);
        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        Button buttonRateNow = dialog.findViewById(R.id.buttonRateNow);
        Button buttonRemindLater = dialog.findViewById(R.id.buttonRemindLater);
        textRateDialog.setTypeface(setLatoRegularFont(this));
        buttonRateNow.setTypeface(setLatoLightFont(this));
        buttonRemindLater.setTypeface(setLatoLightFont(this));
        //TODO:TO Implement on click of Rate Now users will be redirected to PlayStore.
        buttonRateNow.setOnClickListener(v -> dialog.cancel());
        buttonRemindLater.setOnClickListener(v -> dialog.cancel());
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
                    return new SocietyServices();
                case 1:
                    return new ApartmentServices();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
