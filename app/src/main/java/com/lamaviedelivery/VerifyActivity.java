package com.lamaviedelivery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.lamaviedelivery.databinding.ActivityVerifyBinding;
import com.lamaviedelivery.model.LoginModel;
import com.lamaviedelivery.retrofit.ApiClient;
import com.lamaviedelivery.retrofit.Constant;
import com.lamaviedelivery.retrofit.LamavieDeliveryInterface;
import com.lamaviedelivery.utils.DataManager;
import com.lamaviedelivery.utils.NetworkAvailablity;
import com.lamaviedelivery.utils.SessionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VerifyActivity extends AppCompatActivity {
    public String TAG = "VerifyActivity";
    ActivityVerifyBinding binding;
    String refreshedToken = "", userId = "", number = "", code = "",id,mobile ="";
    LamavieDeliveryInterface apiInterface;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(LamavieDeliveryInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify);
        initViews();
    }

    private void initViews() {
        mAuth = FirebaseAuth.getInstance();

        if (getIntent() != null) {
            refreshedToken = getIntent().getStringExtra("refreshedToken");
            userId = getIntent().getStringExtra("user_id");
            code = getIntent().getStringExtra("code");
            number = getIntent().getStringExtra("number");

            mobile = "+"+code + number;


            binding.tvText.setText(getString(R.string.otp_verify_one) + " " + code + "" +
                    number + " " + getString(R.string.otp_verify_one));
        }
      /*  binding.btnSubmit.setOnClickListener(v -> {
            if(NetworkAvailablity.checkNetworkStatus(VerifyActivity.this)) chkOtp();
            else Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

        });*/

        binding.btnBack.setOnClickListener(v -> finish());


        sendVerificationCode();

        binding.btnSubmit.setOnClickListener(v -> {

            if(binding.Otp.getOTP().equals("")){
                Toast.makeText(VerifyActivity.this,getString(R.string.please_enter_otp),Toast.LENGTH_LONG).show();
            }
            else {
                DataManager.getInstance().showProgressMessage(VerifyActivity.this,getString(R.string.please_wait));
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, binding.Otp.getOTP());
                signInWithPhoneAuthCredential(credential);
            }


        });


        binding.btnResend.setOnClickListener(v -> {sendVerificationCode();});


    }


    public void chkOtp() {
        DataManager.getInstance().showProgressMessage(VerifyActivity.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("otp", "123456");
        map.put("register_id", refreshedToken);

        Log.e(TAG, "VerifyOtp Request " + map);
        Call<LoginModel> loginCall = apiInterface.chkOtpValid(map);
        loginCall.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    LoginModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Login Response :" + responseString);
                    if (data.status.equals("1")) {
                        Toast.makeText(VerifyActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        SessionManager.writeString(VerifyActivity.this, Constant.USER_INFO, responseString);
                        if(DataManager.getInstance().getUserData(VerifyActivity.this).result.langunge.equals("ar"))  { DataManager.updateResources(VerifyActivity.this,"ar"); }
                        else { DataManager.updateResources(VerifyActivity.this,"en"); }
                        startActivity(new Intent(VerifyActivity.this, HomeAct.class).putExtra("home","1").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();

                    } else if (data.status.equals("0")) Toast.makeText(VerifyActivity.this, data.message, Toast.LENGTH_SHORT).show();
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

    private void sendVerificationCode() {

        //   binding.tvDes.setText("We have sent you an SMS on " + mobile + " with 6 digit verfication code");

/*
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.tvResend.setText( String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                binding.tvResend.setEnabled(false);
            }
            @Override
            public void onFinish() {
                binding.tvResend.setText(getString(R.string.resend));
                binding.tvResend.setEnabled(true);
                //  binding.llTimer.setVisibility(View.GONE);
                // binding.tvReSend.setVisibility(View.VISIBLE);
            }
        }.start();
*/

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile.replace(" ", ""), 60,  TimeUnit.SECONDS,  this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    // Phone number to verify
                    // Timeout duration
                    // Unit of timeout
                    // Activity (for callback binding)

                    @Override
                    public void onCodeSent(@NonNull String id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        VerifyActivity.this.id = id;
                        DataManager.getInstance().hideProgressMessage();
                        Toast.makeText(VerifyActivity.this, getString(R.string.otp_send), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        //   ProjectUtil.pauseProgressDialog()
                        DataManager.getInstance().hideProgressMessage();
                        Toast.makeText(VerifyActivity.this, ""+phoneAuthCredential.getSmsCode(), Toast.LENGTH_SHORT).show();
                        signInWithPhoneAuthCredential(phoneAuthCredential);

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        // ProjectUtil.pauseProgressDialog();
                        DataManager.getInstance().hideProgressMessage();
                        Toast.makeText(VerifyActivity.this, "Failed"+e, Toast.LENGTH_SHORT).show();
                    }
                });        // OnVerificationStateChangedCallbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        DataManager.getInstance().hideProgressMessage();
                        if (task.isSuccessful()) {
                            // ProjectUtil.pauseProgressDialog();
                            // Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();

                            //  Toast.makeText(VerifyAct.this, "Successs", Toast.LENGTH_SHORT).show();


                            //  startActivity(new Intent(VerifyAct.this, LoginAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            //   finish();
                            if(NetworkAvailablity.checkNetworkStatus(VerifyActivity.this)) {
                                if(NetworkAvailablity.checkNetworkStatus(VerifyActivity.this)) chkOtp();
                                else Toast.makeText(VerifyActivity.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();                            }

                        } else {
                            // ProjectUtil.pauseProgressDialog();
                            // DataManager.getInstance().hideProgressMessage();
                            Toast.makeText(VerifyActivity.this, getString(R.string.invalid_otp), Toast.LENGTH_SHORT).show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                            }

                        }
                    }
                });

    }



}