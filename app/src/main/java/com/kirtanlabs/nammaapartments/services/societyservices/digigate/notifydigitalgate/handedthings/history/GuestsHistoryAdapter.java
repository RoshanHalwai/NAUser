package com.kirtanlabs.nammaapartments.services.societyservices.digigate.notifydigitalgate.handedthings.history;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.invitevisitors.NammaApartmentGuest;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.util.List;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class GuestsHistoryAdapter extends RecyclerView.Adapter<GuestsHistoryAdapter.VisitorViewHolder> implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final List<NammaApartmentGuest> nammaApartmentGuestList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    GuestsHistoryAdapter(List<NammaApartmentGuest> nammaApartmentGuestList, Context mCtx) {
        this.mCtx = mCtx;
        this.nammaApartmentGuestList = nammaApartmentGuestList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public GuestsHistoryAdapter.VisitorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_guests_history, parent, false);
        return new GuestsHistoryAdapter.VisitorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestsHistoryAdapter.VisitorViewHolder holder, int position) {
        //Creating an instance of NammaApartmentGuest class and retrieving the values from Firebase
        NammaApartmentGuest nammaApartmentGuest = nammaApartmentGuestList.get(position);

        String dateAndTime = nammaApartmentGuest.getDateAndTimeOfVisit();
        String separatedDateAndTime[] = TextUtils.split(dateAndTime, "\t\t ");
        holder.textVisitorNameValue.setText(nammaApartmentGuest.getFullName());
        holder.textInvitationDateValue.setText(separatedDateAndTime[0]);
        holder.textInvitationTimeValue.setText(separatedDateAndTime[1]);
        holder.textHandedThingsValue.setText(nammaApartmentGuest.getHandedThings());
        Glide.with(mCtx.getApplicationContext()).load(nammaApartmentGuest.getProfilePhoto())
                .into(holder.profileImage);

        /*We check if the inviters UID is equal to current UID if it is then we don't have to check in
        firebase since we now know that the inviter is current user.*/
        if (nammaApartmentGuest.getInviterUID().equals(NammaApartmentsGlobal.userUID)) {
            holder.textInvitedByValue.setText(
                    ((NammaApartmentsGlobal) mCtx.getApplicationContext())
                            .getNammaApartmentUser()
                            .getPersonalDetails()
                            .getFullName());
        } else {
            /*Visitor has been invited by some other family member; We check in firebase and get the name
             * of that family member*/
            DatabaseReference userPrivateReference = PRIVATE_USERS_REFERENCE.child(nammaApartmentGuest.getInviterUID());
            userPrivateReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    NammaApartmentUser nammaApartmentUser = dataSnapshot.getValue(NammaApartmentUser.class);
                    holder.textInvitedByValue.setText(Objects.requireNonNull(nammaApartmentUser).getPersonalDetails().getFullName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return nammaApartmentGuestList.size();
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */
    @Override
    public void onClick(View v) {

    }

    class VisitorViewHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textVisitorName;
        private final TextView textVisitorNameValue;
        private final TextView textHandedThings;
        private final TextView textHandedThingsValue;
        private final TextView textInvitationDate;
        private final TextView textInvitationDateValue;
        private final TextView textInvitationTime;
        private final TextView textInvitationTimeValue;
        private final TextView textInvitedBy;
        private final TextView textInvitedByValue;

        private final de.hdodenhof.circleimageview.CircleImageView profileImage;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        VisitorViewHolder(View itemView) {
            super(itemView);
            textVisitorName = itemView.findViewById(R.id.textVisitorName);
            textHandedThings = itemView.findViewById(R.id.textHandedThings);
            textInvitationDate = itemView.findViewById(R.id.textInvitationDate);
            textInvitationTime = itemView.findViewById(R.id.textInvitationTime);
            textInvitedBy = itemView.findViewById(R.id.textInvitedBy);
            textVisitorNameValue = itemView.findViewById(R.id.textVisitorNameValue);
            textHandedThingsValue = itemView.findViewById(R.id.textHandedThingsValue);
            textInvitationDateValue = itemView.findViewById(R.id.textInvitationDateValue);
            textInvitationTimeValue = itemView.findViewById(R.id.textInvitationTimeValue);
            textInvitedByValue = itemView.findViewById(R.id.textInvitedByValue);
            profileImage = itemView.findViewById(R.id.profileImage);

            //Setting Fonts for all the views on cardview
            textVisitorName.setTypeface(setLatoRegularFont(mCtx));
            textHandedThings.setTypeface(setLatoRegularFont(mCtx));
            textInvitationDate.setTypeface(setLatoRegularFont(mCtx));
            textInvitationTime.setTypeface(setLatoRegularFont(mCtx));
            textInvitedBy.setTypeface(setLatoRegularFont(mCtx));
            textVisitorNameValue.setTypeface(setLatoBoldFont(mCtx));
            textHandedThingsValue.setTypeface(setLatoBoldFont(mCtx));
            textInvitationDateValue.setTypeface(setLatoBoldFont(mCtx));
            textInvitationTimeValue.setTypeface(setLatoBoldFont(mCtx));
            textInvitedByValue.setTypeface(setLatoBoldFont(mCtx));
        }
    }

}
