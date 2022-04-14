package com.lamaviedelivery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.lamaviedelivery.databinding.ActivitySignupBinding;

import java.util.Arrays;
import java.util.List;

import static com.lamaviedelivery.retrofit.Constant.emailPattern;


public class SignupAct extends AppCompatActivity {
    ActivitySignupBinding binding;
    double latitude = 0.0, longitude = 0.0;
    int AUTOCOMPLETE_REQUEST_CODE_ADDRESS = 101;
    String address="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        initViews();
    }

    private void initViews() {

        binding.ccp.setCountryForPhoneCode(20);

        if (!Places.isInitialized()) {
            Places.initialize(SignupAct.this, getString(R.string.place_api_key));
        }


        binding.tvAddress.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    //.setCountry("SA")
                    .build(SignupAct.this);

            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_ADDRESS);
        });



        binding.btnNext.setOnClickListener(v -> {
            validation();
        });
    }


    public void validation() {
        if (binding.etFName.getText().toString().equals("")) {
            binding.etFName.setError(getString(R.string.please_enter_first_name));
            binding.etFName.setFocusable(true);
        }
        else if (binding.etLName.getText().toString().equals("")) {
            binding.etLName.setError(getString(R.string.please_enter_last_name));
            binding.etLName.setFocusable(true);
        } else if (binding.etEmail.getText().toString().equals("")) {
            binding.etEmail.setError(getString(R.string.please_enter_email));
            binding.etEmail.setFocusable(true);
        } else if (!binding.etEmail.getText().toString().matches(emailPattern)) {
            binding.etEmail.setError(getString(R.string.wrong_email));
            binding.etEmail.setFocusable(true);
        } else if (binding.etDob.getText().toString().equals("")) {
            binding.etDob.setError(getString(R.string.enter_dob));
            binding.etDob.setFocusable(true);
        } else if (binding.etMobile.getText().toString().equals("")) {
            binding.etMobile.setError(getString(R.string.please_enter_mobile_number));
            binding.etMobile.setFocusable(true);
        }
        else if (address.equals("")) {
            Toast.makeText(this, getString(R.string.please_select_address), Toast.LENGTH_SHORT).show();
        }

        else {
            startActivity(new Intent(this, UploadPicAct.class)
                    .putExtra("firstName", binding.etFName.getText().toString())
                    .putExtra("lastName", binding.etLName.getText().toString())
                    .putExtra("email", binding.etEmail.getText().toString())
                    .putExtra("mobile", binding.etMobile.getText().toString())
                    .putExtra("country_code", binding.ccp.getSelectedCountryCode() + "")
                    .putExtra("dob", binding.etDob.getText().toString())
                    .putExtra("address", address)
                    .putExtra("lat", latitude+"")
                    .putExtra("lon", longitude+"")
                    .putExtra("type", "DRIVER")
                    .putExtra("refreshedToken", "123456ke")

            );
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE_ADDRESS) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                try {
                    Log.e("addressStreet====", place.getAddress());
                    address = place.getAddress();
                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;
                    //  city = DataManager.getInstance().getAddress(SignupAct.this,latitude,longitude);
                    //  binding.tvCity.setVisibility(View.VISIBLE);
                    //   binding.tvCity.setText(city);
                    binding.tvAddress.setText(place.getAddress());
                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;
                } catch (Exception e) {
                    e.printStackTrace();
                    //setMarker(latLng);
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
            }

        }

    }

}
