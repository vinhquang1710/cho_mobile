package com.example.administrator.chotot.activity;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.chotot.R;
import com.example.administrator.chotot.utils.FirebaseConfig;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 15/10/2016.
 */

public class RegisterActivity extends AppCompatActivity implements OnClickListener {
    private ImageView mImgBack, mImgHidePwd, mImgShowPwd;
    private EditText mEtPhone, mEtPwd;
    private TextView mTvNext;

    private ProgressDialog mProgress;

    private ArrayList<String> mArrPhone = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
        setListener();
    }

    private void init(){
        mImgBack = (ImageView)findViewById(R.id.img_back);
        mImgHidePwd = (ImageView)findViewById(R.id.img_hide_pwd);
        mImgShowPwd = (ImageView)findViewById(R.id.img_show_pwd);

        mEtPhone = (EditText)findViewById(R.id.et_phone);
        mEtPwd = (EditText)findViewById(R.id.et_pwd);

        mTvNext = (TextView)findViewById(R.id.tv_next);

        mProgress = new ProgressDialog(RegisterActivity.this);
    }

    private void setListener(){
        mImgBack.setOnClickListener(this);
        mTvNext.setOnClickListener(this);
        mImgHidePwd.setOnClickListener(this);
        mImgShowPwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;

            case R.id.img_hide_pwd:
                mEtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                mEtPwd.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "Roboto-Light.ttf"));
                mImgShowPwd.setVisibility(View.VISIBLE);
                mImgHidePwd.setVisibility(View.GONE);
                break;

            case R.id.img_show_pwd:
                mEtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                mEtPwd.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "Roboto-Light.ttf"));
                mImgShowPwd.setVisibility(View.GONE);
                mImgHidePwd.setVisibility(View.VISIBLE);
                break;

            case R.id.tv_next:
                if(!validate()){

                }else{
                    String phone = mEtPhone.getText().toString().trim();
                    String pwd = mEtPwd.getText().toString().trim();

                    doRegister(phone, pwd);
                }

                break;
        }
    }

    private boolean validate(){
        String phone = mEtPhone.getText().toString().trim();
        String pwd = mEtPwd.getText().toString().trim();

        if(phone.length() == 0){
            mEtPhone.setError("Vui lòng nhập số điện thoại");
            return false;
        }

        if(pwd.length() == 0){
            mEtPwd.setError("Vui lòng nhập mật khẩu");
            return false;
        }

        return true;
    }

    private void doRegister(final String phone, final String pwd){
        mProgress.setMessage("Đang xử lý...");
        mProgress.show();

        FirebaseConfig.userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mArrPhone.removeAll(mArrPhone);

                for (DataSnapshot db : dataSnapshot.getChildren()){
                    if(db.getKey().equals(phone)){
                        mArrPhone.add(phone);
                    }
                }

                mProgress.dismiss();

                if(mArrPhone.size() > 0){
                    Toast.makeText(getApplicationContext(), "Số điện thoại này đã đăng ký", Toast.LENGTH_SHORT).show();
                }else{
                    Map<String, Object> map = new HashMap<>();
                    map.put("phone", phone);
                    map.put("password", pwd);
                    map.put("fullname", phone);
                    map.put("idFb", "");
                    map.put("urlAvatar", "https://firebasestorage.googleapis.com/v0/b/chotot-d0d2e.appspot.com/o/avatar%2Fdefault_avatar.jpg?alt=media&token=594ccac8-9e50-45d7-99c0-615b5ceb890a");

                    FirebaseConfig.userRef.child(phone).setValue(map);
                    Toast.makeText(getApplicationContext(), "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
