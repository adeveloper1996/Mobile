package com.retrofit.mobile.model;

/**
 * Created by Nursultan on 06.07.2017.
 */

public class MakeOrder {
    private static String statusStade;
    private static String markId;
    private static String newOld;
    private static String nameDevice;
    private static String modelId;
    private static String catId;
    private static String cityId;
    private static int category;
    private static int subcategory;
    private static int mark;
    private static int model;
    private static int sellerOrBuyer;

    public MakeOrder(String marId) {
        this.markId = marId;
    }

    public MakeOrder() {

    }

    public static String getMarkId() {
        return markId;
    }

    public static void setMarkId(String markId) {
        MakeOrder.markId = markId;
    }

    public static String getNewOld() {
        return newOld;
    }

    public static void setNewOld(String newOld) {
        MakeOrder.newOld = newOld;
    }

    public static String getNameDevice() {
        return nameDevice;
    }

    public static void setNameDevice(String nameDevice) {
        MakeOrder.nameDevice = nameDevice;
    }

    public static String getModelId() {
        return modelId;
    }

    public static void setModelId(String modelId) {
        MakeOrder.modelId = modelId;
    }

    public static String getCatId() {
        return catId;
    }

    public static void setCatId(String catId) {
        MakeOrder.catId = catId;
    }

    public static String getCityId() {
        return cityId;
    }

    public static void setCityId(String cityId) {
        MakeOrder.cityId = cityId;
    }

    public static int getCategory() {
        return category;
    }

    public static void setCategory(int category) {
        MakeOrder.category = category;
    }

    public static int getMark() {
        return mark;
    }

    public static void setMark(int mark) {
        MakeOrder.mark = mark;
    }

    public static int getModel() {
        return model;
    }

    public static void setModel(int model) {
        MakeOrder.model = model;
    }

    public static int getSubcategory() {
        return subcategory;
    }

    public static void setSubcategory(int subcategory) {
        MakeOrder.subcategory = subcategory;
    }

    public static int getSellerOrBuyer() {
        return sellerOrBuyer;
    }

    public static void setSellerOrBuyer(int sellerOrBuyer) {
        MakeOrder.sellerOrBuyer = sellerOrBuyer;
    }

    public static String getStatusStade() {
        return statusStade;
    }

    public static void setStatusStade(String statusStade) {
        MakeOrder.statusStade = statusStade;
    }
}
