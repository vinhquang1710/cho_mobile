package com.example.administrator.chotot.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.example.administrator.chotot.R;

/**
 * Created by Administrator on 11/11/2016.
 */

public class HelpActivity extends AppCompatActivity implements OnClickListener {
    private ImageView mImgBack;
    private WebView mWebHelp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        init();
        setListener();

        mWebHelp.setWebViewClient(new MyWebViewClient());
        String url = "http://trogiup.chotot.com/";
        mWebHelp.getSettings().setJavaScriptEnabled(true);
        mWebHelp.loadUrl(url);
    }

    private void init(){
        mImgBack = (ImageView)findViewById(R.id.img_back);
        mWebHelp = (WebView)findViewById(R.id.web_help);
    }

    private void setListener(){
        mImgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
