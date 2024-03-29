package de.nightcoin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


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
        Button map = (Button)findViewById(R.id.buttonFoodItemViewMap);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude = obj.getLat();
                double longitude = obj.getLong();
                try{
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
                    //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+latitude+","+longitude+"?q="+latitude+","+longitude+"("+obj.getName()+")"));

                    ActivityFoodItemView.this.startActivity(intent);
                    System.out.println("Lon/Lat = "+ latitude + "/" + longitude);
                }catch(Exception e){
                    System.out.println("FEHLER! Lon/Lat = "+ latitude + "/" + longitude);
                }
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
                ParseGeoPoint geo = (ParseGeoPoint) serverObject.get("geoData");
                obj.setLat(geo.getLatitude());
                obj.setLong(geo.getLongitude());
                obj.setAdr((String) serverObject.get("address"));
                obj.setTel((String) serverObject.get("phone"));


                TextView tel = (TextView)findViewById(R.id.textViewFoodItemViewTel);
                if(obj.getTel()!=null){
                    tel.setText(""+obj.getTel());
                    System.out.println(obj.getTel());
                }
                else{System.out.println("Fehler bei Telefonnummer");}
                TextView adr = (TextView)findViewById(R.id.textViewFoodItemViewAdress);
                if(obj.getAdr()!=null){
                    adr.setText(""+obj.getAdr());
                    System.out.println(obj.getAdr());
                }
                else{
                    System.out.println("Fehler bei der Adresse");
                }

                /*try {
                    byte[] stream = serverObject.getParseFile("image").getData();
                    Bitmap bmp = BitmapFactory.decodeByteArray(stream, 0, stream.length);
                    int dominantColor = getDominantColor(bmp);
                    ColorDrawable colorDrawable = new ColorDrawable(dominantColor);

                    findViewById(R.id.layoutFoodItemViewBackground).setBackgroundColor(getDominantColor(bmp));
                    findViewById(R.id.textViewFoodItemViewName).setBackgroundColor(dominantColor);
                    findViewById(R.id.textViewFoodItemViewContact).setBackgroundColor(dominantColor);
                    ActivityFoodItemView.this.getSupportActionBar().setBackgroundDrawable(colorDrawable);

                    bmp.recycle();
                    //System.out.println(getSecundaryColorFromColor(getDominantColor(bmp)));
                } catch (Exception ex) {
                    System.out.println("Error getting color");
                }*/


                ImageView img = (ImageView) findViewById(R.id.imageViewFoodItemView);

                    img.setImageBitmap(obj.getImage());



                //img.loadInBackground();
                getOpening();

                try {
                    byte[] stream = serverObject.getParseFile("image").getData();
                    Bitmap bmp = BitmapFactory.decodeByteArray(stream, 0, stream.length);
                    int dominantColor = getDominantColor(bmp);
                    ColorDrawable colorDrawable = new ColorDrawable(dominantColor);

                    /*RelativeLayout layout = (RelativeLayout) findViewById(R.id.layoutStandardItemViewBackground);
                    TextView hours = (TextView) findViewById(R.id.textViewStandardItemViewHours);
                    TextView contact = (TextView) findViewById(R.id.textViewStandardItemViewContact);
                    Button nextCoins = (Button) findViewById(R.id.buttonStandardItemViewNextCoins);
                    Button nextEvents = (Button) findViewById(R.id.buttonStandardItemViewNextEvents);
                    Button weekPlan = (Button) findViewById(R.id.buttonStandardItemViewWeekplan);
                    Button map = (Button)findViewById(R.id.buttonStandardItemViewMap);
                    Button call = (Button)findViewById(R.id.buttonStandardItemViewCall);*/
                    /*call.setBackgroundColor(dominantColor);
                    map.setBackgroundColor(dominantColor);*/

                    ActivityFoodItemView.this.getSupportActionBar().setBackgroundDrawable(colorDrawable);
                    findViewById(R.id.textViewFoodItemViewContact).setBackgroundColor(dominantColor);
                    findViewById(R.id.textViewFoodItemViewName).setBackgroundColor(dominantColor);
                    findViewById(R.id.ViewfoodItemViewSeperator).setBackgroundColor(dominantColor);
                    Button map = (Button)findViewById(R.id.buttonFoodItemViewMap);
                    map.setTextColor(dominantColor);
                    Button call = (Button)findViewById(R.id.buttonFoodItemViewCall);
                    call.setTextColor(dominantColor);
                    //layout.setBackgroundColor(getDominantColor(bmp));
                    stream = null;
                    bmp.recycle();
                    bmp = null;
                    System.gc();
                    //System.out.println(getSecundaryColorFromColor(getDominantColor(bmp)));
                } catch (Exception ex) {
                    System.out.println("Error getting color");
                }
            }
        });

        //getDailyData();
    }



    public static int getDominantColor(Bitmap bitmap) {
        Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        int color = bitmap1.getPixel(0, 0);
        System.out.println("Dominant Color = "+color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        double luma = 0.2126 * r + 0.7152 * g + 0.0722 * b; // per ITU-R BT.709
        System.out.println("Luma: " + luma);
        if(luma > 170){
            System.out.println("Too light");
            color = -7829368;
        }
        else{
            System.out.println("Color ok");
        }
        return color;
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
