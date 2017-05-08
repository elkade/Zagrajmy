package com.example.lukas.zagrajmy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lukas.zagrajmy.model.User;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountActivity extends BaseActivity {

    @BindView(R.id.account_set_photo_button)
    Button setPhotoButton;
    @BindView(R.id.account_user_name_text)
    EditText userNameText;
    @BindView(R.id.account_user_photo_view)
    ImageView userPhotoView;
    @BindView(R.id.account_save_button)
    Button saveButton;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        mUser = new User();

        int userId = getCurrentUserIdFromCache();
        if (userId != -1) {
            final String url = getServiceUrl();
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url + "/users/" + userId, (String) null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String json = response.toString();
                            Gson g = new Gson();
                            mUser = g.fromJson(json, User.class);

                            userNameText.setText(mUser.getName());

                            ImageLoader imageLoader = volley.getImageLoader();
                            imageLoader.get(url + "/images/" + mUser.getId(), new ImageLoader.ImageListener() {
                                @Override
                                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                                    Bitmap photo = response.getBitmap();
                                    mUser.setPhoto(photo);
                                    userPhotoView.setImageBitmap(photo);
                                }

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            });
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });

            volley.getRequestQueue().add(jsObjRequest);
        }
    }

    @OnClick(R.id.account_set_photo_button)
    public void onClickSetPhotoButton() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            userPhotoView.setImageBitmap(imageBitmap);
            mUser.setPhoto(imageBitmap);
        }
    }

    @OnClick(R.id.account_save_button)
    public void onClickSaveButton() {
        if(mUser.getId()!=-1){
            mUser.setName(userNameText.getText().toString());
            //setPhoto
            String url = getServiceUrl();
            Gson g = new Gson();
            String jsonString = g.toJson(mUser);

            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.PUT, url + "/users/"+mUser.getId(), jsonString, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            SharedPreferences settings = getSharedPreferences("APP_STATE", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putInt("userId", mUser.getId());
                            editor.apply();
                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });

            volley.getRequestQueue().add(jsObjRequest);
        }
        else {
            //ogarnąć cofanie z tego ekranu
            //check if name set
            mUser.setName(userNameText.getText().toString());
            //setPhoto
            String url = getServiceUrl();
            Gson g = new Gson();
            String jsonString = g.toJson(mUser);

            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, url + "/users", jsonString, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String json = response.toString();
                            Gson g = new Gson();
                            mUser = g.fromJson(json, User.class);
                            SharedPreferences settings = getSharedPreferences("APP_STATE", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putInt("userId", mUser.getId());
                            editor.apply();
                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });

            volley.getRequestQueue().add(jsObjRequest);
        }
    }
}
