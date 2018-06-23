package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.notifydigitalgate;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.invitevisitors.NammaApartmentVisitor;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.util.List;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_HANDED_THINGS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_HANDED_THINGS_DESCRIPTION;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;

public class HandedThingsToVisitorsAdapter extends RecyclerView.Adapter<HandedThingsToVisitorsAdapter.VisitorViewHolder> implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final BaseActivity baseActivity;
    private final List<NammaApartmentVisitor> nammaApartmentVisitorList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    HandedThingsToVisitorsAdapter(List<NammaApartmentVisitor> nammaApartmentVisitorList, Context mCtx) {
        this.mCtx = mCtx;
        baseActivity = (BaseActivity) mCtx;
        this.nammaApartmentVisitorList = nammaApartmentVisitorList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public HandedThingsToVisitorsAdapter.VisitorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_handed_things, parent, false);
        return new HandedThingsToVisitorsAdapter.VisitorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HandedThingsToVisitorsAdapter.VisitorViewHolder holder, int position) {
        //Creating an instance of NammaApartmentVisitor class and retrieving the values from Firebase
        NammaApartmentVisitor nammaApartmentVisitor = nammaApartmentVisitorList.get(position);

        //Since we need inviters name we get the details by inviter UID
        DatabaseReference userPrivateReference = PRIVATE_USERS_REFERENCE.child(nammaApartmentVisitor.getInviterUID());
        userPrivateReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NammaApartmentUser nammaApartmentUser = dataSnapshot.getValue(NammaApartmentUser.class);
                String dateAndTime = nammaApartmentVisitor.getDateAndTimeOfVisit();
                String separatedDateAndTime[] = TextUtils.split(dateAndTime, "\t\t ");
                holder.textVisitorNameValue.setText(nammaApartmentVisitor.getFullName());
                holder.textInvitationDateValue.setText(separatedDateAndTime[0]);
                holder.textInvitationTimeValue.setText(separatedDateAndTime[1]);
                holder.textInvitedByValue.setText(Objects.requireNonNull(nammaApartmentUser).getPersonalDetails().getFullName());
                Glide.with(mCtx.getApplicationContext()).load(nammaApartmentVisitor.getProfilePhoto())
                        .into(holder.profileImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return nammaApartmentVisitorList.size();
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */
    @Override
    public void onClick(View v) {

    }

    class VisitorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textVisitorName;
        private final TextView textVisitorNameValue;
        private final TextView textVisitorType;
        private final TextView textVisitorTypeValue;
        private final TextView textInvitationDate;
        private final TextView textInvitationDateValue;
        private final TextView textInvitationTime;
        private final TextView textInvitationTimeValue;
        private final TextView textInvitedBy;
        private final TextView textInvitedByValue;
        private final TextView textGivenThings;
        private final TextView textDescription;
        private final EditText editDescription;

        private final Button buttonYes;
        private final Button buttonNo;
        private final Button buttonNotifyGate;

        private final de.hdodenhof.circleimageview.CircleImageView profileImage;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        VisitorViewHolder(View itemView) {
            super(itemView);
            textVisitorName = itemView.findViewById(R.id.textVisitorName);
            textVisitorType = itemView.findViewById(R.id.textVisitorType);
            textInvitationDate = itemView.findViewById(R.id.textInvitationDate);
            textInvitationTime = itemView.findViewById(R.id.textInvitationTime);
            textInvitedBy = itemView.findViewById(R.id.textInvitedBy);
            textVisitorNameValue = itemView.findViewById(R.id.textVisitorNameValue);
            textVisitorTypeValue = itemView.findViewById(R.id.textVisitorTypeValue);
            textInvitationDateValue = itemView.findViewById(R.id.textInvitationDateValue);
            textInvitationTimeValue = itemView.findViewById(R.id.textInvitationTimeValue);
            textInvitedByValue = itemView.findViewById(R.id.textInvitedByValue);
            textGivenThings = itemView.findViewById(R.id.textGivenThings);
            textDescription = itemView.findViewById(R.id.textDescription);
            editDescription = itemView.findViewById(R.id.editDescription);
            buttonYes = itemView.findViewById(R.id.buttonYes);
            buttonNo = itemView.findViewById(R.id.buttonNo);
            buttonNotifyGate = itemView.findViewById(R.id.buttonNotifyGate);
            profileImage = itemView.findViewById(R.id.profileImage);

            //Setting Fonts for all the views on cardview
            textVisitorName.setTypeface(setLatoRegularFont(mCtx));
            textVisitorType.setTypeface(setLatoRegularFont(mCtx));
            textInvitationDate.setTypeface(setLatoRegularFont(mCtx));
            textInvitationTime.setTypeface(setLatoRegularFont(mCtx));
            textInvitedBy.setTypeface(setLatoRegularFont(mCtx));
            textGivenThings.setTypeface(setLatoBoldFont(mCtx));
            textDescription.setTypeface(setLatoBoldFont(mCtx));
            editDescription.setTypeface(setLatoRegularFont(mCtx));
            buttonYes.setTypeface(setLatoRegularFont(mCtx));
            buttonNo.setTypeface(setLatoRegularFont(mCtx));
            buttonNotifyGate.setTypeface(setLatoLightFont(mCtx));
            textVisitorNameValue.setTypeface(setLatoBoldFont(mCtx));
            textVisitorTypeValue.setTypeface(setLatoBoldFont(mCtx));
            textInvitationDateValue.setTypeface(setLatoBoldFont(mCtx));
            textInvitationTimeValue.setTypeface(setLatoBoldFont(mCtx));
            textInvitedByValue.setTypeface(setLatoBoldFont(mCtx));

            buttonYes.setOnClickListener(this);
            buttonNo.setOnClickListener(this);
            buttonNotifyGate.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            NammaApartmentVisitor nammaApartmentVisitor = nammaApartmentVisitorList.get(position);
            switch (v.getId()) {
                case R.id.buttonYes:
                    textDescription.setVisibility(View.VISIBLE);
                    editDescription.setVisibility(View.VISIBLE);
                    buttonNotifyGate.setVisibility(View.VISIBLE);
                    editDescription.requestFocus();
                    buttonYes.setBackgroundResource(R.drawable.button_guest_selected);
                    buttonNo.setBackgroundResource(R.drawable.button_guest_not_selected);
                    buttonYes.setTextColor(Color.WHITE);
                    buttonNo.setTextColor(Color.BLACK);
                    break;

                case R.id.buttonNo:
                    textDescription.setVisibility(View.GONE);
                    editDescription.setVisibility(View.GONE);
                    buttonNotifyGate.setVisibility(View.GONE);
                    buttonYes.setBackgroundResource(R.drawable.button_guest_not_selected);
                    buttonNo.setBackgroundResource(R.drawable.button_guest_selected);
                    buttonYes.setTextColor(Color.BLACK);
                    buttonNo.setTextColor(Color.WHITE);
                    break;

                case R.id.buttonNotifyGate:
                    String handedThingsDescription = editDescription.getText().toString();
                    nammaApartmentVisitor.setHandedThingsDescription(handedThingsDescription);
                    DatabaseReference preApprovedVisitorReference = Constants.PREAPPROVED_VISITORS_REFERENCE
                            .child(nammaApartmentVisitor.getUid());
                    preApprovedVisitorReference.child(FIREBASE_CHILD_HANDED_THINGS).child(FIREBASE_CHILD_HANDED_THINGS_DESCRIPTION).setValue(handedThingsDescription);
                    baseActivity.showSuccessDialog(mCtx.getResources().getString(R.string.handed_things_success_title),
                            mCtx.getResources().getString(R.string.handed_things_success_message),
                            null);
                    break;
            }
        }
    }

}
