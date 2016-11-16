package com.example.administrator.chotot.activity;

import android.Manifest.permission;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.administrator.chotot.R;

/**
 * Created by Administrator on 08/11/2016.
 */

public class TestLocation extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_location);

        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationListener locListener = new LocationListener() {
            public void onLocationChanged(Location loc) {

                loc.getLatitude();
                loc.getLongitude();

                String Text = "My current location is: " +
                        "Latitud = " + loc.getLatitude() +
                        "Longitud = " + loc.getLongitude();

                Log.e("Test", Text);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (mlocManager != null) {
            if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mlocManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
            mlocManager .requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0, locListener );
        }
    }
}
