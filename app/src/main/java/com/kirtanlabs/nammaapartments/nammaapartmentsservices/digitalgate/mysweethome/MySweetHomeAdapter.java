package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mysweethome;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices.EditDailyServicesAndFamilyMemberDetails;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.util.List;

import static com.kirtanlabs.nammaapartments.Constants.FAMILY_MEMBER;
import static com.kirtanlabs.nammaapartments.Constants.FAMILY_MEMBER_OBJECT;
import static com.kirtanlabs.nammaapartments.Constants.FRIEND;
import static com.kirtanlabs.nammaapartments.Constants.SCREEN_TITLE;

public class MySweetHomeAdapter extends RecyclerView.Adapter<MySweetHomeAdapter.MySweetHomeHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */
    private final Context mCtx;
    private final BaseActivity baseActivity;
    private final List<NammaApartmentUser> nammaApartmentFamilyMembersList;

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
        String stringMemberRelationValue = FAMILY_MEMBER;

        holder.textMemberName.setText(stringMemberName);
        holder.textMemberRelation.setText(stringMemberRelation);
        holder.textGrantedAccess.setText(R.string.granted_access);

        //Creating an instance of NammaApartmentFamilyMembers class and retrieving the values from Firebase.
        NammaApartmentUser nammaApartmentFamilyMembers = nammaApartmentFamilyMembersList.get(position);
        holder.textMemberNameValue.setText(nammaApartmentFamilyMembers.getPersonalDetails().getFullName());

        //Checking how two UIDs/people are related with each other (Family Member/Friend)
        if (nammaApartmentFamilyMembers.getFriends() != null) {
            if (nammaApartmentFamilyMembers.getFriends().containsKey(NammaApartmentsGlobal.userUID)) {
                stringMemberRelationValue = FRIEND;
            }
        }

        //Setting the value of Relation to display in the My Sweet Home screen
        holder.textMemberRelationValue.setText(stringMemberRelationValue);
        boolean grantedAccess = nammaApartmentFamilyMembers.getPrivileges().isGrantedAccess();
        String grantedAccessValue = String.valueOf(grantedAccess);
        String accessValue = grantedAccessValue.substring(0, 1).toUpperCase() + grantedAccessValue.substring(1);
        holder.textGrantedAccessValue.setText(accessValue);
        Glide.with(mCtx.getApplicationContext()).load(nammaApartmentFamilyMembers.getPersonalDetails().getProfilePhoto())
                .into(holder.visitorOrDailyServiceProfilePic);

        holder.textEdit.setText(R.string.edit);
        holder.textCancel.setText(R.string.remove);

        // Making these views Visibility GONE , as they are not required here
        holder.textInvitationDateOrServiceRating.setVisibility(View.GONE);
        holder.textInvitedByOrNumberOfFlats.setVisibility(View.GONE);
        holder.textInvitationDateOrServiceRatingValue.setVisibility(View.GONE);
        holder.textInvitedByOrNumberOfFlatsValue.setVisibility(View.GONE);

        /*Here we are changing edit icon*/
        holder.textEdit.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.edit_new, 0, 0);

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
        private final de.hdodenhof.circleimageview.CircleImageView visitorOrDailyServiceProfilePic;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        MySweetHomeHolder(View itemView) {
            super(itemView);

            /*Getting Id's for all the views*/
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
            visitorOrDailyServiceProfilePic = itemView.findViewById(R.id.visitorOrDailyServiceProfilePic);

            textCall = itemView.findViewById(R.id.textCall);
            textMessage = itemView.findViewById(R.id.textMessage);
            textEdit = itemView.findViewById(R.id.textRescheduleOrEdit);
            textCancel = itemView.findViewById(R.id.textCancel);

            /*Setting font for all the views*/
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

            textCall.setTypeface(Constants.setLatoBoldItalicFont(mCtx));
            textMessage.setTypeface(Constants.setLatoBoldItalicFont(mCtx));
            textEdit.setTypeface(Constants.setLatoBoldItalicFont(mCtx));
            textCancel.setTypeface(Constants.setLatoBoldItalicFont(mCtx));

            //Setting events for items in card view
            textCall.setOnClickListener(this);
            textMessage.setOnClickListener(this);
            textEdit.setOnClickListener(this);
            textCancel.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            NammaApartmentUser nammaApartmentFamilyMembers = nammaApartmentFamilyMembersList.get(position);
            //Here first we are getting current user admin value based on the NammaApartment class.
            NammaApartmentUser currentNammaApartmentUser = ((NammaApartmentsGlobal) mCtx.getApplicationContext()).getNammaApartmentUser();
            boolean isAdmin = currentNammaApartmentUser.getPrivileges().isAdmin();
            switch (v.getId()) {
                case R.id.textCall:
                    baseActivity.makePhoneCall(nammaApartmentFamilyMembers.getPersonalDetails().getPhoneNumber());
                    break;
                case R.id.textMessage:
                    baseActivity.sendTextMessage(nammaApartmentFamilyMembers.getPersonalDetails().getPhoneNumber());
                    break;
                case R.id.textRescheduleOrEdit:
                    //Here we are checking if the value is true i.e he is admin and can edit other
                    //non admin family members.
                    if (isAdmin) {
                        Intent EditIntentFamilyMembers = new Intent(mCtx, EditDailyServicesAndFamilyMemberDetails.class);
                        EditIntentFamilyMembers.putExtra(SCREEN_TITLE, R.string.my_sweet_home);
                        EditIntentFamilyMembers.putExtra(FAMILY_MEMBER_OBJECT, nammaApartmentFamilyMembers);
                        mCtx.startActivity(EditIntentFamilyMembers);
                    } else {
                        //Here we are showing users a dialog box since they are not admin of that particular flat.
                        baseActivity.showSuccessDialog(mCtx.getResources().getString(R.string.non_admin_edit_title_message),
                                mCtx.getResources().getString(R.string.non_admin_edit_message)
                                , null);
                    }
                    break;
                case R.id.textCancel:
                    //TODO:To rethink about the remove functionality in MySweetHome .
                    break;
            }
        }
    }
}
