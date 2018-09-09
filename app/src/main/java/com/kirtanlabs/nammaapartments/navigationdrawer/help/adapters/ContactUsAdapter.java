package com.kirtanlabs.nammaapartments.navigationdrawer.help.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.help.pojo.Support;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class ContactUsAdapter extends RecyclerView.Adapter<ContactUsAdapter.ContactUsViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final List<Support> supportList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public ContactUsAdapter(Context mCtx, List<Support> supportList) {
        this.mCtx = mCtx;
        this.supportList = supportList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public ContactUsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*inflating and returning our view holder*/
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_contact_us_history, parent, false);
        return new ContactUsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactUsViewHolder holder, int position) {

        /*Creating an instance of Support class and retrieving the values from Firebase*/
        Support support = supportList.get(position);
        holder.textSupportCategoryValue.setText(support.getServiceCategory());
        holder.textSupportTypeValue.setText(support.getServiceType());
        holder.textSupportProblemValue.setText(support.getProblemDescription());
        long timestamp = support.getTimestamp();

        /*Decoding Time Stamp to Date*/
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timestamp);
        String date = DateFormat.format("MMM dd, yyyy", calendar).toString();
        holder.textSupportDateValue.setText(date);
        holder.textSupportStatusValue.setText(support.getStatus());
    }

    @Override
    public int getItemCount() {
        return supportList.size();
    }

    class ContactUsViewHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textSupportCategory;
        private final TextView textSupportCategoryValue;
        private final TextView textSupportType;
        private final TextView textSupportTypeValue;
        private final TextView textSupportProblem;
        private final TextView textSupportProblemValue;
        private final TextView textSupportDate;
        private final TextView textSupportDateValue;
        private final TextView textSupportStatus;
        private final TextView textSupportStatusValue;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        ContactUsViewHolder(View itemView) {
            super(itemView);

            /*Getting Id's for all the views on cardview*/
            textSupportCategory = itemView.findViewById(R.id.textSupportCategory);
            textSupportType = itemView.findViewById(R.id.textSupportType);
            textSupportProblem = itemView.findViewById(R.id.textSupportProblem);
            textSupportDate = itemView.findViewById(R.id.textSupportDate);
            textSupportStatus = itemView.findViewById(R.id.textSupportStatus);
            textSupportCategoryValue = itemView.findViewById(R.id.textSupportCategoryValue);
            textSupportTypeValue = itemView.findViewById(R.id.textSupportTypeValue);
            textSupportProblemValue = itemView.findViewById(R.id.textSupportProblemValue);
            textSupportDateValue = itemView.findViewById(R.id.textSupportDateValue);
            textSupportStatusValue = itemView.findViewById(R.id.textSupportStatusValue);

            /*Setting Fonts for all the views on cardview*/
            textSupportCategory.setTypeface(setLatoRegularFont(mCtx));
            textSupportType.setTypeface(setLatoRegularFont(mCtx));
            textSupportProblem.setTypeface(setLatoRegularFont(mCtx));
            textSupportDate.setTypeface(setLatoRegularFont(mCtx));
            textSupportStatus.setTypeface(setLatoRegularFont(mCtx));
            textSupportCategoryValue.setTypeface(setLatoBoldFont(mCtx));
            textSupportTypeValue.setTypeface(setLatoBoldFont(mCtx));
            textSupportProblemValue.setTypeface(setLatoBoldFont(mCtx));
            textSupportDateValue.setTypeface(setLatoBoldFont(mCtx));
            textSupportStatusValue.setTypeface(setLatoBoldFont(mCtx));

        }
    }
}
