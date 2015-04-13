package de.nightcoin;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ActivityEditWeekplan extends ActionBarActivity {

    String[] week = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
    String[] weekContent = new String[7];
    private Activity context;
    private EditWeekplanListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_weekplan);
        context = this;

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Locations");
        query.selectKeys(Arrays.asList("weekplan"));
        query.whereEqualTo("name", "Pavo");
        /*query.whereEqualTo("name", ParseUser.getCurrentUser().get("location"));*/
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                // TODO Auto-generated method stub
                ParseObject object = objects.get(0);
                ArrayList<String> weekplan = new ArrayList<String>();
                weekplan = (ArrayList<String>) object.get("weekplan");

                adapter = new EditWeekplanListAdapter(context, week, weekplan);
                ExpandableListView list = (ExpandableListView) findViewById(R.id.expListViewEditWeekplanListView);
                list.setAdapter(adapter);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_edit_weekplan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            for (int i = 1; i < 7; i++) {
                EditText editText = (EditText)adapter.getChildView(i, 1, true, null, null).findViewById(R.id.editTextEditWeekPlanAdapter);

                System.out.println("" + editText.getText().toString());

            }
        }

        return super.onOptionsItemSelected(item);
    }
}
