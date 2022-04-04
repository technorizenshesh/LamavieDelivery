package com.lamaviedelivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderStatus {

    @SerializedName("result")
    @Expose
    public Result result;
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
        @SerializedName("status_history")
        @Expose
        public List<StatusHistory> statusHistory = null;

        public class StatusHistory {

            @SerializedName("id")
            @Expose
            public String id;
            @SerializedName("order_id")
            @Expose
            public String orderId;
            @SerializedName("status")
            @Expose
            public String status;
            @SerializedName("description")
            @Expose
            public String description;
            @SerializedName("status_date")
            @Expose
            public String statusDate;
            @SerializedName("status_time")
            @Expose
            public String statusTime;


        }


    }

}




