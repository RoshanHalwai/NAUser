package com.kirtanlabs.nammaapartments.services.societyservices.digigate.myvisitorslist.guests;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.help.activities.FrequentlyAskedQuestionsActivity;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.home.DigitalGateHome;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SCREEN_TITLE;

public class GuestsList extends BaseActivity implements View.OnClickListener {

    private GuestsListAdapter guestsListAdapter;

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

        ImageView infoButton = findViewById(R.id.infoButton);
        infoButton.setOnClickListener(this);

        /*Getting Id of recycler view*/
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*Retrieve Guest data*/
        new RetrievingGuestList(GuestsList.this, false).getPreAndPostApprovedGuests(nammaApartmentGuestList -> {
            hideProgressIndicator();
            if (nammaApartmentGuestList.isEmpty()) {
                showFeatureUnavailableLayout(R.string.visitors_unavailable_message);
            } else {
                guestsListAdapter = new GuestsListAdapter(nammaApartmentGuestList, GuestsList.this);
                recyclerView.setAdapter(guestsListAdapter);
            }
        });

    }

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_guests_list;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.my_guests;
    }

    /* ------------------------------------------------------------- *
     * Overriding Back button
     * ------------------------------------------------------------- */

    /*We override these methods since after Inviting visitors we navigate users
     * to this activity and when back button is pressed we don't want users to
     * go back to Invite Visitors screen but instead navigate to Digi Gate Home*/
    @Override
    public void onBackPressed() {
        if (getIntent().getStringExtra(SCREEN_TITLE) != null) {
            Intent visitorsListIntent = new Intent(this, DigitalGateHome.class);
            visitorsListIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            visitorsListIntent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(visitorsListIntent);
        } else {
            super.onBackPressed();
        }
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.infoButton) {
            Intent infoIntent = new Intent(GuestsList.this, FrequentlyAskedQuestionsActivity.class);
            infoIntent.putExtra(SCREEN_TITLE, R.string.my_guests);
            startActivity(infoIntent);
        }
    }

}
