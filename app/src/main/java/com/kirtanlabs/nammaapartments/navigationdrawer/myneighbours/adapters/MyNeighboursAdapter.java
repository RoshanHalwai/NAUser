package com.kirtanlabs.nammaapartments.navigationdrawer.myneighbours.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.myneighbours.activities.SendMessageActivity;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kirtanlabs.nammaapartments.utilities.Constants.NEIGHBOUR_APARTMENT_NAME;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NEIGHBOUR_FLAT_NUMBER;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NEIGHBOUR_UID;
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
    private final List<NammaApartmentUser> neighbourDataList;
    private String recentMessageSenderUID;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public MyNeighboursAdapter(Context mCtx, List<NammaApartmentUser> neighbourDataList, String recentMessageSenderUID) {
        this.mCtx = mCtx;
        this.neighbourDataList = neighbourDataList;
        this.recentMessageSenderUID = recentMessageSenderUID;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public MyNeighbourHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*Inflating and returning our view holder*/
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_my_neighbours, parent, false);
        return new MyNeighbourHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyNeighbourHolder holder, int position) {
        /*Creating an instance of NammaApartmentsUsers class and retrieving the values from Firebase*/
        NammaApartmentUser nammaApartmentUser = neighbourDataList.get(position);
        holder.textNeighbourNameValue.setText(nammaApartmentUser.getPersonalDetails().getFullName());
        holder.textApartmentNameValue.setText(nammaApartmentUser.getFlatDetails().getApartmentName());
        holder.textFlatNumberValue.setText(nammaApartmentUser.getFlatDetails().getFlatNumber());
        Glide.with(mCtx.getApplicationContext()).load(nammaApartmentUser.getPersonalDetails().getProfilePhoto())
                .into(holder.neighbourProfileImage);

        if (recentMessageSenderUID != null && recentMessageSenderUID.equals(nammaApartmentUser.getUID())) {
            /*Displaying badge icon notification in the layout */
            holder.badge_notification_icon.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return neighbourDataList.size();
    }

    /* ------------------------------------------------------------- *
     * My Neighbour View Holder class
     * ------------------------------------------------------------- */

    class MyNeighbourHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textNeighbourName;
        private final TextView textApartmentName;
        private final TextView textFlatNumber;
        private final TextView textNeighbourNameValue;
        private final TextView textApartmentNameValue;
        private final TextView textFlatNumberValue;
        private final TextView badge_notification_icon;
        private final CircleImageView neighbourProfileImage;
        private final CardView cardNeighbourDetails;

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
            badge_notification_icon = itemView.findViewById(R.id.badge_notification_icon);
            neighbourProfileImage = itemView.findViewById(R.id.neighbourProfileImage);
            cardNeighbourDetails = itemView.findViewById(R.id.cardNeighbourDetails);

            /*Setting Fonts for all the views on card view*/
            textNeighbourName.setTypeface(setLatoRegularFont(mCtx));
            textApartmentName.setTypeface(setLatoRegularFont(mCtx));
            textFlatNumber.setTypeface(setLatoRegularFont(mCtx));
            textNeighbourNameValue.setTypeface(setLatoBoldFont(mCtx));
            textFlatNumberValue.setTypeface(setLatoBoldFont(mCtx));
            textApartmentNameValue.setTypeface(setLatoBoldFont(mCtx));

            /*Setting click listeners on the view */
            cardNeighbourDetails.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            NammaApartmentUser nammaApartmentUser = neighbourDataList.get(position);
            if (v.getId() == R.id.cardNeighbourDetails) {
                Intent intentChatNeighbour = new Intent(mCtx, SendMessageActivity.class);
                intentChatNeighbour.putExtra(NEIGHBOUR_UID, nammaApartmentUser.getUID());
                intentChatNeighbour.putExtra(NEIGHBOUR_APARTMENT_NAME, nammaApartmentUser.getFlatDetails().getApartmentName());
                intentChatNeighbour.putExtra(NEIGHBOUR_FLAT_NUMBER, nammaApartmentUser.getFlatDetails().getFlatNumber());
                mCtx.startActivity(intentChatNeighbour);

                if (recentMessageSenderUID != null) {
                    /*Removing Badge icon once user click on the recent message sender card view*/
                    badge_notification_icon.setVisibility(View.GONE);
                    recentMessageSenderUID = "";
                }
            }
        }
    }
}
