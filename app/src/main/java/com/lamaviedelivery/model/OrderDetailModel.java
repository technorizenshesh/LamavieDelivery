package com.lamaviedelivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class OrderDetailModel implements Serializable {

    @SerializedName("result")
    @Expose
    public Result result;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;

    public class Result implements Serializable{

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
        @SerializedName("tax")
        @Expose
        public String tax;
        @SerializedName("pickup_address")
        @Expose
        public String pickupAddress;
        @SerializedName("pickup_lat")
        @Expose
        public String pickupLat;
        @SerializedName("pickup_lon")
        @Expose
        public String pickupLon;
        @SerializedName("otp")
        @Expose
        public String otp;
        @SerializedName("confirmdate")
        @Expose
        public String confirmdate;
        @SerializedName("confirmtime")
        @Expose
        public String confirmtime;
        @SerializedName("pickupdate")
        @Expose
        public String pickupdate;
        @SerializedName("pickuptime")
        @Expose
        public String pickuptime;
        @SerializedName("branch_id")
        @Expose
        public String branchId;
        @SerializedName("sub_branch_id")
        @Expose
        public String subBranchId;
        @SerializedName("payment_status")
        @Expose
        public String paymentStatus;
        @SerializedName("product_id")
        @Expose
        public String productId;
        @SerializedName("delivery_charge")
        @Expose
        public String deliveryCharge;
        @SerializedName("coupon_code")
        @Expose
        public String couponCode;
        @SerializedName("coupon_amount")
        @Expose
        public String couponAmount;
        @SerializedName("bell_notification")
        @Expose
        public String bellNotification;
        @SerializedName("notification_status")
        @Expose
        public String notificationStatus;
        @SerializedName("user_details")
        @Expose
        public UserDetails userDetails;
        @SerializedName("driver_details")
        @Expose
        public DriverDetails driverDetails;
        @SerializedName("total_amount")
        @Expose
        public Integer totalAmount;
        @SerializedName("item_details")
        @Expose
        public List<ItemDetail> itemDetails = null;

        public class UserDetails implements Serializable{

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
            @SerializedName("wallet_amount")
            @Expose
            public String walletAmount;
            @SerializedName("licence")
            @Expose
            public String licence;
            @SerializedName("expiration_date")
            @Expose
            public String expirationDate;
            @SerializedName("national_id")
            @Expose
            public String nationalId;
            @SerializedName("license_number")
            @Expose
            public String licenseNumber;
            @SerializedName("wallet_point")
            @Expose
            public String walletPoint;
            @SerializedName("branch_id")
            @Expose
            public String branchId;
            @SerializedName("sub_branch_id")
            @Expose
            public String subBranchId;



        }

        public class DriverDetails implements Serializable{

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
            @SerializedName("wallet_amount")
            @Expose
            public String walletAmount;
            @SerializedName("licence")
            @Expose
            public String licence;
            @SerializedName("expiration_date")
            @Expose
            public String expirationDate;
            @SerializedName("national_id")
            @Expose
            public String nationalId;
            @SerializedName("license_number")
            @Expose
            public String licenseNumber;
            @SerializedName("wallet_point")
            @Expose
            public String walletPoint;
            @SerializedName("branch_id")
            @Expose
            public String branchId;
            @SerializedName("sub_branch_id")
            @Expose
            public String subBranchId;



        }


        public class ItemDetail implements Serializable{

            @SerializedName("id")
            @Expose
            public String id;
            @SerializedName("name")
            @Expose
            public String name;
            @SerializedName("service_id")
            @Expose
            public String serviceId;
            @SerializedName("category_id")
            @Expose
            public String categoryId;
            @SerializedName("sub_category_id")
            @Expose
            public String subCategoryId;
            @SerializedName("description")
            @Expose
            public String description;
            @SerializedName("amount")
            @Expose
            public String amount;
            @SerializedName("discount")
            @Expose
            public String discount;
            @SerializedName("address")
            @Expose
            public String address;
            @SerializedName("tax")
            @Expose
            public String tax;
            @SerializedName("image")
            @Expose
            public String image;
            @SerializedName("status")
            @Expose
            public String status;
            @SerializedName("branch_id")
            @Expose
            public String branchId;
            @SerializedName("sub_admin_id")
            @Expose
            public String subAdminId;
            @SerializedName("date_time")
            @Expose
            public String dateTime;
            @SerializedName("lon")
            @Expose
            public String lon;
            @SerializedName("lat")
            @Expose
            public String lat;
            @SerializedName("phone")
            @Expose
            public String phone;
            @SerializedName("type")
            @Expose
            public String type;
            @SerializedName("country")
            @Expose
            public String country;
            @SerializedName("state")
            @Expose
            public String state;
            @SerializedName("city")
            @Expose
            public String city;
            @SerializedName("open_time")
            @Expose
            public String openTime;
            @SerializedName("close_time")
            @Expose
            public String closeTime;
            @SerializedName("product_point")
            @Expose
            public String productPoint;
            @SerializedName("delivery_charge")
            @Expose
            public String deliveryCharge;
            @SerializedName("quantity")
            @Expose
            public String quantity;
            @SerializedName("exp_date")
            @Expose
            public String expDate;
            @SerializedName("extra_options")
            @Expose
            public List<ExtraOption> extraOptions = null;

            public class ExtraOption implements Serializable{

                @SerializedName("id")
                @Expose
                public String id;
                @SerializedName("name")
                @Expose
                public String name;
                @SerializedName("price")
                @Expose
                public String price;
                @SerializedName("date_time")
                @Expose
                public String dateTime;
                @SerializedName("branch_id")
                @Expose
                public String branchId;





            }


        }

    }


}



