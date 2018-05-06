package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.myvisitorslist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kirtanlabs.nammaapartments.R;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/5/2018
 */
public class VisitorsListAdapter extends RecyclerView.Adapter<VisitorsListAdapter.VisitorViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;

    //getting the context and product list with constructor
    VisitorsListAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public VisitorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_visitors_list, parent, false);
        return new VisitorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitorViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class VisitorViewHolder extends RecyclerView.ViewHolder {

        VisitorViewHolder(View itemView) {
            super(itemView);
        }
    }

}
