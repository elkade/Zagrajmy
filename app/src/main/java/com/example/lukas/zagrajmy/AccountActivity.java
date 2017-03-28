package com.example.lukas.zagrajmy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lukas.zagrajmy.model.User;
import com.example.lukas.zagrajmy.services.AppService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountActivity extends AppCompatActivity {

    @BindView(R.id.account_set_photo_button)
    Button setPhotoButton;
    @BindView(R.id.account_user_name_text)
    EditText userNameText;
//    @BindView(R.id.account_user_year_text)
//    EditText userYearText;
    @BindView(R.id.account_user_photo_view)
    ImageView userPhotoView;

    private Target target;
    private AppService appService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        appService = AppService.getService();
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                userPhotoView.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
        fillWidgets();
    }

    private void fillWidgets() {

        User user = appService.getCurrentUser();
        if(user==null)
            return;
        userNameText.setText(user.getName());

        Bitmap photo = user.getPhoto();
        if(photo==null)
        {
            String url = user.getPhotoUrl();
            Picasso.with(this).load(url).into(target);
        }
        else
            userPhotoView.setImageBitmap(photo);
    }

    @Override
    public void onDestroy() {
        Picasso.with(this).cancelRequest(target);
        super.onDestroy();
    }

    @OnClick(R.id.account_set_photo_button)
    public void onClickSetPhotoButton(){
        //Toast.makeText(this, "Set Photo Clicked",Toast.LENGTH_LONG).show();
        dispatchTakePictureIntent();
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
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
            appService.setCurrentUserPhoto(imageBitmap);
        }
    }
}
