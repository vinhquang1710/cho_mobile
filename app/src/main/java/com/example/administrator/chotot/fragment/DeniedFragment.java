package com.example.administrator.chotot.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.administrator.chotot.R;

/**
 * Created by Administrator on 17/10/2016.
 */

public class DeniedFragment extends Fragment implements OnClickListener {
    private LinearLayout mLnSale;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_denied, container, false);

        init(view);
        setListener();

        return view;
    }

    private void init(View view){
        mLnSale = (LinearLayout)view.findViewById(R.id.ln_sale);
    }

    private void setListener(){
        mLnSale.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ln_sale:

                break;
        }
    }
}
