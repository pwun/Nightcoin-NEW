package de.nightcoin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class PagerAdapter extends FragmentPagerAdapter {

    //byte[] image1, image2, image3, image4, image5;
    String url1, url2, url3, url4, url5;

    public PagerAdapter(FragmentManager fm, String[] urls) {
        super(fm);
        /*image1 = images.getByteArray("image1");
        image2 = images.getByteArray("image1");
        image3 = images.getByteArray("image1");
        image4 = images.getByteArray("image1");
        image5 = images.getByteArray("image1");*/
        url1 = urls[0];
        url2 = urls[1];
        url3 = urls[2];
        url4 = urls[3];
        url5 = urls[4];
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle;
        ImageSliderFragment fragment;

        switch (position){
            case 0:
                bundle= new Bundle();
                bundle.putString("image", url1);
                fragment = new ImageSliderFragment();
                fragment.setArguments(bundle);
                return fragment;
            case 1:
                bundle= new Bundle();
                bundle.putString("image", url2);
                fragment = new ImageSliderFragment();
                fragment.setArguments(bundle);
                return fragment;
            case 2:
                bundle= new Bundle();
                bundle.putString("image", url3);
                fragment = new ImageSliderFragment();
                fragment.setArguments(bundle);
                return fragment;
            case 3:
                bundle= new Bundle();
                bundle.putString("image", url4);
                fragment = new ImageSliderFragment();
                fragment.setArguments(bundle);
                return fragment;
            case 4:
                bundle= new Bundle();
                bundle.putString("image", url5);
                fragment = new ImageSliderFragment();
                fragment.setArguments(bundle);
                return fragment;
            default:
                bundle= new Bundle();
                bundle.putString("image", url1);
                fragment = new ImageSliderFragment();
                fragment.setArguments(bundle);
                return fragment;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
