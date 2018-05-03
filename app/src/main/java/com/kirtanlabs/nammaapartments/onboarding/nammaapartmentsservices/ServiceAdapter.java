package com.kirtanlabs.nammaapartments.onboarding.nammaapartmentsservices;

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

public class ServiceAdapter extends ArrayAdapter<Service> {

    //the list values in the List of type society
    private List<Service> servicesList;

    //activity context
    private Context context;

    //the layout resource file for the list items
    private int resource;

    ServiceAdapter(@NonNull Context context, int resource, List<Service> servicesList) {
        super(context, resource);
        this.servicesList = servicesList;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layout inflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(resource, null, false);

        //getting the view elements of the list from the view
        ImageView imageSocietyServices = view.findViewById(R.id.imageServiceIcon);
        TextView textSocietyServices = view.findViewById(R.id.textServiceName);

        //getting the society of the specified position
        Service service = servicesList.get(position);

        //adding values to the list item
        imageSocietyServices.setImageDrawable(context.getResources().getDrawable(service.getServiceImage()));
        textSocietyServices.setText(service.getServiceName());
        return view;
    }

    @Override
    public int getCount() {
        return servicesList.size();
    }

}