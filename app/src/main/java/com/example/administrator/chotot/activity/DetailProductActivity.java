package com.example.administrator.chotot.activity;

import android.Manifest.permission;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.chotot.R;
import com.example.administrator.chotot.adapter.ScreenSlidePagerAdapter;
import com.example.administrator.chotot.model.ImageProduct;
import com.example.administrator.chotot.utils.FirebaseConfig;
import com.example.administrator.chotot.utils.FormatMoney;
import com.example.administrator.chotot.view.CirclePageIndicator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static com.example.administrator.chotot.activity.MainActivity.phone;
import static com.example.administrator.chotot.utils.FirebaseConfig.productRef;

/**
 * Created by Administrator on 20/10/2016.
 */

public class DetailProductActivity extends AppCompatActivity implements OnClickListener {
    private ImageView mImgBack, mImgFav, mImgFavPressed, mImgShare, mImgMenu, mImgAvatar;
    private ViewPager mVpImage;
    private TextView mTvTitle, mTvPrice, mTvFullname, mTvTime, mTvDescription, mTvCategory, mTvStatus, mTvAddress, mTvCity;
    private LinearLayout mLnCall, mLnChat, mLnSendSMS, mLnProfile;

    private ProgressDialog mProgress;

    private CirclePageIndicator indicator;

    private ArrayList<ImageProduct> mArr;

    private String idProduct, idUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        init();
        setListener();
        getData();

        displayFav();

        addImageProduct();
    }

    private void init() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        idProduct = bundle.getString("id");
        idUser = bundle.getString("phone");

        mImgBack = (ImageView) findViewById(R.id.img_back);
        mImgFav = (ImageView) findViewById(R.id.img_fav_normal);
        mImgFavPressed = (ImageView) findViewById(R.id.img_fav_pressed);
        mImgShare = (ImageView) findViewById(R.id.img_share);
        mImgMenu = (ImageView) findViewById(R.id.img_more);
        mImgAvatar = (ImageView)findViewById(R.id.img_avatar);

        mVpImage = (ViewPager) findViewById(R.id.pager_image);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);

        mTvTitle = (TextView) findViewById(R.id.tv_title_product);
        mTvFullname = (TextView) findViewById(R.id.tv_fullname);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mTvPrice = (TextView) findViewById(R.id.tv_price_product);
        mTvDescription = (TextView) findViewById(R.id.tv_describe);
        mTvCategory = (TextView) findViewById(R.id.tv_category);
        mTvCity = (TextView) findViewById(R.id.tv_city);
        mTvAddress = (TextView) findViewById(R.id.tv_address);
        mTvStatus = (TextView) findViewById(R.id.tv_status);

        mLnCall = (LinearLayout) findViewById(R.id.ln_call);
        mLnChat = (LinearLayout) findViewById(R.id.ln_chat);
        mLnSendSMS = (LinearLayout) findViewById(R.id.ln_sms);
        mLnProfile = (LinearLayout)findViewById(R.id.ln_profile);

        mProgress = new ProgressDialog(this);
    }

    private void setListener() {
        mImgBack.setOnClickListener(this);
        mImgFav.setOnClickListener(this);
        mImgFavPressed.setOnClickListener(this);
        mImgShare.setOnClickListener(this);
        mImgMenu.setOnClickListener(this);

        mLnCall.setOnClickListener(this);
        mLnChat.setOnClickListener(this);
        mLnSendSMS.setOnClickListener(this);
        mLnProfile.setOnClickListener(this);

        mVpImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;

            case R.id.img_fav_normal:
                doFav();
                break;

            case R.id.img_fav_pressed:
                doUnFav();
                break;

            case R.id.img_share:

                break;

            case R.id.img_more:
                showPopupMenu();
                break;

            case R.id.ln_call:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + idUser));
                if (ActivityCompat.checkSelfPermission(this, permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
                break;

            case R.id.ln_chat:
                if(phone.equals("")){
                    Toast.makeText(getApplicationContext(), "Vui lòng đăng nhập để trò chuyện", Toast.LENGTH_SHORT).show();
                }else {
                    if (phone.equals(idUser)) {
                        Toast.makeText(getApplicationContext(), "Bạn không thể liên hệ với người này", Toast.LENGTH_SHORT).show();
                    } else {

                        Bundle bundleChat = new Bundle();
                        bundleChat.putString("idProduct", idProduct);
                        bundleChat.putString("idUser", idUser);
                        bundleChat.putString("idUserFirstChat", phone);

                        Intent intentChat = new Intent(getApplicationContext(), ChatActivity.class);
                        intentChat.putExtra("bundle", bundleChat);
                        startActivity(intentChat);
                    }
                }
                break;

            case R.id.ln_sms:
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", idUser);
                smsIntent.putExtra("sms_body", mTvTitle.getText().toString());
                startActivity(smsIntent);
                break;

            case R.id.ln_profile:
                Bundle bundleProfile = new Bundle();
                bundleProfile.putString("idUser", idUser);

                Intent intentProfile = new Intent(getApplicationContext(), ProfileActivity.class);
                intentProfile.putExtra("bundle", bundleProfile);
                startActivity(intentProfile);
                break;
        }
    }

    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(DetailProductActivity.this, mImgMenu);

        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        popupMenu.getMenuInflater().inflate(R.menu.menu_more, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        popupMenu.show();
    }

    private void addImageProduct(){
        mProgress.setMessage("Đang tải dữ liệu...");
        mProgress.show();

        mArr = new ArrayList<>();

        productRef.child(idProduct).child("images").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot db : dataSnapshot.getChildren()) {
                    String idImg = db.getKey();

                    ImageProduct imageProduct = new ImageProduct();
                    imageProduct.setId(idImg);
                    imageProduct.setImgUrl(db.child("url").getValue().toString());

                    mArr.add(imageProduct);
                }

                ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), idProduct);
                String[] mArrImage = new String[mArr.size()];
                for(int i = 0; i < mArr.size(); i++){
                    mArrImage[i] = mArr.get(i).getImgUrl();
                }
                pagerAdapter.addAll(Arrays.asList(mArrImage));
                mVpImage.setAdapter(pagerAdapter);
                indicator.setViewPager(mVpImage);

                mProgress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getData(){
        mProgress.setMessage("Đang tải dữ liệu...");
        mProgress.show();

        productRef.child(idProduct).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title = dataSnapshot.child("title").getValue().toString();
                String price = dataSnapshot.child("price").getValue().toString();
                String time = dataSnapshot.child("time").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String category = dataSnapshot.child("category").getValue().toString();

                String city = dataSnapshot.child("city").getValue().toString();
                String description = dataSnapshot.child("description").getValue().toString();

                mTvTitle.setText(title);

                String formatPrice = FormatMoney.format(Integer.parseInt(price));
                mTvPrice.setText(formatPrice + " đ");

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
                mTvTime.setText(calculateTime(time));

                mTvDescription.setText(description);
                mTvCity.setText(city);
                mTvAddress.setText(city);
                mTvStatus.setText(status);
                mTvCategory.setText(category);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseConfig.userRef.child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTvFullname.setText(dataSnapshot.child("fullname").getValue().toString());
                Picasso.with(getApplicationContext()).load(dataSnapshot.child("urlAvatar").getValue().toString())
                        .placeholder(R.drawable.default_avatar)
                        .into(mImgAvatar);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mProgress.dismiss();
    }

    private String calculateTime(String startTime) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));

        Long millisEndTime = System.currentTimeMillis();
        Long millisStartTime = Long.parseLong(startTime);

        String kq = "";
        String startDate = dateFormat.format(new Date(millisStartTime)).replace("-", " ");
        String endTime = dateFormat.format(new Date(millisEndTime)).replace("-", " ");

        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(startDate);
            d2 = format.parse(endTime);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if (diffDays > 7) {
                kq = dateFormat.format(new Date(millisStartTime));
            } else if (diffDays == 7) {
                kq = "một tuần";
            } else if (diffDays >= 1) {
                kq = diffDays + " ngày";
            } else {
                if (diffHours >= 1) {
                    kq = diffHours + " giờ";
                } else {
                    if (diffMinutes >= 1) {
                        kq = diffMinutes + " phút";
                    } else {
                        kq = "Vừa xong";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return kq;
    }

    private void doFav(){
        Map<String, Object> map = new HashMap<>();
        map.put("idUser", phone);

        productRef.child(idProduct).child("Favorites").child(phone).setValue(map);
        Toast.makeText(getApplicationContext(), "Tin đã được đưa vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
    }

    private void doUnFav(){
        productRef.child(idProduct).child("Favorites").child(phone).removeValue();
        Toast.makeText(getApplicationContext(), "Đã hủy yêu thích tin này", Toast.LENGTH_SHORT).show();
    }

    private void displayFav(){
        productRef.child(idProduct).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Favorites").getValue() != null){
                    DatabaseReference favRef = dataSnapshot.child("Favorites").getRef();
                    favRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int count = 0;
                            for (DataSnapshot db : dataSnapshot.getChildren()){
                                if(db.getKey().equals(phone)){
                                    count++;
                                }
                            }

                            if(count == 0){
                                mImgFav.setVisibility(View.VISIBLE);
                                mImgFavPressed.setVisibility(View.GONE);
                            }else{
                                mImgFav.setVisibility(View.GONE);
                                mImgFavPressed.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
