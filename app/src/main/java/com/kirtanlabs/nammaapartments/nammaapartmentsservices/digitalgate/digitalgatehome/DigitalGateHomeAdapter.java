package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.digitalgatehome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.R;

class DigitalGateHomeAdapter extends BaseAdapter {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final int[] icons;
    private final String[] stringDigitalServices;
    private final Context context;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    DigitalGateHomeAdapter(Context context, int[] icons, String[] stringDigitalServices) {
        this.context = context;
        this.icons = icons;
        this.stringDigitalServices = stringDigitalServices;
    }

    /* ------------------------------------------------------------- *
     * Overriding BaseAdapter Objects
     * ------------------------------------------------------------- */

    @Override
    public int getCount() {
        return stringDigitalServices.length;
    }

    @Override
    public Object getItem(int position) {
        return stringDigitalServices[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView = convertView;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (layoutInflater != null) {
                gridView = layoutInflater.inflate(R.layout.grid_layout_digital_gate, parent, false);
            }
        }

        /*Getting Id's for all the views*/
        ImageView imageGridDigitalGateServices = gridView.findViewById(R.id.imageDigitalGateGridLayout);
        TextView textGridDigitalGateServices = gridView.findViewById(R.id.textDigitalGateGridLayout);

        /*Setting values for all the views*/
        imageGridDigitalGateServices.setImageResource(icons[position]);
        textGridDigitalGateServices.setText(stringDigitalServices[position]);
        return gridView;
    }

}
