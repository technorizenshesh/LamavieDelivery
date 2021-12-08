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
import com.lamaviedelivery.ChangePassAct;
import com.lamaviedelivery.EditProfileAct;
import com.lamaviedelivery.R;
import com.lamaviedelivery.databinding.FragmentAccountBinding;
import com.lamaviedelivery.utils.DataManager;
import com.lamaviedelivery.utils.SessionManager;


public class AccountFragment extends Fragment {
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


        binding.layoutLogout.setOnClickListener(v -> LogOutAlert());

        binding.layoutChangePass.setOnClickListener(v -> startActivity(new Intent(getActivity(), ChangePassAct.class)));

        binding.layoutAccount.setOnClickListener(v -> startActivity(new Intent(getActivity(), EditProfileAct.class)));


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
        if(!DataManager.getInstance().getUserData(getActivity()).result.image.equals("")){
            Glide.with(getActivity())
                    .load(DataManager.getInstance().getUserData(getActivity()).result.image)
                    .error(R.drawable.dummy)
                    .into(binding.ivUser);
        }
        binding.tvName.setText(DataManager.getInstance().getUserData(getActivity()).result.firstName + " " +  DataManager.getInstance().getUserData(getActivity()).result.lastName);
        binding.tvAddress.setText(DataManager.getInstance().getUserData(getActivity()).result.address);
    }
}
