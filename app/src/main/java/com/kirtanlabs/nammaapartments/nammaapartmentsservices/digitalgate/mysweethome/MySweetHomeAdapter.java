package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mysweethome;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices.EditDailyServicesAndFamilyMemberDetails;

public class MySweetHomeAdapter extends RecyclerView.Adapter<MySweetHomeAdapter.MySweetHomeHolder> implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final BaseActivity baseActivity;
    private View cancelDialog;
    private AlertDialog dialog;
    private int count = 1;
    private String memberNameValue;
    private String grantedAccessValue;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    MySweetHomeAdapter(Context mCtx) {
        this.mCtx = mCtx;
        baseActivity = (BaseActivity) mCtx;
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
        /*Since we are reusing the layouts we need to modify the Layout weight for Title
         * and values, since in My Sweet Home list title take less space than Visitors
         * list*/
        LinearLayout.LayoutParams layoutTitleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.1f
        );
        layoutTitleParams.setMargins(0, 40, 0, 0);
        LinearLayout.LayoutParams layoutTitleValuesParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.8f
        );
        layoutTitleValuesParams.setMargins(0, 40, 0, 0);
        LinearLayout.LayoutParams textGrantedAccessValueParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        textGrantedAccessValueParams.setMargins(0, 20, 0, 0);

        holder.layoutTitle.setLayoutParams(layoutTitleParams);
        holder.layoutTitleValues.setLayoutParams(layoutTitleValuesParams);
        holder.textGrantedAccessValue.setLayoutParams(textGrantedAccessValueParams);

        holder.textMemberName.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textMemberRelation.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textInvitationDateOrServiceRating.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textGrantedAccess.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textInvitedByOrNumberOfFlats.setTypeface(Constants.setLatoRegularFont(mCtx));

        holder.textMemberNameValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textMemberRelationValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textInvitationDateOrServiceRatingValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textGrantedAccessValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textInvitedByOrNumberOfFlatsValue.setTypeface(Constants.setLatoBoldFont(mCtx));

        holder.textCall.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textMessage.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textEdit.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textCancel.setTypeface(Constants.setLatoRegularFont(mCtx));

        String stringMemberName = mCtx.getResources().getString(R.string.name) + ":";
        String stringMemberRelation = mCtx.getResources().getString(R.string.relation) + ":";

        holder.textMemberName.setText(stringMemberName);
        holder.textMemberRelation.setText(stringMemberRelation);
        holder.textGrantedAccess.setText(R.string.granted_access);

        holder.textMemberNameValue.setText("Ramesh Singh");
        holder.textMemberRelationValue.setText("Son");
        holder.textGrantedAccessValue.setText(R.string.no);

        holder.textEdit.setText(R.string.edit);
        holder.textCancel.setText(R.string.remove);

        // Making these views Visibility GONE , as they are not required here
        holder.textInvitationDateOrServiceRating.setVisibility(View.GONE);
        holder.textInvitedByOrNumberOfFlats.setVisibility(View.GONE);
        holder.textInvitationDateOrServiceRatingValue.setVisibility(View.GONE);
        holder.textInvitedByOrNumberOfFlatsValue.setVisibility(View.GONE);

        /*Here we are changing edit icon*/
        holder.textEdit.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.edit, 0, 0);

        memberNameValue = holder.textMemberNameValue.getText().toString();
        grantedAccessValue = holder.textGrantedAccessValue.getText().toString();

        /*Handling Click event of icons*/
        //TODO: Change Mobile Number here
        holder.textCall.setOnClickListener(this);
        //TODO: Change Mobile Number here
        holder.textMessage.setOnClickListener(this);
        holder.textEdit.setOnClickListener(this);
        holder.textCancel.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        //TODO: To change the get item count here
        return count;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textCall:
                baseActivity.makePhoneCall("9885665744");
                break;
            case R.id.textMessage:
                baseActivity.sendTextMessage("9885665744");
                break;
            case R.id.textRescheduleOrEdit:
                editMyFamilyMemberDetails(memberNameValue, grantedAccessValue);
                break;
            case R.id.textCancel:
                openCancelDialog();
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked when user clicks on Edit icon in the list and passes all daily service details in EditMy daily Services Details
     */
    private void editMyFamilyMemberDetails(String name, String granted_access_type) {
        Intent EditIntent = new Intent(mCtx, EditDailyServicesAndFamilyMemberDetails.class);
        EditIntent.putExtra(Constants.SCREEN_TITLE, R.string.my_sweet_home);
        EditIntent.putExtra(Constants.NAME, name);
        EditIntent.putExtra(Constants.MOBILE_NUMBER, "7895185103");    //TODO :  To change the mobile number here
        EditIntent.putExtra(Constants.GRANTED_ACCESS_TYPE, granted_access_type);
        mCtx.startActivity(EditIntent);
    }

    /**
     * This method is invoked when user clicks on cancel icon.
     */
    private void openCancelDialog() {
        cancelDialog = View.inflate(mCtx, R.layout.layout_dialog_cancel, null);

        /*Getting Id's for all the views*/
        TextView textCancelDescription = cancelDialog.findViewById(R.id.textCancelDescription);

        /*Setting Fonts for all the views*/
        textCancelDescription.setTypeface(Constants.setLatoRegularFont(mCtx));

        /*This method is used to create cancel dialog */
        createCancelDialog();
    }

    /**
     * This method is invoked to create a cancel dialog.
     */
    private void createCancelDialog() {
        AlertDialog.Builder alertCancelDialog = new AlertDialog.Builder(mCtx);
        alertCancelDialog.setTitle("Delete");
        alertCancelDialog.setPositiveButton("Yes", (dialog, which) -> deleteVisitorData());
        alertCancelDialog.setNegativeButton("No", (dialog, which) -> dialog.cancel());
        alertCancelDialog.setView(cancelDialog);
        dialog = alertCancelDialog.create();

        new Dialog(mCtx);
        dialog.show();
    }

    /**
     * This method is invoked to delete visitor data.
     */
    private void deleteVisitorData() {
        notifyItemRemoved(0);
        /*Decrementing the count variable on deletion of one visitor data.*/
        --count;
        /*After deletion of one row we are notifying the adapter*/
        notifyDataSetChanged();
        dialog.cancel();
    }

    /* ------------------------------------------------------------- *
     * Daily Service Holder class
     * ------------------------------------------------------------- */

    class MySweetHomeHolder extends RecyclerView.ViewHolder {

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
        }
    }
}
