package com.kirtanlabs.nammaapartments.navigationdrawer.noticeboard.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.noticeboard.adapters.NoticeBoardAdapter;
import com.kirtanlabs.nammaapartments.navigationdrawer.noticeboard.pojo.NammaApartmentsNotice;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

public class NoticeBoard extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private List<NammaApartmentsNotice> nammaApartmentsNoticeList;
    private NoticeBoardAdapter noticeBoardAdapter;
    private int index = 0;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_notice_board_home;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.notice_board;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Getting Id for all the views*/
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*Initialising Array List*/
        nammaApartmentsNoticeList = new ArrayList<>();

        /*Initializing the adapter*/
        noticeBoardAdapter = new NoticeBoardAdapter(nammaApartmentsNoticeList, this);
        /* Setting the GridView Adapter*/
        recyclerView.setAdapter(noticeBoardAdapter);

        /*Retrieving Notice Details Added By Society Service Admin*/
        retrieveNoticeDetailsFromFirebase();
    }


    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method gets invoked when user tries to see notices when admin has added any notices.
     */
    private void retrieveNoticeDetailsFromFirebase() {
        DatabaseReference noticeBoardReference = Constants.NOTICE_BOARD_REFERENCE;
        noticeBoardReference.keepSynced(true);
        noticeBoardReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    hideProgressIndicator();
                    showFeatureUnavailableLayout(R.string.notice_unavailable);
                } else {
                    hideProgressIndicator();
                    for (DataSnapshot noticeBoardDataSnapshot : dataSnapshot.getChildren()) {
                        String noticeBoardUID = noticeBoardDataSnapshot.getKey();
                        DatabaseReference noticeBoardUIDReference = noticeBoardReference.child(noticeBoardUID);
                        noticeBoardUIDReference.keepSynced(true);
                        noticeBoardUIDReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                NammaApartmentsNotice nammaApartmentsNotice = dataSnapshot.getValue(NammaApartmentsNotice.class);
                                nammaApartmentsNoticeList.add(index++, nammaApartmentsNotice);
                                noticeBoardAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}