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
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.net.URLEncoder;

import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class ApartmentServiceAdapter extends RecyclerView.Adapter<ApartmentServiceAdapter.ApartmentServiceViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final BaseActivity baseActivity;

    public ApartmentServiceAdapter(Context mCtx) {
        this.mCtx = mCtx;
        baseActivity = (BaseActivity) mCtx;
    }

    @NonNull
    @Override
    public ApartmentServiceAdapter.ApartmentServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*inflating and returning our view holder*/
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_apartment_services, parent, false);
        return new ApartmentServiceAdapter.ApartmentServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApartmentServiceAdapter.ApartmentServiceViewHolder holder, int position) {
        //TODO: Data is hardcoded for now. Will be replaced by actual data once it's available.
        holder.textApartmentServiceNameValue.setText("Ashish Jha");
        holder.textApartmentServiceRatingValue.setText("3");
        holder.textApartmentServiceTimeSlotValue.setText("11 AM");
        holder.textApartmentServiceNoOfFlatsSlotValue.setText("5");
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked when user presses on the 'WhatsApp' icon in Card View. User will then be able
     * to contact the Apartment Service via WhatsApp
     */
    private void redirectUserToWhatsApp() {
        PackageManager pm = mCtx.getPackageManager();
        Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
        String message = mCtx.getString(R.string.whatsapp_msg);

        try {
            //TODO: Mobile Number has been hardcoded. Will be replaced later.
            String url = "https://api.whatsapp.com/send?phone=" + "+919885665744" + "&text=" + URLEncoder.encode(message, "UTF-8");
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

            /*Setting font for all the views*/
            textApartmentServiceName.setTypeface(setLatoBoldFont(mCtx));
            textApartmentServiceRating.setTypeface(setLatoBoldFont(mCtx));
            textApartmentServiceTimeSlot.setTypeface(setLatoBoldFont(mCtx));
            textApartmentServiceNoOfFlats.setTypeface(setLatoBoldFont(mCtx));
            textApartmentServiceNameValue.setTypeface(setLatoRegularFont(mCtx));
            textApartmentServiceRatingValue.setTypeface(setLatoRegularFont(mCtx));
            textApartmentServiceTimeSlotValue.setTypeface(setLatoRegularFont(mCtx));
            textApartmentServiceNoOfFlatsSlotValue.setTypeface(setLatoRegularFont(mCtx));
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
            switch (v.getId()) {
                //TODO: Mobile Number has been hardcoded. Will be replaced later.
                case R.id.textCall:
                    baseActivity.makePhoneCall("9986553474");
                    break;
                case R.id.textMessage:
                    baseActivity.sendTextMessage("9986553474", mCtx.getString(R.string.message_body));
                    break;
                case R.id.textWhatsapp:
                    redirectUserToWhatsApp();
                    break;
                case R.id.textRefer:
                    redirectUserToShareWith();
                    break;
            }
        }
    }
}
