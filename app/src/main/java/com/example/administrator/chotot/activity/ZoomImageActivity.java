package com.example.administrator.chotot.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.example.administrator.chotot.R;
import com.example.administrator.chotot.adapter.ZoomImageSliderAdapter;
import com.example.administrator.chotot.model.ImageProduct;
import com.example.administrator.chotot.view.CirclePageIndicator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.administrator.chotot.utils.FirebaseConfig.productRef;

/**
 * Created by Administrator on 22/10/2016.
 */

public class ZoomImageActivity extends AppCompatActivity implements OnClickListener {
    private LinearLayout mLnClose;
    private ViewPager mVpImage;
    private CirclePageIndicator indicator;

    private ArrayList<ImageProduct> mArr;
    private int positionImage;

    private String idProduct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);

        init();
        setListener();
        addImageProduct();
    }

    private void init(){
        Bundle bundle = getIntent().getBundleExtra("bundle");
        positionImage = bundle.getInt("position");
        idProduct = bundle.getString("id");

        mLnClose = (LinearLayout)findViewById(R.id.ln_close);
        mVpImage = (ViewPager)findViewById(R.id.pager_image);
        indicator = (CirclePageIndicator)findViewById(R.id.indicator);
    }

    private void setListener(){
        mLnClose.setOnClickListener(this);
    }

    private void addImageProduct(){
        mArr = new ArrayList<>();

        productRef.child(idProduct).child("images").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot db : dataSnapshot.getChildren()) {
                    String idImg = db.getKey();

                    ImageProduct imageProduct = new ImageProduct();
                    imageProduct.setId(idImg);
                    imageProduct.setImgUrl(db.child("url").getValue().toString());

                    mArr.add(imageProduct);
                }

                setViewPager();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ln_close:
                finish();
                break;
        }
    }

    private void setViewPager(){
        ZoomImageSliderAdapter pagerAdapter = new ZoomImageSliderAdapter(getSupportFragmentManager());
        String[] mArrImage = new String[mArr.size()];
        for(int i = 0; i < mArr.size(); i++){
            mArrImage[i] = mArr.get(i).getImgUrl();
        }
        pagerAdapter.addAll(Arrays.asList(mArrImage));
        mVpImage.setAdapter(pagerAdapter);
        mVpImage.setCurrentItem(positionImage);
        indicator.setViewPager(mVpImage);
        indicator.onPageSelected(positionImage);
    }
}
