package de.nightcoin;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Arrays;


public class ActivityEditPhone extends ActionBarActivity {

    ParseObject serverObject;
    String userLocation;
    EditText phoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone);
        userLocation = ParseUser.getCurrentUser().get("location").toString();
        queryForData();
    }

    private void queryForData() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Locations");
        query.whereEqualTo("name", userLocation);
        query.selectKeys(Arrays.asList("phone"));
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                serverObject = parseObject;
                setPhoneEditText((String) parseObject.get("phone"));
                initButton();
            }
        });
    }

    private void setPhoneEditText(String phoneNumber) {
        phoneEditText = (EditText) findViewById(R.id.editTextActivityEditPhoneNumber);
        phoneEditText.setText(phoneNumber);
    }

    private void initButton() {
        Button saveNumber = (Button) findViewById(R.id.buttonActivityEditPhoneSaveNumber);
        saveNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverObject.put("phone", phoneEditText.getText().toString());
                serverObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast t = Toast.makeText(ActivityEditPhone.this, "Telefonnummer gespeichert", Toast.LENGTH_SHORT);
                        t.show();
                    }
                });
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
