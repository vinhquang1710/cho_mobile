package com.example.administrator.chotot.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.chotot.R;

/**
 * Created by Administrator on 02/11/2016.
 */

public class EditPasswordActivity extends AppCompatActivity implements OnClickListener {
    private ImageView mImgBack;
    private TextInputLayout mInputOldPassword, mInputNewPassword;
    private EditText mEtOldPassword, mEtNewPassword;
    private TextView mTvUpdate;

    private String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        init();
        setListener();
    }

    private void init(){
        Bundle bundle = getIntent().getBundleExtra("bundle");
        password = bundle.getString("pwd");

        mImgBack = (ImageView)findViewById(R.id.img_back);
        mInputOldPassword = (TextInputLayout)findViewById(R.id.input_layout_oldPwd);
        mInputNewPassword = (TextInputLayout)findViewById(R.id.input_layout_newPwd);
        mEtNewPassword = (EditText)findViewById(R.id.et_new_password);
        mEtOldPassword = (EditText)findViewById(R.id.et_old_password);
        mTvUpdate = (TextView)findViewById(R.id.tv_update);
    }

    private void setListener(){
        mImgBack.setOnClickListener(this);
        mTvUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;

            case R.id.tv_update:
                if(validate()){
                    Intent intent = new Intent();
                    intent.putExtra("newPwd", mEtNewPassword.getText().toString().trim());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }

    private boolean validate(){
        String oldPwd = mEtOldPassword.getText().toString().trim();
        String newPwd = mEtNewPassword.getText().toString().trim();

        if(oldPwd.length() == 0){
            mInputOldPassword.setError("Vui lòng nhập mật khẩu cũ");
            return false;
        }else{
            if(!oldPwd.equals(password)){
                mInputOldPassword.setError("Mật khẩu cũ không đúng");
                return false;
            }else{
                mInputOldPassword.setErrorEnabled(false);
            }
        }

        if (newPwd.length() == 0){
            mInputNewPassword.setError("Vui lòng nhập mật khẩu mới");
            return false;
        }else{
            mInputNewPassword.setErrorEnabled(false);
        }

        return true;
    }
}
