package com.example.administrator.chotot.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.example.administrator.chotot.R;
import com.example.administrator.chotot.adapter.AddressAdapter;
import com.example.administrator.chotot.adapter.AddressAdapter.OnItemClickListener;
import com.example.administrator.chotot.view.SpacesItemDecoration;

import java.util.ArrayList;

/**
 * Created by Administrator on 19/10/2016.
 */

public class ChooseAddressActivity extends AppCompatActivity {
    private RecyclerView mRecyAddress;

    private String[] stringCity;
    private ArrayList<String> mArr;
    private AddressAdapter mAdapter;

    private ImageView mImgBack;

    private String city;

    private SharedPreferences pre;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address);

        init();
        setAdapter();
    }

    private void init(){
        Bundle bundle = getIntent().getBundleExtra("bundle");
        city = bundle.getString("city");

        mRecyAddress = (RecyclerView)findViewById(R.id.recy_address);
        mImgBack = (ImageView)findViewById(R.id.img_back);

        mImgBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setAdapter(){
        mArr = new ArrayList<>();
        stringCity = getResources().getStringArray(R.array.city);

        mArr.add("Toàn quốc");

        for(int i = 0; i < stringCity.length; i++){
            mArr.add(stringCity[i]);
        }

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        mRecyAddress.setLayoutManager(manager);
        mAdapter = new AddressAdapter(getApplicationContext(), mArr, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                intent.putExtra("city", mArr.get(position));
                savingPreferences(mArr.get(position));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }, city);
        mRecyAddress.setAdapter(mAdapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        mRecyAddress.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
    }

    private void savingPreferences(String city) {
        pre = getSharedPreferences(LoginActivity.prefname, MODE_PRIVATE);
        editor = pre.edit();

        editor.putString("city", city);
        editor.apply();
    }
}
