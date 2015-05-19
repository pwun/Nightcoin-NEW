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

    ImageView imageview;
    byte[] image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        imageview = (ImageView) getView().findViewById(R.id.imageViewImageSliderFragment);
        Bundle bundle = getArguments();

        image = bundle.getByteArray("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(image,0,image.length);
        imageview.setImageBitmap(bmp);

        return inflater.inflate(R.layout.layout_imageslider_fragment, container,false);
    }
}