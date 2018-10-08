package com.kirtanlabs.nammaapartments.navigationdrawer.myNeighbours.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.R;

import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Shivam Lohani on 10/8/2018
 */
public class MyNeighboursAdapter extends RecyclerView.Adapter<MyNeighboursAdapter.MyNeighbourHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public MyNeighboursAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public MyNeighbourHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*inflating and returning our view holder*/
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_my_neighbours, parent, false);
        return new MyNeighbourHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyNeighbourHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        //TODO: to change the count here
        return 4;
    }

    /* ------------------------------------------------------------- *
     * My Neighbour View Holder class
     * ------------------------------------------------------------- */

    class MyNeighbourHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textNeighbourName;
        private final TextView textApartmentName;
        private final TextView textFlatNumber;
        private final TextView textNeighbourNameValue;
        private final TextView textApartmentNameValue;
        private final TextView textFlatNumberValue;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        MyNeighbourHolder(View itemView) {
            super(itemView);

            /*Getting Id's for all the views on card view*/
            textNeighbourName = itemView.findViewById(R.id.textNeighbourName);
            textApartmentName = itemView.findViewById(R.id.textApartmentName);
            textFlatNumber = itemView.findViewById(R.id.textFlatNumber);
            textNeighbourNameValue = itemView.findViewById(R.id.textNeighbourNameValue);
            textApartmentNameValue = itemView.findViewById(R.id.textApartmentNameValue);
            textFlatNumberValue = itemView.findViewById(R.id.textFlatNumberValue);

            /*Setting Fonts for all the views on card view*/
            textNeighbourName.setTypeface(setLatoRegularFont(mCtx));
            textApartmentName.setTypeface(setLatoRegularFont(mCtx));
            textFlatNumber.setTypeface(setLatoRegularFont(mCtx));
            textNeighbourNameValue.setTypeface(setLatoBoldFont(mCtx));
            textFlatNumberValue.setTypeface(setLatoBoldFont(mCtx));
            textApartmentNameValue.setTypeface(setLatoBoldFont(mCtx));
        }
    }
}
