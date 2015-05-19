package de.nightcoin;

import android.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.*;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ActivityImageSlider extends FragmentActivity {

    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        Bundle images = i.getBundleExtra("bundle");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slider);
        viewpager = (ViewPager) findViewById(R.id.ActivityImageSlider_viewpager);
        android.support.v4.view.PagerAdapter padapter = new PagerAdapter(getSupportFragmentManager(), images);
        viewpager.setAdapter(padapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_image_slider, menu);
        return true;
    }

}
