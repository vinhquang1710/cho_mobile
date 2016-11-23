package com.example.administrator.chotot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.chotot.R;
import com.example.administrator.chotot.activity.DetailProductActivity;
import com.example.administrator.chotot.activity.SaleActivity;
import com.example.administrator.chotot.adapter.ProductsAdapter;
import com.example.administrator.chotot.adapter.ProductsAdapter.OnItemClickListener;
import com.example.administrator.chotot.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.administrator.chotot.activity.MainActivity.phone;
import static com.example.administrator.chotot.utils.FirebaseConfig.productRef;

/**
 * Created by Administrator on 17/10/2016.
 */

public class SellingFragment extends Fragment implements OnClickListener {
    private LinearLayout mLnSale;
    private TextView mTvAlert;
    private RecyclerView mRecySelling;

    private ArrayList<Product> mArr = new ArrayList<>();
    private ProductsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selling, container, false);

        init(view);
        setListener();
        setAdapter();

        return view;
    }

    private void init(View view){
        mLnSale = (LinearLayout)view.findViewById(R.id.ln_sale);
        mTvAlert = (TextView)view.findViewById(R.id.tv_alert);
        mRecySelling = (RecyclerView)view.findViewById(R.id.recy_selling_product);
    }

    private void setListener(){
        mLnSale.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ln_sale:
                if(phone.equals("")){
                    Toast.makeText(getContext(), "Vui lòng đăng nhập để đăng sản phẩm", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getContext(), SaleActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    private void getData(){
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mArr.removeAll(mArr);

                for(DataSnapshot db : dataSnapshot.getChildren()){
                    if(db.child("idUser").getValue().toString().equals(phone)){
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

                if(mArr.size() > 0){
                    mRecySelling.setVisibility(View.VISIBLE);
                    mTvAlert.setVisibility(View.GONE);
                }else{
                    mRecySelling.setVisibility(View.GONE);
                    mTvAlert.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setAdapter(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecySelling.setLayoutManager(linearLayoutManager);
        mAdapter = new ProductsAdapter(getContext(), mArr, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("id", mArr.get(position).getId());
                bundle.putString("phone", mArr.get(position).getIdUser());

                Intent intent = new Intent(getContext(), DetailProductActivity.class);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
        mRecySelling.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!phone.equals("")) {
            getData();
        }

        if(mArr.size() > 0){
            mRecySelling.setVisibility(View.VISIBLE);
            mTvAlert.setVisibility(View.GONE);
        }else{
            mRecySelling.setVisibility(View.GONE);
            mTvAlert.setVisibility(View.VISIBLE);
        }
    }
}
