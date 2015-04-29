package de.nightcoin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ActivityCoinItemView extends ActionBarActivity {

    String id;
    CoinObject coin = new CoinObject();
    private Handler customHandler = new Handler();
    private long startTime = 0L;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;
    TextView timer;
    Button button;
    String coinLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_item_view);
        id = getIntent().getStringExtra("id");
        timer = (TextView) findViewById(R.id.textViewCoinItemViewTimer);
        init();
        checkIfLimited();
    }

    private void updateLocationStatistics() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Locations");
        query.whereEqualTo("name", coinLocation);
        query.setLimit(1);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.increment("cashedInCoins");
                    parseObject.saveEventually();
                }
            }
        });
    }

    private long normalizedDateInMillis(Date date) {
        date.setHours(12);
        date.setMinutes(0);
        return date.getTime();
    }

    private void checkIfLimited(){}

    private void init(){
        final Button location = (Button) findViewById(R.id.textViewCoinItemViewLocation);
        final TextView date = (TextView) findViewById(R.id.textViewCoinItemViewDate);
        final TextView value = (TextView) findViewById(R.id.textViewCoinItemViewValue);
        button = (Button) findViewById(R.id.buttonCoinItemViewCashIn);


        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Coupons");
        query.fromLocalDatastore();
        query.getInBackground(id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {

                System.out.println(object.getObjectId());

                /*coin.setValue((String) object.get("value"));

                coin.setLocation((String) object.get("location"));*/
                coin.setLimited((Boolean) object.get("limited"));

                coin.setDate((Date) object.get("date"));

                location.setText((String) object.get("location"));
                value.setText((String) object.get("value"));
                date.setText(coin.getDate());

                coinLocation = (String) object.get("location");

                location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(ActivityCoinItemView.this, ActivityStandardItemView.class);
                        //put extras here
                        i.putExtra("name", coinLocation);
                        ActivityCoinItemView.this.startActivity(i);
                    }
                });

                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                cal1.setTime(new Date());
                cal2.setTime(object.getDate("date"));
                boolean sameDay = cal1.get(Calendar.YEAR) == cal2
                        .get(Calendar.YEAR)
                        && cal1.get(Calendar.DAY_OF_YEAR) == cal2
                        .get(Calendar.DAY_OF_YEAR);
                boolean sameNight = cal1.get(Calendar.YEAR) == cal2
                        .get(Calendar.YEAR)
                        && cal1.get(Calendar.DAY_OF_YEAR) == cal2
                        .get(Calendar.DAY_OF_YEAR)+1 && cal1.getTime().getHours() < 5;
                boolean correctEvening = cal1.getTimeInMillis() > normalizedDateInMillis(cal1.getTime());

                if(!coin.isLimited()){
                    if(sameDay && !correctEvening) {
                        button.setBackgroundColor(getResources().getColor(R.color.orange));
                        button.setText("Verfügbar am Abend des " + coin.getDate());
                    } else {
                        button.setBackgroundColor(getResources().getColor(R.color.transparent));
                        button.setTextSize(18);
                        button.setText("Du musst diesen Coin bei der Bestellung nicht vorzeigen.");
                    }
                    timer.setVisibility(View.INVISIBLE);
                    button.setVisibility(View.VISIBLE);
                    return;
                }else if(sameDay||sameNight){
                    if (sameDay) {
                        // Überprüfe ob abends
                        if (!correctEvening) {
                            button.setBackgroundColor(getResources().getColor(R.color.orange));
                            button.setText("Verfügbar am Abend des " + coin.getDate());
                            button.setVisibility(View.VISIBLE);
                            return;
                        }
                    }
                    //Überprüfe ob eingelöst
                    ArrayList<String> cashedInUsers = (ArrayList<String>)object.get("cashedInUsers");
                    int cashedInAmount = object.getInt("cashedInAmount");
                    int amount = object.getInt("amount");
                    if(cashedInUsers!=null){
                        if(cashedInUsers.contains(ParseInstallation.getCurrentInstallation().getInstallationId())){
                            button.setBackgroundColor(getResources().getColor(R.color.dark_red));
                            button.setText("Du hast diesen Coin bereits eingelöst");
                            button.setVisibility(View.VISIBLE);
                        }
                        else if(amount - cashedInAmount <= 0){
                            button.setBackgroundColor(getResources().getColor(R.color.dark_red));
                            button.setText("Limit erreicht: dieser Coin ist nicht mehr verfügbar.");
                            button.setVisibility(View.VISIBLE);
                        }
                        else{
                            button.setVisibility(View.VISIBLE);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startTime = SystemClock.uptimeMillis();
                                    customHandler.postDelayed(updateTimerThread, 0);
                                    cashIn();
                                }
                            });
                        }
                    }
                    else{
                        button.setVisibility(View.VISIBLE);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startTime = SystemClock.uptimeMillis();
                                customHandler.postDelayed(updateTimerThread, 0);
                                cashIn();
                            }
                        });
                    }
                } else{
                    button.setBackgroundColor(getResources().getColor(R.color.orange));
                    button.setText("Verfügbar am " + coin.getDate());
                    button.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            if(mins >= 2){
                timer.setText("Ungültig");
            }
            else{
                secs = secs % 60;
                timer.setText("" + (1-mins) + ":"+ String.format("%02d", (59-secs)));
                customHandler.postDelayed(this, 0);
            }

        }

    };

    private void cashIn(){
        button.setVisibility(View.INVISIBLE);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Coupons");
        query.getInBackground(id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null){
                    System.out.println(id + "=" + parseObject.getObjectId());
                    parseObject.add("cashedInUsers", ParseInstallation.getCurrentInstallation().getInstallationId());
                    parseObject.increment("cashedInAmount");
                    parseObject.saveEventually();
                    updateLocationStatistics();
                    //parseObject.saveInBackground();
                }
                else{
                    System.out.println(e.getStackTrace());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_coin_item_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
