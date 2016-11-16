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

public class BuyChatFragment extends Fragment {
    private RecyclerView mRecyHistory;
    private LinearLayout mLnAlert;

    private ArrayList<Message> mArr = new ArrayList<>();
    private HistoryMessageAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_buy, container, false);

        init(view);
        setAdapter();

        return view;
    }

    private void init(View view) {
        mRecyHistory = (RecyclerView) view.findViewById(R.id.recy_history_message);
        mLnAlert = (LinearLayout) view.findViewById(R.id.ln_alert);
    }

    private void getData() {
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mArr.removeAll(mArr);

                for (DataSnapshot db : dataSnapshot.getChildren()) {
                    final String idProduct = db.getKey();
                    final String idUser = db.child("idUser").getValue().toString();

                    if (db.child("Messages").getValue() != null) {
                        final DatabaseReference msgRef = db.child("Messages").getRef();
                        msgRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot db : dataSnapshot.getChildren()) {
                                    if (db.getKey().equals(phone)) {
                                        final String idChat = db.getKey();
                                        Query query = db.getRef().limitToLast(1);

                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot db : dataSnapshot.getChildren()) {
                                                    Message message = new Message();
                                                    message.setIdProduct(idProduct);
                                                    message.setTime(db.child("time").getValue().toString());
                                                    message.setContent(db.child("content").getValue().toString());
                                                    message.setIdSender(db.child("sender").getValue().toString());
                                                    message.setIdChat(idChat);
                                                    message.setIdUser(idUser);

                                                    mArr.add(message);
                                                }

                                                Collections.sort(mArr, Message.sortNew);
                                                mAdapter.notifyDataSetChanged();

                                                if (mArr.size() > 0) {
                                                    mRecyHistory.setVisibility(View.VISIBLE);
                                                    mLnAlert.setVisibility(View.GONE);
                                                } else {
                                                    mRecyHistory.setVisibility(View.GONE);
                                                    mLnAlert.setVisibility(View.VISIBLE);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyHistory.setLayoutManager(linearLayoutManager);
        mAdapter = new HistoryMessageAdapter(getContext(), mArr, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("idUserFirstChat", phone);
                bundle.putString("idProduct", mArr.get(position).getIdProduct());
                bundle.putString("idUser", mArr.get(position).getIdUser());

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

        if (mArr.size() > 0) {
            mRecyHistory.setVisibility(View.VISIBLE);
            mLnAlert.setVisibility(View.GONE);
        } else {
            mRecyHistory.setVisibility(View.GONE);
            mLnAlert.setVisibility(View.VISIBLE);
        }
    }
}
