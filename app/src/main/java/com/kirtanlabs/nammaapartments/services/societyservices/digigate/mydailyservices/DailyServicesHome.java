package com.kirtanlabs.nammaapartments.services.societyservices.digigate.mydailyservices;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_DAILYSERVICES;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_FLAT_MEMBERS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_STATUS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PUBLIC_DAILYSERVICES_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SERVICE_TYPE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoLightFont;

public class DailyServicesHome extends BaseActivity implements DialogInterface.OnCancelListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    public static final Map<String, Long> numberOfFlats = new HashMap<>();
    private AlertDialog dailyServicesListDialog;
    private List<NammaApartmentDailyService> nammaApartmentDailyServiceList;
    private DailyServicesHomeAdapter dailyServicesHomeAdapter;
    private int index = 0;
    private boolean isDailyServicePresent = false;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_daily_services;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.my_daily_services;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();

        /*Getting Id's for all the views*/
        Button buttonAddDailyServices = findViewById(R.id.buttonAddDailyServices);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        /*Setting font for button*/
        buttonAddDailyServices.setTypeface(setLatoLightFont(this));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*Creating recycler view adapter*/
        nammaApartmentDailyServiceList = new ArrayList<>();
        dailyServicesHomeAdapter = new DailyServicesHomeAdapter(nammaApartmentDailyServiceList, this);

        /*Setting adapter to recycler view*/
        recyclerView.setAdapter(dailyServicesHomeAdapter);

        createDailyServicesListDialog();

        /*Setting event for views */
        buttonAddDailyServices.setOnClickListener(v -> dailyServicesListDialog.show());
        dailyServicesListDialog.setOnCancelListener(this);

        /*To retrieve DailyServicesList from firebase.*/
        checkAndRetrieveDailyServices();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Creates a DailyServicesListDialog with a list view which contains the list of daily services.
     */
    private void createDailyServicesListDialog() {
        /*Custom DialogBox with list of all daily services*/
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] dailyServices = getResources().getStringArray(R.array.apartment_services_list);
        Intent intent = new Intent(DailyServicesHome.this, AddDailyService.class);
        intent.putExtra(SCREEN_TITLE, R.string.my_daily_services);
        builder.setItems(dailyServices, (dialog, which) -> {
            dailyServicesListDialog.cancel();
            intent.putExtra(SERVICE_TYPE, dailyServices[which]);
            startActivity(intent);
        });
        dailyServicesListDialog = builder.create();
    }

    /**
     * Check if the flat has any daily service. If it does not have any daily services added we show daily service unavailable message
     * Else, we display the cardView of all daily services of the current user.
     */
    private void checkAndRetrieveDailyServices() {
        DatabaseReference userDataReference = ((NammaApartmentsGlobal) getApplicationContext())
                .getUserDataReference();
        DatabaseReference myDailyServicesReference = userDataReference.child(FIREBASE_CHILD_DAILYSERVICES);

        /*We first check if this flat has any dailyServices*/
        myDailyServicesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    hideProgressIndicator();
                    showFeatureUnavailableLayout(R.string.daily_service_unavailable_message);
                } else {
                    DatabaseReference privateFlatReference = ((NammaApartmentsGlobal) getApplicationContext()).getUserDataReference();
                    privateFlatReference.keepSynced(true);
                    privateFlatReference.child(FIREBASE_CHILD_FLAT_MEMBERS).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot flatSnapshot : dataSnapshot.getChildren()) {
                                retrieveDailyServicesDetailsFromFirebase(flatSnapshot.getKey());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Retrieves all daily services of given userUID
     *
     * @param userUID - whose daily services needs to be retrieved
     */
    private void retrieveDailyServicesDetailsFromFirebase(String userUID) {
        DatabaseReference dailyServicesListReference = ((NammaApartmentsGlobal) getApplicationContext()).getUserDataReference()
                .child(FIREBASE_CHILD_DAILYSERVICES);
        dailyServicesListReference.keepSynced(true);
        dailyServicesListReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot myDailyServiceSnapshot) {
                hideProgressIndicator();
                /*Iterate over each daily service type*/
                for (DataSnapshot dailyServicesSnapshot : myDailyServiceSnapshot.getChildren()) {
                    String dailyServiceType = dailyServicesSnapshot.getKey();
                    DatabaseReference dailyServiceTypeReference = dailyServicesListReference.child(dailyServiceType);
                    dailyServiceTypeReference.keepSynced(true);
                    /*For each daily service type, check how many are added. Say a user can have two
                     * cooks or maids*/
                    dailyServiceTypeReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dailyServiceUIDSnapshot) {
                            /*Iterate over each of them and add listener to each of them*/
                            for (DataSnapshot dailyServicesTypeSnapshot : dailyServiceUIDSnapshot.getChildren()) {
                                if (Objects.requireNonNull(dailyServicesTypeSnapshot.getValue(Boolean.class)).equals(true)) {
                                    isDailyServicePresent = true;
                                    String dailyServiceUID = dailyServicesTypeSnapshot.getKey();
                                    DatabaseReference dailyServicesTypePublicReference = PUBLIC_DAILYSERVICES_REFERENCE
                                            .child(dailyServiceUIDSnapshot.getKey())  // Daily Service Type
                                            .child(dailyServiceUID);                  // Daily Service UID
                                    dailyServicesTypePublicReference.keepSynced(true);
                                    dailyServicesTypePublicReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dailyServiceCountSnapshot) {
                                            String dailyServiceStatus = Objects.requireNonNull(dailyServiceCountSnapshot.child(FIREBASE_CHILD_STATUS).getValue()).toString();
                                            if (dailyServiceCountSnapshot.hasChild(userUID)) {
                                                numberOfFlats.put(dailyServiceUID, dailyServiceCountSnapshot.getChildrenCount() - 1);
                                                DataSnapshot dailyServiceDataSnapshot = dailyServiceCountSnapshot.child(userUID);
                                                NammaApartmentDailyService nammaApartmentDailyService = dailyServiceDataSnapshot.getValue(NammaApartmentDailyService.class);
                                                Objects.requireNonNull(nammaApartmentDailyService).setDailyServiceType(DailyServiceType.get(dailyServiceType));
                                                nammaApartmentDailyService.setStatus(dailyServiceStatus);
                                                nammaApartmentDailyServiceList.add(index++, nammaApartmentDailyService);
                                                dailyServicesHomeAdapter.notifyDataSetChanged();
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                                /*Checking if any daily service is working in users flat or not*/
                                if (isDailyServicePresent) {
                                    hideFeatureUnavailableLayout();
                                } else {
                                    showFeatureUnavailableLayout(R.string.daily_service_unavailable_message);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
