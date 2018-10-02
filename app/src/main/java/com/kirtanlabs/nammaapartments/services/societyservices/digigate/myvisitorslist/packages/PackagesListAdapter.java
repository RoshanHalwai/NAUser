package com.kirtanlabs.nammaapartments.services.societyservices.digigate.myvisitorslist.packages;

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

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_GUARD_APPROVED;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_POSTAPPROVED;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_USERS_REFERENCE;

public class PackagesListAdapter extends RecyclerView.Adapter<PackagesListAdapter.PackageViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */
    private final Context mCtx;
    private final List<NammaApartmentArrival> nammaApartmentArrivalList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    PackagesListAdapter(List<NammaApartmentArrival> nammaApartmentArrivalList, Context mctx) {
        this.mCtx = mctx;
        this.nammaApartmentArrivalList = nammaApartmentArrivalList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */
    @NonNull
    @Override
    public PackagesListAdapter.PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_cabs_list_and_packages_list, parent, false);
        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackagesListAdapter.PackageViewHolder holder, int position) {
        //Creating an instance of NammaApartmentArrival class and retrieving the values from Firebase.
        NammaApartmentArrival nammaApartmentArrival = nammaApartmentArrivalList.get(position);
        holder.textVendorValue.setText(nammaApartmentArrival.getReference());
        holder.textVendorStatusValue.setText(nammaApartmentArrival.getStatus());
        String dateAndTime = nammaApartmentArrival.getDateAndTimeOfArrival();
        String separatedDateAndTime[] = TextUtils.split(dateAndTime, "\t\t ");
        holder.textVendorDateValue.setText(separatedDateAndTime[0]);
        holder.textVendorTimeValue.setText(separatedDateAndTime[1]);

        if (nammaApartmentArrival.getApprovalType().equals(FIREBASE_CHILD_GUARD_APPROVED)) {
            /*If the approval type is GuardApproved, then we change the text from 'Inviter' to 'Approver' */
            holder.textInviter.setText(R.string.approver);
            String guard = mCtx.getString(R.string.guard);
            holder.textInviterValue.setText(guard.substring(0, 5));
        } else {
            /*If the approval type of Package is 'post approved', then we change the text 'Inviter' to 'Approver'*/
            if (nammaApartmentArrival.getApprovalType().equals(FIREBASE_CHILD_POSTAPPROVED)) {
                holder.textInviter.setText(R.string.approver);
            }

        /*We check if the invitors UID is equal to current UID if it is then we don't have to check in
        firebase since we now know that the current user has ordered this package.*/
            if (nammaApartmentArrival.getInviterUID().equals(NammaApartmentsGlobal.userUID)) {
                holder.textInviterValue.setText(
                        ((NammaApartmentsGlobal) mCtx.getApplicationContext())
                                .getNammaApartmentUser()
                                .getPersonalDetails()
                                .getFullName());
            } else {
                /*Package has been ordered by some other family member; We check in firebase and get the name
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
    }

    @Override
    public int getItemCount() {
        return nammaApartmentArrivalList.size();
    }

    /* ------------------------------------------------------------- *
     * Package View Holder class
     * ------------------------------------------------------------- */
    class PackageViewHolder extends RecyclerView.ViewHolder {
        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */
        private final TextView textVendorTitle;
        private final TextView textVendorStatus;
        private final TextView textVendorDate;
        private final TextView textVendorTime;
        private final TextView textInviter;
        private final TextView textVendorValue;
        private final TextView textVendorStatusValue;
        private final TextView textVendorDateValue;
        private final TextView textVendorTimeValue;
        private final TextView textInviterValue;
        private final de.hdodenhof.circleimageview.CircleImageView vendorProfilePic;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */
        PackageViewHolder(View itemView) {
            super(itemView);

            //Getting Id's for all the views on cardview
            vendorProfilePic = itemView.findViewById(R.id.cabOrVendorProfilePic);
            textVendorTitle = itemView.findViewById(R.id.textCabOrVendorTitle);
            textVendorStatus = itemView.findViewById(R.id.textCabOrVendorServiceStatus);
            textVendorDate = itemView.findViewById(R.id.textCabOrVendorDate);
            textVendorTime = itemView.findViewById(R.id.textCabOrVendorTime);
            textInviter = itemView.findViewById(R.id.textInviter);
            textVendorValue = itemView.findViewById(R.id.textCabOrVendorValue);
            textVendorStatusValue = itemView.findViewById(R.id.textCabOrVendorStatusValue);
            textVendorDateValue = itemView.findViewById(R.id.textCabOrVendorDateValue);
            textVendorTimeValue = itemView.findViewById(R.id.textCabOrVendorTimeValue);
            textInviterValue = itemView.findViewById(R.id.textInviterValue);

            //Since we using the same layout for cabs and package we need to update the labels
            textVendorTitle.setText(R.string.package_name);
            vendorProfilePic.setImageResource(R.drawable.delivery_man);

            //Setting Fonts for all the views on cardview
            textVendorTitle.setTypeface(Constants.setLatoRegularFont(mCtx));
            textVendorStatus.setTypeface(Constants.setLatoRegularFont(mCtx));
            textVendorDate.setTypeface(Constants.setLatoRegularFont(mCtx));
            textVendorTime.setTypeface(Constants.setLatoRegularFont(mCtx));
            textInviter.setTypeface(Constants.setLatoRegularFont(mCtx));
            textVendorValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textVendorStatusValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textVendorDateValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textVendorTimeValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textInviterValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        }
    }
}
