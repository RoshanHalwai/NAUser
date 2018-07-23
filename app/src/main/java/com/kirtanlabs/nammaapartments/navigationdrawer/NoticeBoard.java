package com.kirtanlabs.nammaapartments.navigationdrawer;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

public class NoticeBoard extends BaseActivity {
    TextView textTitle;
    View viewLine1;
    TextView textMessage;
    View viewLine2;
    TextView textInCharge;
    TextView textTitle1;
    View viewLine3;
    TextView textMessage1;
    View viewLine4;
    TextView textInCharge1;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_notice_board;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.noticeboard;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        textTitle = findViewById(R.id.textTitle);
        viewLine1 = findViewById(R.id.viewLine1);
        textMessage = findViewById(R.id.textMessage);
        viewLine2 = findViewById(R.id.viewLine2);
        textInCharge = findViewById(R.id.textInCharge);
        textTitle1 = findViewById(R.id.textTitle1);
        viewLine3 = findViewById(R.id.line3);
        textMessage1 = findViewById(R.id.textMessage1);
        viewLine4 = findViewById(R.id.viewLine4);
        textInCharge1 = findViewById(R.id.textInCharge1);

        /*Setting font for all the views*/
        textTitle.setTypeface(Constants.setLatoBlackFont(this));
        textMessage.setTypeface(Constants.setLatoBlackFont(this));
        textInCharge.setTypeface(Constants.setLatoBlackFont(this));
        textTitle1.setTypeface(Constants.setLatoBlackFont(this));
        textMessage1.setTypeface(Constants.setLatoBlackFont(this));
        textInCharge1.setTypeface(Constants.setLatoBlackFont(this));

    }
}
