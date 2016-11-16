package com.example.administrator.chotot.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.chotot.R;

/**
 * Created by Administrator on 25/10/2016.
 */

public class StatusProductDialog extends Activity implements OnClickListener {
    private LinearLayout mLnUsed, mLnNew;
    private ImageView mImgUsedNormal, mImgUsedChecked, mImgNewNormal, mImgNewChecked;
    private TextView mTvUsed, mTvNew;

    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_status_product);

        init();
        setListener();
    }

    private void init(){
        mLnUsed = (LinearLayout)findViewById(R.id.ln_used_products);
        mLnNew = (LinearLayout)findViewById(R.id.ln_new_products);

        mImgUsedNormal = (ImageView)findViewById(R.id.img_rbn_used_normal);
        mImgUsedChecked = (ImageView)findViewById(R.id.img_rbn_used_checked);
        mImgNewNormal = (ImageView)findViewById(R.id.img_rbn_new_normal);
        mImgNewChecked = (ImageView)findViewById(R.id.img_rbn_new_checked);

        mTvUsed = (TextView)findViewById(R.id.tv_used);
        mTvNew = (TextView)findViewById(R.id.tv_new);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        status = bundle.getString("status");

        if(status.equals(mTvUsed.getText().toString())){
            mImgUsedNormal.setVisibility(View.GONE);
            mImgUsedChecked.setVisibility(View.VISIBLE);

            mImgUsedNormal.setVisibility(View.VISIBLE);
            mImgNewChecked.setVisibility(View.GONE);
        }else if(status.equals(mTvNew.getText().toString())){
            mImgUsedNormal.setVisibility(View.VISIBLE);
            mImgUsedChecked.setVisibility(View.GONE);

            mImgUsedNormal.setVisibility(View.GONE);
            mImgNewChecked.setVisibility(View.VISIBLE);
        }
    }

    private void setListener(){
        mLnUsed.setOnClickListener(this);
        mLnNew.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ln_used_products:
                Intent intentUsed = new Intent();
                intentUsed.putExtra("status", mTvUsed.getText().toString());
                setResult(Activity.RESULT_OK, intentUsed);
                finish();
                break;

            case R.id.ln_new_products:
                Intent intentNew = new Intent();
                intentNew.putExtra("status", mTvNew.getText().toString());
                setResult(Activity.RESULT_OK, intentNew);
                finish();
                break;
        }
    }
}
