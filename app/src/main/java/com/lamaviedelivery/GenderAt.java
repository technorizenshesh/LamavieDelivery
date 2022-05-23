package com.lamaviedelivery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.lamaviedelivery.databinding.ActivityGenderBinding;
import com.lamaviedelivery.retrofit.ApiClient;
import com.lamaviedelivery.retrofit.Constant;
import com.lamaviedelivery.retrofit.LamavieDeliveryInterface;
import com.lamaviedelivery.utils.DataManager;
import com.lamaviedelivery.utils.SessionManager;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GenderAt extends AppCompatActivity {
    public String TAG = "GenderAt";
    ActivityGenderBinding binding;
    String firstName="",lastName="",email="",mobile="",countryCode="",dob="",address="",lat="",lon="",city="",refreshedToken="",type="",str_image_path="",gender="";
    LamavieDeliveryInterface apiInterface;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(LamavieDeliveryInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_gender);
        initViews();
    }

    private void initViews() {

        if (getIntent() != null) {
            firstName = getIntent().getStringExtra("firstName");
            lastName = getIntent().getStringExtra("lastName");
            email = getIntent().getStringExtra("email");
            mobile = getIntent().getStringExtra("mobile");
            countryCode = getIntent().getStringExtra("country_code");
            dob = getIntent().getStringExtra("dob");
            address = getIntent().getStringExtra("address");
            lat = getIntent().getStringExtra("lat");
            lon = getIntent().getStringExtra("lon");
            refreshedToken = getIntent().getStringExtra("refreshedToken");
            type = getIntent().getStringExtra("type");
            str_image_path = getIntent().getStringExtra("profileImage");
        }


        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnNext.setOnClickListener(v -> {
            if(gender.equals("")) Toast.makeText(this, getString(R.string.please_select_gender), Toast.LENGTH_SHORT).show();
            else userSignup();

        });

        binding.layoutMale.setOnClickListener(v -> {
            gender = "Male";
            binding.tvMale.setTextColor(getResources().getColor(R.color.color_next));
            binding.tvFemale.setTextColor(getResources().getColor(R.color.black));
        });

        binding.layoutFemale.setOnClickListener(v -> {
            gender = "Female";
            binding.tvMale.setTextColor(getResources().getColor(R.color.black));
            binding.tvFemale.setTextColor(getResources().getColor(R.color.color_next));
        });



    }

    private void userSignup() {
        DataManager.getInstance().showProgressMessage(GenderAt.this, getString(R.string.please_wait));
        MultipartBody.Part filePart;


        if (!str_image_path.equalsIgnoreCase("")) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }
        RequestBody f_name = RequestBody.create(MediaType.parse("text/plain"), firstName);
        RequestBody l_name = RequestBody.create(MediaType.parse("text/plain"), lastName);
        RequestBody Email = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody Dob = RequestBody.create(MediaType.parse("text/plain"), dob);
        RequestBody Mob = RequestBody.create(MediaType.parse("text/plain"), mobile);
        RequestBody CountryCo = RequestBody.create(MediaType.parse("text/plain"), countryCode);
        RequestBody addressss = RequestBody.create(MediaType.parse("text/plain"), address);
        RequestBody lattt = RequestBody.create(MediaType.parse("text/plain"), lat);
        RequestBody lonn = RequestBody.create(MediaType.parse("text/plain"), lon);
        RequestBody Gender = RequestBody.create(MediaType.parse("text/plain"), gender);
        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), refreshedToken);
        RequestBody Type = RequestBody.create(MediaType.parse("text/plain"), type);
        RequestBody lang = RequestBody.create(MediaType.parse("text/plain"), SessionManager.readString(GenderAt.this, Constant.LANGUAGE,""));


        Call<Map<String,String>> editNameCall = apiInterface.signupUser(f_name,l_name,Email,Mob,CountryCo,Dob,Gender,addressss,lattt,lonn,token,Type,lang, filePart);
        editNameCall.enqueue(new Callback<Map<String,String>>() {
            @Override
            public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    Map<String,String> data = response.body();
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e(TAG, "Signup RESPONSE" + dataResponse);
                    if (data.get("status").equals("1")) {
                        Toast.makeText(GenderAt.this, data.get("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(GenderAt.this,LogWithMobileAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();

                    } else if (data.get("status").equals("0")) {
                        Toast.makeText(GenderAt.this, data.get("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Map<String,String>> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });
    }
}
