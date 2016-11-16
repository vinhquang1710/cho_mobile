package com.example.administrator.chotot.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.chotot.R;
import com.example.administrator.chotot.adapter.ProductsAdapter;
import com.example.administrator.chotot.adapter.ProductsAdapter.OnItemClickListener;
import com.example.administrator.chotot.model.Product;
import com.example.administrator.chotot.utils.StringUtils;
import com.example.administrator.chotot.view.SpacesItemDecoration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.administrator.chotot.utils.FirebaseConfig.productRef;

/**
 * Created by Administrator on 19/10/2016.
 */

public class ListProductsActivity extends AppCompatActivity implements OnQueryTextListener, OnClickListener {
    private Toolbar toolbar;
    private SearchView searchView;

    private LinearLayout mLnAddress, mLnCategory, mLnRefine;
    private TextView mTvAddress, mTvCategory, mTvRefine, mTvAlert;

    private ProgressDialog mProgress;

    private RecyclerView mRecyProducts;
    private ArrayList<Product> mArr = new ArrayList<>();
    private ArrayList<Product> mArrCat = new ArrayList<>();
    private ArrayList<Product> mArrCity = new ArrayList<>();
    private ProductsAdapter mAdapter;

    private String idCategory, category, city, keyWord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);

        init();
        setListener();

        setAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    private void init() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        idCategory = bundle.getString("id");
        category = bundle.getString("category");
        city = MainActivity.city;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(category);

        mLnAddress = (LinearLayout) findViewById(R.id.ln_address);
        mLnCategory = (LinearLayout) findViewById(R.id.ln_category);
        mLnRefine = (LinearLayout) findViewById(R.id.ln_refine);

        mTvAddress = (TextView) findViewById(R.id.tv_address);
        mTvCategory = (TextView) findViewById(R.id.tv_category);
        mTvRefine = (TextView) findViewById(R.id.tv_refine);
        mTvAlert = (TextView) findViewById(R.id.tv_alert);

        mRecyProducts = (RecyclerView) findViewById(R.id.recy_products);

        mTvCategory.setText(category);
        mTvAddress.setText(MainActivity.city);
        keyWord = "";

        mProgress = new ProgressDialog(this);
    }

    private void setListener() {
        mLnAddress.setOnClickListener(this);
        mLnCategory.setOnClickListener(this);
        mLnRefine.setOnClickListener(this);
    }

    private void setData() {
        mProgress.setMessage("Đang tải dữ liệu...");
        mProgress.show();

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mArr.removeAll(mArr);
                productRef.keepSynced(true);
                for (DataSnapshot db : dataSnapshot.getChildren()) {
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
                searchProduct(city, category, keyWord);
                /*sortProduct(city, category);*/
                mProgress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        mRecyProducts.setLayoutManager(manager);
        mAdapter = new ProductsAdapter(getApplicationContext(), mArrCat, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("id", mArrCat.get(position).getId());
                bundle.putString("phone", mArrCat.get(position).getIdUser());

                Intent intent = new Intent(getApplicationContext(), DetailProductActivity.class);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
        mRecyProducts.setAdapter(mAdapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.dimen_size_5);
        mRecyProducts.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search_view);

        searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Tìm kiếm...");
        changeSearchViewTextColor(searchView);
        keyWord = searchView.getQuery().toString();

        ImageView mImgClose = (ImageView)searchView.findViewById(R.id.search_close_btn);
        mImgClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery("", false);
                keyWord = "";
                searchProduct(city, category, keyWord);
            }
        });

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                keyWord = "";
                searchProduct(city, category, keyWord);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.BLACK);
                ((TextView) view).setHintTextColor(getResources().getColor(R.color.colorTextHint));
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        keyWord = StringUtils.removeAccent(query);
        searchProduct(mTvAddress.getText().toString(), mTvCategory.getText().toString(), keyWord);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ln_address:
                Bundle bundleAddress = new Bundle();
                bundleAddress.putString("city", mTvAddress.getText().toString());

                Intent intentAddress = new Intent(getApplicationContext(), ChooseAddressActivity.class);
                intentAddress.putExtra("bundle", bundleAddress);
                startActivityForResult(intentAddress, 1);
                break;

            case R.id.ln_category:
                Bundle bundleCat = new Bundle();
                bundleCat.putString("category", mTvCategory.getText().toString());

                Intent intentCat = new Intent(getApplicationContext(), ChooseCategoryActivity.class);
                intentCat.putExtra("bundle", bundleCat);
                startActivityForResult(intentCat, 2);
                break;

            case R.id.ln_refine:
                showDialog();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                mProgress.setMessage("Đang tải dữ liệu...");
                mProgress.show();

                city = data.getStringExtra("city");
                mTvAddress.setText(city);

                searchProduct(city, category, StringUtils.removeAccent(keyWord));
                /*sortProduct(city, category);*/
                mProgress.dismiss();
            }
        }

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                mProgress.setMessage("Đang tải dữ liệu...");
                mProgress.show();

                category = data.getStringExtra("category");
                mTvCategory.setText(category);
                idCategory = data.getStringExtra("id");
                getSupportActionBar().setTitle(category);

                searchProduct(city, category, StringUtils.removeAccent(keyWord));
                /*sortProduct(city, category);*/
                mProgress.dismiss();
            }
        }
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(ListProductsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_refine);

        LinearLayout mLnNew = (LinearLayout) dialog.findViewById(R.id.ln_new_products);
        LinearLayout mLnCheap = (LinearLayout) dialog.findViewById(R.id.ln_cheap_products);

        final ImageView mImgRbnNew = (ImageView) dialog.findViewById(R.id.img_rbn_new_normal);
        final ImageView mImgRbnNewChecked = (ImageView) dialog.findViewById(R.id.img_rbn_new_checked);
        final ImageView mImgRbnCheap = (ImageView) dialog.findViewById(R.id.img_rbn_cheap_normal);
        final ImageView mImgRbnCheapChecked = (ImageView) dialog.findViewById(R.id.img_rbn_cheap_checked);

        String refine = mTvRefine.getText().toString();
        if (refine.equals("Mới nhất")) {
            mImgRbnNew.setVisibility(View.GONE);
            mImgRbnNewChecked.setVisibility(View.VISIBLE);

            mImgRbnCheap.setVisibility(View.VISIBLE);
            mImgRbnCheapChecked.setVisibility(View.GONE);
        } else if (refine.equals("Rẻ nhất")) {
            mImgRbnNew.setVisibility(View.VISIBLE);
            mImgRbnNewChecked.setVisibility(View.GONE);

            mImgRbnCheap.setVisibility(View.GONE);
            mImgRbnCheapChecked.setVisibility(View.VISIBLE);
        }

        mLnNew.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setMessage("Đang tải dữ liệu...");
                mProgress.show();

                mImgRbnNew.setVisibility(View.GONE);
                mImgRbnNewChecked.setVisibility(View.VISIBLE);

                mImgRbnCheap.setVisibility(View.VISIBLE);
                mImgRbnCheapChecked.setVisibility(View.GONE);

                mTvRefine.setText("Mới nhất");

                searchProduct(city, category, StringUtils.removeAccent(keyWord));
                /*sortProduct(city, category);*/

                dialog.dismiss();
                mProgress.dismiss();
            }
        });

        mLnCheap.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setMessage("Đang tải dữ liệu...");
                mProgress.show();

                mImgRbnNew.setVisibility(View.VISIBLE);
                mImgRbnNewChecked.setVisibility(View.GONE);

                mImgRbnCheap.setVisibility(View.GONE);
                mImgRbnCheapChecked.setVisibility(View.VISIBLE);

                mTvRefine.setText("Rẻ nhất");

                searchProduct(city, category, StringUtils.removeAccent(keyWord));
                /*sortProduct(city, category);*/

                dialog.dismiss();
                mProgress.dismiss();
            }
        });

        dialog.show();
    }

    private void sortProduct(String city, String category) {

        mArrCity.removeAll(mArrCity);
        for (int i = 0; i < mArr.size(); i++) {
            if (city.equals("Toàn quốc")) {
                mArrCity.add(mArr.get(i));
            } else {
                if (mArr.get(i).getCity().equals(city)) {
                    mArrCity.add(mArr.get(i));
                }
            }
        }

        mArrCat.removeAll(mArrCat);
        for (int i = 0; i < mArrCity.size(); i++) {
            if (category.equals("Tất cả danh mục")) {
                mArrCat.add(mArrCity.get(i));
            } else {
                if (mArrCity.get(i).getIdCategory().equals(category)) {
                    mArrCat.add(mArrCity.get(i));
                }
            }
        }

        if (mTvRefine.getText().toString().equals("Mới nhất")) {
            Collections.sort(mArrCat, Product.sortNew);
        } else if (mTvRefine.getText().toString().equals("Rẻ nhất")) {
            Collections.sort(mArrCat, Product.sortStatus);
        }

        mAdapter.notifyDataSetChanged();

        if (mArrCat.size() == 0) {
            mTvAlert.setVisibility(View.VISIBLE);
            mRecyProducts.setVisibility(View.GONE);
        } else {
            mTvAlert.setVisibility(View.GONE);
            mRecyProducts.setVisibility(View.VISIBLE);
        }
    }

    private void searchProduct(String city, String category, String keyword) {
        mArrCity.removeAll(mArrCity);
        for (int i = 0; i < mArr.size(); i++) {
            String title = StringUtils.removeAccent(mArr.get(i).getTitle()).toLowerCase();
            if (city.equals("Toàn quốc")) {
                if(keyword.equals("")){
                    mArrCity.add(mArr.get(i));
                }else {
                    if (title.contains(keyword.toLowerCase())) {
                        mArrCity.add(mArr.get(i));
                    }
                }
            } else {
                if (mArr.get(i).getCity().equals(city)) {
                    if(keyword.equals("")){
                        mArrCity.add(mArr.get(i));
                    }else {
                        if (title.contains(keyword.toLowerCase())) {
                            mArrCity.add(mArr.get(i));
                        }
                    }
                }
            }
        }

        mArrCat.removeAll(mArrCat);
        for (int i = 0; i < mArrCity.size(); i++) {
            String title = StringUtils.removeAccent(mArrCity.get(i).getTitle()).toLowerCase();
            if (category.equals("Tất cả danh mục")) {
                if(keyword.equals("")){
                    mArrCat.add(mArrCity.get(i));
                }else{
                    if (title.contains(keyword.toLowerCase())) {
                        mArrCat.add(mArrCity.get(i));
                    }
                }
            } else {
                if (mArrCity.get(i).getIdCategory().equals(category)) {
                    if(keyword.equals("")){
                        mArrCat.add(mArrCity.get(i));
                    }else{
                        if (title.contains(keyword.toLowerCase())) {
                            mArrCat.add(mArrCity.get(i));
                        }
                    }
                }
            }
        }

        if (mTvRefine.getText().toString().equals("Mới nhất")) {
            Collections.sort(mArrCat, Product.sortNew);
        } else if (mTvRefine.getText().toString().equals("Rẻ nhất")) {
            Collections.sort(mArrCat, Product.sortStatus);
        }

        mAdapter.notifyDataSetChanged();

        if (mArrCat.size() == 0) {
            mTvAlert.setVisibility(View.VISIBLE);
            mRecyProducts.setVisibility(View.GONE);
        } else {
            mTvAlert.setVisibility(View.GONE);
            mRecyProducts.setVisibility(View.VISIBLE);
        }
    }
}
