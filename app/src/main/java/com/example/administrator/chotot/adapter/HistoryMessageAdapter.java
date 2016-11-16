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
import com.example.administrator.chotot.adapter.HistoryMessageAdapter.MyViewHolder;
import com.example.administrator.chotot.model.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import static com.example.administrator.chotot.activity.MainActivity.phone;
import static com.example.administrator.chotot.utils.FirebaseConfig.productRef;
import static com.example.administrator.chotot.utils.FirebaseConfig.userRef;

/**
 * Created by Administrator on 06/11/2016.
 */

public class HistoryMessageAdapter extends Adapter<MyViewHolder>{
    private Context mContext;
    private ArrayList<Message> mArr;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public HistoryMessageAdapter(Context mContext, ArrayList<Message> mArr, OnItemClickListener mListener) {
        this.mContext = mContext;
        this.mArr = mArr;
        this.mListener = mListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recy_history_message, parent, false);

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
        private TextView mTvFullname, mTvTime, mTvContent, mTvTitleProduct;
        private ImageView mImgAvatar, mImgProduct, mImgUnread;
        private LinearLayout mLnItem;

        public MyViewHolder(View itemView) {
            super(itemView);

            mTvContent = (TextView)itemView.findViewById(R.id.tv_content);
            mTvFullname = (TextView)itemView.findViewById(R.id.tv_fullname);
            mTvTime = (TextView)itemView.findViewById(R.id.tv_time);
            mTvTitleProduct = (TextView)itemView.findViewById(R.id.tv_title_product);
            mImgAvatar = (ImageView)itemView.findViewById(R.id.img_avatar);
            mImgProduct = (ImageView)itemView.findViewById(R.id.img_product);
            mLnItem = (LinearLayout)itemView.findViewById(R.id.ln_item_history_message);
            mImgUnread = (ImageView)itemView.findViewById(R.id.img_unread);
        }

        public void bind(final int position, final OnItemClickListener mListener){
            mLnItem.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(position);
                }
            });
            String time = mArr.get(position).getTime();
            String idProduct = mArr.get(position).getIdProduct();
            String idSender = mArr.get(position).getIdUser();

            mTvContent.setText(mArr.get(position).getContent());
            mTvTime.setText(calculateTime(time));

            productRef.child(idProduct).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mTvTitleProduct.setText(dataSnapshot.child("title").getValue().toString());
                    Picasso.with(mContext).load(dataSnapshot.child("img").getValue().toString()).into(mImgProduct);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            userRef.child(idSender).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mTvFullname.setText(dataSnapshot.child("fullname").getValue().toString());
                    Picasso.with(mContext).load(dataSnapshot.child("urlAvatar").getValue().toString()).into(mImgAvatar);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            productRef.child(idProduct).child("Messages").child(mArr.get(position).getIdChat()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null) {

                        int count = 0;

                        for (DataSnapshot db : dataSnapshot.getChildren()) {
                            if (db.child("read").getValue().toString().equals("false") && !db.child("sender").getValue().toString().equals(phone)) {
                                count++;
                            }
                        }

                        if (count == 0) {
                            mImgUnread.setVisibility(View.GONE);
                        } else {
                            mImgUnread.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
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
