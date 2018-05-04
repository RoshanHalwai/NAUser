package com.kirtanlabs.nammaapartments.onboarding.nammaapartmentsservices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.R;

public class ImageAdapterDigitalGateServices extends BaseAdapter {

    private int[] icons;
    private String[] stringDigitalServices;
    private Context context;

    public ImageAdapterDigitalGateServices(Context context, int[] icons, String[] stringDigitalServices) {
        this.context = context;
        this.icons = icons;
        this.stringDigitalServices = stringDigitalServices;
    }

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
            gridView = layoutInflater.inflate(R.layout.grid_layout_digital_gate, null);
        }

        /*Getting Id's for all the views*/
        ImageView imageGridDigitalGateServices = gridView.findViewById(R.id.imageDigitalGateGridLayout);
        TextView textGridDigitalGateServices = gridView.findViewById(R.id.textDigitalGateGridLayout);

        imageGridDigitalGateServices.setImageResource(icons[position]);
        textGridDigitalGateServices.setText(stringDigitalServices[position]);
        return gridView;
    }
}
