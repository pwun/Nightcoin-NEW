package de.nightcoin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ActivityStandardItemView extends ActionBarActivity {

	String name;
	StandardObject obj = new StandardObject();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_standard_item_view);
		Intent i = getIntent();
		name = i.getStringExtra("name");
		initButtons();
		getData(name);
	}

	private void initButtons() {
		Button weekplan = (Button) findViewById(R.id.buttonStandardItemViewWeekplan);

		weekplan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ActivityStandardItemView.this,ActivityWeekplan.class);
				i.putExtra("name", name);
				ActivityStandardItemView.this.startActivity(i);
			}
		});

		Button nextEvents = (Button) findViewById(R.id.buttonStandardItemViewNextEvents);

		nextEvents.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ActivityStandardItemView.this,ActivityStandardList.class);
				i.putExtra("name", name);
				i.putExtra("input", "nextEvents");
				ActivityStandardItemView.this.startActivity(i);
			}
		});
		
		Button nextCoins = (Button) findViewById(R.id.buttonStandardItemViewNextCoins);

		nextCoins.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ActivityStandardItemView.this,ActivityStandardList.class);
				i.putExtra("name", name);
				i.putExtra("input", "nextCoins");
				ActivityStandardItemView.this.startActivity(i);
			}
		});
	}

	private void getData(String name) {
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Locations");
		query.whereEqualTo("name", name);
		query.setLimit(1);
		query.fromLocalDatastore();
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				// TODO Auto-generated method stub
				System.out.println("Creating " + objects.get(0).get("name"));
				ParseObject object = objects.get(0);
				obj.setName((String) object.get("name"));
				obj.setImage((ParseFile) object.getParseFile("image"));
				obj.setWeekplan((ArrayList<String>) object.get("weekplan"));
				obj.setOpening((ArrayList<String>) object.get("opensAt"));
				obj.setClosing((ArrayList<String>) object.get("closesAt"));

				ParseImageView img = (ParseImageView) findViewById(R.id.imageViewStandardItemView);
				img.setParseFile(obj.getImage());
				img.loadInBackground();

				getOpening();

			}
		});

		getDailyData();
	}

	private void getOpening() {
		String[] opening = new String[7];
		String[] closing = new String[7];
		opening = obj.getOpening();
		closing = obj.getClosing();

		TextView moOp = (TextView) findViewById(R.id.textViewStandardItemViewMoOpening);
		TextView diOp = (TextView) findViewById(R.id.textViewStandardItemViewDiOpening);
		TextView miOp = (TextView) findViewById(R.id.textViewStandardItemViewMiOpening);
		TextView doOp = (TextView) findViewById(R.id.textViewStandardItemViewDoOpening);
		TextView frOp = (TextView) findViewById(R.id.textViewStandardItemViewFrOpening);
		TextView saOp = (TextView) findViewById(R.id.textViewStandardItemViewSaOpening);
		TextView soOp = (TextView) findViewById(R.id.textViewStandardItemViewSoOpening);
		TextView moCl = (TextView) findViewById(R.id.textViewStandardItemViewMoClosing);
		TextView diCl = (TextView) findViewById(R.id.textViewStandardItemViewDiClosing);
		TextView miCl = (TextView) findViewById(R.id.textViewStandardItemViewMiClosing);
		TextView doCl = (TextView) findViewById(R.id.textViewStandardItemViewDoClosing);
		TextView frCl = (TextView) findViewById(R.id.textViewStandardItemViewFrClosing);
		TextView saCl = (TextView) findViewById(R.id.textViewStandardItemViewSaClosing);
		TextView soCl = (TextView) findViewById(R.id.textViewStandardItemViewSoClosing);

		moOp.setText(opening[0]);
		diOp.setText(opening[1]);
		miOp.setText(opening[2]);
		doOp.setText(opening[3]);
		frOp.setText(opening[4]);
		saOp.setText(opening[5]);
		soOp.setText(opening[6]);
		moCl.setText(closing[0]);
		diCl.setText(closing[1]);
		miCl.setText(closing[2]);
		doCl.setText(closing[3]);
		frCl.setText(closing[4]);
		saCl.setText(closing[5]);
		soCl.setText(closing[6]);

	}

	private int normalizeWeekday(Date date) {
		// Stimmt das auch?
		// 1 = Montag So = 0 oder 7?
		if (date.getDay() == 0) {
			return 6;
		} else {
			return date.getDay() - 1;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_standard_item_view, menu);
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

	private void getDailyData() {
		Date d = new Date();
		d.setHours(1);
		d.setMinutes(0);
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Events");
		query.whereEqualTo("location", name);
		query.whereGreaterThan("date", d);
		query.orderByAscending("date");
		query.getFirstInBackground(new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject object, ParseException e) {
				if (object != null) {
					Calendar cal1 = Calendar.getInstance();
					Calendar cal2 = Calendar.getInstance();
					cal1.setTime(new Date());
					cal2.setTime(object.getDate("date"));
					boolean sameDay = cal1.get(Calendar.YEAR) == cal2
							.get(Calendar.YEAR)
							&& cal1.get(Calendar.DAY_OF_YEAR) == cal2
									.get(Calendar.DAY_OF_YEAR);

					if (sameDay) {
						TextView title = (TextView) findViewById(R.id.textViewStandardItemViewTodaysEvents);
						title.setText((CharSequence) object.get("title"));
						TextView detail = (TextView) findViewById(R.id.textViewStandardItemViewTodaysEventsDetail);
						detail.setText("Eintritt: " + object.get("price"));
					} else {
						TextView title = (TextView) findViewById(R.id.textViewStandardItemViewTodaysEvents);
						title.setText("jeden "
								+ new SimpleDateFormat("EEEE").format(cal1
										.getTime()) + ":");
						TextView detail = (TextView) findViewById(R.id.textViewStandardItemViewTodaysEventsDetail);
						detail.setText(obj.getWeekplan()[normalizeWeekday(cal1
								.getTime())]);
					}
				} else {
					TextView title = (TextView) findViewById(R.id.textViewStandardItemViewTodaysEvents);
					title.setText("jeden "
							+ new SimpleDateFormat("EEEE").format(new Date())
							+ ":");
					TextView detail = (TextView) findViewById(R.id.textViewStandardItemViewTodaysEventsDetail);
					detail.setText(obj.getWeekplan()[normalizeWeekday(new Date())]);
					System.out.println("Object == null");
				}
			}
		});

		query = new ParseQuery<ParseObject>("Coupons");
		query.whereEqualTo("location", name);
		query.whereGreaterThan("date", d);
		query.orderByAscending("date");
		query.getFirstInBackground(new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject object, ParseException e) {
				if (object != null) {
					Calendar cal1 = Calendar.getInstance();
					Calendar cal2 = Calendar.getInstance();
					cal1.setTime(new Date());
					cal2.setTime(object.getDate("date"));
					boolean sameDay = cal1.get(Calendar.YEAR) == cal2
							.get(Calendar.YEAR)
							&& cal1.get(Calendar.DAY_OF_YEAR) == cal2
									.get(Calendar.DAY_OF_YEAR);

					if (sameDay) {
						TextView title = (TextView) findViewById(R.id.textViewStandardItemViewTodaysCoins);
						title.setText("1 Coin verfügbar");
						TextView detail = (TextView) findViewById(R.id.textViewStandardItemViewTodaysCoinsDetail);
						detail.setText("" + object.get("value"));
					} else {
						TextView title = (TextView) findViewById(R.id.textViewStandardItemViewTodaysCoins);
						title.setText("kein Coin verfügbar");
					}

				} else {
					TextView title = (TextView) findViewById(R.id.textViewStandardItemViewTodaysCoins);
					title.setText("kein Coin verfügbar");
				}
			}
		});
	}
}
