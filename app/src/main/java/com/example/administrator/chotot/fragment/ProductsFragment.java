package com.example.administrator.chotot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.chotot.R;
import com.example.administrator.chotot.activity.ListProductsActivity;
import com.example.administrator.chotot.activity.SaleActivity;
import com.example.administrator.chotot.adapter.CategoryAdapter;
import com.example.administrator.chotot.adapter.CategoryAdapter.OnItemClickListener;
import com.example.administrator.chotot.model.Category;
import com.example.administrator.chotot.view.SpacesItemDecoration;

import java.util.ArrayList;

import static com.example.administrator.chotot.activity.MainActivity.phone;

/**
 * Created by Administrator on 15/10/2016.
 */

public class ProductsFragment extends Fragment implements OnClickListener {
    private RecyclerView mRecyCategory;
    private ImageView mImgInvite;
    private FrameLayout mFlSale;

    private ArrayList<Category> mArr;
    private CategoryAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        init(view);
        setListener();
        addList();
        setAdapter();

        return view;
    }

    private void init(View view) {
        mRecyCategory = (RecyclerView) view.findViewById(R.id.recy_category);
        mRecyCategory.setNestedScrollingEnabled(false);
        mImgInvite = (ImageView) view.findViewById(R.id.img_invite);
        mFlSale = (FrameLayout) view.findViewById(R.id.fl_sale);
    }

    public void setListener() {
        mImgInvite.setOnClickListener(this);
        mFlSale.setOnClickListener(this);
    }

    private void addList() {
        mArr = new ArrayList<>();

        Category category = new Category();
        category.setId("1");
        category.setCategory("SamSung");
        category.setUrlImage(R.drawable.samsung);
        mArr.add(category);

        Category category1 = new Category();
        category1.setId("2");
        category1.setCategory("Apple");
        category1.setUrlImage(R.drawable.apple);
        mArr.add(category1);

        Category category2 = new Category();
        category2.setId("3");
        category2.setCategory("Sony");
        category2.setUrlImage(R.drawable.sony);
        mArr.add(category2);

        Category category3 = new Category();
        category3.setId("4");
        category3.setCategory("Nokia");
        category3.setUrlImage(R.drawable.nokia);
        mArr.add(category3);

        Category category4 = new Category();
        category4.setId("5");
        category4.setCategory("LG");
        category4.setUrlImage(R.drawable.lg);
        mArr.add(category4);

        Category category5 = new Category();
        category5.setId("6");
        category5.setCategory("BlackBerry");
        category5.setUrlImage(R.drawable.blackberry);
        mArr.add(category5);

        Category category6 = new Category();
        category6.setId("7");
        category6.setCategory("HTC");
        category6.setUrlImage(R.drawable.htc);
        mArr.add(category6);

        Category category7 = new Category();
        category7.setId("8");
        category7.setCategory("Acer/Asus");
        category7.setUrlImage(R.drawable.asus);
        mArr.add(category7);

        Category category8 = new Category();
        category8.setId("9");
        category8.setCategory("Các hãng khác");
        category8.setUrlImage(R.drawable.more_mobile);
        mArr.add(category8);

        Category category9 = new Category();
        category9.setId("0");
        category9.setCategory("Tất cả danh mục");
        category9.setUrlImage(R.drawable.all_mobile);
        mArr.add(category9);
    }

    private void setAdapter() {
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        manager.setSpanSizeLookup(new SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 3;
                } else {
                    return 1;
                }
            }
        });
        mRecyCategory.setLayoutManager(manager);

        mAdapter = new CategoryAdapter(getContext(), mArr, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("id", mArr.get(position).getId());
                bundle.putString("category", mArr.get(position).getCategory());

                Intent intent = new Intent(getContext(), ListProductsActivity.class);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
        mRecyCategory.setItemAnimator(new DefaultItemAnimator());
        mRecyCategory.setAdapter(mAdapter);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        mRecyCategory.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_invite:

                break;

            case R.id.fl_sale:
                if (phone.equals("")) {
                    Toast.makeText(getContext(), "Vui lòng đăng nhập để đăng sản phẩm", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getContext(), SaleActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }
}
