package com.example.administrator.chotot.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.chotot.R;

/**
 * Created by Administrator on 15/10/2016.
 */

public class ForgotPasswordActivity extends AppCompatActivity implements OnClickListener {
    private ImageView mImgBack;
    private EditText mEtPhone;
    private TextView mTvAccept;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);

        init();
        setListener();
    }

    private void init(){
        mImgBack = (ImageView)findViewById(R.id.img_back);
        mEtPhone = (EditText)findViewById(R.id.et_phone);
        mTvAccept = (TextView)findViewById(R.id.tv_accept);
    }

    private void setListener(){
        mImgBack.setOnClickListener(this);
        mTvAccept.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;

            case R.id.tv_accept:
                validate();
                break;
        }
    }

    private void validate(){
        String phone = mEtPhone.getText().toString().trim();

        if(phone.length() == 0){
            mEtPhone.setError("Vui lòng nhập số điện thoại");
        }
    }
}
