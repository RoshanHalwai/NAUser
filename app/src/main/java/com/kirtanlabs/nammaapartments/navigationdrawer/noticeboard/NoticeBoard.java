package com.kirtanlabs.nammaapartments.navigationdrawer.noticeboard;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;

public class NoticeBoard extends BaseActivity implements AdapterView.OnClickListener {

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

        /*Getting for grid view*/
        GridView gridViewNoticeBoard = findViewById(R.id.gridViewNoticeBoard);

        // Setting the imageAdapter
        gridViewNoticeBoard.setAdapter(getAdapter());
    }

    @Override
    public void onClick(View view) {

    }

    private NoticeBoardAdapter getAdapter() {
        String[] stringTitle = {
                getString(R.string.ganesha_festival),
                getString(R.string.ganesha_festival),
                getString(R.string.ganesha_festival),
                getString(R.string.ganesha_festival)
        };
        String[] stringMessage = {
                getString(R.string.notice_board_message),
                getString(R.string.notice_board_message),
                getString(R.string.notice_board_message),
                getString(R.string.notice_board_message)
        };
        String[] stringInCharge = {
                getString(R.string.in_charge),
                getString(R.string.in_charge),
                getString(R.string.in_charge),
                getString(R.string.in_charge)
        };
        return new NoticeBoardAdapter(this, stringTitle, stringMessage, stringInCharge);
    }
}