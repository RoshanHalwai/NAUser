package com.kirtanlabs.nammaapartments.navigationdrawer.noticeboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.utilities.Constants;

public class NoticeBoardAdapter extends BaseAdapter {

    private final Context context;
    String[] stringTitle, stringMessage, stringInCharge;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    NoticeBoardAdapter(Context context, String[] stringTitle, String[] stringMessage, String[] stringInCharge) {
        this.context = context;
        this.stringTitle = stringTitle;
        this.stringMessage = stringMessage;
        this.stringInCharge = stringInCharge;
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
                gridView = layoutInflater.inflate(R.layout.grid_layout_notice_board, viewGroup, false);
            }
        }
        /*Getting Id's for all the views*/
        TextView textTitleGridLayout = gridView.findViewById(R.id.textTitleGridLayout);
        TextView textMessageGridLayout = gridView.findViewById(R.id.textMessageGridLayout);
        TextView textInChargeGridLayout = gridView.findViewById(R.id.textInChargeGridLayout);

        /*Setting font for the TextView*/
        textTitleGridLayout.setTypeface(Constants.setLatoRegularFont(context));
        textMessageGridLayout.setTypeface(Constants.setLatoRegularFont(context));
        textInChargeGridLayout.setTypeface(Constants.setLatoRegularFont(context));

        /*Setting values for all the views*/
        textTitleGridLayout.setText(stringTitle[i]);
        textMessageGridLayout.setText(stringMessage[i]);
        textInChargeGridLayout.setText(stringInCharge[i]);
        return gridView;
    }
}
