package com.kirtanlabs.nammaapartments.nammaapartmentshome.navigationdrawer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.R;

import java.util.List;

import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;

public class HelpAdapter extends ArrayAdapter<String> {

    private final Context mCtx;
    private final List<String> helpList;

    HelpAdapter(Context mCtx, List<String> helpList) {
        super(mCtx, R.layout.layout_notify_digital_gate, helpList);
        this.mCtx = mCtx;
        this.helpList = helpList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_notify_digital_gate, parent, false);
        TextView textNotification = view.findViewById(R.id.textNotification);
        ImageView imageNotificationService = view.findViewById(R.id.imageNotificationService);
        textNotification.setText(helpList.get(position));
        textNotification.setTypeface(setLatoRegularFont(mCtx));
        imageNotificationService.setVisibility(View.GONE);
        return view;
    }
}
