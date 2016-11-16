package com.example.administrator.chotot.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.chotot.R;
import com.example.administrator.chotot.adapter.AddressAdapter;
import com.example.administrator.chotot.adapter.AddressAdapter.OnItemClickListener;
import com.example.administrator.chotot.view.SpacesItemDecoration;

import java.util.ArrayList;

/**
 * Created by Administrator on 24/10/2016.
 */

public class CityDialog extends Activity {
    private RecyclerView mRecyAddress;

    private String[] stringCity;
    private ArrayList<String> mArr;
    private AddressAdapter mAdapter;

    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_city);

        init();
        setAdapter();
    }

    private void init(){
        Bundle bundle = getIntent().getBundleExtra("bundle");
        city = bundle.getString("city");

        mRecyAddress = (RecyclerView)findViewById(R.id.recy_address);
    }

    private void setAdapter(){
        mArr = new ArrayList<>();
        stringCity = getResources().getStringArray(R.array.city);

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
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }, city);
        mRecyAddress.setAdapter(mAdapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        mRecyAddress.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
    }
}
