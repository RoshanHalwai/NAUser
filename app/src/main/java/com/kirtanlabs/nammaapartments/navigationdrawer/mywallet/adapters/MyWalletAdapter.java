package com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.utilities.Constants;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 8/23/2018
 * This class has been created to display the contents of the Grid View that is included in the 'My Wallet' home screen
 */

public class MyWalletAdapter extends BaseAdapter {

    public final Context context;
    private String[] stringTitle;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    public MyWalletAdapter(Context context, String[] stringTitle) {
        this.context = context;
        this.stringTitle = stringTitle;
    }


    @Override
    public int getCount() {
        return stringTitle.length;
    }

    @Override
    public Object getItem(int i) {
        return stringTitle[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View gridView = view;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (layoutInflater != null) {
                gridView = layoutInflater.inflate(R.layout.grid_layout_my_wallet, viewGroup, false);
            }
        }

        /*Getting Id's for all the views*/
        TextView textTitleGridLayout = gridView.findViewById(R.id.textTitleGridLayout);

        /*Setting font for the TextView*/
        textTitleGridLayout.setTypeface(Constants.setLatoRegularFont(context));

        /*Setting values for all the views*/
        textTitleGridLayout.setText(stringTitle[i]);
        return gridView;
    }
}
