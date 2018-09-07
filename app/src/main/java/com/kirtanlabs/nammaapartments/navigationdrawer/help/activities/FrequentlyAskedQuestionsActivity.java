package com.kirtanlabs.nammaapartments.navigationdrawer.help.activities;

import android.os.Bundle;
import android.widget.ListView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.help.adapters.FrequentlyAskedQuestionsAdapter;
import com.kirtanlabs.nammaapartments.navigationdrawer.help.pojo.FrequentlyAskedQuestionsList;

import java.util.ArrayList;

import static com.kirtanlabs.nammaapartments.utilities.Constants.CARPENTER_SECTION;
import static com.kirtanlabs.nammaapartments.utilities.Constants.DAILY_SERVICE_SECTION;
import static com.kirtanlabs.nammaapartments.utilities.Constants.ELECTRICIAN_SECTION;
import static com.kirtanlabs.nammaapartments.utilities.Constants.EMERGENCY_SECTION;
import static com.kirtanlabs.nammaapartments.utilities.Constants.EVENT_MANAGEMENT_SECTION;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FLAT_MEMBER_SECTION;
import static com.kirtanlabs.nammaapartments.utilities.Constants.GARBAGE_MANAGEMENT_SECTION;
import static com.kirtanlabs.nammaapartments.utilities.Constants.HANDED_THINGS_SECTION;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PLUMBER_SECTION;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.VISITOR_SECTION;

public class FrequentlyAskedQuestionsActivity extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private ListView listFrequentlyAskedQuestions;
    private ArrayList<Object> faqList;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_frequently_asked_questions;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.faq;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        listFrequentlyAskedQuestions = findViewById(R.id.listFrequentlyAskedQuestions);

        /*We are initialising faqList with arrayList*/
        faqList = new ArrayList<>();

        /*Creating the Adapter*/
        FrequentlyAskedQuestionsAdapter frequentlyAskedQuestionsAdapter = new FrequentlyAskedQuestionsAdapter(this, faqList);

        /*Attaching adapter to the listview */
        listFrequentlyAskedQuestions.setAdapter(frequentlyAskedQuestionsAdapter);

        /*This method adds all the items in the list*/
        addItemsInList();

        /*This method shows appropriate views*/
        showAppropriateViews();

    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method gets invoked when user comes from different screens and we need to display
     * appropriate view according to screen title.
     */
    private void showAppropriateViews() {
        int screenTitle = getIntent().getIntExtra(SCREEN_TITLE, 0);
        switch (screenTitle) {
            case R.string.inviting_visitors:
            case R.string.my_guests:
            case R.string.cab_arrivals:
            case R.string.package_arrivals:
            case R.string.expecting_cab_arrival:
            case R.string.expecting_package_arrival:
                listFrequentlyAskedQuestions.setSelection(VISITOR_SECTION);
                break;
            case R.string.my_daily_services:
            case R.string.add_my_daily_service:
                listFrequentlyAskedQuestions.setSelection(DAILY_SERVICE_SECTION);
                break;
            case R.string.given_things:
            case R.string.history:
                listFrequentlyAskedQuestions.setSelection(HANDED_THINGS_SECTION);
                break;
            case R.string.my_sweet_home:
            case R.string.add_my_family_members:
                listFrequentlyAskedQuestions.setSelection(FLAT_MEMBER_SECTION);
                break;
            case R.string.medical_emergency:
            case R.string.raise_fire_alarm:
            case R.string.raise_theft_alarm:
            case R.string.raise_water_alarm:
                listFrequentlyAskedQuestions.setSelection(EMERGENCY_SECTION);
                break;
            case R.string.plumber:
                listFrequentlyAskedQuestions.setSelection(PLUMBER_SECTION);
                break;
            case R.string.carpenter:
                listFrequentlyAskedQuestions.setSelection(CARPENTER_SECTION);
                break;
            case R.string.electrician:
                listFrequentlyAskedQuestions.setSelection(ELECTRICIAN_SECTION);
                break;
            case R.string.garbage_collection:
                listFrequentlyAskedQuestions.setSelection(GARBAGE_MANAGEMENT_SECTION);
                break;
            case R.string.event_management:
                listFrequentlyAskedQuestions.setSelection(EVENT_MANAGEMENT_SECTION);
                break;
            default:
                addItemsInList();
                break;
        }
    }

    /**
     * This method adds all the items in the list which contains all the frequently asked questions.
     */
    private void addItemsInList() {
        faqList.add(getString(R.string.general_related));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.general_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.change_name_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.change_email_address_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.change_mobile_number_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.change_language_query)));
        faqList.add(getString(R.string.account_related));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.account_delete_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.account_deactivate_query)));
        faqList.add(getString(R.string.visitor_related));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.invite_visitor_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.visitor_cancel_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.visitor_reschedule_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.visitor_notification_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.valid_for_query)));
        faqList.add(getString(R.string.daily_services_related));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.daily_service_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.daily_service_remove_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.daily_services_reschedule_query)));
        faqList.add(getString(R.string.flat_members_related));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.flat_members_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.flat_members_friend_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.flat_member_admin_query)));
        faqList.add(getString(R.string.handed_things_related));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.handed_things_description)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.handed_things_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.guard_handed_things_query)));
        faqList.add(getString(R.string.emergency_related));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.emergency_related_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.emergency_alarm_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.emergency_alarm_response_query)));
        faqList.add(getString(R.string.plumber_related));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.plumber_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.plumber_late_arrive_query)));
        faqList.add(getString(R.string.carpenter_related));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.carpenter_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.carpenter_late_arrive_query)));
        faqList.add(getString(R.string.electrician_related));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.electrician_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.electrician_late_arrive_query)));
        faqList.add(getString(R.string.garbage_management_related));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.garbage_management_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.garbage_query)));
        faqList.add(getString(R.string.event_management_related));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.event_management_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.event_reschedule_query)));
        faqList.add(getString(R.string.notifications_related));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.notifications_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.internet_connectivity_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.notification_issue)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.notification_settings_query)));
        faqList.add(new FrequentlyAskedQuestionsList(getString(R.string.notifications_delay_query)));
    }

}
