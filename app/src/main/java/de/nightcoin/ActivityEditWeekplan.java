package de.nightcoin;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ActivityEditWeekplan extends ActionBarActivity {

    String[] week = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
    EditText[] edits = new EditText[7];
    ArrayList<String> weekplan = new ArrayList<String>();
    private Activity context;
    private EditWeekplanListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private void init(){
        setContentView(R.layout.activity_edit_weekplan);
        context = this;

        EditText mon = (EditText)findViewById(R.id.editTextEditWeekplanMo);
        edits[0] = mon;
        EditText die = (EditText)findViewById(R.id.editTextEditWeekplanDi);
        edits[1] = die;
        EditText mit = (EditText)findViewById(R.id.editTextEditWeekplanMi);
        edits[2] = mit;
        EditText don = (EditText)findViewById(R.id.editTextEditWeekplanDo);
        edits[3] = don;
        EditText fre = (EditText)findViewById(R.id.editTextEditWeekplanFr);
        edits[4] = fre;
        EditText sam = (EditText)findViewById(R.id.editTextEditWeekplanSa);
        edits[5] = sam;
        EditText son = (EditText)findViewById(R.id.editTextEditWeekplanSo);
        edits[6] = son;

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

                weekplan = (ArrayList<String>) object.get("weekplan");

                for(int i = 0; i < 7; i++){
                    edits[i].setText(weekplan.get(i));
                }

                //adapter = new EditWeekplanListAdapter(context, week, weekplan);
                //ExpandableListView list = (ExpandableListView) findViewById(R.id.expListViewEditWeekplanListView);
                //list.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                //list.setAdapter(adapter);
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
            int counter = 0;
            for (int i = 0; i < 7; i++) {

                System.out.println("EditText: "+ edits[i].getText().toString());
                if(edits[i].getText().toString().trim().length() < 1){
                    counter ++;
                    System.out.println("Leerer Eintrag!!");
                    final EditText finalEdit = edits[i];
                    final int finalI = i;
                    AlertDialog.Builder alert = new AlertDialog.Builder(this)
                    .setTitle("Feld darf nicht leer sein")
                    .setCancelable(false)
                    .setMessage("Das Feld für " + week[i] + " ist leer.\nBitte wähle für diesen Tag ein Inhalt aus oder überarbeite den Eintrag!")
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            init();
                        }
                    })
                    .setPositiveButton("Normaler Betrieb", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("Überarbeiten: Normaler Bertrieb ausgewählt");
                            weekplan.set(finalI, "Normaler Betrieb");
                            finalEdit.setText("Normaler Betrieb");
                            save();
                        }
                    })
                    .setNegativeButton("Überarbeiten", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("Überarbeiten: Cancel");
                            weekplan.set(finalI, "");
                            finalEdit.setText("");
                            save();
                        }
                    })
                    .setNeutralButton("Geschlossen", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("Überarbeiten: Geschlossen ausgewählt");
                            weekplan.set(finalI, "Geschlossen");
                            finalEdit.setText("Geschlossen");
                            save();
                        }
                    });

                    AlertDialog a = alert.create();
                    a.show();

                    Button normal = a.getButton(DialogInterface.BUTTON_POSITIVE);
                    normal.setTextColor(getResources().getColor(R.color.green));
                    Button closed = a.getButton(DialogInterface.BUTTON_NEUTRAL);
                    closed.setTextColor(getResources().getColor(R.color.green));
                }

                weekplan.set(i,edits[i].getText().toString());




                //EditText editText = (EditText)adapter.getChildView(i, 1, true, null, null).findViewById(R.id.editTextEditWeekPlanAdapter);

                //System.out.println("" + editText.getText().toString());

            }

            save();

        }

        return super.onOptionsItemSelected(item);
    }

    private void save(){
        System.out.println("Saving...");
        for(int i = 0; i < 7; i++){
            System.out.println("Saved "+ week[i] + ":" +weekplan.get(i));
        }
    }
}
