package de.nightcoin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by paul on 19.05.15.
 */
public class ImageSliderFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_imageslider_fragment, container,false);
        ImageView imageview = (ImageView) view.findViewById(R.id.imageViewImageSliderFragment);
        Bundle bundle = getArguments();
        String imageurl = bundle.getString("image");
        if(imageurl.equals("Fehler")){
            imageview.setImageResource(R.drawable.ic_launcher);
        }
        else {
            try {
                imageview.setImageURI(Uri.parse(imageurl));
            } catch (Exception e) {
                imageview.setImageResource(R.drawable.ic_launcher);
            }
        }

        return view;
    }
}