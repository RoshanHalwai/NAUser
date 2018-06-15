package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mysweethome;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentUser;
import com.kirtanlabs.nammaapartments.R;

import java.util.List;

public class MySweetHomeAdapter extends RecyclerView.Adapter<MySweetHomeAdapter.MySweetHomeHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */
    private final Context mCtx;
    private final BaseActivity baseActivity;
    private List<NammaApartmentUser> nammaApartmentFamilyMembersList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    MySweetHomeAdapter(List<NammaApartmentUser> nammaApartmentFamilyMembers, Context mCtx) {
        this.mCtx = mCtx;
        baseActivity = (BaseActivity) mCtx;
        this.nammaApartmentFamilyMembersList = nammaApartmentFamilyMembers;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView Adapter object
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public MySweetHomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_visitors_and_my_daily_services_list, parent, false);
        return new MySweetHomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MySweetHomeHolder holder, int position) {
        String stringMemberName = mCtx.getResources().getString(R.string.name) + ":";
        String stringMemberRelation = mCtx.getResources().getString(R.string.relation) + ":";

        holder.textMemberName.setText(stringMemberName);
        holder.textMemberRelation.setText(stringMemberRelation);
        holder.textGrantedAccess.setText(R.string.granted_access);

        //Creating an instance of NammaApartmentFamilyMembers class and retrieving the values from Firebase.
        NammaApartmentUser nammaApartmentFamilyMembers = nammaApartmentFamilyMembersList.get(position);
        holder.textMemberNameValue.setText(nammaApartmentFamilyMembers.getFullName());
        //TODO: Change text here
        holder.textMemberRelationValue.setText("Family Member");
        boolean grantedAccess = nammaApartmentFamilyMembers.isGrantedAccess();
        holder.textGrantedAccessValue.setText(String.valueOf(grantedAccess));

        holder.textEdit.setText(R.string.edit);
        holder.textCancel.setText(R.string.remove);

        // Making these views Visibility GONE , as they are not required here
        holder.textInvitationDateOrServiceRating.setVisibility(View.GONE);
        holder.textInvitedByOrNumberOfFlats.setVisibility(View.GONE);
        holder.textInvitationDateOrServiceRatingValue.setVisibility(View.GONE);
        holder.textInvitedByOrNumberOfFlatsValue.setVisibility(View.GONE);

        /*Here we are changing edit icon*/
        holder.textEdit.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.edit, 0, 0);

    }

    @Override
    public int getItemCount() {
        return nammaApartmentFamilyMembersList.size();
    }

    /* ------------------------------------------------------------- *
     * My Sweet Home Holder class
     * ------------------------------------------------------------- */

    class MySweetHomeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        final LinearLayout layoutTitle;
        final LinearLayout layoutTitleValues;
        final TextView textMemberName;
        final TextView textMemberNameValue;
        final TextView textMemberRelation;
        final TextView textMemberRelationValue;
        final TextView textInvitationDateOrServiceRating;
        final TextView textInvitationDateOrServiceRatingValue;
        final TextView textGrantedAccess;
        final TextView textGrantedAccessValue;
        final TextView textInvitedByOrNumberOfFlats;
        final TextView textInvitedByOrNumberOfFlatsValue;
        final TextView textCall;
        final TextView textMessage;
        final TextView textEdit;
        final TextView textCancel;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        MySweetHomeHolder(View itemView) {
            super(itemView);

            layoutTitle = itemView.findViewById(R.id.layoutTitle);
            layoutTitleValues = itemView.findViewById(R.id.layoutTitleValues);

            textMemberName = itemView.findViewById(R.id.textVisitorOrServiceName);
            textMemberRelation = itemView.findViewById(R.id.textVisitorOrServiceType);
            textInvitationDateOrServiceRating = itemView.findViewById(R.id.textInvitationDateOrServiceRating);
            textGrantedAccess = itemView.findViewById(R.id.textInvitationTime);
            textInvitedByOrNumberOfFlats = itemView.findViewById(R.id.textInvitedByOrNumberOfFlats);

            textMemberNameValue = itemView.findViewById(R.id.textVisitorOrServiceNameValue);
            textMemberRelationValue = itemView.findViewById(R.id.textVisitorOrServiceTypeValue);
            textInvitationDateOrServiceRatingValue = itemView.findViewById(R.id.textInvitationDateOrServiceRatingValue);
            textGrantedAccessValue = itemView.findViewById(R.id.textInvitationTimeValue);
            textInvitedByOrNumberOfFlatsValue = itemView.findViewById(R.id.textInvitedByOrNumberOfFlatsValue);

            textCall = itemView.findViewById(R.id.textCall);
            textMessage = itemView.findViewById(R.id.textMessage);
            textEdit = itemView.findViewById(R.id.textRescheduleOrEdit);
            textCancel = itemView.findViewById(R.id.textCancel);

            textMemberName.setTypeface(Constants.setLatoRegularFont(mCtx));
            textMemberRelation.setTypeface(Constants.setLatoRegularFont(mCtx));
            textInvitationDateOrServiceRating.setTypeface(Constants.setLatoRegularFont(mCtx));
            textGrantedAccess.setTypeface(Constants.setLatoRegularFont(mCtx));
            textInvitedByOrNumberOfFlats.setTypeface(Constants.setLatoRegularFont(mCtx));

            textMemberNameValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textMemberRelationValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textInvitationDateOrServiceRatingValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textGrantedAccessValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textInvitedByOrNumberOfFlatsValue.setTypeface(Constants.setLatoBoldFont(mCtx));

            textCall.setTypeface(Constants.setLatoRegularFont(mCtx));
            textMessage.setTypeface(Constants.setLatoRegularFont(mCtx));
            textEdit.setTypeface(Constants.setLatoRegularFont(mCtx));
            textCancel.setTypeface(Constants.setLatoRegularFont(mCtx));

            //Setting events for items in card view
            textCall.setOnClickListener(this);
            textMessage.setOnClickListener(this);
            textEdit.setOnClickListener(this);
            textCancel.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            /*int position = getLayoutPosition();
            NammaApartmentFamilyMembers nammaApartmentFamilyMembers = nammaApartmentFamilyMembersList.get(position);
            switch (v.getId()) {
                case R.id.textCall:
                    baseActivity.makePhoneCall(nammaApartmentFamilyMembers.getphoneNumber());
                    break;
                case R.id.textMessage:
                    baseActivity.sendTextMessage(nammaApartmentFamilyMembers.getphoneNumber());
                    break;
                case R.id.textRescheduleOrEdit:
                    Intent EditIntentFamilyMembers = new Intent(mCtx, EditDailyServicesAndFamilyMemberDetails.class);
                    EditIntentFamilyMembers.putExtra(SCREEN_TITLE, R.string.my_sweet_home);
                    EditIntentFamilyMembers.putExtra(FAMILY_MEMBER_OBJECT, nammaApartmentFamilyMembers);
                    mCtx.startActivity(EditIntentFamilyMembers);
                    break;
                case R.id.textCancel:
                    nammaApartmentFamilyMembersList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, nammaApartmentFamilyMembersList.size());
                    PRIVATE_USERS_REFERENCE.child(NammaApartmentsGlobal.userUID)
                            .child(FIREBASE_CHILD_MYFAMILYMEMBERS)
                            .removeValue();
                    break;
            }*/
        }
    }
}
