package com.kirtanlabs.nammaapartments.services.apartmentservices.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.home.adapters.ApartmentServiceAdapter;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.mydailyservices.NammaApartmentDailyService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kirtanlabs.nammaapartments.utilities.Constants.DAILYSERVICES_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_CARBIKECLEANERS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_CHILDDAYCARES;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_COOKS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_DAILYNEWSPAPERS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_DRIVERS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_LAUNDRIES;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_MAIDS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_MILKMEN;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PUBLIC_DAILYSERVICES_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoItalicFont;

public class ApartmentServices extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    public static Map<String, Long> numberOfFlats = new HashMap<>();
    private List<NammaApartmentDailyService> nammaApartmentDailyServiceList;
    private ApartmentServiceAdapter apartmentServiceAdapter;
    private int index = 0;
    private int screenTitle;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.layout_apartment_services;
    }

    @Override
    protected int getActivityTitle() {
        screenTitle = getIntent().getIntExtra(SCREEN_TITLE, 0);
        return screenTitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();

        /*Initialising Array List*/
        nammaApartmentDailyServiceList = new ArrayList<>();

        /*Getting Id of recycler view*/
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*Passing Array List to the Adapter*/
        apartmentServiceAdapter = new ApartmentServiceAdapter(this, nammaApartmentDailyServiceList);

        /*Setting adapter to recycler view*/
        recyclerView.setAdapter(apartmentServiceAdapter);

        switch (screenTitle) {

            case R.string.cook: {
                /*This method is called to make sure the user permits the app to use the Location Service for the very first time*/
                enableLocationService();
                /*To retrieve ApartmentServicesList from firebase.*/
                retrieveApartmentServices(FIREBASE_CHILD_COOKS);
                break;
            }
            case R.string.maid: {
                /*This method is called to make sure the user permits the app to use the Location Service for the very first time*/
                enableLocationService();
                /*To retrieve ApartmentServicesList from firebase.*/
                retrieveApartmentServices(FIREBASE_CHILD_MAIDS);
                break;
            }
            case R.string.car_bike_cleaning: {
                /*This method is called to make sure the user permits the app to use the Location Service for the very first time*/
                enableLocationService();
                /*To retrieve ApartmentServicesList from firebase.*/
                retrieveApartmentServices(FIREBASE_CHILD_CARBIKECLEANERS);
                break;
            }
            case R.string.child_day_care: {
                /*This method is called to make sure the user permits the app to use the Location Service for the very first time*/
                enableLocationService();
                /*To retrieve ApartmentServicesList from firebase.*/
                retrieveApartmentServices(FIREBASE_CHILD_CHILDDAYCARES);
                break;
            }
            case R.string.daily_newspaper: {
                /*This method is called to make sure the user permits the app to use the Location Service for the very first time*/
                enableLocationService();
                /*To retrieve ApartmentServicesList from firebase.*/
                retrieveApartmentServices(FIREBASE_CHILD_DAILYNEWSPAPERS);
                break;
            }
            case R.string.milk_man: {
                /*This method is called to make sure the user permits the app to use the Location Service for the very first time*/
                enableLocationService();
                /*To retrieve ApartmentServicesList from firebase.*/
                retrieveApartmentServices(FIREBASE_CHILD_MILKMEN);
                break;
            }
            case R.string.laundry: {
                /*This method is called to make sure the user permits the app to use the Location Service for the very first time*/
                enableLocationService();
                /*To retrieve ApartmentServicesList from firebase.*/
                retrieveApartmentServices(FIREBASE_CHILD_LAUNDRIES);
                break;
            }
            case R.string.driver: {
                /*This method is called to make sure the user permits the app to use the Location Service for the very first time*/
                enableLocationService();
                /*To retrieve ApartmentServicesList from firebase.*/
                retrieveApartmentServices(FIREBASE_CHILD_DRIVERS);
                break;
            }
            case R.string.groceries:
                hideProgressIndicator();
                showGroceriesFeatureUnavailableLayout(R.string.groceries_unavailable);
                break;
        }

    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Check if the society has any apartment service. If it does not have any apartment services added we
     * show daily service unavailable message. Else, we display the cardView of only that apartment
     * service type selected .
     */
    private void retrieveApartmentServices(String dailyServiceType) {

        /*We first check if this society has any apartmentServices*/
        DAILYSERVICES_REFERENCE.keepSynced(true);
        DAILYSERVICES_REFERENCE.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    hideProgressIndicator();
                    changeLayoutMessages();
                } else {
                    hideProgressIndicator();
                    DatabaseReference publicDailyServiceTypeReference = PUBLIC_DAILYSERVICES_REFERENCE
                            .child(dailyServiceType);
                    publicDailyServiceTypeReference.keepSynced(true);
                    publicDailyServiceTypeReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                changeLayoutMessages();
                            }
                            for (DataSnapshot dailyServiceTypeUID : dataSnapshot.getChildren()) {
                                String serviceTypeUID = dailyServiceTypeUID.getKey();
                                DatabaseReference serviceOwnerUIDReference = publicDailyServiceTypeReference.child(serviceTypeUID);
                                serviceOwnerUIDReference.keepSynced(true);
                                serviceOwnerUIDReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot serviceOwnerUIDSnapshot) {
                                        for (DataSnapshot ownerUIDSnapshot : serviceOwnerUIDSnapshot.getChildren()) {
                                            String ownerUID = ownerUIDSnapshot.getKey();
                                            if (!ownerUID.equals(getString(R.string.status))) {
                                                numberOfFlats.put(serviceTypeUID, serviceOwnerUIDSnapshot.getChildrenCount() - 1);
                                                NammaApartmentDailyService nammaApartmentDailyService = ownerUIDSnapshot.getValue(NammaApartmentDailyService.class);
                                                nammaApartmentDailyServiceList.add(index++, nammaApartmentDailyService);
                                                apartmentServiceAdapter.notifyDataSetChanged();
                                                break;
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     *This method invokes to inflate show a new layout for users that this feature will be implemented later.
     * @param text contains feature unavailable message.
     */
    private void showGroceriesFeatureUnavailableLayout(int text) {
        LinearLayout featureUnavailableLayout = findViewById(R.id.layoutGroceriesUnavailable);
        featureUnavailableLayout.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.textGroceryFeatureUnavailable);
        textView.setTypeface(setLatoItalicFont(this));
        textView.setText(text);
    }

    /**
     * This method gets invoked when society service has not added any apartment services.
     */
    private void changeLayoutMessages() {
        String messageDescription = getString(R.string.apartment_service_unavailable);
        String textReplacement = getString(R.string.apartment);
        String updatedDescription = "";
        switch (screenTitle) {
            case R.string.cook: {
                updatedDescription = messageDescription.replace(textReplacement, getString(R.string.cook));
                break;
            }
            case R.string.maid: {
                updatedDescription = messageDescription.replace(textReplacement, getString(R.string.maid));
                break;
            }
            case R.string.car_bike_cleaning: {
                updatedDescription = messageDescription.replace(textReplacement, getString(R.string.car_bike_cleaning));
                break;
            }
            case R.string.child_day_care: {
                updatedDescription = messageDescription.replace(textReplacement, getString(R.string.child_day_care));
                break;
            }
            case R.string.daily_newspaper: {
                updatedDescription = messageDescription.replace(textReplacement, getString(R.string.daily_newspaper));
                break;
            }
            case R.string.milk_man: {
                updatedDescription = messageDescription.replace(textReplacement, getString(R.string.milk_man));
                break;
            }
            case R.string.laundry: {
                updatedDescription = messageDescription.replace(textReplacement, getString(R.string.laundry));
                break;
            }
            case R.string.driver: {
                updatedDescription = messageDescription.replace(textReplacement, getString(R.string.driver));
                break;
            }
        }
      showFeatureUnAvailableLayout(updatedDescription);

    }

}
