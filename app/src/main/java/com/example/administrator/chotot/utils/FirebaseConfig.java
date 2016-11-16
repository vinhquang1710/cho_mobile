package com.example.administrator.chotot.utils;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Administrator on 29/10/2016.
 */

public class FirebaseConfig extends Application{

    private static FirebaseDatabase rootData;

    private static FirebaseStorage rootStorage;

    public static DatabaseReference userRef;
    public static DatabaseReference productRef;

    public static StorageReference storageRef;

    @Override
    public void onCreate() {
        super.onCreate();
        rootData = FirebaseDatabase.getInstance();
        rootData.setPersistenceEnabled(true);

        rootStorage = FirebaseStorage.getInstance();
        storageRef = rootStorage.getReferenceFromUrl("gs://chotot-d0d2e.appspot.com");

        userRef = rootData.getReference("Users");
        productRef = rootData.getReference("Products");
    }
}
