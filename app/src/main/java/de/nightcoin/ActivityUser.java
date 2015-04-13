package de.nightcoin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseUser;


public class ActivityUser extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initButtons();
        Toast t = Toast.makeText(ActivityUser.this, "Angemeldet als " + ParseUser.getCurrentUser().get("location").toString(), Toast.LENGTH_LONG);
        t.show();
    }

    private void initButtons() {
        Button dashboardButton = (Button) findViewById(R.id.buttonUserActivityDashboard);
        dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityUser.this, ActivityDashboard.class);
                ActivityUser.this.startActivity(i);
            }
        });

        Button editPhoneButton = (Button) findViewById(R.id.buttonUserActivityEditPhone);
        editPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityUser.this, ActivityEditPhone.class);
                ActivityUser.this.startActivity(i);
            }
        });

        Button editWeekplanButton = (Button) findViewById(R.id.buttonUserActivityEditWeekplan);
        editWeekplanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityUser.this, ActivityEditWeekplan.class);
                ActivityUser.this.startActivity(i);
            }
        });

        Button editCoinsButton = (Button) findViewById(R.id.buttonUserActivityEditCoins);
        editCoinsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityUser.this, ActivityStandardList.class);
                i.putExtra("input", "nextCoins");
                i.putExtra("userModeActive", true);
                i.putExtra("name", ParseUser.getCurrentUser().get("location").toString());
                ActivityUser.this.startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ActivityUser.this, ActivityMain.class);
        ActivityUser.this.startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser();
            Intent i = new Intent(ActivityUser.this, ActivityMain.class);
            ActivityUser.this.startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
