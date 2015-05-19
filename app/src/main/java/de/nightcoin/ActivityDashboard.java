package de.nightcoin;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;


public class ActivityDashboard extends ActionBarActivity {

    private String userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        userLocation = ParseUser.getCurrentUser().get("location").toString();
        queryForData();
    }

    private void queryForData() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Locations");
        query.whereEqualTo("name", userLocation);
        query.selectKeys(Arrays.asList("cashedInCoins", "favorites", "numberOfOpens"));
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                setPageViewsTextView("" + parseObject.get("numberOfOpens"));
                int favorites = ((ArrayList)parseObject.get("favorites")).size();
                setFavoritesTextView("" + favorites);
                setCashedInCoinsTextView("" + parseObject.get("cashedInCoins"));
            }
        });
    }

    private void setPageViewsTextView(String text) {
        TextView pageViewsTextView = (TextView) findViewById(R.id.textViewActivityDashboardPageViews);
        pageViewsTextView.setText(text);
    }

    private void setFavoritesTextView(String text) {
        TextView favoritesTextView = (TextView) findViewById(R.id.textViewActivityDashboardFavorites);
        favoritesTextView.setText(text);
    }

    private void setCashedInCoinsTextView(String text) {
        TextView cashedInCoinsTextView = (TextView) findViewById(R.id.textViewActivityDashboardCashedInCoins);
        cashedInCoinsTextView.setText(text);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            queryForData();
        }

        return super.onOptionsItemSelected(item);
    }
}
