package de.nightcoin;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.List;


public class ActivityEditCoin extends ActionBarActivity {

    ParseObject serverObject;
    EditText editDate;
    EditText editValue;
    EditText editAmount;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_coin);
        init();
        setSaveButtonClickListener();
    }

    private void setSaveButtonClickListener() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void init(){
        editDate = (EditText) findViewById(R.id.editTextActivityEditCoinsDate);
        editValue = (EditText) findViewById(R.id.editTextActivityEditCoinsValue);
        editAmount = (EditText) findViewById(R.id.editTextActivityEditCoinsAmount);
        saveButton = (Button) findViewById(R.id.buttonActivityEditCoinsSave);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Coupons");
        query.whereEqualTo("objectId", getIntent().getStringExtra("id"));
        query.setLimit(1);
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                // TODO Auto-generated method stub
                serverObject = objects.get(0);
                editDate.setText(new SimpleDateFormat("dd. MMMM yyyy").format(serverObject.getDate("date")));
                editValue.setText(serverObject.get("value").toString());
                editAmount.setText(serverObject.get("amount").toString());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_edit_coin, menu);
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
