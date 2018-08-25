package com.kirtanlabs.nammaapartments.navigationdrawer.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import static com.kirtanlabs.nammaapartments.utilities.Constants.LANGUAGE;

public class LanguageList extends BaseActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private ListView listView;
    private ArrayAdapter<String> adapter;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_society_service_problem_list;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.choose_language;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Here we are hiding the keyboard on launch of screen since user has to see the UI first.*/
        hideKeyboard();

        /*Getting Id's for all the views*/
        SearchView searchProblem = findViewById(R.id.searchProblem);
        listView = findViewById(R.id.listView);

        String[] problemsList = getResources().getStringArray(R.array.change_languages_list);

        /*Setting font for all the items in the list*/
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, problemsList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textProblem = view.findViewById(android.R.id.text1);
                textProblem.setTypeface(Constants.setLatoRegularFont(LanguageList.this));
                return view;
            }
        };
        /*Setting adapter to list view*/
        listView.setAdapter(adapter);

        /*Setting listeners to the view*/
        searchProblem.setOnQueryTextListener(this);
        listView.setOnItemClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnQueryText and OnItemClick Listener Methods
     * ------------------------------------------------------------- */

    @Override
    public boolean onQueryTextSubmit(String query) {
        returnSelectedLanguage(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        returnSelectedLanguage((String) listView.getItemAtPosition(position));
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to send problem back to Society Service Screen that user has selected from the list.
     *
     * @param language that is selected by the user.
     */
    private void returnSelectedLanguage(String language) {
        Intent intent = new Intent();
        intent.putExtra(LANGUAGE, language);
        setResult(RESULT_OK, intent);
        finish();
    }
}
