package com.kirtanlabs.nammaapartments.navigationdrawer.help.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.help.pojo.FrequentlyAskedQuestionsList;

import java.util.ArrayList;

public class FrequentlyAskedQuestionsAdapter extends BaseAdapter {

    private static final int QUESTION_ITEM = 0;
    private static final int HEADER_TITLE = 1;
    private ArrayList<Object> list;
    private LayoutInflater layoutInflater;

    public FrequentlyAskedQuestionsAdapter(Context context, ArrayList<Object> list) {
        this.list = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) instanceof FrequentlyAskedQuestionsList) {
            return QUESTION_ITEM;
        } else {
            return HEADER_TITLE;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            switch (getItemViewType(position)) {
                case QUESTION_ITEM:
                    convertView = layoutInflater.inflate(R.layout.layout_frequently_asked_questions, null);
                    break;
                case HEADER_TITLE:
                    convertView = layoutInflater.inflate(R.layout.list_header_modules, null);
                    break;
            }
        }
        if (convertView != null) {
            switch (getItemViewType(position)) {
                case QUESTION_ITEM:
                    TextView textModuleQuestions = convertView.findViewById(R.id.textModuleQuestions);
                    textModuleQuestions.setText(((FrequentlyAskedQuestionsList) list.get(position)).getQuestionName());
                    break;
                case HEADER_TITLE:
                    TextView textModuleTitle = convertView.findViewById(R.id.textModuleTitle);
                    textModuleTitle.setText(((String) list.get(position)));
                    break;
            }
        }
        return convertView;
    }
}

