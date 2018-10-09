package com.kirtanlabs.nammaapartments.navigationdrawer.myfood;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.navigationdrawer.myfood.pojo.DonateFood;

import java.util.ArrayList;
import java.util.List;

import static com.kirtanlabs.nammaapartments.utilities.Constants.DONATE_FOOD_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_FOOD_DONATIONS;

public class RetrievingFoodDonationList {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context context;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public RetrievingFoodDonationList(Context context) {
        this.context = context;
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private void getFoodDonationUIDList(FoodDonationUIDListCallback foodDonationUIDListCallback) {
        NammaApartmentsGlobal nammaApartmentsGlobal = ((NammaApartmentsGlobal) context.getApplicationContext());
        DatabaseReference userDataFoodDonationReference = nammaApartmentsGlobal.getUserDataReference().child(FIREBASE_CHILD_FOOD_DONATIONS);
        userDataFoodDonationReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<String> notificationUIDList = new ArrayList<>();
                    for (DataSnapshot notificationUIDDataSnapshot : dataSnapshot.getChildren()) {
                        notificationUIDList.add(notificationUIDDataSnapshot.getKey());
                    }
                    if (dataSnapshot.getChildrenCount() == notificationUIDList.size()) {
                        foodDonationUIDListCallback.onCallBack(notificationUIDList);
                    }
                } else {
                    foodDonationUIDListCallback.onCallBack(new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /* ------------------------------------------------------------- *
     * Public Methods
     * ------------------------------------------------------------- */

    public void getFoodDonationDataList(FoodDonationDataListCallBack foodDonationDataListCallBack) {
        getFoodDonationUIDList(foodDonationNotificationUIDList -> {
            if (!foodDonationNotificationUIDList.isEmpty()) {
                List<DonateFood> donateFoodDataList = new ArrayList<>();
                for (String notificationUID : foodDonationNotificationUIDList) {
                    DatabaseReference foodDonationDataReference = DONATE_FOOD_REFERENCE
                            .child(notificationUID);
                    foodDonationDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DonateFood donateFood = dataSnapshot.getValue(DonateFood.class);
                            donateFoodDataList.add(donateFood);
                            if (foodDonationNotificationUIDList.size() == donateFoodDataList.size()) {
                                foodDonationDataListCallBack.onCallBack(donateFoodDataList);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            } else {
                foodDonationDataListCallBack.onCallBack(new ArrayList<>());
            }
        });
    }

    /* ------------------------------------------------------------- *
     * Interfaces
     * ------------------------------------------------------------- */

    interface FoodDonationUIDListCallback {
        void onCallBack(List<String> foodDonationUIDList);
    }

    public interface FoodDonationDataListCallBack {
        void onCallBack(List<DonateFood> donateFoodList);
    }

}
