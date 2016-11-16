package com.example.administrator.chotot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.administrator.chotot.R;
import com.example.administrator.chotot.activity.ChatActivity;
import com.example.administrator.chotot.adapter.HistoryMessageAdapter;
import com.example.administrator.chotot.adapter.HistoryMessageAdapter.OnItemClickListener;
import com.example.administrator.chotot.model.Message;
import com.example.administrator.chotot.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.administrator.chotot.activity.MainActivity.phone;
import static com.example.administrator.chotot.utils.FirebaseConfig.productRef;

/**
 * Created by Administrator on 17/10/2016.
 */

public class SellChatFragment extends Fragment{
    private RecyclerView mRecyHistory;
    private LinearLayout mLnAlert;

    private ArrayList<Product> mArrProduct = new ArrayList<>();
    private ArrayList<String> mArrIdUser = new ArrayList<>();
    private ArrayList<Message> mArrMessage = new ArrayList<>();

    private HistoryMessageAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_sell, container, false);

        init(view);
        setAdapter();

        return view;
    }

    private void init(View view){
        mRecyHistory = (RecyclerView)view.findViewById(R.id.recy_history_message);
        mLnAlert = (LinearLayout)view.findViewById(R.id.ln_alert);
    }

    private void getData(){
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mArrProduct.removeAll(mArrProduct);
                for(DataSnapshot db : dataSnapshot.getChildren()){
                    String user = db.child("idUser").getValue().toString();
                    String idProduct = db.getKey();

                    if(phone.equals(user)){
                        Product product = new Product();
                        product.setId(idProduct);

                        mArrProduct.add(product);
                    }
                }

                getListUser();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getListUser(){
        for(int i = 0; i < mArrProduct.size(); i++){
            final int finalI = i;
            productRef.child(mArrProduct.get(i).getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String idProduct = mArrProduct.get(finalI).getId();

                    if(dataSnapshot.child("Messages").getValue() != null){
                        final DatabaseReference msgRef = dataSnapshot.child("Messages").getRef();

                        msgRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                mArrIdUser.removeAll(mArrIdUser);
                                mArrMessage.removeAll(mArrMessage);
                                for(DataSnapshot db : dataSnapshot.getChildren()){
                                    mArrIdUser.add(db.getKey());
                                }

                                for(int i = 0; i < mArrIdUser.size(); i++){
                                    final String idChat = mArrIdUser.get(i);
                                    Query lastTime = msgRef.child(mArrIdUser.get(i)).limitToLast(1);

                                    lastTime.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {


                                            for(DataSnapshot db : dataSnapshot.getChildren()){
                                                Message message = new Message();
                                                message.setIdSender(db.child("sender").getValue().toString());
                                                message.setContent(db.child("content").getValue().toString());
                                                message.setTime(db.child("time").getValue().toString());
                                                message.setIdProduct(idProduct);
                                                message.setIdChat(idChat);
                                                message.setIdUser(dataSnapshot.getKey());

                                                mArrMessage.add(message);
                                            }
                                            Collections.sort(mArrMessage, Message.sortNew);

                                            mAdapter.notifyDataSetChanged();

                                            if(mArrMessage.size() == 0){
                                                mRecyHistory.setVisibility(View.GONE);
                                                mLnAlert.setVisibility(View.VISIBLE);
                                            }else{
                                                mRecyHistory.setVisibility(View.VISIBLE);
                                                mLnAlert.setVisibility(View.GONE);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void setAdapter(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyHistory.setLayoutManager(linearLayoutManager);

        mAdapter = new HistoryMessageAdapter(getContext(), mArrMessage, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("idUserFirstChat", mArrMessage.get(position).getIdUser());
                bundle.putString("idProduct", mArrMessage.get(position).getIdProduct());
                bundle.putString("idUser", phone);

                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
        mRecyHistory.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!phone.equals("")) {
            getData();
        }

        if(mArrMessage.size() == 0){
            mRecyHistory.setVisibility(View.GONE);
            mLnAlert.setVisibility(View.VISIBLE);
        }else{
            mRecyHistory.setVisibility(View.VISIBLE);
            mLnAlert.setVisibility(View.GONE);
        }
    }
}
