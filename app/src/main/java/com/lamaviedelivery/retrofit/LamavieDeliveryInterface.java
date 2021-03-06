package com.lamaviedelivery.retrofit;



import com.lamaviedelivery.model.BookingModel;
import com.lamaviedelivery.model.HistoryModel;
import com.lamaviedelivery.model.LoginModel;
import com.lamaviedelivery.model.OrderDetailModel;
import com.lamaviedelivery.model.OrderStatus;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface LamavieDeliveryInterface {



    @Multipart
    @POST("signup")
    Call<Map<String,String>> signupUser(
            @Part("first_name") RequestBody first_name,
            @Part("last_name")RequestBody last_name,
            @Part("email") RequestBody email,
            @Part("mobile") RequestBody mobile,
            @Part("country_code") RequestBody country_code,
            @Part("dob") RequestBody dob,
            @Part("gender") RequestBody gender,
            @Part("address") RequestBody address,
            @Part("lat") RequestBody lat,
            @Part("lon") RequestBody password,
            @Part("register_id") RequestBody lon,
            @Part("type") RequestBody type,
            @Part("langunge") RequestBody language,
            @Part MultipartBody.Part file);















    @FormUrlEncoded
    @POST("login")
    Call<Map<String,String>> userLogin (@FieldMap Map<String, String> params);



    @FormUrlEncoded
    @POST("check_otp")
    Call<LoginModel> chkOtpValid (@FieldMap Map<String, String> params);

   /* @FormUrlEncoded
    @POST("otpVerified")
    Call<SignupModel> loginWithOtp (@FieldMap Map<String, String> params);*/


    @FormUrlEncoded
    @POST("logout")
    Call<Map<String, String>> logout(@Header("Authorization") String auth,@FieldMap Map<String, String> params);



    @FormUrlEncoded
    @POST("update_lat_lon")
    Call<Map<String, String>> updateLocation(@FieldMap Map<String, String> params);



    @FormUrlEncoded
    @POST("get_orderdetails_driver_by_status")
    Call<BookingModel> getAllBooking(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("change_password")
    Call<Map<String, String>> changePass(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("driver_accept_and_cancel_order")
    Call<Map<String, String>> requestAcceptCancel(@FieldMap Map<String, String> params);

    @Multipart
    @POST("update_profile")
    Call<LoginModel> profileUpdate(
            @Part("first_name") RequestBody first_name,
            @Part("last_name") RequestBody last_name,
            @Part("email") RequestBody email,
            @Part("mobile") RequestBody mobile,
            @Part("country_code") RequestBody country_code,
            @Part("address") RequestBody address,
            @Part("dob") RequestBody dob,
            @Part("gender") RequestBody gender,
            @Part("user_id") RequestBody user_id,
            @Part MultipartBody.Part file);




    @FormUrlEncoded
    @POST("get_orderdetails_byid")
    Call<OrderDetailModel> getOrderDetailsss(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("get_driver_orderhistory")
    Call<HistoryModel> getOrderHistory(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("order_status_history")
    Call<OrderStatus> getStatus(@FieldMap Map<String, String> params);



    @Multipart
    @POST("update_licence")
    Call<LoginModel> updateDocument(
            @Part("national_id") RequestBody national_id,
            @Part("license_number") RequestBody license_number,
            @Part("expiration_date") RequestBody expiration_date,
            @Part("user_id") RequestBody user_id,
            @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("update_langunge")
    Call<LoginModel> changeLang (@FieldMap Map<String, String> params);

}



