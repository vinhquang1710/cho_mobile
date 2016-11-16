package com.example.administrator.chotot.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.chotot.R;
import com.example.administrator.chotot.notification.EndPoints;
import com.example.administrator.chotot.notification.SharedPrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.administrator.chotot.utils.FirebaseConfig.userRef;

/**
 * Created by Administrator on 03/11/2016.
 */

public class InputPhoneActivity extends AppCompatActivity implements OnClickListener {
    private ImageView mImgBack;
    private EditText mEtPhone;
    private TextView mTvNext;

    private SharedPreferences pre;
    private SharedPreferences.Editor editor;

    private ProgressDialog mProgress;

    public static final String prefname = "my_account";

    private String fullname, idFb, avatar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        init();
        setListener();
    }

    private void init(){
        Bundle bundle = getIntent().getBundleExtra("bundle");
        fullname = bundle.getString("fullname");
        idFb = bundle.getString("idFb");
        avatar = bundle.getString("avatar");

        mImgBack = (ImageView)findViewById(R.id.img_back);
        mEtPhone = (EditText) findViewById(R.id.et_phone_number);
        mTvNext = (TextView)findViewById(R.id.tv_next);

        mProgress = new ProgressDialog(this);
    }

    private void setListener(){
        mImgBack.setOnClickListener(this);
        mTvNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;

            case R.id.tv_next:
                if(validate()){
                    doRegister();
                }
                break;
        }
    }

    private boolean validate(){
        if(mEtPhone.getText().toString().trim().length() == 0){
            mEtPhone.setError("Vui lòng nhập số điện thoại");
            return false;
        }
        return true;
    }

    private void doRegister(){
        final String phone = mEtPhone.getText().toString().trim();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;

                for(DataSnapshot db : dataSnapshot.getChildren()){
                    if(phone.equals(db.getKey())){
                        count++;
                    }
                }

                if(count == 0){
                    Map<String, Object> map = new HashMap<>();
                    map.put("fullname", fullname);
                    map.put("idFb", idFb);
                    map.put("password", "");
                    map.put("phone", phone);
                    map.put("urlAvatar", avatar);

                    userRef.child(phone).setValue(map);

                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    savingPreferences(phone);
                    /*sendTokenToServer(phone);*/
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Số điện thoại này đã được đăng ký", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void savingPreferences(String username) {
        pre = getSharedPreferences(prefname, MODE_PRIVATE);
        editor = pre.edit();

        editor.putString("username", username);
        editor.putString("city", "Toàn quốc");
        editor.commit();
    }

    private void sendTokenToServer(final String phone) {
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Registering Device...");
        mProgress.show();

        final String token = SharedPrefManager.getInstance(getApplicationContext()).getDeviceToken();;

        if (token == null) {
            mProgress.dismiss();
            Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_REGISTER_DEVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgress.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgress.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone", phone);
                params.put("token", token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
