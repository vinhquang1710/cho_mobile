package com.example.administrator.chotot.activity;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.example.administrator.chotot.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static com.example.administrator.chotot.utils.FirebaseConfig.userRef;

/**
 * Created by Administrator on 02/11/2016.
 */

public class EditInfoActivity extends AppCompatActivity implements OnClickListener {
    private TextInputLayout mInputFullname;
    private EditText mEtFullname, mEtPassword;
    private TextView mTvUpdate;
    private ImageView mImgBack, mImgEditPassword;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        init();
        setListener();
        getData();
    }

    private void init() {
        mInputFullname = (TextInputLayout) findViewById(R.id.input_layout_fullname);
        mEtFullname = (EditText) findViewById(R.id.et_fullname);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mTvUpdate = (TextView) findViewById(R.id.tv_update);

        mImgBack = (ImageView)findViewById(R.id.img_back);
        mImgEditPassword = (ImageView)findViewById(R.id.img_edit_password);

        mProgress = new ProgressDialog(this);
    }

    private void setListener() {
        mTvUpdate.setOnClickListener(this);
        mImgBack.setOnClickListener(this);
        mImgEditPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_update:
                if(validate()){
                    Map<String, Object> map = new HashMap<>();
                    map.put("fullname", mEtFullname.getText().toString());
                    map.put("password", mEtPassword.getText().toString());

                    userRef.child(MainActivity.phone).updateChildren(map);
                    Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.img_back:
                finish();
                break;

            case R.id.img_edit_password:
                Bundle bundle = new Bundle();
                bundle.putString("pwd", mEtPassword.getText().toString());

                Intent intent = new Intent(getApplicationContext(), EditPasswordActivity.class);
                intent.putExtra("bundle", bundle);
                startActivityForResult(intent, 1);
                break;
        }
    }

    private boolean validate() {
        if (mEtFullname.getText().toString().length() == 0) {
            mInputFullname.setError("Vui lòng nhập họ và tên");
            return false;
        } else {
            mInputFullname.setErrorEnabled(false);
        }

        return true;
    }

    private void getData(){
        mProgress.setMessage("Đang tải dữ liệu");
        mProgress.show();

        userRef.child(MainActivity.phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mEtFullname.setText(dataSnapshot.child("fullname").getValue().toString());
                mEtPassword.setText(dataSnapshot.child("password").getValue().toString());

                mProgress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                String newPwd = data.getStringExtra("newPwd");
                mEtPassword.setText(newPwd);
            }
        }
    }
}
