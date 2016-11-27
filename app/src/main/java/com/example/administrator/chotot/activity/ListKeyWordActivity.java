package com.example.administrator.chotot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.chotot.R;
import com.example.administrator.chotot.adapter.KeyWordAdapter;
import com.example.administrator.chotot.adapter.KeyWordAdapter.OnItemClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.administrator.chotot.activity.MainActivity.phone;
import static com.example.administrator.chotot.utils.FirebaseConfig.userRef;

/**
 * Created by Administrator on 27/11/2016.
 */

public class ListKeyWordActivity extends AppCompatActivity implements OnClickListener {
    private ImageView mImgBack;
    private RecyclerView mRecyKeyWord;
    private TextView mTvAlert;

    private ArrayList<String> mArr = new ArrayList<>();
    private KeyWordAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_keyword);

        init();
        setListener();
        getData();
        setAdapter();
    }

    private void init(){
        mImgBack = (ImageView)findViewById(R.id.img_back);
        mRecyKeyWord = (RecyclerView)findViewById(R.id.recy_keyword);
        mTvAlert = (TextView)findViewById(R.id.tv_alert);

        mTvAlert.setVisibility(View.VISIBLE);
        mRecyKeyWord.setVisibility(View.GONE);
    }

    private void setListener(){
        mImgBack.setOnClickListener(this);
    }

    private void getData(){
        userRef.child(phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("keyword").getValue() != null){
                    DatabaseReference keyword = dataSnapshot.child("keyword").getRef();
                    keyword.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mArr.removeAll(mArr);
                            for(DataSnapshot db : dataSnapshot.getChildren()){
                                mArr.add(db.getKey());
                            }

                            if(mArr.size() > 0){
                                mTvAlert.setVisibility(View.GONE);
                                mRecyKeyWord.setVisibility(View.VISIBLE);
                            }else{
                                mTvAlert.setVisibility(View.VISIBLE);
                                mRecyKeyWord.setVisibility(View.GONE);
                            }

                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setAdapter(){
        getData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyKeyWord.setLayoutManager(linearLayoutManager);

        mAdapter = new KeyWordAdapter(getApplicationContext(), mArr, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("keyword", mArr.get(position));
                bundle.putString("category", "Tất cả danh mục");

                Intent intent = new Intent(getApplicationContext(), ListProductsActivity.class);
                intent.putExtra("bundle", bundle);
                startActivity(intent);

                finish();
            }
        }, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });

        mRecyKeyWord.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
        }
    }
}
