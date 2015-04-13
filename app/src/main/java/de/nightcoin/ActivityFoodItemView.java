package de.nightcoin;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class ActivityFoodItemView extends ActionBarActivity {

    String name;
    ParseObject serverObject;
    StandardObject obj = new StandardObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_view);
        Intent i = getIntent();
        name = i.getStringExtra("title");
        setTitle(name);
        getData(name);
        initButton();
    }

    private void initButton(){
        Button call = (Button)findViewById(R.id.buttonFoodItemViewCall);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel = obj.getTel();

                String uri = "tel:" + tel.trim() ;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
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
                serverObject = objects.get(0);
                obj.setName((String) serverObject.get("name"));
                obj.setImage((ParseFile) serverObject.getParseFile("image"));
                obj.setOpening((ArrayList<String>) serverObject.get("opensAt"));
                obj.setClosing((ArrayList<String>) serverObject.get("closesAt"));
                obj.setAdr((String) serverObject.get("address"));
                obj.setTel((String) serverObject.get("phone"));


                TextView tel = (TextView)findViewById(R.id.textViewFoodItemViewTel);
                if(obj.getTel()!=null){
                    tel.setText("Tel.: "+obj.getTel());
                    System.out.println(obj.getTel());
                }
                else{System.out.println("Fehler bei Telefonnummer");}
                TextView adr = (TextView)findViewById(R.id.textViewFoodItemViewAdress);
                if(obj.getAdr()!=null){
                    adr.setText("Adr.: "+obj.getAdr());
                    System.out.println(obj.getAdr());
                }
                else{
                    System.out.println("Fehler bei der Adresse");
                }

                ImageView img = (ImageView) findViewById(R.id.imageViewFoodItemView);

                    img.setImageBitmap(obj.getImage());


                //img.loadInBackground();
                getOpening();
            }
        });

        //getDailyData();
    }

    private void getOpening() {
        String[] opening = new String[7];
        String[] closing = new String[7];
        opening = obj.getOpening();
        closing = obj.getClosing();

        TextView moOp = (TextView) findViewById(R.id.textViewFoodItemViewMoOpening);
        TextView diOp = (TextView) findViewById(R.id.textViewFoodItemViewDiOpening);
        TextView miOp = (TextView) findViewById(R.id.textViewFoodItemViewMiOpening);
        TextView doOp = (TextView) findViewById(R.id.textViewFoodItemViewDoOpening);
        TextView frOp = (TextView) findViewById(R.id.textViewFoodItemViewFrOpening);
        TextView saOp = (TextView) findViewById(R.id.textViewFoodItemViewSaOpening);
        TextView soOp = (TextView) findViewById(R.id.textViewFoodItemViewSoOpening);
        TextView moCl = (TextView) findViewById(R.id.textViewFoodItemViewMoClosing);
        TextView diCl = (TextView) findViewById(R.id.textViewFoodItemViewDiClosing);
        TextView miCl = (TextView) findViewById(R.id.textViewFoodItemViewMiClosing);
        TextView doCl = (TextView) findViewById(R.id.textViewFoodItemViewDoClosing);
        TextView frCl = (TextView) findViewById(R.id.textViewFoodItemViewFrClosing);
        TextView saCl = (TextView) findViewById(R.id.textViewFoodItemViewSaClosing);
        TextView soCl = (TextView) findViewById(R.id.textViewFoodItemViewSoClosing);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_food_item_view, menu);
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
