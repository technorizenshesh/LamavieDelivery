package com.lamaviedelivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.lamaviedelivery.databinding.ActivitySplashBinding;
import com.lamaviedelivery.retrofit.Constant;
import com.lamaviedelivery.utils.DataManager;
import com.lamaviedelivery.utils.SessionManager;
import com.lamaviedelivery.welcome.WecomeFirstAct;
import com.lamaviedelivery.welcome.WelcomeAct;


public class SplashAct extends AppCompatActivity {
    ActivitySplashBinding binding;
    public static int SPLASH_TIME_OUT = 3000;
    int PERMISSION_ID = 44;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash);
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                processNextActivity();
            } else {
                Toast.makeText(SplashAct.this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        } else {
            requestPermissions();
        }
    }


    private void processNextActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (DataManager.getInstance().getUserData(getApplicationContext()) != null&&
                        DataManager.getInstance().getUserData(getApplicationContext()).result != null &&
                        DataManager.getInstance().getUserData(getApplicationContext()).result.id!=null) {
                    if(DataManager.getInstance().getUserData(SplashAct.this).result.langunge.equals("ar"))  { DataManager.updateResources(SplashAct.this,"ar"); }
                    else { DataManager.updateResources(SplashAct.this,"en"); }
                    startActivity(new Intent(SplashAct.this, HomeAct.class));
                    finish();
                } else {

                    if(SessionManager.readString(SplashAct.this, Constant.LANGUAGE, "").equals("en")){
                        DataManager.updateResources(SplashAct.this,"en");
                        SessionManager.writeString(SplashAct.this, Constant.LANGUAGE, "en");
                    }
                    else {
                        DataManager.updateResources(SplashAct.this,"ar");
                        SessionManager.writeString(SplashAct.this, Constant.LANGUAGE, "ar");
                    }
                    startActivity(new Intent(SplashAct.this, WelcomeAct.class));
                    finish();
                }


            }
        }, SPLASH_TIME_OUT);
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(SplashAct.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(SplashAct.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(SplashAct.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED ) {
            return true;
        }
        return false;
    }


    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                SplashAct.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                processNextActivity();
            }
        }
    }

}