package com.example.administrator.chotot.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.chotot.R;
import com.example.administrator.chotot.adapter.FavoriteAdapter.MyViewHolder;
import com.example.administrator.chotot.model.Product;
import com.example.administrator.chotot.utils.FormatMoney;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Administrator on 11/11/2016.
 */

public class FavoriteAdapter extends Adapter<MyViewHolder>{
    private Context mContext;
    private ArrayList<Product> mArr;

    private OnItemClickListener mListener;
    private OnItemLongClickListener mLongListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener{
        void onItemClick(int position);
    }

    public FavoriteAdapter(Context mContext, ArrayList<Product> mArr, OnItemClickListener mListener, OnItemLongClickListener mLongListener) {
        this.mContext = mContext;
        this.mArr = mArr;
        this.mListener = mListener;
        this.mLongListener = mLongListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recy_products, parent, false);

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

    public class MyViewHolder extends ViewHolder {
        private ImageView mImgProduct;
        private TextView mTvTitle, mTvPrice, mTvTime;
        private CardView mCvProduct;

        public MyViewHolder(View itemView) {
            super(itemView);

            mImgProduct = (ImageView)itemView.findViewById(R.id.img_product);
            mTvTitle = (TextView)itemView.findViewById(R.id.tv_title_product);
            mTvPrice = (TextView)itemView.findViewById(R.id.tv_price);
            mTvTime = (TextView)itemView.findViewById(R.id.tv_time);
            mCvProduct = (CardView)itemView.findViewById(R.id.cv_product);
        }

        public void bind(final int position, final OnItemClickListener mListener, final OnItemLongClickListener mLongListener){
            mTvTitle.setText(mArr.get(position).getTitle());

            int price = Integer.parseInt(mArr.get(position).getPrice());
            mTvPrice.setText(FormatMoney.format(price) + " đ");

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
            mTvTime.setText(calculateTime(mArr.get(position).getDate()));

            mCvProduct.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(position);
                }
            });

            mCvProduct.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongListener.onItemClick(position);
                    return false;
                }
            });

            Picasso.with(mContext).load(mArr.get(position).getUrlImage())
                    .placeholder(R.drawable.icon)
                    .into(mImgProduct);
        }

        private String calculateTime(String startTime) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));

            Long millisEndTime = System.currentTimeMillis();
            Long millisStartTime = Long.parseLong(startTime);

            String kq = "";
            String startDate = dateFormat.format(new Date(millisStartTime)).replace("-", " ");
            String endTime = dateFormat.format(new Date(millisEndTime)).replace("-", " ");

            //HH converts hour in 24 hours format (0-23), day calculation
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            Date d1 = null;
            Date d2 = null;

            try {
                d1 = format.parse(startDate);
                d2 = format.parse(endTime);

                //in milliseconds
                long diff = d2.getTime() - d1.getTime();

                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                long diffDays = diff / (24 * 60 * 60 * 1000);

                if (diffDays > 7) {
                    kq = dateFormat.format(new Date(millisStartTime));
                } else if (diffDays == 7) {
                    kq = "một tuần";
                } else if (diffDays >= 1) {
                    kq = diffDays + " ngày";
                } else {
                    if (diffHours >= 1) {
                        kq = diffHours + " giờ";
                    } else {
                        if (diffMinutes >= 1) {
                            kq = diffMinutes + " phút";
                        } else {
                            kq = "Vừa xong";
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return kq;
        }
    }
}
