package com.example.administrator.chotot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.chotot.R;
import com.example.administrator.chotot.adapter.ProductsAdapter;
import com.example.administrator.chotot.adapter.ProductsAdapter.OnItemClickListener;
import com.example.administrator.chotot.model.Product;
import com.example.administrator.chotot.view.SpacesItemDecoration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.administrator.chotot.utils.FirebaseConfig.productRef;
import static com.example.administrator.chotot.utils.FirebaseConfig.userRef;

/**
 * Created by Administrator on 24/11/2016.
 */

public class ProfileActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mRecyProduct;
    private TextView mTvFullname;
    private ImageView mImgAvatar;
    private CollapsingToolbarLayout mTbLayout;

    private ArrayList<Product> mArr = new ArrayList<>();
    private ProductsAdapter mAdapter;

    private String idUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        getData();
        setAdapter();
    }

    private void init(){
        Bundle bundle = getIntent().getBundleExtra("bundle");
        idUser = bundle.getString("idUser");

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mRecyProduct = (RecyclerView)findViewById(R.id.recy_product);

        mTvFullname = (TextView)findViewById(R.id.tv_fullname);
        mImgAvatar = (ImageView)findViewById(R.id.img_avatar);
        mTbLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void getData(){
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mArr.removeAll(mArr);

                for(DataSnapshot db : dataSnapshot.getChildren()){
                    if(db.child("idUser").getValue().toString().equals(idUser)){
                        String id = db.getKey();
                        String title = db.child("title").getValue().toString();
                        String category = db.child("category").getValue().toString();
                        String city = db.child("city").getValue().toString();
                        String price = db.child("price").getValue().toString();
                        String time = db.child("time").getValue().toString();
                        String idUser = db.child("idUser").getValue().toString();
                        String imgUrl = db.child("img").getValue().toString();

                        Product product = new Product();
                        product.setId(id);
                        product.setTitle(title);
                        product.setIdCategory(category);
                        product.setCity(city);
                        product.setPrice(price);
                        product.setDate(time);
                        product.setIdUser(idUser);
                        product.setUrlImage(imgUrl);

                        mArr.add(product);
                    }
                }

                Collections.sort(mArr, Product.sortNew);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userRef.child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTvFullname.setText(dataSnapshot.child("fullname").getValue().toString());
                Picasso.with(getApplicationContext()).load(dataSnapshot.child("urlAvatar").getValue().toString())
                        .placeholder(R.drawable.default_avatar)
                        .into(mImgAvatar);
                mTbLayout.setTitle(dataSnapshot.child("fullname").getValue().toString());
                /*getSupportActionBar().setTitle(dataSnapshot.child("fullname").getValue().toString());*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setAdapter(){
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        mRecyProduct.setLayoutManager(manager);
        mAdapter = new ProductsAdapter(getApplicationContext(), mArr, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("id", mArr.get(position).getId());
                bundle.putString("phone", mArr.get(position).getIdUser());

                Intent intent = new Intent(getApplicationContext(), DetailProductActivity.class);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
        mRecyProduct.setAdapter(mAdapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.dimen_size_5);
        mRecyProduct.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
    }
}
