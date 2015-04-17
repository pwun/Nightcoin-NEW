package de.nightcoin;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


public class EditWeekplanListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private String[] week = new String[7];
    private ArrayList<String> itemContents = new ArrayList<String>();

    public EditWeekplanListAdapter(Activity context, String[] items, ArrayList<String>itemContents) {
        this.context = context;
        week = items;
        this.itemContents = itemContents;
    }

    @Override
    public int getGroupCount() {
        return week.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return week[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return week[groupPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.edit_weekplan_header_view, null);
        }

        TextView weekplanHeaderTextView = (TextView) convertView.findViewById(R.id.editWeekplanHeaderViewText);
        weekplanHeaderTextView.setText(week[groupPosition]);

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.edit_weekplan_list_adapter, null);
        }

        final EditText editText = (EditText) convertView.findViewById(R.id.editTextEditWeekPlanAdapter);
        editText.setText(itemContents.get(groupPosition));

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("TextWatcherTest" + "afterTextChanged:\t" + s.toString());
                itemContents.set(groupPosition, s.toString());
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}


