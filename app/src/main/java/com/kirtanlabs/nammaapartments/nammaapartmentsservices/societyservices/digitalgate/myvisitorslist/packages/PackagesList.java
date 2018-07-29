package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.myvisitorslist.packages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.digitalgatehome.DigitalGateHome;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.kirtanlabs.nammaapartments.Constants.SCREEN_TITLE;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 6/28/2018
 */
public class PackagesList extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private PackagesListAdapter packagesListAdapter;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_guests_list;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.package_arrivals;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Set Listener for back button here since after Inviting visitors we navigate users
         * to this activity and when back button is pressed we don't want users to
         * go back to Invite Visitors screen but instead navigate to Digi Gate Home*/
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> onBackPressed());

        /*Getting Id of recycler view*/
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*Retrieve Packages data*/
        new RetrievingPackagesList(PackagesList.this).getPackages(nammaApartmentArrivalList -> {
            hideProgressIndicator();
            if (nammaApartmentArrivalList.isEmpty()) {
                showFeatureUnavailableLayout(R.string.deliveries_unavailable_message);
            } else {
                packagesListAdapter = new PackagesListAdapter(nammaApartmentArrivalList, PackagesList.this);
                recyclerView.setAdapter(packagesListAdapter);
            }
        });
        recyclerView.setAdapter(packagesListAdapter);

    }

    /* ------------------------------------------------------------- *
     * Overriding Back button
     * ------------------------------------------------------------- */

    /*We override these methods since after Expecting Package Arrival screen we navigate users
     * to this activity and when back button is pressed we don't want users to
     * go back to Expecting Package Arrival screen but instead navigate to Digi Gate Home*/
    @Override
    public void onBackPressed() {
        if (getIntent().getStringExtra(SCREEN_TITLE) != null) {
            Intent PackagesListIntent = new Intent(this, DigitalGateHome.class);
            PackagesListIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            PackagesListIntent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(PackagesListIntent);
        } else {
            super.onBackPressed();
        }
    }
}