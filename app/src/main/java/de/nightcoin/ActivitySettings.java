package de.nightcoin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ActivitySettings extends ActionBarActivity {

    private TextView errorMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initButtons();
    }

    private void initButtons() {

        errorMessageTextView = (TextView)findViewById(R.id.textViewSettingsErrorMessage);
        errorMessageTextView.setVisibility(View.INVISIBLE);

        Button loginButton = (Button)findViewById(R.id.buttonSettingsLogin);
        final EditText usernameTextField = (EditText)findViewById(R.id.textFieldSettingsUsername);
        final EditText passwordTextField = (EditText)findViewById(R.id.textFieldSettingsPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Check if data is entered correctly, log in user and open userActivity
                // if data is wrong show error message
                String username = usernameTextField.getText().toString();
                String password = passwordTextField.getText().toString();

                Intent i = new Intent(ActivitySettings.this,
                        ActivityUser.class);
                i.putExtra("userLocation", "test");
                ActivitySettings.this.startActivity(i);

                /*
                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Intent i = new Intent(ActivitySettings.this,
                                    ActivityUser.class);
                            i.putExtra("userLocation", ParseUser.getCurrentUser().get("location").toString());
                            ActivitySettings.this.startActivity(i);
                            errorMessageTextView.setVisibility(View.INVISIBLE);
                        } else {
                            errorMessageTextView.setVisibility(View.VISIBLE);
                        }
                    }
                });*/
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_settings, menu);
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
