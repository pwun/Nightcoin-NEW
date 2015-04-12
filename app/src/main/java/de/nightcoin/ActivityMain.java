package de.nightcoin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ActivityMain extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initButtons();
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Roboto-Light.ttf"); // font from assets: "assets/fonts/Roboto-Regular.tt
	}

	private void initButtons() {
		Button coins = (Button) findViewById(R.id.buttonMainCoins);
		Button bars = (Button) findViewById(R.id.buttonMainBars);
		Button clubs = (Button) findViewById(R.id.buttonMainClubs);
		Button food = (Button) findViewById(R.id.buttonMainFood);
		Button taxi = (Button) findViewById(R.id.buttonMainTaxi);
		Button favorites = (Button) findViewById(R.id.buttonMainFavorites);
		Button events = (Button) findViewById(R.id.buttonMainEvents);

		coins.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ActivityMain.this, ActivityStandardList.class);
				i.putExtra("input", "Coins");
				ActivityMain.this.startActivity(i);
			}
		});

		bars.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ActivityMain.this,
						ActivityStandardList.class);
				i.putExtra("input", "Bars");
				ActivityMain.this.startActivity(i);
			}
		});

		clubs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ActivityMain.this,
						ActivityStandardList.class);
				i.putExtra("input", "Clubs");
				ActivityMain.this.startActivity(i);
			}
		});

		favorites.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ActivityMain.this,
						ActivityStandardList.class);
				i.putExtra("input", "Favoriten");
				ActivityMain.this.startActivity(i);
			}
		});

		events.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ActivityMain.this,
						ActivityStandardList.class);
				i.putExtra("input", "Events");
				ActivityMain.this.startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			openSettings();
		}
		return super.onOptionsItemSelected(item);
	}

    private void openSettings() {
        Intent i = new Intent(ActivityMain.this, ActivitySettings.class);
        ActivityMain.this.startActivity(i);
    }
}
