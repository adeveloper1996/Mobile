package com.retrofit.mobile.model;



public class MyOrderId {
    private static String orderId;
    private static String userType = "0";
    private static int posOrder;

    public static String getOrderId() {
        return orderId;
    }

    public static void setOrderId(String orderId) {
        MyOrderId.orderId = orderId;
    }

    public static String getUserType() {
        return userType;
    }

    public static void setUserType(String userType) {
        MyOrderId.userType = userType;
    }

    public static int getPosOrder() {
        return posOrder;
    }

    public static void setPosOrder(int posOrder) {
        MyOrderId.posOrder = posOrder;
    }
}

