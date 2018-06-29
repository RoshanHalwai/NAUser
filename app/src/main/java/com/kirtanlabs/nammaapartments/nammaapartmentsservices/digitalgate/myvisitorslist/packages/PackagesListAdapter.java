package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.myvisitorslist.packages;

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
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.notifydigitalgate.NammaApartmentArrival;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.util.List;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_USERS_REFERENCE;

public class PackagesListAdapter extends RecyclerView.Adapter<PackagesListAdapter.PackageViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */
    private Context mCtx;
    private List<NammaApartmentArrival> nammaApartmentArrivalList;

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
        String packageName = mCtx.getResources().getString(R.string.package_name);
        holder.textCabOrVendorTitle.setText(packageName);
        holder.cabOrVendorProfilePic.setImageResource(R.drawable.delivery_man);
        //Creating an instance of NammaApartmentArrival class and retrieving the values from Firebase.
        NammaApartmentArrival nammaApartmentArrival = nammaApartmentArrivalList.get(position);
        //Since we need invitors name we get the details by uid
        DatabaseReference userPrivateReference = PRIVATE_USERS_REFERENCE.child(NammaApartmentsGlobal.userUID);
        userPrivateReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NammaApartmentUser nammaApartmentUser = dataSnapshot.getValue(NammaApartmentUser.class);
                holder.textCabOrVendorValue.setText(nammaApartmentArrival.getReference());
                String dateAndTime = nammaApartmentArrival.getDateAndTimeOfArrival();
                String separatedDateAndTime[] = TextUtils.split(dateAndTime, "\t\t ");
                holder.textCabOrVendorDateValue.setText(separatedDateAndTime[0]);
                holder.textCabOrVendorTimeValue.setText(separatedDateAndTime[1]);
                holder.textInviterValue.setText(Objects.requireNonNull(nammaApartmentUser).getPersonalDetails().getFullName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        private final TextView textCabOrVendorTitle;
        private final TextView textCabOrVendorServiceType;
        private final TextView textCabOrVendorDate;
        private final TextView textCabOrVendorTime;
        private final TextView textInviter;
        private final TextView textCabOrVendorValue;
        private final TextView textCabOrVendorTypeValue;
        private final TextView textCabOrVendorDateValue;
        private final TextView textCabOrVendorTimeValue;
        private final TextView textInviterValue;
        private final de.hdodenhof.circleimageview.CircleImageView cabOrVendorProfilePic;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */
        PackageViewHolder(View itemView) {
            super(itemView);

            //Getting Id's for all the views on cardview
            cabOrVendorProfilePic = itemView.findViewById(R.id.cabOrVendorProfilePic);
            textCabOrVendorTitle = itemView.findViewById(R.id.textCabOrVendorTitle);
            textCabOrVendorServiceType = itemView.findViewById(R.id.textCabOrVendorServiceType);
            textCabOrVendorDate = itemView.findViewById(R.id.textCabOrVendorDate);
            textCabOrVendorTime = itemView.findViewById(R.id.textCabOrVendorTime);
            textInviter = itemView.findViewById(R.id.textInviter);
            textCabOrVendorValue = itemView.findViewById(R.id.textCabOrVendorValue);
            textCabOrVendorTypeValue = itemView.findViewById(R.id.textCabOrVendorTypeValue);
            textCabOrVendorDateValue = itemView.findViewById(R.id.textCabOrVendorDateValue);
            textCabOrVendorTimeValue = itemView.findViewById(R.id.textCabOrVendorTimeValue);
            textInviterValue = itemView.findViewById(R.id.textInviterValue);

            //Setting Fonts for all the views on cardview
            textCabOrVendorTitle.setTypeface(Constants.setLatoRegularFont(mCtx));
            textCabOrVendorServiceType.setTypeface(Constants.setLatoRegularFont(mCtx));
            textCabOrVendorDate.setTypeface(Constants.setLatoRegularFont(mCtx));
            textCabOrVendorTime.setTypeface(Constants.setLatoRegularFont(mCtx));
            textInviter.setTypeface(Constants.setLatoRegularFont(mCtx));
            textCabOrVendorValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textCabOrVendorTypeValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textCabOrVendorDateValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textCabOrVendorTimeValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textInviterValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        }
    }
}
