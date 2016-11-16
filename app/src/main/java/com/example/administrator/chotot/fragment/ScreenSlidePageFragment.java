package com.example.administrator.chotot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.chotot.R;
import com.example.administrator.chotot.activity.ZoomImageActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 20/10/2016.
 */

public class ScreenSlidePageFragment extends Fragment {
    private static final String PIC_URL = "screenslidepagefragment.picurl";
    private static final String POSITION = "";
    private static final String PRODUCT = "idProduct";

    public static ScreenSlidePageFragment newInstance(String picUrl, int position, String idProduct) {

        Bundle arguments = new Bundle();
        arguments.putString(PIC_URL, picUrl);
        arguments.putInt(POSITION, position);
        arguments.putString(PRODUCT, idProduct);

        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_zoom_image, container, false);

        ImageView imageView = (ImageView)rootView.findViewById(R.id.image);

        Bundle arguments = getArguments();
        String url = arguments.getString(PIC_URL);
        final int position = arguments.getInt(POSITION);
        final String idProduct = arguments.getString(PRODUCT);

        Picasso.with(getContext()).load(url).into(imageView);

        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putString("id", idProduct);

                Intent intent = new Intent(getContext(), ZoomImageActivity.class);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
