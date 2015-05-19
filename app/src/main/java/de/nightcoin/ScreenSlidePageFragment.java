package de.nightcoin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by paul on 19.05.15.
 */
public class ScreenSlidePageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);

        Bundle bundle = getArguments();
        String Test = bundle.getString("test");
        TextView test = (TextView) rootView.findViewById(R.id.test);

        test.setText(Test);


        return rootView;
    }

}
