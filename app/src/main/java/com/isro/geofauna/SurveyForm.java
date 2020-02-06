package com.isro.geofauna;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.isro.geofauna.data.DatabaseColumns;
import com.isro.geofauna.utils.PreferenceUtils;
import com.isro.stupidlocation.StupidLocation;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class SurveyForm extends AppCompatActivity {

    private AppCompatImageView imageViewAnimal;
    private AppCompatImageView imageViewHabitat;
    private AppCompatImageView imageViewHost;

    private AppCompatImageView animal_cancel_btn;
    private AppCompatImageView habitat_cancel_btn;
    private AppCompatImageView host_cancel_btn;

    private int flag = -1;
    private boolean error = false;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 1;

    private Uri photoUri;

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

        fab.setOnClickListener(v -> {
            Intent replyIntent = saveDataOnRoom(new Intent());
            if (!error) {
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });

        /* Set Collector from Prefs */
        String collector = PreferenceUtils.getCollector(this.getApplicationContext());
        if (!collector.isEmpty()) {
            final TextInputEditText collector_tv = (TextInputEditText) findViewById(R.id.collector);
            collector_tv.setText(collector);
        }

        /* Link Camera */
        imageViewAnimal = (AppCompatImageView) findViewById(R.id.animal_holder);
        imageViewHabitat = (AppCompatImageView) findViewById(R.id.habitat_holder);
        imageViewHost = (AppCompatImageView) findViewById(R.id.host_holder);

        /* Image Cancel */
        animal_cancel_btn = (AppCompatImageView) findViewById(R.id.animal_cancel);
        habitat_cancel_btn = (AppCompatImageView) findViewById(R.id.habitat_cancel);
        host_cancel_btn = (AppCompatImageView) findViewById(R.id.host_cancel);

        animal_cancel_btn.setOnClickListener(v -> {
            imageViewAnimal.setImageDrawable(getResources().getDrawable(R.drawable.placeholder_image));
            animal_cancel_btn.setVisibility(View.INVISIBLE);
        });
        habitat_cancel_btn.setOnClickListener(v -> {
            imageViewHabitat.setImageDrawable(getResources().getDrawable(R.drawable.placeholder_image));
            habitat_cancel_btn.setVisibility(View.INVISIBLE);
        });
        host_cancel_btn.setOnClickListener(v -> {
            imageViewHost.setImageDrawable(getResources().getDrawable(R.drawable.placeholder_image));
            host_cancel_btn.setVisibility(View.INVISIBLE);
        });

        imageViewAnimal.setOnClickListener(v -> {
            //Call Camera --> Set ImageView as Animal
            flag = 0;
            if (checkPermisssions()) {
                capturePhoto();
            }
        });

        imageViewHabitat.setOnClickListener(v -> {
            //Call Camera --> Set ImageView as habitat
            flag = 1;
            if (checkPermisssions()) {
                capturePhoto();
            }
        });

        imageViewHost.setOnClickListener(v -> {
            //Call Camera --> Set ImageView as Host
            flag = 2;
            if (checkPermisssions()) {
                capturePhoto();
            }
        });


        /* Geo-Tag Date Time */
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
            public void getLocation(@NotNull Location location) {
                lat_holder.setText(String.format(Locale.ENGLISH, "   %f   ", location.getLatitude()));
                lag_holder.setText(String.format(Locale.ENGLISH, "   %f   ", location.getLongitude()));
                accuracy_holder.setText(String.format(Locale.ENGLISH, "Accurate up to %.2f meters", location.getAccuracy()));
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

    private Intent saveDataOnRoom(Intent intent) {
        error = false;  // Reset flag

        /* Geo-Tag */
        final TextView date_tv = (TextView) findViewById(R.id.date_holder_tv);
        final TextView time_tv = (TextView) findViewById(R.id.time_holder_tv);
        final TextView latitude_tv = (TextView) findViewById(R.id.latitude_holder_tv);
        final TextView longitude_tv = (TextView) findViewById(R.id.longitude_holder_tv);
        final TextView accuracy_tv = (TextView) findViewById(R.id.accuracy_tv);

        /* Mandatory fields*/
        final TextInputEditText uniqueId = (TextInputEditText) findViewById(R.id.unique_id);
        final TextInputLayout uniqueIdLayout = (TextInputLayout) findViewById(R.id.unique_id_layout);

        final TextInputEditText serialNo = (TextInputEditText) findViewById(R.id.serial_no);
        final TextInputLayout serialNoLayout = (TextInputLayout) findViewById(R.id.serial_no_layout);

        final TextInputEditText locality = (TextInputEditText) findViewById(R.id.locality_tv);
        final TextInputLayout localityLayout = (TextInputLayout) findViewById(R.id.locality_tv_layout);

        final AppCompatSpinner stateSpinner = (AppCompatSpinner) findViewById(R.id.state_spinner);
        final TextInputEditText collector = (TextInputEditText) findViewById(R.id.collector);
        final TextInputLayout collectorLayout = (TextInputLayout) findViewById(R.id.collector_layout);

        final AppCompatSpinner habitatSpinner = (AppCompatSpinner) findViewById(R.id.habitat_spinner);
        final AppCompatSpinner entomofoSpinner = (AppCompatSpinner) findViewById(R.id.entomofauna_spinner);
        final AppCompatSpinner otherVerSpinner = (AppCompatSpinner) findViewById(R.id.otherInvertebrate_spinner);
        final AppCompatSpinner vertebrateSpinner = (AppCompatSpinner) findViewById(R.id.vertebrate_spinner);

        /* Optional */
        final AppCompatSpinner examplesSpinner = (AppCompatSpinner) findViewById(R.id.examples_spinner);
        final TextInputEditText temperature = (TextInputEditText) findViewById(R.id.temperature);
        final TextInputEditText humidity = (TextInputEditText) findViewById(R.id.humidity);

        intent.putExtra(DatabaseColumns.date, date_tv.getText().toString());
        intent.putExtra(DatabaseColumns.time, time_tv.getText().toString());
        intent.putExtra(DatabaseColumns.latitude, latitude_tv.getText().toString());
        intent.putExtra(DatabaseColumns.longitude, longitude_tv.getText().toString());
        intent.putExtra(DatabaseColumns.accuracy, accuracy_tv.getText().toString().substring(getResources().getInteger(R.integer.accuracy_string_trim_start)));

        uniqueIdLayout.setError(null);
        serialNoLayout.setError(null);
        localityLayout.setError(null);
        collectorLayout.setError(null);

        if (TextUtils.isEmpty(uniqueId.getText())) {
            uniqueIdLayout.setError(getResources().getString(R.string.error_empty));
            error = true;
        } else {
            intent.putExtra(DatabaseColumns.uniqueSurveyId, Objects.requireNonNull(uniqueId.getText()).toString());
        }

        if (TextUtils.isEmpty(serialNo.getText())) {
            serialNoLayout.setError(getResources().getString(R.string.error_empty));
            error = true;
        } else {
            intent.putExtra(DatabaseColumns.serialNo, Objects.requireNonNull(serialNo.getText()).toString());
        }

        if (TextUtils.isEmpty(locality.getText())) {
            localityLayout.setError(getResources().getString(R.string.error_empty));
            error = true;
        } else {
            intent.putExtra(DatabaseColumns.locality, Objects.requireNonNull(locality.getText()).toString());
        }

        if (TextUtils.isEmpty(collector.getText())) {
            collectorLayout.setError(getResources().getString(R.string.error_empty));
            error = true;
        } else {
            intent.putExtra(DatabaseColumns.collector, Objects.requireNonNull(collector.getText()).toString());
        }

        intent.putExtra(DatabaseColumns.state, Objects.requireNonNull(stateSpinner.getSelectedItem().toString()));
        intent.putExtra(DatabaseColumns.habitat, Objects.requireNonNull(habitatSpinner.getSelectedItem().toString()));
        intent.putExtra(DatabaseColumns.entomofauna, Objects.requireNonNull(entomofoSpinner.getSelectedItem().toString()));
        intent.putExtra(DatabaseColumns.otherInvertebrate, Objects.requireNonNull(otherVerSpinner.getSelectedItem().toString()));
        intent.putExtra(DatabaseColumns.vertebrate, Objects.requireNonNull(vertebrateSpinner.getSelectedItem().toString()));

        intent.putExtra(DatabaseColumns.noOfExamples, Objects.requireNonNull(examplesSpinner.getSelectedItem().toString()));
        intent.putExtra(DatabaseColumns.temperature, Objects.requireNonNull(temperature.getText()).toString());
        intent.putExtra(DatabaseColumns.humidity, Objects.requireNonNull(humidity.getText()).toString());

        return intent;
    }

    private AppCompatImageView getImageView() {
        switch (flag) {
            case 0:
                return imageViewAnimal;
            case 1:
                return imageViewHabitat;
            case 2:
                return imageViewHost;
            default:
                return imageViewAnimal;
        }
    }

    private AppCompatImageView getCancelBtn() {
        switch (flag) {
            case 0:
                return animal_cancel_btn;
            case 1:
                return habitat_cancel_btn;
            case 2:
                return host_cancel_btn;
            default:
                return animal_cancel_btn;
        }
    }

    String currentPhotoPath;

    private boolean checkPermisssions() {
        if ((ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)) {

            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_STORAGE);
        } else {
            // Permission has already been granted
            return true;
        }

        return false;
    }

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


    private void capturePhoto() {
//        INTENT_ACTION_STILL_IMAGE_CAMERA
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        photoUri = Uri.EMPTY;
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i("IOExceptionPhoto", Objects.requireNonNull(ex.getMessage()));
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.isro.geofauna.fileprovider",
                        photoFile);
                photoUri = photoURI;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File storageDir = new File(Environment.getExternalStorageDirectory(), "/geofauna/");

            if (photoUri.getPath() != null) {
                AppCompatImageView imageView = getImageView();
                imageView.setImageBitmap(BitmapFactory.decodeFile(currentPhotoPath));
                Log.i("Path", currentPhotoPath);
                AppCompatImageView imageBtn = getCancelBtn();
                imageBtn.setVisibility(View.VISIBLE);
            }
            // Do other work with full size photo saved in locationForPhotos
        }
    }


}
