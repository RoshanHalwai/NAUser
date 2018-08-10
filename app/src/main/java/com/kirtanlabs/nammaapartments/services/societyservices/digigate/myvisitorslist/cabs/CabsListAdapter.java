package com.kirtanlabs.nammaapartments.services.societyservices.digigate.myvisitorslist.cabs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.notifydigitalgate.arrivals.NammaApartmentArrival;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.util.List;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_USERS_REFERENCE;

public class CabsListAdapter extends RecyclerView.Adapter<CabsListAdapter.CabsViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */
    private final Context mCtx;
    private final List<NammaApartmentArrival> nammaApartmentArrivalList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    CabsListAdapter(List<NammaApartmentArrival> nammaApartmentArrivalList, Context mCtx) {
        this.mCtx = mCtx;
        this.nammaApartmentArrivalList = nammaApartmentArrivalList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */
    @NonNull
    @Override
    public CabsListAdapter.CabsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_cabs_list_and_packages_list, parent, false);
        return new CabsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CabsListAdapter.CabsViewHolder holder, int position) {
        //Creating an instance of NammaApartmentArrival class and retrieving the values from Firebase
        NammaApartmentArrival nammaApartmentArrival = nammaApartmentArrivalList.get(position);
        holder.textCabValue.setText(nammaApartmentArrival.getReference());
        holder.textCabStatusValue.setText(nammaApartmentArrival.getStatus());
        String dateAndTime = nammaApartmentArrival.getDateAndTimeOfArrival();
        String separatedDateAndTime[] = TextUtils.split(dateAndTime, "\t\t ");
        holder.textCabDateValue.setText(separatedDateAndTime[0]);
        holder.textCabTimeValue.setText(separatedDateAndTime[1]);

        /*We check if the invitors UID is equal to current UID if it is then we don't have to check in
        firebase since we now know that the inviter is current user.*/
        if (nammaApartmentArrival.getInviterUID().equals(NammaApartmentsGlobal.userUID)) {
            holder.textInviterValue.setText(
                    ((NammaApartmentsGlobal) mCtx.getApplicationContext())
                            .getNammaApartmentUser()
                            .getPersonalDetails()
                            .getFullName());
        } else {
            /*Cab has been invited by some other family member; We check in firebase and get the name
             * of that family member*/
            DatabaseReference userPrivateReference = PRIVATE_USERS_REFERENCE.child(nammaApartmentArrival.getInviterUID());
            userPrivateReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    NammaApartmentUser nammaApartmentUser = dataSnapshot.getValue(NammaApartmentUser.class);
                    holder.textInviterValue.setText(Objects.requireNonNull(nammaApartmentUser).getPersonalDetails().getFullName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return nammaApartmentArrivalList.size();
    }

    /* ------------------------------------------------------------- *
     * Cabs View Holder class
     * ------------------------------------------------------------- */
    class CabsViewHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */
        private final TextView textCabTitle;
        private final TextView textCabStatus;
        private final TextView textCabDate;
        private final TextView textCabTime;
        private final TextView textInviter;
        private final TextView textCabValue;
        private final TextView textCabStatusValue;
        private final TextView textCabDateValue;
        private final TextView textCabTimeValue;
        private final TextView textInviterValue;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */
        CabsViewHolder(View itemView) {
            super(itemView);

            //Getting Id's for all the views on cardview
            textCabTitle = itemView.findViewById(R.id.textCabOrVendorTitle);
            textCabStatus = itemView.findViewById(R.id.textCabOrVendorServiceStatus);
            textCabDate = itemView.findViewById(R.id.textCabOrVendorDate);
            textCabTime = itemView.findViewById(R.id.textCabOrVendorTime);
            textInviter = itemView.findViewById(R.id.textInviter);
            textCabValue = itemView.findViewById(R.id.textCabOrVendorValue);
            textCabStatusValue = itemView.findViewById(R.id.textCabOrVendorStatusValue);
            textCabDateValue = itemView.findViewById(R.id.textCabOrVendorDateValue);
            textCabTimeValue = itemView.findViewById(R.id.textCabOrVendorTimeValue);
            textInviterValue = itemView.findViewById(R.id.textInviterValue);

            //Setting Fonts for all the views on cardview
            textCabTitle.setTypeface(Constants.setLatoRegularFont(mCtx));
            textCabStatus.setTypeface(Constants.setLatoRegularFont(mCtx));
            textCabDate.setTypeface(Constants.setLatoRegularFont(mCtx));
            textCabTime.setTypeface(Constants.setLatoRegularFont(mCtx));
            textInviter.setTypeface(Constants.setLatoRegularFont(mCtx));
            textCabValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textCabStatusValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textCabDateValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textCabTimeValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textInviterValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        }
    }

}
