package com.lamaviedelivery.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.lamaviedelivery.ChangePassAct;
import com.lamaviedelivery.EditProfileAct;
import com.lamaviedelivery.HomeAct;
import com.lamaviedelivery.OrderDetailAct;
import com.lamaviedelivery.OrderHistoryAct;
import com.lamaviedelivery.R;
import com.lamaviedelivery.databinding.FragmentAccountBinding;
import com.lamaviedelivery.listener.StatusListener;
import com.lamaviedelivery.model.LoginModel;
import com.lamaviedelivery.retrofit.ApiClient;
import com.lamaviedelivery.retrofit.Constant;
import com.lamaviedelivery.retrofit.LamavieDeliveryInterface;
import com.lamaviedelivery.utils.DataManager;
import com.lamaviedelivery.utils.SessionManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountFragment extends Fragment implements StatusListener {
    public String TAG = "AccountFragment";
    FragmentAccountBinding binding;
    LamavieDeliveryInterface apiInterface;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiInterface = ApiClient.getClient().create(LamavieDeliveryInterface.class);


        binding.tvLogout.setOnClickListener(v -> LogOutAlert());

        binding.layoutChangePass.setOnClickListener(v -> startActivity(new Intent(getActivity(), ChangePassAct.class)));

        binding.btnEdit.setOnClickListener(v -> startActivity(new Intent(getActivity(), EditProfileAct.class)));

        binding.layoutHistory.setOnClickListener(v -> startActivity(new Intent(getActivity(), OrderHistoryAct.class)));


        binding.btnDocumentEdit.setOnClickListener(v -> {
            new UpdateDocumentBottomSheet().callBack(this:: onStatus).show(getActivity().getSupportFragmentManager(),"");

        });


        if(DataManager.getInstance().getUserData(getActivity()).result.langunge.equals("ar")) {
            binding.switchLang.setText(getString(R.string.arabic));
            binding.switchLang.setChecked(true);

        }else {
            binding.switchLang.setText(getString(R.string.english));
            binding.switchLang.setChecked(false);
        }




        binding.switchLang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    DataManager.updateResources(getActivity(),"ar");
                    //  SessionManager.writeString(getActivity(), Constant.LANGUAGE,"ar");
                    binding.switchLang.setText(getString(R.string.arabic));
                    updateLang("ar");

                }
                else {
                    DataManager.updateResources(getActivity(),"en");
                    SessionManager.writeString(getActivity(), Constant.LANGUAGE,"en");
                    binding.switchLang.setText(getString(R.string.english));
                    updateLang("en");                }
            }
        });


    }

    public void LogOutAlert(){
        AlertDialog.Builder  builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage(getResources().getString(R.string.are_you_sure_you_want_to_logout_this_app));
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        SessionManager.clear(getActivity(), "","");
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onResume() {
        super.onResume();
       setUserInfo();
    }



    public void setUserInfo(){
        binding.etFName.setText(DataManager.getInstance().getUserData(getActivity()).result.firstName);
        binding.etLName.setText(DataManager.getInstance().getUserData(getActivity()).result.lastName);
        binding.etEmail.setText(DataManager.getInstance().getUserData(getActivity()).result.email);
        if(!DataManager.getInstance().getUserData(getActivity()).result.mobile.equals(""))   binding.etMobile.setText(DataManager.getInstance().getUserData(getActivity()).result.mobile);
        if(!DataManager.getInstance().getUserData(getActivity()).result.countryCode.equals("")) binding.ccp.setCountryForPhoneCode(Integer.parseInt(DataManager.getInstance().getUserData(getActivity()).result.countryCode));

        if(!DataManager.getInstance().getUserData(getActivity()).result.image.equals("")){
            Glide.with(getActivity())
                    .load(DataManager.getInstance().getUserData(getActivity()).result.image)
                    .error(R.drawable.dummy)
                    .into(binding.ivUser);
        }

        setDocInfo();

    }

    @Override
    public void onStatus(String status) {
        setUserInfo();
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


    private void updateLang(String lang) {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("langunge", lang);
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).result.id + "");
        Log.e(TAG, "Update Language Request " + map);
        Call<LoginModel> loginCall = apiInterface.changeLang(map);
        loginCall.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    LoginModel data = response.body();
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e("MapMap", "Update Language RESPONSE" + dataResponse);
                    if (data.status.equals("1")) {
                        Toast.makeText(getActivity(), data.message, Toast.LENGTH_SHORT).show();
                        SessionManager.writeString(getActivity(), Constant.USER_INFO, dataResponse);
                       // reloadFrg();
                        startActivity(new Intent(getActivity(), HomeAct.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    } else if (data.status.equals("0")) {
                        Toast.makeText(getActivity(), data.message, Toast.LENGTH_SHORT).show();
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

    public void reloadFrg(){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container,new AccountFragment()).addToBackStack(null).commit();

    }

}
