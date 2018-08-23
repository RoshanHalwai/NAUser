package com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.adapters.MyWalletAdapter;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 8/23/2018
 * This class has been created to implement the home screen of 'My Wallet'
 */

public class MyWalletActivity extends BaseActivity implements AdapterView.OnClickListener, AdapterView.OnItemClickListener {

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.layout_my_wallet;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.my_wallet;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Getting for grid view*/
        GridView gridViewPayForItemList = findViewById(R.id.gridViewItemList);

        /* Setting the imageAdapter*/
        gridViewPayForItemList.setAdapter(getAdapter());

        /*Setting click of each item of grid view*/
        gridViewPayForItemList.setOnItemClickListener(this);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            //TODO: Implementation of all items in the grid view
            case 0:
                break;
            case 1:
                break;
        }
    }

    public MyWalletAdapter getAdapter() {
        String[] stringTitle = {
                getString(R.string.maintenance),
                getString(R.string.my_apartment_services),
                getString(R.string.parties_get_togethers),
                getString(R.string.festivals),
                getString(R.string.donations),
                getString(R.string.extras)
        };

        return new MyWalletAdapter(this, stringTitle);

    }

}
