package com.kirtanlabs.nammaapartments.navigationdrawer.noticeboard.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.noticeboard.pojo.NammaApartmentsNotice;

import java.util.List;

import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class NoticeBoardAdapter extends RecyclerView.Adapter<NoticeBoardAdapter.NoticeBoardViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final List<NammaApartmentsNotice> nammaApartmentsNoticeList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public NoticeBoardAdapter(List<NammaApartmentsNotice> nammaApartmentsNoticeList, Context mCtx) {
        this.mCtx = mCtx;
        this.nammaApartmentsNoticeList = nammaApartmentsNoticeList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public NoticeBoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*inflating and returning our view holder*/
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_notice_board, parent, false);
        return new NoticeBoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeBoardViewHolder holder, int position) {

        /*Creating an instance of NammaApartmentsNotice class and retrieving the values from Firebase*/
        NammaApartmentsNotice nammaApartmentsNotice = nammaApartmentsNoticeList.get(position);
        holder.textNoticeDate.setText(nammaApartmentsNotice.getDateAndTime());
        holder.textNoticeTitle.setText(nammaApartmentsNotice.getTitle());
        holder.textNoticeMessage.setText(nammaApartmentsNotice.getDescription());
        holder.textAdminName.setText(nammaApartmentsNotice.getNameOfAdmin());

    }

    @Override
    public int getItemCount() {
        return nammaApartmentsNoticeList.size();
    }

    /* ------------------------------------------------------------- *
     * Notice Board View Holder class
     * ------------------------------------------------------------- */

    class NoticeBoardViewHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textNoticeTitle;
        private final TextView textNoticeMessage;
        private final TextView textAdminName;
        private final TextView textNoticeDate;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        NoticeBoardViewHolder(View itemView) {
            super(itemView);

            /*Getting id's for all the views on cardview*/
            textNoticeDate = itemView.findViewById(R.id.textNoticeDate);
            textNoticeTitle = itemView.findViewById(R.id.textNoticeTitle);
            textNoticeMessage = itemView.findViewById(R.id.textNoticeMessage);
            textAdminName = itemView.findViewById(R.id.textAdminName);

            /*Setting Fonts for all the views on cardview*/
            textNoticeDate.setTypeface(setLatoRegularFont(mCtx));
            textNoticeTitle.setTypeface(setLatoBoldFont(mCtx));
            textNoticeMessage.setTypeface(setLatoRegularFont(mCtx));
            textAdminName.setTypeface(setLatoBoldFont(mCtx));

        }
    }

}
