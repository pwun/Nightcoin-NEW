package de.nightcoin;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.*;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;


public class ActivityImageSlider extends FragmentActivity {

    ViewPager viewpager;
    String[] uris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        uris = new String[5];
        Intent i = getIntent();
        String name = i.getStringExtra("location");

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Locations");
        query.whereEqualTo("name", name);
        query.fromLocalDatastore();
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                generateUris(list.get(0));
                setContentView(R.layout.activity_image_slider);
                viewpager = (ViewPager) findViewById(R.id.ActivityImageSlider_viewpager);
                android.support.v4.view.PagerAdapter padapter = new PagerAdapter(getSupportFragmentManager(), uris);
                viewpager.setAdapter(padapter);
            }
        });

        super.onCreate(savedInstanceState);

    }

    private void generateUris(ParseObject serverObject){

        //create a file to write bitmap data
        File f = new File(ActivityImageSlider.this.getCacheDir(), "myfile");
        String uristring="";
        try{
            f.createNewFile();
            //Convert bitmap to byte array
            byte[] bitmapdata = serverObject.getParseFile("image").getData();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            Uri uri = Uri.fromFile(f);
            uristring = uri.toString();
        }catch (Exception m){
            uristring = "Fehler";
            System.out.println("Fehler beim File schreiben!");
        }
        uris[0] = uristring;


        //create a file to write bitmap data
        f = new File(ActivityImageSlider.this.getCacheDir(), "myfile2");
        uristring="";
        try{
            f.createNewFile();
            //Convert bitmap to byte array
            byte[] bitmapdata = serverObject.getParseFile("image2").getData();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            Uri uri = Uri.fromFile(f);
            uristring = uri.toString();
        }catch (Exception m){
            uristring = "Fehler";
            System.out.println("Fehler beim File schreiben!");
        }
        uris[1] = uristring;


        //create a file to write bitmap data
        f = new File(ActivityImageSlider.this.getCacheDir(), "myfile3");
        uristring="";
        try{
            f.createNewFile();
            //Convert bitmap to byte array
            byte[] bitmapdata = serverObject.getParseFile("image3").getData();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            Uri uri = Uri.fromFile(f);
            uristring = uri.toString();
        }catch (Exception m){
            uristring = "Fehler";
            System.out.println("Fehler beim File schreiben!");
        }
        uris[2] = uristring;


        //create a file to write bitmap data
        f = new File(ActivityImageSlider.this.getCacheDir(), "myfile4");
        uristring="";
        try{
            f.createNewFile();
            //Convert bitmap to byte array
            byte[] bitmapdata = serverObject.getParseFile("image4").getData();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            Uri uri = Uri.fromFile(f);
            uristring = uri.toString();
        }catch (Exception m){
            uristring = "Fehler";
            System.out.println("Fehler beim File schreiben!");
        }
        uris[3] = uristring;


        //create a file to write bitmap data
        f = new File(ActivityImageSlider.this.getCacheDir(), "myfile5");
        uristring="";
        try{
            f.createNewFile();
            //Convert bitmap to byte array
            byte[] bitmapdata = serverObject.getParseFile("image5").getData();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            Uri uri = Uri.fromFile(f);
            uristring = uri.toString();
        }catch (Exception m){
            uristring = "Fehler";
            System.out.println("Fehler beim File schreiben!");
        }
        uris[4] = uristring;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_image_slider, menu);
        return true;
    }

}
