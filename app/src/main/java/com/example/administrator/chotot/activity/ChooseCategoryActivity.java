package com.example.administrator.chotot.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.example.administrator.chotot.R;
import com.example.administrator.chotot.adapter.ChooseCategoryAdapter.OnItemClickListener;
import com.example.administrator.chotot.adapter.ChooseCategoryAdapter;
import com.example.administrator.chotot.model.Category;

import java.util.ArrayList;

/**
 * Created by Administrator on 19/10/2016.
 */

public class ChooseCategoryActivity extends AppCompatActivity {
    private RecyclerView mRecyCategory;
    private ImageView mImgBack;

    private ArrayList<Category> mArr;
    private ChooseCategoryAdapter mAdapter;

    private String idCategory, categoryName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);

        init();
        setListener();
        setAdapter();
    }

    private void init(){
        Bundle bundle = getIntent().getBundleExtra("bundle");
        idCategory = bundle.getString("id");
        categoryName = bundle.getString("category");

        mRecyCategory = (RecyclerView)findViewById(R.id.recy_category);
        mImgBack = (ImageView)findViewById(R.id.img_back);
    }

    private void setListener(){
        mImgBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setAdapter(){
        mArr = new ArrayList<>();

        Category cat = new Category();
        cat.setId("0");
        cat.setCategory("Tất cả danh mục");
        mArr.add(cat);

        Category category = new Category();
        category.setId("1");
        category.setCategory("SamSung");
        mArr.add(category);

        Category category1 = new Category();
        category1.setId("2");
        category1.setCategory("Apple");
        mArr.add(category1);

        Category category2 = new Category();
        category2.setId("3");
        category2.setCategory("Sony");
        mArr.add(category2);

        Category category3 = new Category();
        category3.setId("4");
        category3.setCategory("Nokia");
        mArr.add(category3);

        Category category4 = new Category();
        category4.setId("5");
        category4.setCategory("LG");
        mArr.add(category4);

        Category category5 = new Category();
        category5.setId("6");
        category5.setCategory("BlackBerry");
        mArr.add(category5);

        Category category6 = new Category();
        category6.setId("7");
        category6.setCategory("HTC");
        mArr.add(category6);

        Category category7 = new Category();
        category7.setId("8");
        category7.setCategory("Acer/Asus");
        mArr.add(category7);

        Category category8 = new Category();
        category8.setId("9");
        category8.setCategory("Các hãng khác");
        mArr.add(category8);

        mAdapter = new ChooseCategoryAdapter(getApplicationContext(), mArr, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                intent.putExtra("id", mArr.get(position).getId());
                intent.putExtra("category", mArr.get(position).getCategory());
                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        }, categoryName);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        mRecyCategory.setLayoutManager(manager);
        mRecyCategory.setAdapter(mAdapter);
    }
}
