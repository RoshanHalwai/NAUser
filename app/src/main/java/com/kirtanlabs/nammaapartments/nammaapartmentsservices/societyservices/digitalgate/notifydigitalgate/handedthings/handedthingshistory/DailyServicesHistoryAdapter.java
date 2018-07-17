package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.notifydigitalgate.handedthings.handedthingshistory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.mydailyservices.NammaApartmentDailyService;

import java.util.List;

import static com.kirtanlabs.nammaapartments.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;

public class DailyServicesHistoryAdapter extends RecyclerView.Adapter<DailyServicesHistoryAdapter.DailyServiceViewHolder> implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final List<NammaApartmentDailyService> nammaApartmentDailyServiceList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    DailyServicesHistoryAdapter(List<NammaApartmentDailyService> nammaApartmentDailyServiceList, Context mCtx) {
        this.mCtx = mCtx;
        this.nammaApartmentDailyServiceList = nammaApartmentDailyServiceList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public DailyServicesHistoryAdapter.DailyServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_daily_services_history, parent, false);
        return new DailyServicesHistoryAdapter.DailyServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyServicesHistoryAdapter.DailyServiceViewHolder holder, int position) {
        //Creating an instance of NammaApartmentGuest class and retrieving the values from Firebase
        NammaApartmentDailyService nammaApartmentDailyService = nammaApartmentDailyServiceList.get(position);
        holder.textInvitationTimeValue.setText(nammaApartmentDailyService.getTimeOfVisit());
        holder.textDailyServiceNameValue.setText(nammaApartmentDailyService.getFullName());
        holder.textDailyServiceTypeValue.setText(nammaApartmentDailyService.getDailyServiceType());
        holder.textHandedThingsValue.setText((nammaApartmentDailyService.getDailyServiceHandedThingsDescription()));
        holder.textInvitationDateValue.setText(nammaApartmentDailyService.getDateOfVisit());
        Glide.with(mCtx.getApplicationContext()).load(nammaApartmentDailyService.getProfilePhoto())
                .into(holder.profileImage);
    }

    @Override
    public int getItemCount() {
        return nammaApartmentDailyServiceList.size();
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */
    @Override
    public void onClick(View v) {

    }

    class DailyServiceViewHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textDailyServiceName;
        private final TextView textDailyServiceType;
        private final TextView textHandedThings;
        private final TextView textInvitationTime;
        private final TextView textInvitationDate;
        private final TextView textDailyServiceNameValue;
        private final TextView textDailyServiceTypeValue;
        private final TextView textHandedThingsValue;
        private final TextView textInvitationTimeValue;
        private final TextView textInvitationDateValue;
        private final de.hdodenhof.circleimageview.CircleImageView profileImage;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        DailyServiceViewHolder(View itemView) {
            super(itemView);
            textDailyServiceName = itemView.findViewById(R.id.textDailyServiceName);
            textDailyServiceType = itemView.findViewById(R.id.textDailyServiceType);
            textHandedThings = itemView.findViewById(R.id.textHandedThings);
            textInvitationTime = itemView.findViewById(R.id.textInvitationTime);
            textInvitationDate = itemView.findViewById(R.id.textInvitationDate);
            textDailyServiceNameValue = itemView.findViewById(R.id.textDailyServiceNameValue);
            textDailyServiceTypeValue = itemView.findViewById(R.id.textDailyServiceTypeValue);
            textHandedThingsValue = itemView.findViewById(R.id.textHandedThingsValue);
            textInvitationTimeValue = itemView.findViewById(R.id.textInvitationTimeValue);
            textInvitationDateValue = itemView.findViewById(R.id.textInvitationDateValue);
            profileImage = itemView.findViewById(R.id.profileImage);

            //Setting Fonts for all the views on cardview
            textDailyServiceName.setTypeface(setLatoRegularFont(mCtx));
            textDailyServiceType.setTypeface(setLatoRegularFont(mCtx));
            textHandedThings.setTypeface(setLatoRegularFont(mCtx));
            textInvitationTime.setTypeface(setLatoRegularFont(mCtx));
            textInvitationDate.setTypeface(setLatoRegularFont(mCtx));
            textDailyServiceNameValue.setTypeface(setLatoBoldFont(mCtx));
            textDailyServiceTypeValue.setTypeface(setLatoBoldFont(mCtx));
            textHandedThingsValue.setTypeface(setLatoBoldFont(mCtx));
            textInvitationTimeValue.setTypeface(setLatoBoldFont(mCtx));
            textInvitationDateValue.setTypeface(setLatoBoldFont(mCtx));
        }
    }

}
