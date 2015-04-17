package de.nightcoin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.Date;


public class ActivityStandardList extends ActionBarActivity {

    public StandardListViewAdapter parseAdapter;
    Intent i;
    String contentMode;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_list);
        i = getIntent();
        contentMode = i.getStringExtra("input");

        parseAdapter = new StandardListViewAdapter(ActivityStandardList.this, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {
                return requestedQuery(contentMode);
            }
        });


        ListView listView = (ListView) findViewById(R.id.listViewStandardList);
        listView.setAdapter(parseAdapter);
    }

    public ParseQuery requestedQuery(String contentMode) {
        if (contentMode.equals("Bar") || contentMode.equals("Club") || contentMode.equals("Favorites")) {
            return locationQuery(contentMode);
        }
        if (contentMode.equals("Events")) {
            return eventQuery();
        }
        if (contentMode.equals("Coins")) {
            return coinQuery();
        }
        return null;
    }

    private ParseQuery locationQuery(String contentMode) {
        ParseQuery query = new ParseQuery("Locations");
        if (contentMode.equals("Favorites")) {
            query.whereEqualTo("favorites", ParseInstallation.getCurrentInstallation().getInstallationId());
        }
        query.whereEqualTo("category", contentMode);
        query.orderByAscending("name");
        return query;
    }

    private ParseQuery eventQuery() {
        ParseQuery query = new ParseQuery("Events");
        query.whereGreaterThan("date", hourbasedDate());
        query.orderByAscending("date");
        return query;
    }

    private ParseQuery coinQuery() {
        ParseQuery query = new ParseQuery("Coupons");
        query.whereGreaterThan("date", hourbasedDate());
        query.orderByAscending("date");
        return query;
    }


    // Utility Methods

    // get date of 12am today
    private Date normalizedDate(Date date) {
        date.setHours(12);
        date.setMinutes(0);
        return date;
    }

    // get date of last evening at 6pm
    private Date lastEvening() {
        Date now = new Date();
        now.setDate(now.getDate() - 1);
        now.setHours(18);
        now.setMinutes(0);
        return now;
    }

    // get date of last evening if it's between midnight and 6am
    private Date hourbasedDate() {
        Date now = new Date();
        if (now.getHours() < 6) {
            return lastEvening();
        }
        return now;
    }


}