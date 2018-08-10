package com.kirtanlabs.nammaapartments.home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.home.activities.ApartmentServices;
import com.kirtanlabs.nammaapartments.home.adapters.NammaApartmentServiceAdapter;
import com.kirtanlabs.nammaapartments.home.pojo.NammaApartmentService;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApartmentServicesHome extends Fragment implements AdapterView.OnItemClickListener {

    /* ------------------------------------------------------------- *
     * Overriding Fragment Objects
     * ------------------------------------------------------------- */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_namma_apartments_services, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*Getting Id for listView*/
        ListView listView = view.findViewById(R.id.listviewApartmentServices);

        /*Attaching adapter to the listview*/
        listView.setAdapter(getAdapter());

        /*Setting event for list view items*/
        listView.setOnItemClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnItemClickListener
     * ------------------------------------------------------------- */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                Intent intentCook = new Intent(getActivity(), ApartmentServices.class);
                intentCook.putExtra(Constants.SCREEN_TITLE, R.string.cook);
                startActivity(intentCook);
                break;
            case 1:
                Intent intentMaid = new Intent(getActivity(), ApartmentServices.class);
                intentMaid.putExtra(Constants.SCREEN_TITLE, R.string.maid);
                startActivity(intentMaid);
                break;
            case 2:
                Intent intentCarBikeCleaning = new Intent(getActivity(), ApartmentServices.class);
                intentCarBikeCleaning.putExtra(Constants.SCREEN_TITLE, R.string.car_bike_cleaning);
                startActivity(intentCarBikeCleaning);
                break;
            case 3:
                Intent intentChildDayCare = new Intent(getActivity(), ApartmentServices.class);
                intentChildDayCare.putExtra(Constants.SCREEN_TITLE, R.string.child_day_care);
                startActivity(intentChildDayCare);
                break;
            case 4:
                Intent intentDailyNewspaper = new Intent(getActivity(), ApartmentServices.class);
                intentDailyNewspaper.putExtra(Constants.SCREEN_TITLE, R.string.daily_newspaper);
                startActivity(intentDailyNewspaper);
                break;
            case 5:
                Intent intentMilkMan = new Intent(getActivity(), ApartmentServices.class);
                intentMilkMan.putExtra(Constants.SCREEN_TITLE, R.string.milk_man);
                startActivity(intentMilkMan);
                break;
            case 6:
                Intent intentLaundry = new Intent(getActivity(), ApartmentServices.class);
                intentLaundry.putExtra(Constants.SCREEN_TITLE, R.string.laundry);
                startActivity(intentLaundry);
                break;
            case 7:
                Intent intentDriver = new Intent(getActivity(), ApartmentServices.class);
                intentDriver.putExtra(Constants.SCREEN_TITLE, R.string.driver);
                startActivity(intentDriver);
                break;
            case 8:
                Intent intentGrocery = new Intent(getActivity(), ApartmentServices.class);
                intentGrocery.putExtra(Constants.SCREEN_TITLE, R.string.groceries);
                startActivity(intentGrocery);
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private NammaApartmentServiceAdapter getAdapter() {
        List<NammaApartmentService> apartmentServicesList = new ArrayList<>();

        /*Adding apartment services to the list*/
        apartmentServicesList.add(new NammaApartmentService(R.drawable.cook_service, getString(R.string.cook)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.maid, getString(R.string.maid)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.car_cleaning, getString(R.string.car_bike_cleaning)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.child_care, getString(R.string.child_day_care)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.newspaper, getString(R.string.daily_newspaper)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.milk, getString(R.string.milk_man)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.laundry_service, getString(R.string.laundry)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.driving, getString(R.string.driver)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.groceries, getString(R.string.groceries)));

        return new NammaApartmentServiceAdapter(Objects.requireNonNull(getActivity()), apartmentServicesList);
    }
}

