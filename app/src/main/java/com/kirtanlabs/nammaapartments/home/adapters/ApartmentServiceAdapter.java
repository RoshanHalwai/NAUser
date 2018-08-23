package com.kirtanlabs.nammaapartments.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.services.apartmentservices.activities.ApartmentServices;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.mydailyservices.NammaApartmentDailyService;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.net.URLEncoder;
import java.util.List;

import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class ApartmentServiceAdapter extends RecyclerView.Adapter<ApartmentServiceAdapter.ApartmentServiceViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final BaseActivity baseActivity;
    private final List<NammaApartmentDailyService> nammaApartmentDailyServiceList;

    public ApartmentServiceAdapter(Context mCtx, List<NammaApartmentDailyService> nammaApartmentDailyServiceList) {
        this.mCtx = mCtx;
        baseActivity = (BaseActivity) mCtx;
        this.nammaApartmentDailyServiceList = nammaApartmentDailyServiceList;
    }

    @NonNull
    @Override
    public ApartmentServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*inflating and returning our view holder*/
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_apartment_services, parent, false);
        return new ApartmentServiceAdapter.ApartmentServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApartmentServiceAdapter.ApartmentServiceViewHolder holder, int position) {
        /*Creating an instance of NammaApartmentDailyService class and retrieving the values from Firebase.*/
        NammaApartmentDailyService nammaApartmentDailyService = nammaApartmentDailyServiceList.get(position);
        holder.textApartmentServiceNameValue.setText(nammaApartmentDailyService.getFullName());
        holder.textApartmentServiceRatingValue.setText(String.valueOf(nammaApartmentDailyService.getRating()));
        /*TODO:Rethink About the Time Slot Value In CardView*/
        holder.textApartmentServiceTimeSlotValue.setText(nammaApartmentDailyService.getTimeOfVisit());
        holder.textApartmentServiceNoOfFlatsSlotValue.setText(String.valueOf(ApartmentServices.numberOfFlats.get(nammaApartmentDailyService.getUID())));
        Glide.with(mCtx.getApplicationContext()).load(nammaApartmentDailyService.getProfilePhoto())
                .into(holder.visitorOrDailyServiceProfilePic);
    }

    @Override
    public int getItemCount() {
        return nammaApartmentDailyServiceList.size();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked when user presses on the 'WhatsApp' icon in Card View. User will then be able
     * to contact the Apartment Service via WhatsApp
     */
    private void redirectUserToWhatsApp(int position) {
        PackageManager pm = mCtx.getPackageManager();
        Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
        String message = mCtx.getString(R.string.whatsapp_msg);
        NammaApartmentDailyService nammaApartmentDailyService = nammaApartmentDailyServiceList.get(position);
        String serviceMobileNumber = nammaApartmentDailyService.getPhoneNumber();
        try {
            String url = "https://api.whatsapp.com/send?phone=" + "+91" + serviceMobileNumber + "&text=" + URLEncoder.encode(message, "UTF-8");
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.setData(Uri.parse(url));
            if (whatsappIntent.resolveActivity(pm) != null) {
                mCtx.startActivity(whatsappIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is invoked when user presses on the 'Refer' icon in Card View. User will then be able
     * to share the Apartment Service with his Contacts
     */
    private void redirectUserToShareWith() {
        Intent referIntent = new Intent(android.content.Intent.ACTION_SEND);
        referIntent.setType("text/plain");
        mCtx.startActivity(Intent.createChooser(referIntent, mCtx.getString(R.string.refer_title)));
    }

    /* ------------------------------------------------------------- *
     * Apartment Service Holder class
     * ------------------------------------------------------------- */

    class ApartmentServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textApartmentServiceName;
        private final TextView textApartmentServiceNameValue;
        private final TextView textApartmentServiceRating;
        private final TextView textApartmentServiceRatingValue;
        private final TextView textApartmentServiceTimeSlot;
        private final TextView textApartmentServiceTimeSlotValue;
        private final TextView textApartmentServiceNoOfFlats;
        private final TextView textApartmentServiceNoOfFlatsSlotValue;
        private final TextView textCall;
        private final TextView textMessage;
        private final TextView textWhatsapp;
        private final TextView textRefer;
        private final ImageView visitorOrDailyServiceProfilePic;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        ApartmentServiceViewHolder(View itemView) {
            super(itemView);
            /*Getting Id's for all the views*/
            textApartmentServiceName = itemView.findViewById(R.id.textApartmentServiceName);
            textApartmentServiceRating = itemView.findViewById(R.id.textApartmentServiceRating);
            textApartmentServiceTimeSlot = itemView.findViewById(R.id.textApartmentServiceTimeSlot);
            textApartmentServiceNoOfFlats = itemView.findViewById(R.id.textApartmentServiceFlats);
            textApartmentServiceNameValue = itemView.findViewById(R.id.textApartmentServiceNameValue);
            textApartmentServiceRatingValue = itemView.findViewById(R.id.textApartmentServiceRatingValue);
            textApartmentServiceTimeSlotValue = itemView.findViewById(R.id.textApartmentServiceTimeSlotValue);
            textApartmentServiceNoOfFlatsSlotValue = itemView.findViewById(R.id.textApartmentServiceFlatsValue);
            textCall = itemView.findViewById(R.id.textCall);
            textMessage = itemView.findViewById(R.id.textMessage);
            textWhatsapp = itemView.findViewById(R.id.textWhatsapp);
            textRefer = itemView.findViewById(R.id.textRefer);
            visitorOrDailyServiceProfilePic = itemView.findViewById(R.id.visitorOrDailyServiceProfilePic);

            /*Setting font for all the views*/
            textApartmentServiceName.setTypeface(setLatoRegularFont(mCtx));
            textApartmentServiceRating.setTypeface(setLatoRegularFont(mCtx));
            textApartmentServiceTimeSlot.setTypeface(setLatoRegularFont(mCtx));
            textApartmentServiceNoOfFlats.setTypeface(setLatoRegularFont(mCtx));
            textApartmentServiceNameValue.setTypeface(setLatoBoldFont(mCtx));
            textApartmentServiceRatingValue.setTypeface(setLatoBoldFont(mCtx));
            textApartmentServiceTimeSlotValue.setTypeface(setLatoBoldFont(mCtx));
            textApartmentServiceNoOfFlatsSlotValue.setTypeface(setLatoBoldFont(mCtx));
            textCall.setTypeface(Constants.setLatoBoldItalicFont(mCtx));
            textMessage.setTypeface(Constants.setLatoBoldItalicFont(mCtx));
            textWhatsapp.setTypeface(Constants.setLatoBoldItalicFont(mCtx));
            textRefer.setTypeface(Constants.setLatoBoldItalicFont(mCtx));

            /*Setting onClick Listeners*/
            textCall.setOnClickListener(this);
            textMessage.setOnClickListener(this);
            textWhatsapp.setOnClickListener(this);
            textRefer.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            NammaApartmentDailyService nammaApartmentDailyService = nammaApartmentDailyServiceList.get(position);
            switch (v.getId()) {
                case R.id.textCall:
                    baseActivity.makePhoneCall(nammaApartmentDailyService.getPhoneNumber());
                    break;
                case R.id.textMessage:
                    baseActivity.sendTextMessage(nammaApartmentDailyService.getPhoneNumber(), mCtx.getString(R.string.message_body));
                    break;
                case R.id.textWhatsapp:
                    redirectUserToWhatsApp(position);
                    break;
                case R.id.textRefer:
                    redirectUserToShareWith();
                    break;
            }
        }
    }
}
