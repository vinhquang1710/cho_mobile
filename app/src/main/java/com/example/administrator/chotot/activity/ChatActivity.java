package com.example.administrator.chotot.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.chotot.R;
import com.example.administrator.chotot.adapter.MessageAdapter;
import com.example.administrator.chotot.model.Message;
import com.example.administrator.chotot.notification.EndPoints;
import com.example.administrator.chotot.notification.MyVolley;
import com.example.administrator.chotot.utils.FormatMoney;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.administrator.chotot.activity.MainActivity.phone;
import static com.example.administrator.chotot.utils.FirebaseConfig.productRef;
import static com.example.administrator.chotot.utils.FirebaseConfig.userRef;

/**
 * Created by Administrator on 04/11/2016.
 */

public class ChatActivity extends AppCompatActivity implements OnClickListener {
    private ImageView mImgBack, mImgProduct, mImgCapture, mImgSend;
    private TextView mTvFullname, mTvTitle, mTvPrice;
    private EditText mEtMessage;
    private RecyclerView mRecyMessage;

    private String idProduct, idUser, idUserFirstChat;

    private ArrayList<Message> mArr = new ArrayList<>();
    private MessageAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
        setListener();
        getData();
        setAdapter();
    }

    private void init() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        idProduct = bundle.getString("idProduct");
        idUser = bundle.getString("idUser");
        idUserFirstChat = bundle.getString("idUserFirstChat");

        mImgBack = (ImageView) findViewById(R.id.img_back);
        mImgCapture = (ImageView) findViewById(R.id.img_chat_capture);
        mImgProduct = (ImageView) findViewById(R.id.img_product);
        mImgSend = (ImageView) findViewById(R.id.img_send);

        mTvFullname = (TextView) findViewById(R.id.tv_fullname);
        mTvTitle = (TextView) findViewById(R.id.tv_title_product);
        mTvPrice = (TextView) findViewById(R.id.tv_price_product);

        mEtMessage = (EditText) findViewById(R.id.et_message);

        mRecyMessage = (RecyclerView) findViewById(R.id.recy_message);
    }

    private void setListener() {
        mImgBack.setOnClickListener(this);
        mImgCapture.setOnClickListener(this);
        mImgSend.setOnClickListener(this);
        mRecyMessage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;

            case R.id.img_chat_capture:

                break;

            case R.id.img_send:
                doChat();
                mRecyMessage.post(new Runnable() {
                    @Override
                    public void run() {
                        mRecyMessage.smoothScrollToPosition(mAdapter.getItemCount());
                    }
                });
                break;

            case R.id.recy_message:
                doRead();
                break;
        }
    }

    private void getData() {
        productRef.child(idProduct).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTvTitle.setText(dataSnapshot.child("title").getValue().toString());

                String price = FormatMoney.format(Integer.parseInt(dataSnapshot.child("price").getValue().toString()));
                mTvPrice.setText(price + " đ");

                Picasso.with(getApplicationContext()).load(dataSnapshot.child("img").getValue().toString()).into(mImgProduct);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userRef.child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTvFullname.setText(dataSnapshot.child("fullname").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void doChat() {
        if(mEtMessage.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(), "Nội dung không được bỏ trống", Toast.LENGTH_SHORT).show();
        }else {
            long time = System.currentTimeMillis();

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("content", mEtMessage.getText().toString());
            map.put("sender", phone);
            map.put("time", time);
            map.put("img", "");
            map.put("read", "false");

            productRef.child(idProduct).child("Messages").child(idUserFirstChat).child(time + "").setValue(map);
            /*sendSinglePush();*/
            mEtMessage.setText("");
        }
    }

    private void getMessage(){
        productRef.child(idProduct).child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    mArr.removeAll(mArr);
                    dataSnapshot.child(idUserFirstChat).getRef().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot db : dataSnapshot.getChildren()){
                                Message message = new Message();
                                message.setContent(db.child("content").getValue().toString());
                                message.setIdSender(db.child("sender").getValue().toString());
                                message.setTime(db.child("time").getValue().toString());

                                mArr.add(message);
                            }
                            mAdapter.notifyDataSetChanged();
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

    private void setAdapter(){
        getMessage();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        mRecyMessage.setLayoutManager(linearLayoutManager);

        mAdapter = new MessageAdapter(getApplicationContext(), mArr, phone);
        mRecyMessage.setAdapter(mAdapter);
    }

    private void sendSinglePush() {
        final String title = "Bạn có tin nhắn mới";
        final String message = mEtMessage.getText().toString();
        final String phoneNumber;

        if(idUser.equals(phone)){
            phoneNumber = idUserFirstChat;
        }else{
            phoneNumber = idUser;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_SEND_SINGLE_PUSH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("message", message);
                params.put("phone", phoneNumber);
                return params;
            }
        };

        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void doRead(){
        productRef.child(idProduct).child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    mArr.removeAll(mArr);
                    dataSnapshot.child(idUserFirstChat).getRef().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot db : dataSnapshot.getChildren()){
                                if(db.child("read").getValue().toString().equals("false") && !db.child("sender").getValue().toString().equals(phone)){
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("read", "true");

                                    db.getRef().updateChildren(map);
                                }
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

    @Override
    protected void onResume() {
        super.onResume();
        doRead();
    }
}
