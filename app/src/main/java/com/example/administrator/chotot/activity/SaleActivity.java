package com.example.administrator.chotot.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.chotot.R;
import com.example.administrator.chotot.dialog.CategoryDialog;
import com.example.administrator.chotot.dialog.CityDialog;
import com.example.administrator.chotot.dialog.StatusProductDialog;
import com.example.administrator.chotot.utils.FirebaseConfig;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.administrator.chotot.utils.FirebaseConfig.storageRef;

/**
 * Created by Administrator on 23/10/2016.
 */

public class SaleActivity extends AppCompatActivity implements OnClickListener {
    private EditText mEtTitle, mEtCategory, mEtCity, mEtPrice, mEtStatus, mEtDescribe;
    private TextInputLayout mInputLayoutTitle, mInputLayoutCategory, mInputLayoutCity,
            mInputLayoutPrice, mInputLayoutStatus, mInputLayoutDescribe;
    private TextView mTvSale, mTvAddImage, mTvDelImage;
    private ImageView mImgBack;
    private LinearLayout mLnImage;

    private ProgressDialog mProgressDialog;

    private String idCategory;

    private final int PICK_IMAGE_MULTIPLE = 100;
    private final int CAMERA_CODE = 200;
    private ArrayList<String> mArrImagesPathList = new ArrayList<String>();
    private Bitmap bitmap;
    private View v;
    private ImageView mImgImage;

    private ArrayList<String> mArrNameImage = new ArrayList<>();
    private ArrayList<String> mArrUrlImage = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        init();
        setListener();
    }

    private void init() {
        mImgBack = (ImageView) findViewById(R.id.img_back);
        mTvSale = (TextView) findViewById(R.id.tv_sale);
        mEtTitle = (EditText) findViewById(R.id.et_title_product);
        mEtCategory = (EditText) findViewById(R.id.et_category);
        mEtCity = (EditText) findViewById(R.id.et_city);
        mEtPrice = (EditText) findViewById(R.id.et_price);
        mEtStatus = (EditText) findViewById(R.id.et_status);
        mEtDescribe = (EditText) findViewById(R.id.et_describe);

        mTvAddImage = (TextView) findViewById(R.id.tv_add_image);
        mTvDelImage = (TextView) findViewById(R.id.tv_del_image);
        mLnImage = (LinearLayout) findViewById(R.id.ln_image_product);

        mInputLayoutTitle = (TextInputLayout) findViewById(R.id.input_layout_title);
        mInputLayoutCategory = (TextInputLayout) findViewById(R.id.input_layout_category);
        mInputLayoutCity = (TextInputLayout) findViewById(R.id.input_layout_city);
        mInputLayoutPrice = (TextInputLayout) findViewById(R.id.input_layout_price);
        mInputLayoutStatus = (TextInputLayout) findViewById(R.id.input_layout_status);

        mInputLayoutTitle.setCounterMaxLength(50);

    }

    private void setListener() {
        mImgBack.setOnClickListener(this);
        mTvSale.setOnClickListener(this);
        mEtCategory.setOnClickListener(this);
        mEtCity.setOnClickListener(this);
        mEtStatus.setOnClickListener(this);

        mTvAddImage.setOnClickListener(this);
        mTvDelImage.setOnClickListener(this);

        mEtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    mInputLayoutTitle.setCounterEnabled(false);
                } else {
                    mInputLayoutTitle.setCounterEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEtPrice.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    if (!s.toString().equals(current)) {

                        String cleanString = s.toString().replaceAll("[,.]", "");

                        double parsed = Double.parseDouble(cleanString);

                        String formated = NumberFormat.getInstance().format((parsed));

                        current = formated;

                        mEtPrice.setText(formated);
                        mEtPrice.setSelection(formated.length());
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;

            case R.id.tv_sale:
                if (mArrUrlImage.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Ít nhất phải có 1 tấm hình", Toast.LENGTH_SHORT).show();
                } else {
                    if (validate()) {
                        doSale();
                    }
                }
                break;

            case R.id.et_category:
                Bundle bundleCat = new Bundle();
                bundleCat.putString("category", mEtCategory.getText().toString());

                Intent intentCat = new Intent(getApplicationContext(), CategoryDialog.class);
                intentCat.putExtra("bundle", bundleCat);
                startActivityForResult(intentCat, 2);
                break;

            case R.id.et_city:
                Bundle bundleAddress = new Bundle();
                bundleAddress.putString("city", mEtCity.getText().toString());

                Intent intentAddress = new Intent(getApplicationContext(), CityDialog.class);
                intentAddress.putExtra("bundle", bundleAddress);
                startActivityForResult(intentAddress, 1);
                break;

            case R.id.et_status:
                Bundle bundleStatus = new Bundle();
                bundleStatus.putString("status", mEtStatus.getText().toString());

                Intent intentStatus = new Intent(getApplicationContext(), StatusProductDialog.class);
                intentStatus.putExtra("bundle", bundleStatus);
                startActivityForResult(intentStatus, 3);
                break;

            case R.id.tv_add_image:
                showPopupMenu();
                break;

            case R.id.tv_del_image:
                mLnImage.removeAllViews();
                for (int i = 0; i < mArrNameImage.size(); i++) {
                    StorageReference imageRef = storageRef.child("images/" + mArrNameImage.get(i));

                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mArrNameImage.removeAll(mArrNameImage);
                            mArrUrlImage.removeAll(mArrUrlImage);
                        }
                    });
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String city = data.getStringExtra("city");
                mEtCity.setText(city);
            }
        }

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                String category = data.getStringExtra("category");
                mEtCategory.setText(category);
                idCategory = data.getStringExtra("id");
            }
        }

        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                String status = data.getStringExtra("status");
                mEtStatus.setText(status);
            }
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_MULTIPLE) {
                final String[] imagesPath = data.getStringExtra("data").split("\\|");

                for (int i = 0; i < imagesPath.length; i++) {
                    mArrImagesPathList.add(imagesPath[i]);
                    bitmap = BitmapFactory.decodeFile(imagesPath[i]);

                    uploadImage(bitmap);

                    v = getLayoutInflater().inflate(R.layout.item_layout_image, null);
                    mImgImage = (ImageView) v.findViewById(R.id.img_image);

                    LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(5, 0, 5, 0);
                    mImgImage.setImageBitmap(bitmap);

                    mLnImage.addView(v, layoutParams);
                }
            }

            if (requestCode == CAMERA_CODE) {
                v = getLayoutInflater().inflate(R.layout.item_layout_image, null);
                mImgImage = (ImageView) v.findViewById(R.id.img_image);

                LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(5, 0, 5, 0);

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                uploadImage(bitmap);

                mImgImage.setImageBitmap(bitmap);
                mLnImage.addView(v, layoutParams);
            }
        }
    }

    private boolean validate() {
        String title = mEtTitle.getText().toString().trim();
        String category = mEtCategory.getText().toString().trim();
        String city = mEtCity.getText().toString().trim();
        String price = mEtPrice.getText().toString().trim();
        String status = mEtStatus.getText().toString().trim();

        if (title.length() == 0) {
            mInputLayoutTitle.setError("Vui lòng nhập tiêu đề sản phẩm");
            return false;
        } else {
            mInputLayoutTitle.setErrorEnabled(false);
        }

        if (category.length() == 0) {
            mInputLayoutCategory.setError("Vui lòng chọn danh mục");
            return false;
        } else {
            mInputLayoutCategory.setErrorEnabled(false);
        }

        if (city.length() == 0) {
            mInputLayoutCity.setError("Vui lòng chọn địa điểm giao dịch");
            return false;
        } else {
            mInputLayoutCity.setErrorEnabled(false);
        }

        if (price.length() == 0) {
            mInputLayoutPrice.setError("Vui lòng nhập giá sản phẩm");
            return false;
        } else {
            mInputLayoutPrice.setErrorEnabled(false);
        }

        if (status.length() == 0) {
            mInputLayoutStatus.setError("Vui lòng chọn tình trạng sản phẩm");
            return false;
        } else {
            mInputLayoutStatus.setErrorEnabled(false);
        }

        return true;
    }

    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(SaleActivity.this, mTvAddImage);
        popupMenu.getMenuInflater().inflate(R.menu.menu_add_image, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_gallery:
                        Bundle bundleAddImg = new Bundle();
                        bundleAddImg.putInt("count", mArrUrlImage.size());

                        Intent intentGallery = new Intent(getApplicationContext(), SelectImageFromGalleryActivity.class);
                        intentGallery.putExtra("bundle", bundleAddImg);
                        startActivityForResult(intentGallery, PICK_IMAGE_MULTIPLE);
                        break;

                    case R.id.item_camera:
                        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intentCamera, CAMERA_CODE);
                        break;
                }

                return true;
            }
        });

        popupMenu.show();
    }

    private void doSale() {
        long currentMillis = System.currentTimeMillis();
        String title = mEtTitle.getText().toString().trim();
        String city = mEtCity.getText().toString().trim();
        String price = mEtPrice.getText().toString().trim();
        String status = mEtStatus.getText().toString().trim();
        String describe = mEtDescribe.getText().toString().trim();
        String category = mEtCategory.getText().toString().trim();

        String priceRep = price.replace(".", "");

        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("category", category);
        map.put("city", city);
        map.put("price", priceRep);
        map.put("status", status);
        map.put("description", describe);
        map.put("time", currentMillis);
        map.put("idUser", MainActivity.phone);

        Map<String, Object> mapImage = new HashMap<>();
        for (int i = 0; i < mArrUrlImage.size(); i++) {
            Map<String, Object> mapUrl = new HashMap<>();
            mapUrl.put("url", mArrUrlImage.get(i));

            mapImage.put(String.valueOf(i), mapUrl);
        }

        map.put("images", mapImage);
        map.put("img", mArrUrlImage.get(0));

        FirebaseConfig.productRef.child(currentMillis + "").setValue(map);
        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putString("id", currentMillis + "");
        bundle.putString("phone", MainActivity.phone);

        Intent intent = new Intent(getApplicationContext(), DetailProductActivity.class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
        finish();
    }

    private void uploadImage(Bitmap bitmap) {
        final StorageReference nameImage = storageRef.child("images/" + System.currentTimeMillis() + ".jpg");

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
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        }).addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                mArrNameImage.add(nameImage.getName());
                mArrUrlImage.add(downloadUrl + "");
                hideProgressDialog();
            }
        });
    }

    private void showProgressDialog(String title, String message) {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.setMessage(message);
        else
            mProgressDialog = ProgressDialog.show(this, title, message, true, false);
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void showHorizontalProgressDialog(String title, String body) {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.setTitle(title);
            mProgressDialog.setMessage(body);
        } else {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle(title);
            mProgressDialog.setMessage(body);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setProgress(0);
            mProgressDialog.setMax(100);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    public void updateProgress(int progress) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.setProgress(progress);
        }
    }
}
