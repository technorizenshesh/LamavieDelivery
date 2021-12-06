package com.lamaviedelivery.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingModel {

    @SerializedName("result")
    @Expose
    public List<Result> result = null;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;


    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("invoice")
        @Expose
        public String invoice;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("driver_id")
        @Expose
        public String driverId;
        @SerializedName("price")
        @Expose
        public String price;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("payment_type")
        @Expose
        public String paymentType;
        @SerializedName("date")
        @Expose
        public String date;
        @SerializedName("time")
        @Expose
        public String time;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("cart_id")
        @Expose
        public String cartId;
        @SerializedName("pickup_date")
        @Expose
        public String pickupDate;
        @SerializedName("pickup_time")
        @Expose
        public String pickupTime;
        @SerializedName("delivery_date")
        @Expose
        public String deliveryDate;
        @SerializedName("delivery_time")
        @Expose
        public String deliveryTime;
        @SerializedName("lat")
        @Expose
        public String lat;
        @SerializedName("lon")
        @Expose
        public String lon;
        @SerializedName("address_type")
        @Expose
        public String addressType;
        @SerializedName("user_details")
        @Expose
        public UserDetails userDetails;
        @SerializedName("item_details")
        @Expose
        public List<ItemDetail> itemDetails = null;

        public class UserDetails {

            @SerializedName("id")
            @Expose
            public String id;
            @SerializedName("type")
            @Expose
            public String type;
            @SerializedName("user_name")
            @Expose
            public String userName;
            @SerializedName("first_name")
            @Expose
            public String firstName;
            @SerializedName("last_name")
            @Expose
            public String lastName;
            @SerializedName("mobile")
            @Expose
            public String mobile;
            @SerializedName("email")
            @Expose
            public String email;
            @SerializedName("password")
            @Expose
            public String password;
            @SerializedName("image")
            @Expose
            public String image;
            @SerializedName("otp")
            @Expose
            public String otp;
            @SerializedName("country_code")
            @Expose
            public String countryCode;
            @SerializedName("lat")
            @Expose
            public String lat;
            @SerializedName("lon")
            @Expose
            public String lon;
            @SerializedName("dob")
            @Expose
            public String dob;
            @SerializedName("address")
            @Expose
            public String address;
            @SerializedName("gender")
            @Expose
            public String gender;
            @SerializedName("status")
            @Expose
            public String status;
            @SerializedName("social_id")
            @Expose
            public String socialId;
            @SerializedName("register_id")
            @Expose
            public String registerId;
            @SerializedName("ios_register_id")
            @Expose
            public Object iosRegisterId;
            @SerializedName("date_time")
            @Expose
            public String dateTime;



        }

        public class ItemDetail {

            @SerializedName("id")
            @Expose
            public String id;
            @SerializedName("category_id")
            @Expose
            public String categoryId;
            @SerializedName("sub_category_id")
            @Expose
            public String subCategoryId;
            @SerializedName("child_cate_name")
            @Expose
            public String childCateName;
            @SerializedName("price")
            @Expose
            public String price;
            @SerializedName("status")
            @Expose
            public String status;
            @SerializedName("date_time")
            @Expose
            public String dateTime;



        }

    }

}





