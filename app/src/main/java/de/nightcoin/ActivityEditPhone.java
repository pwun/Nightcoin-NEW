package de.nightcoin;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Arrays;


public class ActivityEditPhone extends ActionBarActivity {

    ParseObject serverObject;
    String userLocation;
    TextView phoneTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone);
        initButton();
        userLocation = "Pavo";
        queryForData();
    }

    private void queryForData() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Locations");
        query.whereEqualTo("name", userLocation);
        query.selectKeys(Arrays.asList("phone"));
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                setPhoneTextView((String) parseObject.get("phone"));
            }
        });
    }

    private void setPhoneTextView(String phoneNumber) {
        phoneTextView = (TextView) findViewById(R.id.textViewActivityEditPhoneNumber);
        phoneTextView.setText(phoneNumber);
    }

    private void initButton() {
        Button dashboardButton = (Button) findViewById(R.id.buttonUserActivityDashboard);
        dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverObject.put("phone", phoneTextView.getText());
                serverObject.saveInBackground();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_edit_phone, menu);
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
