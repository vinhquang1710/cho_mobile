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
import com.example.administrator.chotot.adapter.ChooseCategoryAdapter.MyViewHolder;
import com.example.administrator.chotot.model.Category;

import java.util.ArrayList;

/**
 * Created by Administrator on 19/10/2016.
 */

public class ChooseCategoryAdapter extends Adapter<MyViewHolder>{
    private Context mContext;
    private ArrayList<Category> mArr;

    private OnItemClickListener mListener;

    private String id;


    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public ChooseCategoryAdapter(Context mContext, ArrayList<Category> mArr, OnItemClickListener mListener, String id) {
        this.mContext = mContext;
        this.mArr = mArr;
        this.mListener = mListener;
        this.id = id;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recy_radio_button, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(position, mListener, id);
    }

    @Override
    public int getItemCount() {
        return mArr.size();
    }

    public class MyViewHolder extends ViewHolder {
        private TextView mTvCity;
        private ImageView mImgRbnNormal, mImgRbnChecked;
        private LinearLayout mLnCity;

        public MyViewHolder(View itemView) {
            super(itemView);

            mTvCity = (TextView)itemView.findViewById(R.id.tv_content);
            mImgRbnNormal = (ImageView)itemView.findViewById(R.id.img_rbn_normal);
            mImgRbnChecked = (ImageView)itemView.findViewById(R.id.img_rbn_checked);
            mLnCity = (LinearLayout)itemView.findViewById(R.id.ln_content);
        }

        public void bind(final int position, final OnItemClickListener mListener, String id){
            mTvCity.setText(mArr.get(position).getCategory());

            if(mArr.get(position).getCategory().toLowerCase().equals(id.toLowerCase())){
                mImgRbnChecked.setVisibility(View.VISIBLE);
                mImgRbnNormal.setVisibility(View.GONE);
            }else{
                mImgRbnChecked.setVisibility(View.GONE);
                mImgRbnNormal.setVisibility(View.VISIBLE);
            }

            mLnCity.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(position);
                }
            });
        }
    }
}
