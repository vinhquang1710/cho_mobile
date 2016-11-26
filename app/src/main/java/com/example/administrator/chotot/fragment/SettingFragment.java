package com.example.administrator.chotot.fragment;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.chotot.R;
import com.example.administrator.chotot.activity.EditInfoActivity;
import com.example.administrator.chotot.activity.HelpActivity;
import com.example.administrator.chotot.activity.ListFavoriteActivity;
import com.example.administrator.chotot.activity.LoginActivity;
import com.example.administrator.chotot.activity.MainActivity;
import com.example.administrator.chotot.notification.MyFirebaseMessagingService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.example.administrator.chotot.activity.MainActivity.phone;
import static com.example.administrator.chotot.utils.FirebaseConfig.storageRef;
import static com.example.administrator.chotot.utils.FirebaseConfig.userRef;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Administrator on 15/10/2016.
 */

public class SettingFragment extends Fragment implements OnClickListener {
    private ImageView mImgAvatar, mImgInvite;
    private TextView mTvFullname;

    private FrameLayout mFlEditAvatar;
    private LinearLayout mLnEditInfo;
    private LinearLayout mLnSearch, mLnListFriend, mLnFavAds, mLnSaveSearch, mLnHelp, mLnLogout, mLnLogin, mLnInfo;

    private ProgressDialog mProgress;

    private SharedPreferences pre;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        init(view);
        setListener();

        if(!phone.equals("")) {
            getData();
        }

        return view;
    }

    private void init(View view) {
        pre = this.getActivity().getSharedPreferences(LoginActivity.prefname, Context.MODE_PRIVATE);

        mImgAvatar = (ImageView) view.findViewById(R.id.img_avatar);
        mImgInvite = (ImageView) view.findViewById(R.id.img_invite);
        mTvFullname = (TextView) view.findViewById(R.id.tv_fullname);

        mFlEditAvatar = (FrameLayout) view.findViewById(R.id.fl_edit_avatar);
        mLnEditInfo = (LinearLayout) view.findViewById(R.id.ln_edit_info);

        mLnSearch = (LinearLayout) view.findViewById(R.id.ln_search_friends);
        mLnListFriend = (LinearLayout) view.findViewById(R.id.ln_list_friend);
        mLnFavAds = (LinearLayout) view.findViewById(R.id.ln_ad_fav);
        mLnSaveSearch = (LinearLayout) view.findViewById(R.id.ln_save_search);
        mLnHelp = (LinearLayout) view.findViewById(R.id.ln_help);
        mLnLogout = (LinearLayout) view.findViewById(R.id.ln_log_out);
        mLnLogin = (LinearLayout)view.findViewById(R.id.ln_login);
        mLnInfo = (LinearLayout)view.findViewById(R.id.ln_info);

        mProgress = new ProgressDialog(getContext());
    }

    private void setListener() {
        mFlEditAvatar.setOnClickListener(this);
        mLnEditInfo.setOnClickListener(this);
        mLnSearch.setOnClickListener(this);
        mLnListFriend.setOnClickListener(this);
        mLnFavAds.setOnClickListener(this);
        mLnHelp.setOnClickListener(this);
        mLnLogout.setOnClickListener(this);
        mLnSaveSearch.setOnClickListener(this);
        mImgInvite.setOnClickListener(this);
        mLnLogin.setOnClickListener(this);
    }

    private void checkLogin(){
        if(phone.equals("")){
            mLnLogin.setVisibility(View.VISIBLE);
            mLnInfo.setVisibility(View.GONE);
            mLnLogout.setVisibility(View.GONE);
        }else{
            mLnLogin.setVisibility(View.GONE);
            mLnInfo.setVisibility(View.VISIBLE);
            mLnLogout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_edit_avatar:
                showPopupMenu();
                break;

            case R.id.ln_edit_info:
                Intent intentEditInfo = new Intent(getContext(), EditInfoActivity.class);
                startActivity(intentEditInfo);
                break;

            case R.id.ln_search_friends:

                break;

            case R.id.ln_list_friend:

                break;

            case R.id.ln_ad_fav:
                Intent intentFav = new Intent(getContext(), ListFavoriteActivity.class);
                startActivity(intentFav);
                break;

            case R.id.ln_save_search:

                break;

            case R.id.ln_help:
                Intent intentHelp = new Intent(getContext(), HelpActivity.class);
                startActivity(intentHelp);
                break;

            case R.id.ln_log_out:
                SharedPreferences.Editor editor = pre.edit();
                /*editor.clear();*/
                editor.remove("username");
                editor.commit();

                PackageManager pm = getContext().getPackageManager();
                pm.setComponentEnabledSetting(new ComponentName(getContext(), MyFirebaseMessagingService.class),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

                Intent intentLogout = new Intent(getContext(), MainActivity.class);
                startActivity(intentLogout);
                getActivity().finish();
                Toast.makeText(getContext(), "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.img_invite:
                shareUrl();
                break;

            case R.id.ln_login:
                Intent intentLogin = new Intent(getContext(), LoginActivity.class);
                startActivityForResult(intentLogin, 3);
                break;
        }
    }

    private void getData() {
        mProgress.setMessage("Đang tải dữ liệu");
        mProgress.show();
        userRef.child(phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTvFullname.setText(dataSnapshot.child("fullname").getValue().toString());
                Picasso.with(getContext()).load(dataSnapshot.child("urlAvatar").getValue().toString())
                        .placeholder(R.drawable.default_avatar)
                        .into(mImgAvatar);

                mProgress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(), mFlEditAvatar);
        popupMenu.getMenuInflater().inflate(R.menu.menu_add_image, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_gallery:
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                        break;

                    case R.id.item_camera:
                        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intentCamera, 2);
                        break;
                }

                return true;
            }
        });

        popupMenu.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri imageUri = data.getData();
                InputStream imageStream;
                try {
                    imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    uploadImage(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            if (requestCode == 2) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                uploadImage(bitmap);
            }

            if(requestCode == 3){
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        }
    }

    private void uploadImage(Bitmap bitmap) {
        final StorageReference nameImage = storageRef.child("avatar/" + System.currentTimeMillis() + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = nameImage.putBytes(data);
        showHorizontalProgressDialog("Đang tải hình ảnh", "Vui lòng đợi...");
        uploadTask.addOnProgressListener(new OnProgressListener<TaskSnapshot>() {
            @Override
            public void onProgress(TaskSnapshot taskSnapshot) {
                int progress = (int) (100 * (float) taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                updateProgress(progress);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        }).addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("urlAvatar", downloadUrl+"");

                userRef.child(phone).updateChildren(map);
                hideProgressDialog();
            }
        });
    }

    private void hideProgressDialog() {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    public void showHorizontalProgressDialog(String title, String body) {

        if (mProgress != null && mProgress.isShowing()) {
            mProgress.setTitle(title);
            mProgress.setMessage(body);
        } else {
            mProgress = new ProgressDialog(getContext());
            mProgress.setTitle(title);
            mProgress.setMessage(body);
            mProgress.setIndeterminate(false);
            mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgress.setProgress(0);
            mProgress.setMax(100);
            mProgress.setCancelable(false);
            mProgress.show();
        }
    }

    public void updateProgress(int progress) {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.setProgress(progress);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        checkLogin();
    }

    private void shareUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Chợ tốt");
        share.putExtra(Intent.EXTRA_TEXT, "https://www.chotot.com/");

        startActivity(Intent.createChooser(share, "Chia sẻ!"));
        Toast.makeText(getContext(), "Chia sẻ thành công", Toast.LENGTH_SHORT).show();
    }
}
