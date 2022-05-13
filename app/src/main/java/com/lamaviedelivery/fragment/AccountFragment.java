package com.lamaviedelivery.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lamaviedelivery.ChangePassAct;
import com.lamaviedelivery.EditProfileAct;
import com.lamaviedelivery.OrderDetailAct;
import com.lamaviedelivery.OrderHistoryAct;
import com.lamaviedelivery.R;
import com.lamaviedelivery.databinding.FragmentAccountBinding;
import com.lamaviedelivery.listener.StatusListener;
import com.lamaviedelivery.utils.DataManager;
import com.lamaviedelivery.utils.SessionManager;


public class AccountFragment extends Fragment implements StatusListener {
   FragmentAccountBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.tvLogout.setOnClickListener(v -> LogOutAlert());

        binding.layoutChangePass.setOnClickListener(v -> startActivity(new Intent(getActivity(), ChangePassAct.class)));

        binding.btnEdit.setOnClickListener(v -> startActivity(new Intent(getActivity(), EditProfileAct.class)));

        binding.layoutHistory.setOnClickListener(v -> startActivity(new Intent(getActivity(), OrderHistoryAct.class)));


        binding.btnDocumentEdit.setOnClickListener(v -> {
            new UpdateDocumentBottomSheet().callBack(this:: onStatus).show(getActivity().getSupportFragmentManager(),"");

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

}
