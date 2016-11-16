package com.example.administrator.chotot.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.chotot.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 22/10/2016.
 */

public class ZoomImageSliderFragment extends Fragment {
    private static final String PIC_URL = "screenslidepagefragment.picurl";

    public static ZoomImageSliderFragment newInstance(String picUrl) {

        Bundle arguments = new Bundle();
        arguments.putString(PIC_URL, picUrl);

        ZoomImageSliderFragment fragment = new ZoomImageSliderFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_screen_slider_page, container, false);

        ImageView imageView = (ImageView)rootView.findViewById(R.id.image);

        Bundle arguments = getArguments();
        String url = arguments.getString(PIC_URL);

        Picasso.with(getContext()).load(url).into(imageView);

        return rootView;
    }
}