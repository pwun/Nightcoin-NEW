package de.nightcoin;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
                i.putExtra("input", "Coins");
                i.putExtra("filterMode", "location");
                i.putExtra("locationToFilter", ParseUser.getCurrentUser().getString("location"));
                i.putExtra("userModeActive", true);
                i.putExtra("usermode", true);
                ActivityUser.this.startActivity(i);
            }
        });

        Button editImagesButton = (Button) findViewById(R.id.buttonUserActivityEditImages);
        editImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityUser.this, ActivityEditImages.class);
                i.putExtra("name", ParseUser.getCurrentUser().get("location").toString());
                ActivityUser.this.startActivity(i);
            }
        });

        Button editEventsButton = (Button) findViewById(R.id.buttonUserActivityEditEvents);
        editEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ActivityUser.this)
                        .setTitle("Sende uns eine Email, um ein Event zu erstellen, zu bearbeiten oder zu löschen.")
                        .setMessage("Wir arbeiten daran, dies schnellstmöglich innerhalb der App zu ermöglichen.")
                        .setPositiveButton("Zur Email-Vorlage", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/html");
                                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"info@nightcoin.de"});
                                intent.putExtra(Intent.EXTRA_SUBJECT, ParseUser.getCurrentUser().get("location").toString() + " - Events");
                                intent.putExtra(Intent.EXTRA_TEXT, "Titel:\n\nDatum & Uhrzeit:\n\nEintritt:\n\nAnzahl zugelassener Reservierungen über Nightcoin:\n\nBeschreibung:\n\nBitte ein Bild im Anhang einfügen.\n");

                                startActivity(Intent.createChooser(intent, "Send Email"));
                                /*Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("message");
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Betreff");
                                intent.putExtra(Intent.EXTRA_TEXT, "Nachricht");
                                Intent mailer = Intent.createChooser(intent, null);
                                startActivity(mailer);*/
                            }
                        })
                        .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ActivityUser.this, ActivityMain.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
