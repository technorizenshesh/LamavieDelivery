package com.lamaviedelivery;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.lamaviedelivery.databinding.ActivityHomeBinding;
import com.lamaviedelivery.fragment.AccountFragment;
import com.lamaviedelivery.fragment.HomeFragment;
import com.lamaviedelivery.fragment.WalletFragment;

public class HomeAct extends AppCompatActivity {
    ActivityHomeBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        initViews();
    }


    private void initViews() {

        tab(1);

        binding.layoutHome.setOnClickListener(v -> tab(1));


        binding.layoutWallet.setOnClickListener(v -> tab(2));


        binding.layoutAccount.setOnClickListener(v -> tab(3));


    }


    public void tab(int i){
        if (i == 1) {
            binding.ivHome.setImageDrawable(getResources().getDrawable(R.drawable.active_home));
            binding.ivWallet.setImageDrawable(getResources().getDrawable(R.drawable.inactive_wallet));
            binding.ivAccount.setImageDrawable(getResources().getDrawable(R.drawable.inactive_account));

            binding.tvHome.setTextColor(getColor(R.color.color_next));
            binding.tvWallet.setTextColor(getColor(R.color.black));
            binding.tvAccount.setTextColor(getColor(R.color.black));
            FragTrans(new HomeFragment());
        }  else if (i == 2) {
            binding.ivHome.setImageDrawable(getResources().getDrawable(R.drawable.active_home));
            binding.ivWallet.setImageDrawable(getResources().getDrawable(R.drawable.active_wallet));
            binding.ivAccount.setImageDrawable(getResources().getDrawable(R.drawable.inactive_account));

            binding.tvHome.setTextColor(getColor(R.color.black));
            binding.tvWallet.setTextColor(getColor(R.color.color_next));
            binding.tvAccount.setTextColor(getColor(R.color.black));
            FragTrans(new WalletFragment());
        }



        else if (i == 3) {
            binding.ivHome.setImageDrawable(getResources().getDrawable(R.drawable.active_home));
            binding.ivWallet.setImageDrawable(getResources().getDrawable(R.drawable.inactive_wallet));
            binding.ivAccount.setImageDrawable(getResources().getDrawable(R.drawable.active_account));

            binding.tvHome.setTextColor(getColor(R.color.black));
            binding.tvWallet.setTextColor(getColor(R.color.black));
            binding.tvAccount.setTextColor(getColor(R.color.color_next));

            FragTrans(new AccountFragment());
        }
    }

    public void FragTrans(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack("fragment");
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        ExitAlert();
    }

    public void ExitAlert(){
        AlertDialog.Builder  builder1 = new AlertDialog.Builder(HomeAct.this);
        builder1.setMessage(getResources().getString(R.string.are_you_sure_you_want_to_exit_this_app));
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        finish();
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
}
