package de.nightcoin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityWeekplan extends ActionBarActivity {

	String name;
    int color;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weekplan);
		Intent i = getIntent();
		name = i.getStringExtra("name");
        color = i.getIntExtra("color", 1);
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Locations");
        query.selectKeys(Arrays.asList("weekplan"));
		query.whereEqualTo("name", name);
		query.setLimit(1);
		query.fromLocalDatastore();
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				// TODO Auto-generated method stub
				ParseObject object = objects.get(0);
				ArrayList<String> weekplan = new ArrayList<String>();
				weekplan = (ArrayList<String>) object.get("weekplan");
				ListView listview = (ListView) findViewById(R.id.listViewWeekplan);
				WeekplanAdapter adapter = new WeekplanAdapter(ActivityWeekplan.this, weekplan, color);
				listview.setAdapter(adapter);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_weekplan, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
