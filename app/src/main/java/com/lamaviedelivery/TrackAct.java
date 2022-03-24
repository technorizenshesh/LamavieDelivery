package com.lamaviedelivery;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.lamaviedelivery.adapter.OrderAdapter;
import com.lamaviedelivery.databinding.ActivityTrackBinding;
import com.lamaviedelivery.maps.DrawPollyLine;
import com.lamaviedelivery.model.OrderDetailModel;
import com.lamaviedelivery.retrofit.ApiClient;
import com.lamaviedelivery.retrofit.Constant;
import com.lamaviedelivery.retrofit.LamavieDeliveryInterface;
import com.lamaviedelivery.service.UpdateLocationService;
import com.lamaviedelivery.utils.DataManager;
import com.lamaviedelivery.utils.GPSTracker;
import com.lamaviedelivery.utils.NetworkAvailablity;
import com.lamaviedelivery.utils.SessionManager;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackAct extends AppCompatActivity  implements OnMapReadyCallback {
    public String TAG = "TrackAct";
    ActivityTrackBinding binding;
    LamavieDeliveryInterface apiInterface;
    GoogleMap mMap;
    private PolylineOptions lineOptions;
    private LatLng PickUpLatLng, DropOffLatLng, carLatLng, prelatLng;
    private MarkerOptions PicUpMarker, DropOffMarker, carMarker1;
    ArrayList<LatLng> polineLanLongLine = new ArrayList<>();
    Marker carMarker;
    int PERMISSION_ID = 44;
    double dLatitude = 0.0, dLongitude = 0.0;
    GPSTracker gpsTracker;
    private float start_rotation;
    OrderDetailModel model;

    BroadcastReceiver LocationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("latitude") != null) {
                //carLatLng = new LatLng(Double.parseDouble(intent.getStringExtra("latitude")), Double.parseDouble(intent.getStringExtra("longitude")));
               /* if (prelatLng == null) {
                    if (carMarker != null) carMarker.remove();
                    carMarker1.position(carLatLng);
                    carMarker = mMap.addMarker(carMarker1);
                    carMarker1.flat(true);
                    prelatLng = carLatLng;
                } else {
                    if (prelatLng != carLatLng) {
                        Log.e("locationChange====", carLatLng + "");
                        Location temp = new Location(LocationManager.GPS_PROVIDER);
                        temp.setLatitude(Double.parseDouble(intent.getStringExtra("latitude")));
                        temp.setLongitude(Double.parseDouble(intent.getStringExtra("longitude")));

                        moveVechile(carMarker, temp);
                        rotateMarker(carMarker, temp.getBearing(), start_rotation);

                        prelatLng = carLatLng;
                    }
                }*/

                PickUpLatLng = new LatLng(Double.parseDouble(intent.getStringExtra("latitude")), Double.parseDouble(intent.getStringExtra("longitude")));

                DrawPolyLine();

                animateCamera(PickUpLatLng);
            }

            //  binding.tvAddress.setText(getAddress(TrackAct.this,Double.parseDouble(intent.getStringExtra("latitude")), Double.parseDouble(intent.getStringExtra("longitude"))));
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gpsTracker = new GPSTracker(TrackAct.this);
        apiInterface = ApiClient.getClient().create(LamavieDeliveryInterface.class);
        binding =   DataBindingUtil.setContentView(this,R.layout.activity_track);
        initViews();
       SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
       mapFragment.getMapAsync(this);
    }

    private void initViews() {

        if(getIntent()!=null) model = (OrderDetailModel) getIntent().getSerializableExtra("OrderDetail");
         setDataaa();



        binding.ivBack.setOnClickListener(v -> finish());


        PicUpMarker = new MarkerOptions().title("Pick Up Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_gray));
        DropOffMarker = new MarkerOptions().title("Drop Off Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_gray));

        carMarker1 = new MarkerOptions().title("Car")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_gray));

        binding.btnSave.setOnClickListener(v -> {
            if(model.result.status.equals("Accept")){
                if(binding.etOtp.getText().toString().equals("")) {
                    Toast.makeText(TrackAct.this, getString(R.string.please_enter_otp), Toast.LENGTH_SHORT).show();
                }else {
                     if(NetworkAvailablity.checkNetworkStatus(TrackAct.this)) orderChangeStatus("Pickup");
                   else Toast.makeText(TrackAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                }
            }
            else {
                if(NetworkAvailablity.checkNetworkStatus(TrackAct.this)) orderChangeStatus("Deliver");
               else Toast.makeText(TrackAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void setDataaa(){
        if(model!=null){
            DropOffLatLng = new LatLng(Double.parseDouble(model.result.lat),Double.parseDouble(model.result.lon));
            PickUpLatLng = new LatLng(Double.parseDouble(model.result.pickupLat),Double.parseDouble(model.result.pickupLon));


            if (model.result.status.equals("Accept")) {
                binding.tvAddressName.setText(getString(R.string.pickup_address));
                binding.tvAddress.setText(model.result.pickupAddress);
                binding.tvBtnOrder.setText(getString(R.string.pickup));
                binding.etOtp.setVisibility(View.VISIBLE);
            }else if(model.result.status.equals("Pickup")){
                binding.tvAddressName.setText(getString(R.string.delivery_address));
                binding.tvAddress.setText(model.result.address);
                binding.tvBtnOrder.setText(getString(R.string.deliver_order));
                binding.etOtp.setVisibility(View.GONE);
            }
            binding.tvProduct.setText(model.result.itemDetails.size() + "Items");

        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //   gpsTracker = new GPSTracker(TrackAct.this);
      /*  if (!request_id.equals(""))
            getBookingDetail(request_id);*/
        setCurrentLoc();
    }

    private void callService() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                startService(new Intent(TrackAct.this, UpdateLocationService.class));
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private void DrawPolyLine() {
        DrawPollyLine.get(this).setOrigin(PickUpLatLng)
                .setDestination(DropOffLatLng).execute(new DrawPollyLine.onPolyLineResponse() {
            @Override
            public void Success(ArrayList<LatLng> latLngs) {
                mMap.clear();
                polineLanLongLine.clear();
                polineLanLongLine = latLngs;
                lineOptions = new PolylineOptions();
                lineOptions.addAll(latLngs);
                lineOptions.width(10);
                lineOptions.geodesic(true);
                lineOptions.color(R.color.black);
                AddDefaultMarker();
                prelatLng = null;
               // AddCarMarker(carLatLng);
            }
        });
    }

    public void AddDefaultMarker() {
        if (mMap != null) {
            mMap.clear();
            if (lineOptions != null)
                mMap.addPolyline(lineOptions);
            if (PickUpLatLng != null) {
                PicUpMarker.position(PickUpLatLng);
                mMap.addMarker(PicUpMarker);
                // mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(PickUpLatLng)));
            }
            if (DropOffLatLng != null) {
                DropOffMarker.position(DropOffLatLng);
                mMap.addMarker(DropOffMarker);
            }
        }
    }


    private void animateCamera(@NonNull LatLng location) {
        //  LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(location)));
    }


    @NonNull
    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(16).build();
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }


    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
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
                setCurrentLoc();
            }
        }
    }

    private void setCurrentLoc() {
        if(gpsTracker!=null){
            PickUpLatLng =  new LatLng( gpsTracker.getLatitude(),gpsTracker.getLongitude());
        }
        DrawPolyLine();
    }





    @Override
    protected void onResume() {
        super.onResume();
        callService();
        registerReceiver(LocationReceiver, new IntentFilter("data_update_location1"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(LocationReceiver);
        stopService(new Intent(TrackAct.this, UpdateLocationService.class));
    }

    public void moveVechile(final Marker myMarker, final Location finalPosition) {

        final LatLng startPosition = myMarker.getPosition();

        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 3000;
        final boolean hideMarker = false;

        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);

                LatLng currentPosition = new LatLng(
                        startPosition.latitude * (1 - t) + (finalPosition.getLatitude()) * t,
                        startPosition.longitude * (1 - t) + (finalPosition.getLongitude()) * t);
                myMarker.setPosition(currentPosition);
                // myMarker.setRotation(finalPosition.getBearing());


                // Repeat till progress is completeelse
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                    // handler.postDelayed(this, 100);
                } else {
                    if (hideMarker) {
                        myMarker.setVisible(false);
                    } else {
                        myMarker.setVisible(true);
                    }
                }
            }
        });


    }


    public void rotateMarker(final Marker marker, final float toRotation, final float st) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final float startRotation = marker.getRotation();
        final long duration = 1555;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);

                float rot = t * toRotation + (1 - t) * startRotation;


                marker.setRotation(-rot > 180 ? rot / 2 : rot);
                start_rotation = -rot > 180 ? rot / 2 : rot;
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    private void orderChangeStatus(String status) {
        Map<String, String> map = new HashMap<>();
        map.put("status", status);
        map.put("order_id", model.result.id+"");
        map.put("otp", binding.etOtp.getText().toString());
        map.put("date",DataManager.getCurrent12());
        map.put("time",DataManager.getCurrentTime12());
        Log.e(TAG, "Order status change REQUEST" + map);
        Call<Map<String, String>> subCategoryCall = apiInterface.requestAcceptCancel(map);
        subCategoryCall.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                try {
                    Map<String, String> data = response.body();
                    if (data.get("status").equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e(TAG, "Order status change RESPONSE" + dataResponse);
                        if(status.equals("Deliver")){
                            startActivity(new Intent(TrackAct.this,HomeAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                           finish();
                        }
                        else {

                            if (NetworkAvailablity.checkNetworkStatus(TrackAct.this)) getOrderDetail();
                            else Toast.makeText(TrackAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

                        }

                    } else if (data.get("status").equals("0")) {
                        Toast.makeText(TrackAct.this, data.get("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    public void getOrderDetail() {
        DataManager.getInstance().showProgressMessage(TrackAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("order_id", model.result.id+"");
        Log.e(TAG, "get Current OrderDetail Request" + map);
        Call<OrderDetailModel> loginCall = apiInterface.getOrderDetailsss(map);
        loginCall.enqueue(new Callback<OrderDetailModel>() {
            @Override
            public void onResponse(Call<OrderDetailModel> call, Response<OrderDetailModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    model = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "get Current OrderDetail Response :" + responseString);
                    if (model.status.equals("1")) {
                       setDataaa();
                    } else if (model.status.equals("0")) {
                        Toast.makeText(TrackAct.this, model.message, Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<OrderDetailModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

}
