package com.kirtanlabs.nammaapartments.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.home.pojo.NammaApartmentService;
import com.kirtanlabs.nammaapartments.services.apartmentservices.activities.ApartmentServices;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.home.DigitalGateHome;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.notifydigitalgate.NotifyGateAndEmergencyHome;
import com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.RetrievingSocietyServiceHistoryList;
import com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.activities.AwaitingResponse;
import com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.activities.EventManagement;
import com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.activities.SocietyServicesHome;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.CARPENTER_SERVICE_NOTIFICATION_UID;
import static com.kirtanlabs.nammaapartments.utilities.Constants.ELECTRICIAN_SERVICE_NOTIFICATION_UID;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_ACCEPTED;
import static com.kirtanlabs.nammaapartments.utilities.Constants.GARBAGE_MANAGEMENT_SERVICE_NOTIFICATION_UID;
import static com.kirtanlabs.nammaapartments.utilities.Constants.IN_PROGRESS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NAMMA_APARTMENTS_PREFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PLUMBER_SERVICE_NOTIFICATION_UID;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SERVICE_TYPE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class NammaApartmentServiceAdapter extends RecyclerView.Adapter<NammaApartmentServiceAdapter.NammaApartmentServiceViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context context;
    private final List<NammaApartmentService> apartmentServicesList;
    private final int serviceType;
    private String notificationUID, societyServiceType;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public NammaApartmentServiceAdapter(Context context, int serviceType, List<NammaApartmentService> apartmentServicesList) {
        this.context = context;
        this.serviceType = serviceType;
        this.apartmentServicesList = apartmentServicesList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public NammaApartmentServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_services, parent, false);
        return new NammaApartmentServiceViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull NammaApartmentServiceAdapter.NammaApartmentServiceViewHolder holder, int position) {
        NammaApartmentService nammaApartmentService = apartmentServicesList.get(position);
        holder.textServiceName.setTypeface(setLatoRegularFont(context));
        holder.textServiceName.setText(nammaApartmentService.getServiceName());
        holder.imageServiceIcon.setImageResource(nammaApartmentService.getServiceImage());
    }

    @Override
    public int getItemCount() {
        return apartmentServicesList.size();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is used to check if there is any previous society service request raised by user
     * is still active or not.
     */
    private void checkPreviousRequestStatus(int screenTitle) {
        /*Retrieving user's previous society service request UID from shared preference*/
        SharedPreferences sharedPreferences = Objects.requireNonNull(context.getSharedPreferences(NAMMA_APARTMENTS_PREFERENCE, MODE_PRIVATE));
        switch (screenTitle) {
            case R.string.plumber:
                notificationUID = sharedPreferences.getString(PLUMBER_SERVICE_NOTIFICATION_UID, null);
                societyServiceType = context.getString(R.string.plumber).toLowerCase();
                break;
            case R.string.carpenter:
                notificationUID = sharedPreferences.getString(CARPENTER_SERVICE_NOTIFICATION_UID, null);
                societyServiceType = context.getString(R.string.carpenter).toLowerCase();
                break;
            case R.string.electrician:
                notificationUID = sharedPreferences.getString(ELECTRICIAN_SERVICE_NOTIFICATION_UID, null);
                societyServiceType = context.getString(R.string.electrician).toLowerCase();
                break;
            case R.string.garbage_collection:
                notificationUID = sharedPreferences.getString(GARBAGE_MANAGEMENT_SERVICE_NOTIFICATION_UID, null);
                societyServiceType = Constants.FIREBASE_CHILD_GARBAGE_COLLECTION;
                break;
        }

        if (notificationUID != null) {
            /*Checking status of previous Request*/
            new RetrievingSocietyServiceHistoryList(context)
                    .getSocietyServiceRequestStatus(notificationUID, status -> {
                        if (status != null && (status.equals(IN_PROGRESS) || status.equals(FIREBASE_ACCEPTED))) {
                            Intent awaitingResponseIntent = new Intent(context, AwaitingResponse.class);
                            awaitingResponseIntent.putExtra(Constants.NOTIFICATION_UID, notificationUID);
                            awaitingResponseIntent.putExtra(Constants.SOCIETY_SERVICE_TYPE, societyServiceType);
                            context.startActivity(awaitingResponseIntent);
                        } else {
                            /*Updating previous user's society service request Uid to null*/
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            switch (screenTitle) {
                                case R.string.plumber:
                                    editor.putString(PLUMBER_SERVICE_NOTIFICATION_UID, null);
                                    break;
                                case R.string.carpenter:
                                    editor.putString(CARPENTER_SERVICE_NOTIFICATION_UID, null);
                                    break;
                                case R.string.electrician:
                                    editor.putString(ELECTRICIAN_SERVICE_NOTIFICATION_UID, null);
                                    break;
                                case R.string.garbage_collection:
                                    editor.putString(GARBAGE_MANAGEMENT_SERVICE_NOTIFICATION_UID, null);
                                    break;
                                case R.string.event_management:
                                    editor.putString(Constants.EVENT_MANAGEMENT_SERVICE_NOTIFICATION_UID, null);
                                    break;
                            }
                            editor.apply();
                            /*Navigating user to Society Service Home Screen where user can request for society services*/
                            makeSocietyServiceRequest(screenTitle);
                        }
                    });
        } else {
            /*Navigating user to Society Service Home Screen where user can request for society services*/
            makeSocietyServiceRequest(screenTitle);
        }
    }

    /**
     * This method is invoked to open Society Service Home screen, where user can request for society services.
     *
     * @param screenTitle - type of society service user needs.
     */
    private void makeSocietyServiceRequest(int screenTitle) {
        if (screenTitle == R.string.event_management) {
            context.startActivity(new Intent(context, EventManagement.class));
        } else {
            Intent intent = new Intent(context, SocietyServicesHome.class);
            intent.putExtra(Constants.SCREEN_TITLE, screenTitle);
            context.startActivity(intent);
        }
    }

    /* ------------------------------------------------------------- *
     * NammaApartment Service ViewHolder Class
     * ------------------------------------------------------------- */

    public class NammaApartmentServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView textServiceName;
        final ImageView imageServiceIcon;
        private final Context context;

        NammaApartmentServiceViewHolder(View itemView, Context context) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.context = context;
            textServiceName = itemView.findViewById(R.id.textServiceName);
            imageServiceIcon = itemView.findViewById(R.id.imageServiceIcon);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            /*Since we are using same layout for Society And Apartment Services we need to
             * change click event for both layout*/
            if (serviceType == R.string.society_services) {
                switch (position) {
                    case 0:
                        Intent digiGateIntent = new Intent(context, DigitalGateHome.class);
                        context.startActivity(digiGateIntent);
                        break;
                    case 1:
                        /*To Check if User's previous request for that particular society service is completed or not.*/
                        checkPreviousRequestStatus(R.string.plumber);
                        break;
                    case 2:
                        /*To Check if User's previous request for that particular society service is completed or not.*/
                        checkPreviousRequestStatus(R.string.carpenter);
                        break;
                    case 3:
                        /*To Check if User's previous request for that particular society service is completed or not.*/
                        checkPreviousRequestStatus(R.string.electrician);
                        break;
                    case 4:
                        /*To Check if User's previous request for that particular society service is completed or not.*/
                        checkPreviousRequestStatus(R.string.garbage_collection);
                        break;
                    case 5:
                        Intent notifyGateAndEmergencyIntent = new Intent(context, NotifyGateAndEmergencyHome.class);
                        notifyGateAndEmergencyIntent.putExtra(SERVICE_TYPE, R.string.emergency);
                        context.startActivity(notifyGateAndEmergencyIntent);
                        break;
                    case 6:
                        /*To Check if User's previous request for that particular society service is completed or not.*/
                        checkPreviousRequestStatus(R.string.event_management);
                        break;
                    case 7:
                        Intent intent = new Intent(context, SocietyServicesHome.class);
                        intent.putExtra(Constants.SCREEN_TITLE, R.string.scrap_collection);
                        context.startActivity(intent);
                        break;
                }
            } else {
                Intent apartmentServiceIntent = new Intent(context, ApartmentServices.class);
                switch (position) {
                    case 0:
                        apartmentServiceIntent.putExtra(Constants.SCREEN_TITLE, R.string.cook);
                        break;
                    case 1:
                        apartmentServiceIntent.putExtra(Constants.SCREEN_TITLE, R.string.maid);
                        break;
                    case 2:
                        apartmentServiceIntent.putExtra(Constants.SCREEN_TITLE, R.string.car_bike_cleaning);
                        break;
                    case 3:
                        apartmentServiceIntent.putExtra(Constants.SCREEN_TITLE, R.string.child_day_care);
                        break;
                    case 4:
                        apartmentServiceIntent.putExtra(Constants.SCREEN_TITLE, R.string.daily_newspaper);
                        break;
                    case 5:
                        apartmentServiceIntent.putExtra(Constants.SCREEN_TITLE, R.string.milk_man);
                        break;
                    case 6:
                        apartmentServiceIntent.putExtra(Constants.SCREEN_TITLE, R.string.laundry);
                        break;
                    case 7:
                        apartmentServiceIntent.putExtra(Constants.SCREEN_TITLE, R.string.driver);
                        break;
                    case 8:
                        apartmentServiceIntent.putExtra(Constants.SCREEN_TITLE, R.string.groceries);
                        break;
                }
                context.startActivity(apartmentServiceIntent);
            }

        }
    }

}