package com.example.administrator.chotot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.chotot.R;
import com.example.administrator.chotot.adapter.KeyWordAdapter.MyViewHolder;

import java.util.ArrayList;

/**
 * Created by Administrator on 27/11/2016.
 */

public class KeyWordAdapter extends Adapter<MyViewHolder>{
    private Context mContext;
    private ArrayList<String> mArr;

    private OnItemClickListener mListener, mLongListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public KeyWordAdapter(Context mContext, ArrayList<String> mArr, OnItemClickListener mListener, OnItemClickListener mLongListener) {
        this.mContext = mContext;
        this.mArr = mArr;
        this.mListener = mListener;
        this.mLongListener = mLongListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recy_keyword, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(position, mListener, mLongListener);
    }

    @Override
    public int getItemCount() {
        return mArr.size();
    }

    public class MyViewHolder extends ViewHolder{
        private TextView mTvKeyword;

        public MyViewHolder(View itemView) {
            super(itemView);

            mTvKeyword = (TextView)itemView.findViewById(R.id.tv_keyword);
        }

        public void bind(final int position, final OnItemClickListener mListener, final OnItemClickListener mLongListener){
            mTvKeyword.setText(mArr.get(position));

            mTvKeyword.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(position);
                }
            });

            mTvKeyword.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongListener.onItemClick(position);

                    return true;
                }
            });
        }
    }
}
