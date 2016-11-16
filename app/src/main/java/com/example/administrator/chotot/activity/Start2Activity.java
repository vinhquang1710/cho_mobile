package com.example.administrator.chotot.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.chotot.R;

import static com.example.administrator.chotot.activity.LoginActivity.prefname;

/**
 * Created by Administrator on 13/10/2016.
 */

public class Start2Activity extends AppCompatActivity{
    private SharedPreferences pre;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start2);

        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(1000);
                } catch (Exception e) {

                } finally {

                    savingPreferences();
                    Intent i = new Intent(Start2Activity.this,
                            MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }

    private void savingPreferences() {
        pre = getSharedPreferences(prefname, MODE_PRIVATE);
        editor = pre.edit();

        editor.putString("city", "Toàn quốc");
        editor.commit();
    }
}
