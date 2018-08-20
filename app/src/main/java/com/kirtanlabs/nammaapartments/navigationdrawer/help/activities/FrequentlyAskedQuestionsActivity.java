package com.kirtanlabs.nammaapartments.navigationdrawer.help.activities;

import android.os.Bundle;
import android.widget.ListView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.help.adapters.FrequentlyAskedQuestionsAdapter;
import com.kirtanlabs.nammaapartments.navigationdrawer.help.pojo.FrequentlyAskedQuestionsList;

import java.util.ArrayList;

public class FrequentlyAskedQuestionsActivity extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_frequently_asked_questions;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.frequently_asked_questions;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        ListView listFrequentlyAskedQuestions = findViewById(R.id.listFrequentlyAskedQuestions);

        /*We are initialising list with array list*/
        ArrayList<Object> list = new ArrayList<>();

        /*Adding items in the list*/
        list.add(getString(R.string.account_related));
        list.add(new FrequentlyAskedQuestionsList(getString(R.string.account_delete_query)));
        list.add(new FrequentlyAskedQuestionsList(getString(R.string.account_deactivate_query)));
        list.add(getString(R.string.visitor_related));
        list.add(new FrequentlyAskedQuestionsList(getString(R.string.invite_visitor_query)));
        list.add(new FrequentlyAskedQuestionsList(getString(R.string.visitor_cancel_query)));
        list.add(new FrequentlyAskedQuestionsList(getString(R.string.visitor_reschedule_query)));
        list.add(new FrequentlyAskedQuestionsList(getString(R.string.visitor_notification_query)));
        list.add(getString(R.string.daily_services_related));
        list.add(new FrequentlyAskedQuestionsList(getString(R.string.daily_service_remove_query)));
        list.add(new FrequentlyAskedQuestionsList(getString(R.string.daily_services_reschedule_query)));
        list.add(getString(R.string.flat_members_related));
        list.add(new FrequentlyAskedQuestionsList(getString(R.string.flat_members_query)));
        list.add(new FrequentlyAskedQuestionsList(getString(R.string.flat_members_friend_query)));
        list.add(getString(R.string.handed_things_related));
        list.add(new FrequentlyAskedQuestionsList(getString(R.string.handed_things_query)));
        list.add(getString(R.string.emergency_related));
        list.add(new FrequentlyAskedQuestionsList(getString(R.string.emergency_related_query)));
        list.add(new FrequentlyAskedQuestionsList(getString(R.string.emergency_alarm_query)));
        list.add(new FrequentlyAskedQuestionsList(getString(R.string.emergency_alarm_response_query)));

        /*Creating the Adapter*/
        FrequentlyAskedQuestionsAdapter frequentlyAskedQuestionsAdapter = new FrequentlyAskedQuestionsAdapter(this, list);

        /*Attaching adapter to the listview */
        listFrequentlyAskedQuestions.setAdapter(frequentlyAskedQuestionsAdapter);
    }
}
