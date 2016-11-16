package com.example.administrator.chotot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.chotot.R;
import com.example.administrator.chotot.adapter.CategoryAdapter.MyViewHolder;
import com.example.administrator.chotot.model.Category;

import java.util.ArrayList;

/**
 * Created by Administrator on 16/10/2016.
 */

public class CategoryAdapter extends Adapter<MyViewHolder>{
    private Context mContext;
    private ArrayList<Category> mArr;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public CategoryAdapter(Context mContext, ArrayList<Category> mArr, OnItemClickListener mListener) {
        this.mContext = mContext;
        this.mArr = mArr;
        this.mListener = mListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recy_category, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(position, mListener);
    }

    @Override
    public int getItemCount() {
        return mArr.size();
    }

    public class MyViewHolder extends ViewHolder{

        private TextView mTvCategory;
        private ImageView mImgCategory;
        private LinearLayout mLnCategory;

        public MyViewHolder(View itemView) {
            super(itemView);

            mTvCategory = (TextView)itemView.findViewById(R.id.tv_category);
            mImgCategory = (ImageView)itemView.findViewById(R.id.img_category);
            mLnCategory = (LinearLayout)itemView.findViewById(R.id.ln_category);
        }

        public void bind(final int position, final OnItemClickListener mListener){
            mTvCategory.setText(mArr.get(position).getCategory());
            mImgCategory.setImageResource(mArr.get(position).getUrlImage());

            mLnCategory.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(position);
                }
            });
        }
    }
}
