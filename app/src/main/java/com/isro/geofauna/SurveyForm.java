package com.isro.geofauna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.isro.stupidlocation.StupidLocation;

import org.angmarch.views.NiceSpinner;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class SurveyForm extends AppCompatActivity {

    private AppCompatImageView imageViewAnimal;
    private AppCompatImageView imageViewHabitat;
    private AppCompatImageView imageViewHost;

    private int flag = -1;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void capturePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Drawable bg = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_white_24dp, null);
        assert bg != null;
        bg = DrawableCompat.wrap(bg);
        DrawableCompat.setTint(bg, ContextCompat.getColor(this, R.color.colorWhite));

        toolbar.setNavigationIcon(bg);
        toolbar.setTitle(getResources().getString(R.string.Survey_Form));
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Save Fab
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(view -> Snackbar.make((CoordinatorLayout) findViewById(R.id.survey_layout), "Saved Successfully!", Snackbar.LENGTH_SHORT).show());

        /* Image Cards */
        CardView animalCard = (CardView) findViewById(R.id.image_animal);
        CardView habitatCard = (CardView) findViewById(R.id.image_habitat);
        CardView hostCard = (CardView) findViewById(R.id.image_host);

        /* Set Title */
        setCardViewTitle(animalCard, getResources().getString(R.string.ImageAnimal));
        setCardViewTitle(habitatCard, getResources().getString(R.string.ImageHabitat));
        setCardViewTitle(hostCard, getResources().getString(R.string.ImageHost));

        /* Link Camera */
        imageViewAnimal = (AppCompatImageView) animalCard.findViewById(R.id.image_holder);
        imageViewHabitat = (AppCompatImageView) habitatCard.findViewById(R.id.image_holder);
        imageViewHost = (AppCompatImageView) hostCard.findViewById(R.id.image_holder);

        Button animalImageBtn = (Button) animalCard.findViewById(R.id.take_photo_btn);
        Button habitatImageBtn = (Button) habitatCard.findViewById(R.id.take_photo_btn);
        Button hostImageBtn = (Button) hostCard.findViewById(R.id.take_photo_btn);

        animalImageBtn.setOnClickListener(v -> {
            //Call Camera --> Set ImageView as Animal
            flag = 0;
            capturePhoto();
        });

        habitatImageBtn.setOnClickListener(v -> {
            //Call Camera --> Set ImageView as habitat
            flag = 1;
            capturePhoto();
        });

        hostImageBtn.setOnClickListener(v -> {
            //Call Camera --> Set ImageView as Host
            flag = 2;
            capturePhoto();
        });

        TextView date_holder = (TextView) findViewById(R.id.date_holder_tv);
        TextView time_holder = (TextView) findViewById(R.id.time_holder_tv);
        final TextView lat_holder = (TextView) findViewById(R.id.latitude_holder_tv);
        final TextView lag_holder = (TextView) findViewById(R.id.longitude_holder_tv);

        final TextView accuracy_holder = (TextView) findViewById(R.id.accuracy_tv);

        Date today = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/YYYY", Locale.ENGLISH);
        SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        String date = dateformat.format(today);
        String time = timeformat.format(today);

        date_holder.setText(date);
        time_holder.setText(time);

        /* Get Geo-Tagging from Location Provider */

        new StupidLocation(this, new StupidLocation.StupidLocationCallBack() {
            @Override
            public void getLocation(Location location) {
                lat_holder.setText(String.format(Locale.ENGLISH,"   %f   ",location.getLatitude()));
                lag_holder.setText(String.format(Locale.ENGLISH,"   %f   ",location.getLongitude()));
                accuracy_holder.setText(String.format(Locale.ENGLISH,"Accurate up to %.2f meters",location.getAccuracy()));
            }

            @Override
            public void locationSettingFailed() {
                Log.i("Location", "setting failed");
            }

            @Override
            public void permissionDenied() {
                Log.i("Location", "permission  denied");
            }
        });

    }

    private AppCompatImageView getImageView(){
        switch (flag){
            case 0: return imageViewAnimal;
            case 1: return imageViewHabitat;
            case 2: return imageViewHost;
            default: return imageViewAnimal;
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


//    private void capturePhoto() {
////        INTENT_ACTION_STILL_IMAGE_CAMERA
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.isro.geofauna.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//            }
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap thumbnail = data.getParcelableExtra("data");

            if (thumbnail != null) {
                AppCompatImageView imageView = getImageView();
                imageView.setImageBitmap(thumbnail);
            }
            // Do other work with full size photo saved in locationForPhotos
        }
    }


    private void setCardViewTitle(CardView view, String title){
        TextView textView= (TextView) view.findViewById(R.id.card_title_tv);
        textView.setText(title);
    }

    /* Array Getters */

    private List<String> getHabitatList(){
        return new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.Habitat_items)));
    }

    private List<String> getNoOfExamplesList(){
        return new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.Examples_items)));
    }

    private List<String> getStateList(){
        return new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.indian_states)));
    }

    private List<String> getBinaryList(){
        return new LinkedList<>(Arrays.asList( "No", "Yes"));
    }


}
