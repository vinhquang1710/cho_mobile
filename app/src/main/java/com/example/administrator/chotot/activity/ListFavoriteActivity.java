package com.example.administrator.chotot.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.chotot.R;
import com.example.administrator.chotot.adapter.FavoriteAdapter;
import com.example.administrator.chotot.adapter.FavoriteAdapter.OnItemClickListener;
import com.example.administrator.chotot.adapter.FavoriteAdapter.OnItemLongClickListener;
import com.example.administrator.chotot.model.Product;
import com.example.administrator.chotot.view.SpacesItemDecoration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.administrator.chotot.activity.MainActivity.phone;
import static com.example.administrator.chotot.utils.FirebaseConfig.productRef;

/**
 * Created by Administrator on 11/11/2016.
 */

public class ListFavoriteActivity extends AppCompatActivity implements OnClickListener {
    private ImageView mImgBack;
    private RecyclerView mRecyFavorite;
    private TextView mTvAlert;

    private ArrayList<Product> mArr = new ArrayList<>();
    private FavoriteAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_favorite);

        init();
        setListener();

        setAdapter();
    }

    private void init(){
        mImgBack = (ImageView)findViewById(R.id.img_back);
        mRecyFavorite = (RecyclerView)findViewById(R.id.recy_favorite_product);
        mTvAlert = (TextView)findViewById(R.id.tv_alert);
    }

    private void setListener(){
        mImgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
        }
    }

    private void getData(){
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mArr.removeAll(mArr);

                for(DataSnapshot db : dataSnapshot.getChildren()){
                    final String idProduct = db.getKey();
                    final String title = db.child("title").getValue().toString();
                    final String category = db.child("category").getValue().toString();
                    final String city = db.child("city").getValue().toString();
                    final String price = db.child("price").getValue().toString();
                    final String time = db.child("time").getValue().toString();
                    final String idUser = db.child("idUser").getValue().toString();
                    final String imgUrl = db.child("img").getValue().toString();

                    if(db.child("Favorites").getValue() != null){
                        DatabaseReference favRef = db.child("Favorites").getRef();
                        favRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot db : dataSnapshot.getChildren()){
                                    if(db.getKey().equals(phone)){
                                        Product product = new Product();
                                        product.setId(idProduct);
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

                                if(mArr.size() == 0){
                                    mTvAlert.setVisibility(View.VISIBLE);
                                    mRecyFavorite.setVisibility(View.GONE);
                                }else{
                                    mTvAlert.setVisibility(View.GONE);
                                    mRecyFavorite.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
                if(mArr.size() == 0){
                    mTvAlert.setVisibility(View.VISIBLE);
                    mRecyFavorite.setVisibility(View.GONE);
                }else{
                    mTvAlert.setVisibility(View.GONE);
                    mRecyFavorite.setVisibility(View.VISIBLE);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setAdapter(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyFavorite.setLayoutManager(linearLayoutManager);
        mAdapter = new FavoriteAdapter(getApplicationContext(), mArr, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("id", mArr.get(position).getId());
                bundle.putString("phone", mArr.get(position).getIdUser());

                Intent intent = new Intent(getApplicationContext(), DetailProductActivity.class);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        }, new OnItemLongClickListener() {
            @Override
            public void onItemClick(int position) {
                showDialog(mArr.get(position).getId());
            }
        });
        mRecyFavorite.setAdapter(mAdapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.dimen_size_5);
        mRecyFavorite.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mArr.size() == 0){
            mTvAlert.setVisibility(View.VISIBLE);
            mRecyFavorite.setVisibility(View.GONE);
        }else{
            mTvAlert.setVisibility(View.GONE);
            mRecyFavorite.setVisibility(View.VISIBLE);
        }
        getData();
    }

    private void showDialog(final String idProduct){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                switch (choice) {
                    case DialogInterface.BUTTON_POSITIVE:
                        productRef.child(idProduct).child("Favorites").child(phone).removeValue();
                        getData();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn hủy yêu thích tin này?")
                .setPositiveButton("Có", dialogClickListener)
                .setNegativeButton("Không", dialogClickListener).show();
    }
}
