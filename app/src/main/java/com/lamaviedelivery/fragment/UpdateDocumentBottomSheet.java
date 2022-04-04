package com.lamaviedelivery.fragment;

import static android.app.Activity.RESULT_OK;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.lamaviedelivery.BuildConfig;
import com.lamaviedelivery.EditProfileAct;
import com.lamaviedelivery.R;
import com.lamaviedelivery.databinding.FragmentEditDocumentBinding;
import com.lamaviedelivery.listener.StatusListener;
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

public class UpdateDocumentBottomSheet  extends BottomSheetDialogFragment {
    public String TAG = "UpdateDocumentBottomSheet";
    FragmentEditDocumentBinding binding;
    BottomSheetDialog dialog;
    private BottomSheetBehavior<View> mBehavior;


    private LamavieDeliveryInterface apiInterface;
    StatusListener listener;


    String str_image_path = "";
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int MY_PERMISSION_CONSTANT = 5;
    private Uri uriSavedImage;


    public UpdateDocumentBottomSheet() {
        //this.productData = productData;
    }


    public UpdateDocumentBottomSheet callBack(StatusListener listener) {
        this.listener = listener;
        return this;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_edit_document, null, false);
        dialog.setContentView(binding.getRoot());
        mBehavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        initViews();
        return dialog;
    }

    private void initViews() {
        apiInterface = ApiClient.getClient().create(LamavieDeliveryInterface.class);
        setDocInfo();

        binding.rlDocument.setOnClickListener(v -> {
            if(checkPermisssionForReadStorage())
                showImageSelection();
        });

        binding.btnUpdate.setOnClickListener(v -> {
            if(binding.etNationalId.getText().toString().equals("")){
                binding.etNationalId.setError(getString(R.string.required));
                binding.etNationalId.setFocusable(true);
            }
           else if(binding.etLincenseNumber.getText().toString().equals("")){
                binding.etLincenseNumber.setError(getString(R.string.required));
                binding.etLincenseNumber.setFocusable(true);
            }

           else if(binding.etExpiry.getText().toString().equals("")){
                binding.etExpiry.setError(getString(R.string.required));
                binding.etExpiry.setFocusable(true);
            }
           else {
               if(NetworkAvailablity.checkNetworkStatus(getActivity())) updateDoc();
            }


        });
    }

    private void updateDoc() {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        MultipartBody.Part filePart;
        if (!str_image_path.equalsIgnoreCase("")) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            filePart = MultipartBody.Part.createFormData("licence", file.getName(), RequestBody.create(MediaType.parse("licence/*"), file));
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }
        RequestBody Id = RequestBody.create(MediaType.parse("text/plain"), binding.etNationalId.getText().toString());
        RequestBody licenseNumber = RequestBody.create(MediaType.parse("text/plain"), binding.etLincenseNumber.getText().toString());
        RequestBody expiry = RequestBody.create(MediaType.parse("text/plain"), binding.etExpiry.getText().toString());
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), DataManager.getInstance().getUserData(getActivity()).result.id);




        Call<LoginModel> signupCall = apiInterface.updateDocument(Id,licenseNumber, expiry, userId, filePart);


        signupCall.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    LoginModel data = response.body();
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        SessionManager.writeString(getActivity(), Constant.USER_INFO, dataResponse);
                        listener.onStatus("Update");
                        dialog.dismiss();
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


    public void setDocInfo(){
        if(!DataManager.getInstance().getUserData(getActivity()).result.licence.equals("")){
          binding.ivDocument.setVisibility(View.VISIBLE);
            binding.ivUpload.setVisibility(View.GONE);
            binding.tvUpload.setVisibility(View.GONE);

            Glide.with(getActivity())
                    .load("https://lamavietech.ml/lamavie_laundry/uploads/images/"+DataManager.getInstance().getUserData(getActivity()).result.licence)
                    .error(R.drawable.dummy)
                    .into(binding.ivDocument);

            binding.etLincenseNumber.setText(DataManager.getInstance().getUserData(getActivity()).result.licenseNumber);
            binding.etNationalId.setText(DataManager.getInstance().getUserData(getActivity()).result.nationalId);
            binding.etExpiry.setText(DataManager.getInstance().getUserData(getActivity()).result.expirationDate);
        }
        else {
            binding.ivDocument.setVisibility(View.GONE);
            binding.ivUpload.setVisibility(View.VISIBLE);
            binding.tvUpload.setVisibility(View.VISIBLE);
        }

    }



    public void showImageSelection() {

        final Dialog dialog = new Dialog(getActivity());
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

        uriSavedImage = FileProvider.getUriForFile(getActivity(),
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
            binding.ivDocument.setVisibility(View.VISIBLE);
            binding.ivUpload.setVisibility(View.GONE);
            binding.tvUpload.setVisibility(View.GONE);

            if (requestCode == SELECT_FILE) {
                str_image_path = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());
                Glide.with(getActivity())
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.ivDocument);

            } else if (requestCode == REQUEST_CAMERA) {
                Glide.with(getActivity())
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.ivDocument);
            }

        }
    }


    //CHECKING FOR Camera STATUS
    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)


            ) {


                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
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
                        Toast.makeText(getActivity(), " permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "  permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                }
                // return;
            }


        }
    }



}
