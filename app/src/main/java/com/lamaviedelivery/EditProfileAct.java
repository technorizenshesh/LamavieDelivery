package com.lamaviedelivery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.lamaviedelivery.databinding.ActivityEditProfileBinding;
import com.lamaviedelivery.model.LoginModel;
import com.lamaviedelivery.retrofit.ApiClient;
import com.lamaviedelivery.retrofit.Constant;
import com.lamaviedelivery.retrofit.LamavieDeliveryInterface;
import com.lamaviedelivery.utils.DataManager;
import com.lamaviedelivery.utils.NetworkAvailablity;
import com.lamaviedelivery.utils.SessionManager;


import java.io.File;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileAct extends AppCompatActivity {
    ActivityEditProfileBinding binding;
    String str_image_path = "";
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int MY_PERMISSION_CONSTANT = 5;
    private Uri uriSavedImage;
    LamavieDeliveryInterface apiInterface;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(LamavieDeliveryInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_profile);
        initViews();
    }

    private void initViews() {
        setUserInfo();
        binding.ivBack.setOnClickListener(v -> finish());

        binding.btnSave.setOnClickListener(v -> validation());

        binding.ivProfile.setOnClickListener(v -> {
            if(checkPermisssionForReadStorage())
                showImageSelection();
        });



    }


    private void validation() {
        if (binding.etFName.getText().toString().equals("")) {
            binding.etFName.setError(getString(R.string.required));
            binding.etFName.setFocusable(true);
        }
        if (binding.etLName.getText().toString().equals("")) {
            binding.etLName.setError(getString(R.string.required));
            binding.etLName.setFocusable(true);
        }

        else if (binding.etMobile.getText().toString().equals("")) {
            binding.etMobile.setError(getString(R.string.required));
            binding.etMobile.setFocusable(true);
        }
        else {
            if(NetworkAvailablity.checkNetworkStatus(EditProfileAct.this)) updateProfile();
            else Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        }
    }

    public void setUserInfo(){
        binding.etFName.setText(DataManager.getInstance().getUserData(EditProfileAct.this).result.firstName);
        binding.etLName.setText(DataManager.getInstance().getUserData(EditProfileAct.this).result.lastName);
        binding.etEmail.setText(DataManager.getInstance().getUserData(EditProfileAct.this).result.email);
        binding.etMobile.setText(DataManager.getInstance().getUserData(EditProfileAct.this).result.mobile);
        binding.ccp.setCountryForPhoneCode(Integer.parseInt(DataManager.getInstance().getUserData(EditProfileAct.this).result.countryCode));

        Glide.with(EditProfileAct.this)
                .load(DataManager.getInstance().getUserData(EditProfileAct.this).result.image)
                .override(80,80)
                .apply(new RequestOptions().placeholder(R.drawable.user_default))
                .into(binding.ivProfile);

    }

    public void showImageSelection() {

        final Dialog dialog = new Dialog(EditProfileAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_image_selection);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        LinearLayout layoutCamera = (LinearLayout) dialog.findViewById(R.id.layoutCemera);
        LinearLayout layoutGallary = (LinearLayout) dialog.findViewById(R.id.layoutGallary);
        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                openCamera();
            }
        });
        layoutGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                getPhotoFromGallary();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    private void getPhotoFromGallary() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);

    }

    private void openCamera() {

        File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + "/LamavieDelivery/Images/");

        if (!dirtostoreFile.exists()) {
            dirtostoreFile.mkdirs();
        }

        String timestr = DataManager.getInstance().convertDateToString(Calendar.getInstance().getTimeInMillis());

        File tostoreFile = new File(Environment.getExternalStorageDirectory() + "/LamavieDelivery/Images/" + "IMG_" + timestr + ".jpg");

        str_image_path = tostoreFile.getPath();

        uriSavedImage = FileProvider.getUriForFile(EditProfileAct.this,
                BuildConfig.APPLICATION_ID + ".provider",
                tostoreFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

        startActivityForResult(intent, REQUEST_CAMERA);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.e("Result_code", requestCode + "");
            if (requestCode == SELECT_FILE) {
                str_image_path = DataManager.getInstance().getRealPathFromURI(EditProfileAct.this, data.getData());
                Glide.with(EditProfileAct.this)
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.ivProfile);

            } else if (requestCode == REQUEST_CAMERA) {
                Glide.with(EditProfileAct.this)
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.ivProfile);
            }

        }
    }


    //CHECKING FOR Camera STATUS
    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(EditProfileAct.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(EditProfileAct.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(EditProfileAct.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfileAct.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(EditProfileAct.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(EditProfileAct.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)


            ) {


                ActivityCompat.requestPermissions(EditProfileAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(EditProfileAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            }
            return false;
        } else {

            //  explain("Please Allow Location Permission");
            return true;
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read_external_storage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean write_external_storage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (camera && read_external_storage && write_external_storage) {
                        showImageSelection();
                    } else {
                        Toast.makeText(EditProfileAct.this, " permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditProfileAct.this, "  permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                }
                // return;
            }


        }
    }


    private void updateProfile() {
        DataManager.getInstance().showProgressMessage(EditProfileAct.this, getString(R.string.please_wait));
        MultipartBody.Part filePart;
        if (!str_image_path.equalsIgnoreCase("")) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }
        RequestBody f_name = RequestBody.create(MediaType.parse("text/plain"), binding.etFName.getText().toString());
        RequestBody l_name = RequestBody.create(MediaType.parse("text/plain"), binding.etLName.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), binding.etEmail.getText().toString());
        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), binding.etMobile.getText().toString());
        RequestBody country_code = RequestBody.create(MediaType.parse("text/plain"), binding.ccp.getSelectedCountryCode());
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), DataManager.getInstance().getUserData(EditProfileAct.this).result.address);
        RequestBody DOB = RequestBody.create(MediaType.parse("text/plain"),DataManager.getInstance().getUserData(EditProfileAct.this).result.dob );
        RequestBody GEN = RequestBody.create(MediaType.parse("text/plain"),DataManager.getInstance().getUserData(EditProfileAct.this).result.gender );
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), DataManager.getInstance().getUserData(EditProfileAct.this).result.id);



        Call<LoginModel> signupCall = apiInterface.profileUpdate(f_name,l_name, email, mobile, country_code,address,DOB,GEN,user_id, filePart);


        signupCall.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    LoginModel data = response.body();
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        SessionManager.writeString(EditProfileAct.this, Constant.USER_INFO, dataResponse);
                        finish();
                    } else if (data.status.equals("0")) {
                       // App.showToast(EditProfileActivity.this, data.message, Toast.LENGTH_SHORT);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });
    }

}
