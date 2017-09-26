package com.retrofit.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.retrofit.mobile.R;
import com.retrofit.mobile.model.DataEdit;
import com.retrofit.mobile.model.InfoClient;
import com.retrofit.mobile.response.EditResponse;
import com.retrofit.mobile.rest.ApiClient;
import com.retrofit.mobile.rest.ApiInterface;
import com.retrofit.mobile.utils.SessionManager;
import com.retrofit.mobile.utils.dialog.LoadingDialog;
import com.retrofit.mobile.utils.dialog.LoadingView;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class EditPersonalActivity extends AppCompatActivity {

     private EditText eTxtName;
     private EditText eTxtEmail;
     private EditText eTxtPassword;
     private EditText eTxtDopPhone;
     private EditText eTxtCity;
     private EditText eTxtAddress;
     private Button btnSave;
     private Button btnCancel;
     private SessionManager sessionManager;
     private LoadingView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        initWidget();

        loadingView = LoadingDialog.view(getFragmentManager());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<InfoClient> clients = realm.where(InfoClient.class).findAll();
        eTxtName.setText(clients.get(0).getName());
        eTxtDopPhone.setText(clients.get(0).getDopPhone());
        eTxtEmail.setText(clients.get(0).getEmail());
        eTxtAddress.setText(clients.get(0).getAddress());
        eTxtCity.setText(clients.get(0).getCity());

        initButtonClick();
    }

       private void initButtonClick() {
           btnSave.setOnClickListener(v -> {
               loadingView.showLoading();
               String name = eTxtName.getText().toString();
               String dopPhone = eTxtDopPhone.getText().toString();
               String email = eTxtEmail.getText().toString();
               String address = eTxtAddress.getText().toString();
               String city = eTxtCity.getText().toString();
               String pass = eTxtPassword.getText().toString();

               ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
               Call<EditResponse> call = apiInterface.edit(dopPhone, email, name, city, address, pass);
               call.enqueue(new Callback<EditResponse>() {
                   @Override
                   public void onResponse(Call<EditResponse> call, Response<EditResponse> response) {
                       EditResponse editResponse = response.body();
                       DataEdit dataEdit = editResponse.getDataEdits().get(0);
                       boolean success = dataEdit.isSuccess();
                       if(success) {
                           Realm realm = Realm.getDefaultInstance();
                           RealmResults<InfoClient> results = realm.where(InfoClient.class).findAll();
                           realm.beginTransaction();
                           results.get(0).setDopPhone(dopPhone);
                           results.get(0).setEmail(email);
                           results.get(0).setName(name);
                           results.get(0).setCity(city);
                           results.get(0).setAddress(address);
                           realm.commitTransaction();

                           Intent intent = new Intent(EditPersonalActivity.this, MarketMobileActivity.class);
                           startActivity(intent);
                           loadingView.hideLoading();

                       }  else {
                           String error = dataEdit.getErrors().get(0);
                           Toast.makeText(EditPersonalActivity.this,"llll"+error,Toast.LENGTH_LONG).show();
                           loadingView.hideLoading();
                       }
                   }

                   @Override
                   public void onFailure(Call<EditResponse> call, Throwable t) {
                       Toast.makeText(EditPersonalActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                       loadingView.hideLoading();
                   }
               });
           });

           btnCancel.setOnClickListener(v -> {
               startActivity(new Intent(this,MarketMobileActivity.class));
           });
       }

       private void initWidget() {
           eTxtName = (EditText)findViewById(R.id.edit_personal_name);
           eTxtDopPhone = (EditText)findViewById(R.id.edit_personal_dop_phone);
           decore(eTxtDopPhone);
           eTxtEmail = (EditText)findViewById(R.id.edit_personal_email);
           eTxtCity = (EditText)findViewById(R.id.edit_personal_city);
           eTxtAddress = (EditText)findViewById(R.id.edit_personal_address);
           eTxtPassword = (EditText)findViewById(R.id.edit_personal_password);
           eTxtPassword.setVisibility(View.GONE);
           btnSave = (Button) findViewById(R.id.btn_edit_personal_save);
           btnCancel = (Button) findViewById(R.id.btn_edit_personal_cancel);
       }

    private void decore (EditText editText){
        Slot[] slots = new UnderscoreDigitSlotsParser().parseSlots("(___)___-____");
        FormatWatcher formatWatcher = new MaskFormatWatcher(MaskImpl.createTerminated(slots));
        formatWatcher.installOn(editText);
    }
}
