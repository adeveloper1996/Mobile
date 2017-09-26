package com.retrofit.mobile.rest;

import com.retrofit.mobile.response.AddAdvFavoriteResponse;
import com.retrofit.mobile.response.AddAdvertResponse;
import com.retrofit.mobile.response.AddSellerProductResponse;
import com.retrofit.mobile.response.AllAdvertResronse;
import com.retrofit.mobile.response.AuthResponse;
import com.retrofit.mobile.response.CategoryResponse;
import com.retrofit.mobile.response.ChangeTimeResponse;
import com.retrofit.mobile.response.CityResponse;
import com.retrofit.mobile.response.ClouseActionResponse;
import com.retrofit.mobile.response.DeleteAdvFavoriteResponse;
import com.retrofit.mobile.response.DeleteArchiveResponse;
import com.retrofit.mobile.response.EditResponse;
import com.retrofit.mobile.response.LoggedInResponse;
import com.retrofit.mobile.response.LoggedResponse;
import com.retrofit.mobile.response.MakeOrderResponse;
import com.retrofit.mobile.response.MarkResponse;
import com.retrofit.mobile.response.ModelResponse;
import com.retrofit.mobile.response.MyOrderResponse;
import com.retrofit.mobile.response.MyTovarResponse;
import com.retrofit.mobile.response.RegResponse;
import com.retrofit.mobile.response.RegionResponse;
import com.retrofit.mobile.response.RequestBuyerResponse;
import com.retrofit.mobile.response.SendPriceResponse;
import com.retrofit.mobile.response.SendsmsResponse;
import com.retrofit.mobile.response.ShowMyAdvertResponse;
import com.retrofit.mobile.response.SubCategoryResponse;
import com.retrofit.mobile.response.UserAnketaResponse;
import com.retrofit.mobile.response.ViewCountResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Nursultan on 03.07.2017.
 */

public interface ApiInterface {

    @GET("?act=register")
    Call<RegResponse> register(@Query("phone") String phone, @Query("password") String password, @Query("name") String name, @Query("code") String code);

    @GET("?act=auth&device=android")
    Call<AuthResponse> auth(@Query("login") String login, @Query("password") String password, @Query("token") String token);

    @GET("?act=edit_profile")
    Call<EditResponse> edit(@Query("dop_phone") String dopPhone, @Query("email") String email, @Query("name") String name, @Query("city") String city, @Query("address") String address, @Query("password") String password);

    @GET("?act=logged_in")
    Call<LoggedInResponse> loggedIn();

    @GET("?act=getcategories&parent=0&limit=100&page=0")
    Call<CategoryResponse> getCategory();

    @GET("?act=logout")
    Call<LoggedResponse> logout();

    @GET("?act=sendsms")
    Call<SendsmsResponse> sendsms(@Query("phone") String phone, @Query("token") String token);

    @GET("?act=getoblast")
    Call<RegionResponse> getRegion();

    @GET("?act=getcategories&limit=100&page=0")
    Call<SubCategoryResponse> getSubCategory(@Query("parent") String parent);

    @GET("?act=getmarks&page=0")
    Call<MarkResponse> getMark();

    @GET("?act=getmodels&limit=5&page=0&android=1")
    Call<ModelResponse> getModel(@Query("markid") String markid);

    @GET("?act=getcities&limit=100&page=0")
    Call<CityResponse> getCity(@Query("obl_id") String oblId);

    @POST("?act=add_announcement")
    Call<MakeOrderResponse> makeOrder(@Query("catid") String catid, @Query("modelid") String modelId, @Query("markid") String markid,
                                      @Query("cityid") String cityid, @Query("isnew") String isnew, @Query("endtime") String endtime);

    @GET("?act=get_obs&limit=10")
    Call<AllAdvertResronse> getAllAdvert(@QueryMap Map<String,String> filter, @Query("page") int page);

    @GET("?act=update_view_count_of_ob")
    Call<ViewCountResponse> getViewCount(@Query("id") String id);

    @GET("?act=update_tview_count_of_ob")
    Call<ViewCountResponse> getViewCountNumber(@Query("id") String id);

    @POST("?act=del_ob_from_favourite")
    Call<DeleteAdvFavoriteResponse> deleteFavoriteAdvert(@Query("ob_id") String id);

    @POST("?act=add_ob_to_favourite")
    Call<AddAdvFavoriteResponse> addFavoriteAdvert(@Query("ob_id") String id);

    @Multipart
    @POST("?act=add_ob")
    Call<AddAdvertResponse> addAdvert(@PartMap() Map<String, RequestBody> mapFileAndName );

    @GET("?act=my_obs&status=1&page=0&limit=15")
    Call<ShowMyAdvertResponse> showMyAdverts();

    @GET("?act=my_obs&status=0&page=0&limit=15")
    Call<ShowMyAdvertResponse> showMyAdvertsArchive();

    @GET("?act=ob_and_archive&turn_on=0")
    Call<ViewCountResponse> sendArchiveMyAdvert(@Query("ob_id") String id);

    @GET("?act=ob_and_archive&turn_on=1")
    Call<ViewCountResponse> doActiveMyAdvert(@Query("ob_id") String id);

    @GET("?act=ob_and_archive&delete=1")
    Call<ViewCountResponse> deleteMyAdvert(@Query("ids") String id);

    @GET("?act=my_favourite_obs&limit=10")
    Call<AllAdvertResronse> myFavoriteAdvert(@Query("page") int page);

    @GET("?act=my_orders&limit=10&page=0")
    Call<MyOrderResponse> getMyOrder();

    @GET("?act=change_status&id=1&status=1")
    Call<ClouseActionResponse> closeAuction(@Query("id") String id, @Query("status") String status);

    @GET("?act=change_time")
    Call<ChangeTimeResponse> changeTime(@Query("id") String id, @Query("endtime") String time);

    @GET("?act=getprofile&id=1")
    Call<UserAnketaResponse> showAnketa(@Query("id") String id);

    @GET("?act=add_tovar")
    Call<AddSellerProductResponse> addProduct(@QueryMap Map<String,String> product);

    @GET("?act=my_tovary&limit=10&page=0")
    Call<MyTovarResponse> getMyTovar();

    @GET("?act=my_tovar_del&delete=1")
    Call<DeleteArchiveResponse> deleteProduct(@Query("tvm_ids") String tvmid);

    @GET("?act=my_tovar_del&delete=1")
    Call<DeleteArchiveResponse> deleteMyTovar(@Query("ids") String ids);

    @GET("?act=getnotices&limit=10&page=0")
    Call<RequestBuyerResponse> getBuyerRequest();

    @GET("?act=send_price&id=1&price=1000")
    Call<SendPriceResponse> sendPrice(@Query("id") String id, @Query("price") String price);

}
