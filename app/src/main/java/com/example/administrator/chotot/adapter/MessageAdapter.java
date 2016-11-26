package com.example.administrator.chotot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.chotot.R;
import com.example.administrator.chotot.adapter.MessageAdapter.MyViewHolder;
import com.example.administrator.chotot.model.Message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Administrator on 05/11/2016.
 */

public class MessageAdapter extends Adapter<MyViewHolder>{
    private Context mContext;
    private ArrayList<Message> mArr;
    private String idUser;

    public MessageAdapter(Context mContext, ArrayList<Message> mArr, String idUser) {
        this.mContext = mContext;
        this.mArr = mArr;
        this.idUser = idUser;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case 0:
                View viewMe = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recy_message_right, parent, false);

                return new MyViewHolder(viewMe);

            case 1:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recy_message_left, parent, false);
                return new MyViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mArr.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mArr.get(position).getIdSender().equals(idUser)){
            return 0;
        }else{
            return 1;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvContent, mTvTime;

        public MyViewHolder(View itemView) {
            super(itemView);

            mTvContent = (TextView)itemView.findViewById(R.id.tv_message);
            mTvTime = (TextView)itemView.findViewById(R.id.tv_time);
        }

        public void bind(final int position){
            String content = mArr.get(position).getContent();
            String time = mArr.get(position).getTime();

            mTvContent.setText(content);
            mTvTime.setText(calculateTime(time));
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
