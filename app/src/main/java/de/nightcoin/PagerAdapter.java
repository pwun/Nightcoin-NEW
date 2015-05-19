package de.nightcoin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class PagerAdapter extends FragmentPagerAdapter {

    //byte[] image1, image2, image3, image4, image5;
    String uri1, uri2, uri3, uri4, uri5;

    public PagerAdapter(FragmentManager fm, String[] urls) {
        super(fm);
        /*image1 = images.getByteArray("image1");
        image2 = images.getByteArray("image1");
        image3 = images.getByteArray("image1");
        image4 = images.getByteArray("image1");
        image5 = images.getByteArray("image1");*/
        uri1 = urls[0];
        uri2 = urls[1];
        uri3 = urls[2];
        uri4 = urls[3];
        uri5 = urls[4];
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle;
        ImageSliderFragment fragment;

        switch (position){
            case 0:
                System.out.println("POSITION: 1");
                System.out.println("Lade Bild: "+ uri1);
                bundle= new Bundle();
                bundle.putString("image", uri1);
                fragment = new ImageSliderFragment();
                fragment.setArguments(bundle);
                return fragment;
            case 1:
                System.out.println("POSITION: 2");
                System.out.println("Lade Bild: "+ uri2);
                bundle= new Bundle();
                bundle.putString("image", uri2);
                fragment = new ImageSliderFragment();
                fragment.setArguments(bundle);
                return fragment;
            case 2:
                System.out.println("POSITION: 3");
                System.out.println("Lade Bild: "+ uri3);
                bundle= new Bundle();
                bundle.putString("image", uri3);
                fragment = new ImageSliderFragment();
                fragment.setArguments(bundle);
                return fragment;
            case 3:
                System.out.println("POSITION: 4");
                System.out.println("Lade Bild: "+ uri4);
                bundle= new Bundle();
                bundle.putString("image", uri4);
                fragment = new ImageSliderFragment();
                fragment.setArguments(bundle);
                return fragment;
            case 4:
                System.out.println("POSITION: 5");
                System.out.println("Lade Bild: "+ uri5);
                bundle= new Bundle();
                bundle.putString("image", uri5);
                fragment = new ImageSliderFragment();
                fragment.setArguments(bundle);
                return fragment;
            default:
                System.out.println("POSITION: DEFAULT");
                System.out.println("Lade Bild: "+ uri1);
                bundle= new Bundle();
                bundle.putString("image", uri1);
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
