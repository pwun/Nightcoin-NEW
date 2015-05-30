package de.nightcoin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ActivityEditCoin extends ActionBarActivity {

    ParseObject serverObject;
    DatePicker datePicker;
    EditText editValue;
    EditText editAmount;
    Button saveButton;
    boolean isNewCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Calendar calNow = Calendar.getInstance();
        final Calendar calCoin = Calendar.getInstance();
        calNow.setTime(new Date());
        setContentView(R.layout.activity_edit_coin);
        init();
        setSaveButtonClickListener();
        isNewCoin = getIntent().getBooleanExtra("isNewCoin", true);
        if (isNewCoin) {
            datePicker.init(calNow.get(Calendar.YEAR), calNow.get(Calendar.MONTH), calNow.get(Calendar.DAY_OF_MONTH), null);
            editValue.setHint("Wert");
            editAmount.setHint("Anzahl");
        } else {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Coupons");
            query.whereEqualTo("objectId", getIntent().getStringExtra("id"));
            query.setLimit(1);
            query.fromLocalDatastore();
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    // TODO Auto-generated method stub
                    serverObject = objects.get(0);
                    Date objectDate = (serverObject.getDate("date"));
                    calCoin.setTime(objectDate);
                    datePicker.init(calCoin.get(Calendar.YEAR), calCoin.get(Calendar.MONTH), calCoin.get(Calendar.DAY_OF_MONTH), null);
                    editValue.setText(serverObject.get("value").toString());
                    editAmount.setText(serverObject.get("amount").toString());
                }
            });
        }
    }

    private void setSaveButtonClickListener() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calCoin2 = Calendar.getInstance();
                calCoin2.set(Calendar.YEAR, datePicker.getYear());
                calCoin2.set(Calendar.MONTH,datePicker.getMonth());
                calCoin2.set(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth());
                calCoin2.set(Calendar.HOUR_OF_DAY,21);
                Date coinDate = calCoin2.getTime();
                //Date coinDate = new Date((datePicker.getYear()-1900),datePicker.getMonth(),datePicker.getDayOfMonth());
                //coinDate.setHours(21);
                if (isNewCoin) {
                    serverObject = new ParseObject("Coupons");
                }
                serverObject.put("date", coinDate);
                serverObject.put("value", editValue.getText().toString());
                serverObject.put("amount", Integer.parseInt(editAmount.getText().toString()));
                serverObject.put("limited", true);
                serverObject.put("city", "Regensburg");
                serverObject.put("location", ParseUser.getCurrentUser().get("location").toString());
                serverObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast t = Toast.makeText(ActivityEditCoin.this, "Coin wurde erfolgreich gespeichert.", Toast.LENGTH_SHORT);
                            t.show();
                        } else {
                            Toast t = Toast.makeText(ActivityEditCoin.this, "Fehler: Coin konnte nicht gespeichert werden.", Toast.LENGTH_SHORT);
                            t.show();
                        }
                    }
                });
            }
        });
    }


    private void init(){
        datePicker = (DatePicker) findViewById(R.id.datePickerActivityEditCoins);
        //datePicker.setMinDate(new Date().getTime());
        editValue = (EditText) findViewById(R.id.editTextActivityEditCoinsValue);
        editAmount = (EditText) findViewById(R.id.editTextActivityEditCoinsAmount);
        saveButton = (Button) findViewById(R.id.buttonActivityEditCoinsSave);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_edit_coin, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ActivityEditCoin.this, ActivityStandardList.class);
        i.putExtra("input", "Coins");
        i.putExtra("filterMode", "location");
        i.putExtra("usermode", true);
        i.putExtra("locationToFilter", ParseUser.getCurrentUser().getString("location"));
        i.putExtra("userModeActive", true);
        ActivityEditCoin.this.startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            if(serverObject!=null){
                serverObject.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Intent i = new Intent(ActivityEditCoin.this, ActivityStandardList.class);
                            i.putExtra("input", "Coins");
                            i.putExtra("filterMode", "location");
                            i.putExtra("usermode", true);
                            i.putExtra("locationToFilter", ParseUser.getCurrentUser().getString("location"));
                            i.putExtra("userModeActive", true);
                            ActivityEditCoin.this.startActivity(i);
                            Toast t = Toast.makeText(ActivityEditCoin.this, "Coin wurde erfolgreich gelöscht.", Toast.LENGTH_SHORT);
                            t.show();
                        } else {
                            e.printStackTrace();
                            Toast t = Toast.makeText(ActivityEditCoin.this, "Fehler: Coin konnte nicht gelöscht werden.", Toast.LENGTH_SHORT);
                            t.show();
                        }
                    }
                });
            }
            else {
                Toast t = Toast.makeText(ActivityEditCoin.this, "Fehler: Coin konnte nicht gelöscht werden.", Toast.LENGTH_SHORT);
                t.show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
