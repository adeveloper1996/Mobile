package com.retrofit.mobile.fragment;


import android.support.v4.app.Fragment;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.activity.main.MainActivity;
import com.retrofit.mobile.model.DataCategory;
import com.retrofit.mobile.model.DataLogged;
import com.retrofit.mobile.model.InfoCategory;
import com.retrofit.mobile.model.InfoClient;
import com.retrofit.mobile.model.MyOrderId;
import com.retrofit.mobile.response.CategoryResponse;
import com.retrofit.mobile.response.LoggedResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import com.retrofit.mobile.utils.CircleTransform;
import com.retrofit.mobile.utils.SessionManager;
import com.retrofit.mobile.utils.dialog.LoadingDialog;
import com.retrofit.mobile.utils.dialog.LoadingView;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalAreaFragment extends Fragment implements View.OnClickListener{
    private TextView txtName;
    private TextView txtAnketaProsmotr;
    private TextView txtPhone;    private TextView txtDopPhone;
    private TextView txtEmail;
    private TextView txtCity;
    private TextView txtAddress;
    private ImageView imgAvatar;
    private Button btnBuyer;
    private Button btnSeller;
    private ImageButton iBtnAvatar;
    private String userType;
    private InfoClient infoClient;
    private SessionManager session;
    private List<Image> images;
    private int numberOfImagesToSelect = 1;
    private LoadingView loadingView;
    private Realm realm;

    public PersonalAreaFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal_area, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        realm = Realm.getDefaultInstance();
        RealmResults<InfoClient> infoClients = realm.where(InfoClient.class).findAll();
        loadingView = LoadingDialog.view(getActivity().getFragmentManager());
        infoClient = infoClients.get(0);
        if(infoClient == null) {
            logout();
        }
        getActivity().setTitle("Личный кабинет");
        initWidget(view);
        userTypeButtons();
        showInfoClient(infoClient);
        makeOrder();
    }

    private void initWidget(View view) {
        txtName = (TextView)view.findViewById(R.id.name_personal_area);
        txtAnketaProsmotr = (TextView)view.findViewById(R.id.anketa_personal_area_id);
        txtPhone = (TextView)view.findViewById(R.id.phone_personal_area);
        txtDopPhone = (TextView)view.findViewById(R.id.addphone_personal_area);
        txtEmail = (TextView)view.findViewById(R.id.email_personal_area);
        txtCity = (TextView)view.findViewById(R.id.city_personal_area);
        txtAddress = (TextView)view.findViewById(R.id.address_personal_area);
        imgAvatar = (ImageView)view.findViewById(R.id.img_anketa_seller_persons);
      //  iBtnAvatar = (ImageButton)view.findViewById(R.id.iBtn_per_area_avatar);
        btnBuyer = (Button)view.findViewById(R.id.btn_my_area_buyer);
        btnSeller = (Button)view.findViewById(R.id.btn_my_area_seller);
       // iBtnAvatar.setOnClickListener(this);
        imgAvatar.setOnClickListener(this);
    }

    private void userTypeButtons() {
        if (infoClient.getUserType().equals("0")) {
            MyOrderId.setUserType("0");
            btnSeller.setTextColor(getResources().getColor(R.color.black));
            btnBuyer.setTextColor(getResources().getColor(R.color.black));
           // btnBuyer.setBackground(getResources().getDrawable(R.drawable.rounded_button_yellow));
        } else {
            MyOrderId.setUserType("1");
            btnBuyer.setTextColor(getResources().getColor(R.color.black));
            btnSeller.setTextColor(getResources().getColor(R.color.black));
           // btnSeller.setBackground(getResources().getDrawable(R.drawable.rounded_button_yellow));
        }

        btnSeller.setOnClickListener(v -> {
            loadingView.showLoading();
            realm.beginTransaction();
            RealmQuery<InfoClient> clients = realm.where(InfoClient.class).equalTo("id",infoClient.getId());
            clients.findFirst().setUserType("1");
            Toast.makeText(getActivity(),"Вы теперь продавец",Toast.LENGTH_SHORT).show();
            btnBuyer.setTextColor(getResources().getColor(R.color.black));
            btnSeller.setTextColor(getResources().getColor(R.color.black));
            btnBuyer.setBackground(getResources().getDrawable(R.drawable.rounded_button_white));
            btnSeller.setBackground(getResources().getDrawable(R.drawable.rounded_button_yellow));
            MyOrderId.setUserType("1");
            loadingView.hideLoading();
            realm.commitTransaction();
        });

        btnBuyer.setOnClickListener(v -> {
            loadingView.showLoading();
            realm.beginTransaction();
            RealmQuery<InfoClient> clients = realm.where(InfoClient.class).equalTo("id",infoClient.getId());
            clients.findFirst().setUserType("0");
            Toast.makeText(getActivity(),"Вы теперь покупатель",Toast.LENGTH_SHORT).show();
            btnSeller.setTextColor(getResources().getColor(R.color.black));
            btnBuyer.setTextColor(getResources().getColor(R.color.black));
            btnBuyer.setBackground(getResources().getDrawable(R.drawable.rounded_button_yellow));
            btnSeller.setBackground(getResources().getDrawable(R.drawable.rounded_button_white));
            MyOrderId.setUserType("0");
            loadingView.hideLoading();
            realm.commitTransaction();
        });
    }

    private void makeOrder() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CategoryResponse> callCategory = apiInterface.getCategory();
        callCategory.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                CategoryResponse categoryResponse = response.body();
                DataCategory dataCategory = categoryResponse.getDataCategories().get(0);
                boolean suc = dataCategory.isSuccess();
                List<InfoCategory> infoCategories = dataCategory.getInfoCategories();
                if (suc){
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(infoCategories);
                    realm.commitTransaction();
                }

                else {
                    Log.i("ssss","errorapi");
                }

            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Log.i("ssss",t.toString());
            }
        });
    }

    private void showInfoClient(@NonNull InfoClient infoClient) {
        txtName.setText(infoClient.getName());
        txtPhone.setText(infoClient.getPhone());
        txtDopPhone.setText(infoClient.getDopPhone());
        txtEmail.setText(infoClient.getEmail());
        txtCity.setText(infoClient.getCity());
        txtAddress.setText(infoClient.getAddress());
        txtAnketaProsmotr.setText("ID:"+infoClient.getId());
        if(infoClient.getAvatar().length() == 0) {
            Picasso.with(getActivity()).load(R.drawable.ic_perm_identity_white_48dp).transform(new CircleTransform()).into(imgAvatar);
        }else {
            //Picasso.with(getActivity()).load(infoClient.getAvatar()).transform(new CircleTransform()).into(imgAvatar);
            Picasso.with(getActivity()).load(R.drawable.ic_perm_identity_white_48dp).transform(new CircleTransform()).into(imgAvatar);
        }

    }

    private void logout() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LoggedResponse> call = apiInterface.logout();
        call.enqueue(new Callback<LoggedResponse>() {
            @Override
            public void onResponse(Call<LoggedResponse> call, Response<LoggedResponse> response) {
                LoggedResponse loggedResponse = response.body();
                DataLogged dataLogged = loggedResponse.getDataLoggeds().get(0);
                boolean success = dataLogged.isSuccess();
                if (success) {
                    session.setLoggedin(false);
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
                else
                    Toast.makeText(getActivity(),"error",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<LoggedResponse> call, Throwable t) {
                Toast.makeText(getActivity(),t.toString(),Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_anketa_seller_persons:
            //    Intent intent = new Intent(getActivity(), AlbumSelectActivity.class);
                //set limit on number of images that can be selected, default is 10
            //    intent.putExtra(Constants.INTENT_EXTRA_LIMIT, numberOfImagesToSelect);
             //   startActivityForResult(intent, Constants.REQUEST_CODE);
                break;
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
//            images = new ArrayList<Image>();
//            images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
//            StringBuffer stringBuffer = new StringBuffer();
//            for (int i = 0; i < images.size(); i++) {
//                stringBuffer.append(images.get(i).path + "\n");
//            }
//            if (images != null) {
//                loadingView.showLoading();
//                Map<String, RequestBody> params = new HashMap<>();
//                File file = new File(images.get(0).path);
//                RequestBody photo = RequestBody.create(MediaType.parse("image/*"), file);
//                params.put("avatar\";filename=\"kll.jpeg\"", photo);
//                ApiInterface apiInterface = ApiClient.getApiInterface();
//                Call<UploadAvatarResponse> call = apiInterface.uploadAvatar(params);
//                call.enqueue(new Callback<UploadAvatarResponse>() {
//                    @Override
//                    public void onResponse(Call<UploadAvatarResponse> call, Response<UploadAvatarResponse> response) {
//                        UploadAvatarResponse avatarResponse = response.body();
//                        DataUploadAvatar avatar = avatarResponse.getUploadAvatars().get(0);
//                        List<InfoUploadAvatar> uploadAvatar = avatar.getAvatarUri();
//                        boolean success = avatar.isSuccess();
//                        if (success) {
//                            String uri = uploadAvatar.get(0).getAvatar();
//                            realm.commitTransaction();
//                            Picasso.with(getActivity()).load(uri).transform(new CircleTransform()).into(imgAvatar);
//                            loadingView.hideLoading();
//                        } else {
//                            String error = avatar.getErrors().get(0);
//                            Toast.makeText(getActivity(), "error" + error, Toast.LENGTH_SHORT).show();
//                            loadingView.hideLoading();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<UploadAvatarResponse> call, Throwable t) {
//                        Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
//                        loadingView.hideLoading();
//                    }
//                });
//            }
//        }

}
