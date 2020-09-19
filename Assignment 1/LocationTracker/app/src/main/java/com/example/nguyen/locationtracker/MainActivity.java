package com.example.nguyen.locationtracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.*;

public class MainActivity extends AppCompatActivity {

    private Button button_1;
    private Button button_2;
    private TextView lo;
    private TextView la;
    private TextView a;
    private TextView sp;
    private LocationManager locationManager;
    private LocationListener listener;
    private ImageButton imageButton;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_1 = (Button) findViewById(R.id.button_start);
        button_2 = (Button) findViewById(R.id.button_stop);
        lo = (TextView) findViewById(R.id.y);
        la = (TextView) findViewById(R.id.x);
        a = (TextView) findViewById(R.id.z);
        sp = (TextView) findViewById(R.id.s);
        geocoder = new Geocoder(this, Locale.getDefault());
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lo.setText(String.format("%s", location.getLongitude()));
                la.setText(String.format("%s", location.getLatitude()));
                List<Address> addresses;
                Address add;
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && addresses.size() > 0) {
                        String address = addresses.get(0).getAddressLine(0);
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();
                        a.setText(address);
                        sp.setText(String.format("%s", location.getSpeed()+"m/s"+" "+"("+location.getSpeed()*3.6f+"km/h"+")"));
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, "The location has changed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        configure_button();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        button_1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                Toast.makeText(MainActivity.this, "You clicked 'Start Tracking' button", Toast.LENGTH_LONG).show();
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 15, listener);
            }
        });
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "You clicked 'Stop Tracking' button to pause", Toast.LENGTH_LONG).show();
                locationManager.removeUpdates(listener);

            }
        });
    }
}
