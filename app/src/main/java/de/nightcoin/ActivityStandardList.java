package de.nightcoin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.parse.DeleteCallback;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.SaveCallback;

import java.util.Date;
import java.util.List;


public class ActivityStandardList extends ActionBarActivity {

    public StandardListViewAdapter parseAdapter;
    Intent i;
    Menu menu;
    String contentMode;
    private final int END_OF_NIGHT = 6;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadActivity();
    }

    private void loadActivity(){
        setContentView(R.layout.activity_standard_list);
        i = getIntent();
        contentMode = i.getStringExtra("input");

        setCorrectTitle();

        parseAdapter = new StandardListViewAdapter(ActivityStandardList.this, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {
                return requestedQuery(contentMode);
            }
        });

        parseAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<ParseObject>() {
            public void onLoading() {
                progressDialog = new ProgressDialog(ActivityStandardList.this);
                progressDialog.setTitle(contentMode);
                progressDialog.setMessage("Lädt...");
                progressDialog.setIndeterminate(true);
                progressDialog.show();
            }
            @Override
            public void onLoaded(final List<ParseObject> objects, Exception e) {
                if(objects.size()==0){
                    findViewById(R.id.textViewListNoData).setVisibility(View.VISIBLE);
                }
                progressDialog.dismiss();
                ParseObject.unpinAllInBackground(contentMode, new DeleteCallback() {


                    @Override
                    public void done(com.parse.ParseException e) {
                        ParseObject.pinAllInBackground(objects, new SaveCallback() {

                            @Override
                            public void done(com.parse.ParseException e) {
                                System.out.println("Pinned " + objects.size() + " objects successfully");

                            }
                        });
                    }
                });
            }
        });
        ListView listView = (ListView) findViewById(R.id.listViewStandardList);
        listView.setAdapter(parseAdapter);
    }

    private void setCorrectTitle(){
        if(contentMode.equals("Bar")){
            setTitle("Bars");
        } else if(contentMode.equals("Club")){
            setTitle("Clubs");
        }
        else if (contentMode.equals("Favorites")){
            setTitle("Favoriten");
        }
        else{
            setTitle(contentMode);
        }
    }

    public ParseQuery requestedQuery(String contentMode) {
        if (contentMode.equals("Bar") || contentMode.equals("Club") || contentMode.equals("Favorites")|| contentMode.equals("Food")|| contentMode.equals("Taxi")) {
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
            query.fromLocalDatastore();
        } else {
            query.whereEqualTo("category", contentMode);
        }
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
        if (now.getHours() < END_OF_NIGHT) {
            return lastEvening();
        }
        return now;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_standard_list, menu);
        this.menu = menu;
        checkIfUserMode();
        return true;
    }

    private void checkIfUserMode(){
        //System.out.println();
        if(contentMode.equals("Coins")) {
            boolean user = i.getBooleanExtra("userModeActive",false);
            if(user){//usermode abfragen
                menu.findItem(R.id.action_newCoin).setVisible(true);
                menu.findItem(R.id.action_newCoin).setEnabled(true);
            }
            else{
                menu.findItem(R.id.action_Info).setVisible(true);
                menu.findItem(R.id.action_Info).setEnabled(true);
            }
        }
        /*if(menu!=null) {
                            System.out.println(menu);
                            MenuItem item = menu.findItem(R.id.action_newCoin);
                            item.setVisible(true);
                            item.setEnabled(true);
                        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_newCoin) {
            Intent intent = new Intent(ActivityStandardList.this, ActivityEditCoin.class);

            intent.putExtra("isNewCoin", true);
            ActivityStandardList.this.startActivity(intent);
        }
        if(id == R.id.action_refresh){
            loadActivity();
        }
        if (id == R.id.action_Info) {
            new AlertDialog.Builder(ActivityStandardList.this)
                    .setTitle("So funktionieren die Coins")
                    .setMessage("Nightcoins sind Rabatt-Gutscheine, die einfach an der Bar vorgezeigt werden. Sobald du auf 'Einlösen' tippst, startet ein Countdown bis der Coin abläuft. Du solltest coins also immer erst bei der Bestellung einlösen. \n\nJeder Coin ist pro Smartphone nur einmal einlösbar.")
                    .setPositiveButton("Verstanden!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }


}