package com.example.administrator.chotot.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.administrator.chotot.notification.MyFirebaseMessagingService;
import com.example.administrator.chotot.notification.SharedPrefManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.example.administrator.chotot.utils.FirebaseConfig.userRef;

/**
 * Created by Administrator on 14/10/2016.
 */

public class LoginActivity extends AppCompatActivity implements OnClickListener {
    private EditText mEtPhone, mEtPwd;
    private ImageView mImgHidePwd, mImgShowPwd;
    private TextView mTvLogin, mTvForgotPwd, mTvRegister;
    private LinearLayout mLnLoginFb;

    private ProgressDialog mProgress;

    private SharedPreferences pre;
    private SharedPreferences.Editor editor;

    public static final String prefname = "my_account";

    private FirebaseAuth fbAuth;
    private FirebaseAuth.AuthStateListener fbAuthListener;

    private CallbackManager callbackManager;

    private LoginManager loginManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        init();
        setListener();

        fbAuth = FirebaseAuth.getInstance();
        fbAuthListener = new AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = fbAuth.getCurrentUser();
                if(user != null) {

                }else{
                    Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();
                }
            }
        };


        /*restoringPreferences();*/
    }

    private void init(){
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();

        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtPwd = (EditText)findViewById(R.id.et_pwd);

        mImgHidePwd = (ImageView)findViewById(R.id.img_hide_pwd);
        mImgShowPwd = (ImageView)findViewById(R.id.img_show_pwd);

        mTvLogin = (TextView)findViewById(R.id.tv_login);
        mTvForgotPwd = (TextView)findViewById(R.id.tv_forgot_pwd);
        mTvRegister = (TextView)findViewById(R.id.tv_register);

        mLnLoginFb = (LinearLayout)findViewById(R.id.ln_login_fb);

        mProgress = new ProgressDialog(LoginActivity.this);
    }

    private void setListener(){
        mImgShowPwd.setOnClickListener(this);
        mImgHidePwd.setOnClickListener(this);
        mTvLogin.setOnClickListener(this);
        mTvForgotPwd.setOnClickListener(this);
        mTvRegister.setOnClickListener(this);
        mLnLoginFb.setOnClickListener(this);

        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void savingPreferences(String username) {
        pre = getSharedPreferences(prefname, MODE_PRIVATE);
        editor = pre.edit();

        editor.putString("username", username);
        editor.commit();
    }

    private void restoringPreferences(){
        pre = getSharedPreferences(prefname, MODE_PRIVATE);
        String idUser = pre.getString("username", "");
        if(idUser.equals("")){
            return;
        }else{
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            sendTokenToServer(idUser);

            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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

            case R.id.tv_login:
                if(validate()){
                    String phone = mEtPhone.getText().toString().trim();
                    String pwd = mEtPwd.getText().toString().trim();

                    doLogin(phone, pwd);
                }
                break;

            case R.id.tv_forgot_pwd:
                Intent intentForgot = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intentForgot);
                break;

            case R.id.tv_register:
                Intent intentReg = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intentReg);
                break;

            case R.id.ln_login_fb:
                loginManager.logInWithReadPermissions(this, Arrays.asList("email", "public_profile", "user_birthday"));
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

    private void doLogin(final String phone, final String pwd){
        mProgress.setMessage("Đang đăng nhập...");
        mProgress.show();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;

                for(DataSnapshot db : dataSnapshot.getChildren()){
                    if(db.child("phone").getValue().toString().equals(phone) && db.child("password").getValue().toString().equals(pwd)){
                        mProgress.dismiss();

                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();

                        PackageManager pm = getPackageManager();
                        pm.setComponentEnabledSetting(new ComponentName(getApplicationContext(), MyFirebaseMessagingService.class),
                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                        savingPreferences(phone);
						sendTokenToServer(phone);

                        finish();

                        count++;
                    }
                }

                if(count == 0){
                    mProgress.dismiss();
                    Toast.makeText(getApplicationContext(), "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void handleFacebookAccessToken(AccessToken accessToken){
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        fbAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                final String fullname = task.getResult().getUser().getDisplayName();
                final String idFb = task.getResult().getUser().getUid();
                final String avatar = task.getResult().getUser().getPhotoUrl()+"";

                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int count = 0;
                        String phone = "";

                        for(DataSnapshot db : dataSnapshot.getChildren()){
                            if(idFb.equals(db.child("idFb").getValue().toString())){
                                count++;
                                phone = db.getKey();
                            }
                        }

                        if(count ==0){
                            Bundle bundle = new Bundle();
                            bundle.putString("fullname", fullname);
                            bundle.putString("idFb", idFb);
                            bundle.putString("avatar", avatar);

                            Intent intent = new Intent(getApplicationContext(), InputPhoneActivity.class);
                            intent.putExtra("bundle", bundle);
                            startActivity(intent);
                        }else{

                            sendTokenToServer(phone);
                            Bundle bundle = new Bundle();
                            bundle.putString("fullname", fullname);
                            bundle.putString("idFb", idFb);
                            bundle.putString("avatar", avatar);

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("bundle", bundle);
                            startActivity(intent);

                            sendTokenToServer(phone);
                            savingPreferences(phone);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
