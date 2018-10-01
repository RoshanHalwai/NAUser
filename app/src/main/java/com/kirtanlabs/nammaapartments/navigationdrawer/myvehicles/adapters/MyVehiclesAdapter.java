package com.kirtanlabs.nammaapartments.navigationdrawer.myvehicles.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.myvehicles.pojo.Vehicle;

import java.util.List;

import static com.kirtanlabs.nammaapartments.utilities.Constants.ALL_VEHICLES_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_VEHICLES;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_VEHICLE_NUMBER;
import static com.kirtanlabs.nammaapartments.utilities.Constants.HYPHEN;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_VEHICLES_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 8/13/2018
 */
public class MyVehiclesAdapter extends RecyclerView.Adapter<MyVehiclesAdapter.MyVehiclesHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final BaseActivity baseActivity;
    private final List<Vehicle> notificationServicesList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public MyVehiclesAdapter(Context mCtx, List<Vehicle> notificationServicesList) {
        this.mCtx = mCtx;
        baseActivity = (BaseActivity) mCtx;
        this.notificationServicesList = notificationServicesList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public MyVehiclesAdapter.MyVehiclesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_my_vehicles, parent, false);
        return new MyVehiclesAdapter.MyVehiclesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVehiclesAdapter.MyVehiclesHolder holder, int position) {
        Vehicle vehicle = notificationServicesList.get(position);
        holder.textVehicleNumberValue.setText(vehicle.getVehicleNumber());
        holder.textAddedDateValue.setText(vehicle.getAddedDate());
        holder.textOwnerValue.setText(vehicle.getOwnerName());
        if (vehicle.getVehicleType().equals(mCtx.getString(R.string.vehicle_type_car))) {
            holder.imageVehicleType.setImageResource(R.drawable.car);
            holder.textVehicleNumber.setText(mCtx.getString(R.string.car_no));
        } else {
            holder.imageVehicleType.setImageResource(R.drawable.motorbike);
            holder.textVehicleNumber.setText(mCtx.getString(R.string.bike_no));
        }

    }

    @Override
    public int getItemCount() {
        return notificationServicesList.size();
    }

    /* ------------------------------------------------------------- *
     *  My Vehicles Holder class
     * ------------------------------------------------------------- */

    class MyVehiclesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        final TextView textVehicleNumber;
        final TextView textVehicleNumberValue;
        final TextView textAddedDate;
        final TextView textAddedDateValue;
        final TextView textOwner;
        final TextView textOwnerValue;
        final ImageView imageVehicleType;
        final Button buttonEdit;
        final Button buttonRemove;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        MyVehiclesHolder(View itemView) {
            super(itemView);

            /*Getting Id's for all the views on card view*/
            textVehicleNumber = itemView.findViewById(R.id.textVehicleNumber);
            textVehicleNumberValue = itemView.findViewById(R.id.textVehicleNumberValue);
            textAddedDate = itemView.findViewById(R.id.textAddedDate);
            textAddedDateValue = itemView.findViewById(R.id.textAddedDateValue);
            textOwner = itemView.findViewById(R.id.textOwner);
            textOwnerValue = itemView.findViewById(R.id.textOwnerValue);
            imageVehicleType = itemView.findViewById(R.id.imageVehicleType);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);

            /*Setting Fonts for all the views on card view*/
            textVehicleNumber.setTypeface(setLatoRegularFont(mCtx));
            textAddedDate.setTypeface(setLatoRegularFont(mCtx));
            textOwner.setTypeface(setLatoRegularFont(mCtx));
            textVehicleNumberValue.setTypeface(setLatoBoldFont(mCtx));
            textAddedDateValue.setTypeface(setLatoBoldFont(mCtx));
            textOwnerValue.setTypeface(setLatoBoldFont(mCtx));
            buttonEdit.setTypeface(setLatoLightFont(mCtx));
            buttonRemove.setTypeface(setLatoLightFont(mCtx));

            /*Setting onClickListener for view*/
            buttonEdit.setOnClickListener(this);
            buttonRemove.setOnClickListener(this);

        }

        /* ------------------------------------------------------------- *
         * Overriding OnClickListener Methods
         * ------------------------------------------------------------- */

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            switch (v.getId()) {
                case R.id.buttonEdit:
                    openEditMyVehicleDialog(position);
                    break;
                case R.id.buttonRemove:
                    removeVehicleDetails(position);
                    break;
            }
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to open dialog box where user can edit their vehicle details
     *
     * @param position of the view
     */
    private void openEditMyVehicleDialog(final int position) {
        Vehicle vehicle = notificationServicesList.get(position);
        String userVehicleUID = vehicle.getUid();
        String vehicleNumber = vehicle.getVehicleNumber();
        View editVehicleDetailsDialogView = View.inflate(mCtx, R.layout.layout_edit_my_vehicle, null);

        /*Getting Id's for all the views*/
        TextView textVehicleNumber = editVehicleDetailsDialogView.findViewById(R.id.textVehicleNumber);
        TextView textErrorCabNumber = editVehicleDetailsDialogView.findViewById(R.id.textErrorCabNumber);
        EditText editVehicleStateCode = editVehicleDetailsDialogView.findViewById(R.id.editVehicleStateCode);
        EditText editVehicleRtoNumber = editVehicleDetailsDialogView.findViewById(R.id.editVehicleRtoNumber);
        EditText editVehicleSerialNumberOne = editVehicleDetailsDialogView.findViewById(R.id.editVehicleSerialNumberOne);
        EditText editVehicleSerialNumberTwo = editVehicleDetailsDialogView.findViewById(R.id.editVehicleSerialNumberTwo);
        Button buttonCancel = editVehicleDetailsDialogView.findViewById(R.id.buttonCancel);
        Button buttonUpdate = editVehicleDetailsDialogView.findViewById(R.id.buttonUpdate);

        /*Setting font for all the views*/
        textVehicleNumber.setTypeface(setLatoBoldFont(mCtx));
        textErrorCabNumber.setTypeface(setLatoBoldFont(mCtx));
        editVehicleStateCode.setTypeface(setLatoRegularFont(mCtx));
        editVehicleRtoNumber.setTypeface(setLatoRegularFont(mCtx));
        editVehicleSerialNumberOne.setTypeface(setLatoRegularFont(mCtx));
        editVehicleSerialNumberTwo.setTypeface(setLatoRegularFont(mCtx));
        buttonCancel.setTypeface(setLatoLightFont(mCtx));
        buttonUpdate.setTypeface(setLatoLightFont(mCtx));

        /*Filling vehicle details in the fields*/
        String[] separatedVehicleNumber = TextUtils.split(vehicleNumber, HYPHEN);
        String stateCode = separatedVehicleNumber[0];
        String rtoNumber = separatedVehicleNumber[1];
        String vehicleSerialNumberOne = separatedVehicleNumber[2];
        String vehicleSerialNumberTwo = separatedVehicleNumber[3];
        editVehicleStateCode.setText(stateCode);
        editVehicleRtoNumber.setText(rtoNumber);
        editVehicleSerialNumberOne.setText(vehicleSerialNumberOne);
        editVehicleSerialNumberTwo.setText(vehicleSerialNumberTwo);

        AlertDialog.Builder editVehicleDialog = new AlertDialog.Builder(mCtx);
        editVehicleDialog.setView(editVehicleDetailsDialogView);
        editVehicleDialog.setCancelable(false);
        AlertDialog dialog = editVehicleDialog.create();

        new Dialog(mCtx);
        dialog.show();

        /*Setting On Click Listeners to the view*/
        buttonCancel.setOnClickListener(v -> dialog.cancel());
        buttonUpdate.setOnClickListener(v -> {
            /*Getting Updated Vehicle details*/
            String updatedStateCode = editVehicleStateCode.getText().toString().trim();
            String updatedRtoNumber = editVehicleRtoNumber.getText().toString().trim();
            String updatedSerialCodeOne = editVehicleSerialNumberOne.getText().toString().trim();
            String updatedSerialCodeTwo = editVehicleSerialNumberTwo.getText().toString().trim();

            if (TextUtils.isEmpty(updatedStateCode) || TextUtils.isEmpty(updatedRtoNumber) || TextUtils.isEmpty(updatedSerialCodeOne)
                    || TextUtils.isEmpty(updatedSerialCodeTwo)) {
                textErrorCabNumber.setVisibility(View.VISIBLE);
            } else {
                String updatedVehicleNumber = updatedStateCode + HYPHEN + updatedRtoNumber + HYPHEN + updatedSerialCodeOne
                        + HYPHEN + updatedSerialCodeTwo;
                if (!updatedVehicleNumber.equals(vehicleNumber)) {
                    updateVehicleDetails(position, updatedVehicleNumber, vehicleNumber, userVehicleUID);
                }
                dialog.cancel();
            }
        });
    }

    /**
     * This method is used to store updated vehicle details in firebase.
     *
     * @param position             of the card view
     * @param updatedVehicleNumber edited vehicle number
     * @param vehicleNumber        previous vehicle number
     * @param userVehicleUID       UID of that vehicle
     */
    private void updateVehicleDetails(final int position, final String updatedVehicleNumber, final String vehicleNumber, final String userVehicleUID) {
        /*Updating vehicle number in the list*/
        Vehicle vehicle = notificationServicesList.get(position);
        vehicle.setVehicleNumber(updatedVehicleNumber);
        notifyItemChanged(position);

        /*Mapping Updated vehicle number with vehicle uid in firebase under (vehicles->all->updatedVehicleNumber)*/
        DatabaseReference myVehicleAllUpdatedReference = ALL_VEHICLES_REFERENCE.child(updatedVehicleNumber);
        myVehicleAllUpdatedReference.setValue(userVehicleUID).addOnSuccessListener(aVoid -> {

            /*Removing previous vehicle number in firebase from (vehicles->all->previousVehicleNumber)*/
            DatabaseReference myVehicleAllReference = ALL_VEHICLES_REFERENCE.child(vehicleNumber);
            myVehicleAllReference.removeValue();

            /*Updating vehicle number in firebase under (vehicles->private->vehicleUID->vehicleNumber) */
            DatabaseReference myVehiclePrivateReference = PRIVATE_VEHICLES_REFERENCE.child(userVehicleUID).child(FIREBASE_CHILD_VEHICLE_NUMBER);
            myVehiclePrivateReference.setValue(updatedVehicleNumber);
        });
    }

    /**
     * This method is used to remove the User's Vehicle details from firebase.
     *
     * @param position of the card view.
     */
    private void removeVehicleDetails(final int position) {
        Vehicle vehicle = notificationServicesList.get(position);

        /*Runnable Interface which gets invoked once user presses OK button in Confirmation Dialog*/
        Runnable removeVehicle = () -> {
            String userVehicleUID = vehicle.getUid();
            String vehicleNumber = vehicle.getVehicleNumber();
            /*removing item from the list*/
            notificationServicesList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, notificationServicesList.size());

            /*Removing vehicle uid from (userData->userFlatNumber->vehicles) in firebase*/
            DatabaseReference userDataReference = ((NammaApartmentsGlobal) mCtx.getApplicationContext()).getUserDataReference();
            userDataReference.child(FIREBASE_CHILD_VEHICLES).child(userVehicleUID).removeValue().addOnSuccessListener(aVoid -> {
                /*Removing vehicle number in firebase from (vehicles->all->vehicleNumber)*/
                DatabaseReference myVehicleAllReference = ALL_VEHICLES_REFERENCE.child(vehicleNumber);
                myVehicleAllReference.removeValue();

                /*Removing vehicleUID number from (vehicles->private->vehicleUID) in firebase*/
                DatabaseReference myVehiclePrivateReference = PRIVATE_VEHICLES_REFERENCE.child(userVehicleUID);
                myVehiclePrivateReference.removeValue();
            });

            /*This is to ensure when user deletes the last item in the list a blank screen is not shown
             * instead feature unavailable layout is shown*/
            if (notificationServicesList.isEmpty()) {
                baseActivity.showFeatureUnavailableLayout(R.string.add_your_vehicle_message);
            }
        };

        baseActivity.showConfirmDialog(mCtx.getString(R.string.remove_vehicle_title), mCtx.getString(R.string.remove_vehicle_message), removeVehicle);
    }

}
