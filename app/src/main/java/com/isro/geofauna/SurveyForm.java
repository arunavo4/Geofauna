package com.isro.geofauna;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.isro.geofauna.data.DatabaseColumns;
import com.isro.geofauna.data.Geofauna;
import com.isro.geofauna.utils.PreferenceUtils;
import com.isro.stupidlocation.StupidLocation;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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

    public String[] imageFiles = new String[3];
    public String[] imageUris = new String[3];

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
        final FloatingActionButton fab = findViewById(R.id.fab);

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

        // Check if Intent Request is to view
        Intent intent = getIntent();
        intent.getIntExtra("ACTIVITY_CODE", -1);
        if (intent.getIntExtra("ACTIVITY_CODE", -1) != -1) {

            Geofauna geofauna = Objects.requireNonNull(intent.getParcelableExtra(DatabaseColumns.parcelGeofauna));

            fab.setOnClickListener(v -> {
                Intent replyIntent = saveDataOnRoom(new Intent(), geofauna);
                if (!error) {
                    setResult(RESULT_OK, replyIntent);
                    finish();
                }
            });

            populateViews(geofauna);
        } else {

            fab.setOnClickListener(v -> {
                Intent replyIntent = saveDataOnRoom(new Intent(), new Geofauna());
                if (!error) {
                    setResult(RESULT_OK, replyIntent);
                    finish();
                }
            });

            final AppCompatSpinner stateSpinner = (AppCompatSpinner) findViewById(R.id.state_spinner);

//            stateSpinner.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    SpinnerAdapter adapter = stateSpinner.getAdapter();
//                }
//            });


            /* Set Collector from Prefs */
            String collector = PreferenceUtils.getCollector(this.getApplicationContext());
            if (!collector.isEmpty()) {
                final TextInputEditText collector_tv = (TextInputEditText) findViewById(R.id.collector);
                collector_tv.setText(collector);
            }

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
            SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
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
                    Toast.makeText(getApplicationContext(), getString(R.string.location_permission), Toast.LENGTH_LONG).show();
                    setResult(RESULT_CANCELED);
                    finish();
                }
            });
        }

    }

    private void populateViews(Geofauna geofauna) {
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

        uniqueIdLayout.setError(null);
        serialNoLayout.setError(null);
        localityLayout.setError(null);
        collectorLayout.setError(null);

        date_tv.setText(geofauna.getDate());
        time_tv.setText(geofauna.getTime());
        latitude_tv.setText(String.valueOf(geofauna.getLatitude()));
        longitude_tv.setText(String.valueOf(geofauna.getLongitude()));
        accuracy_tv.setText(String.format(Locale.ENGLISH, "Accurate up to %s", geofauna.getAccuracy()));

        uniqueId.setText(geofauna.getUniqueSurveyId());
        uniqueId.setInputType(InputType.TYPE_NULL);     // Disable editing unique id

        serialNo.setText(geofauna.getSerialNo());
        locality.setText(geofauna.getLocality());
        collector.setText(geofauna.getCollector());
        stateSpinner.setSelection(getStateList().indexOf(geofauna.getState()));
        habitatSpinner.setSelection(getHabitatList().indexOf(geofauna.getHabitat()));
        entomofoSpinner.setSelection(getBinaryList().indexOf(geofauna.getEntomofauna()));
        otherVerSpinner.setSelection(getBinaryList().indexOf(geofauna.getOtherInvertebrate()));
        vertebrateSpinner.setSelection(getBinaryList().indexOf(geofauna.getVertebrate()));

        examplesSpinner.setSelection(getNoOfExamplesList().indexOf(geofauna.getNoOfExamples()));
        temperature.setText(geofauna.getTemperature());
        humidity.setText(geofauna.getHumidity());

        // Set images if available
        setImageView(imageViewAnimal, geofauna.getImageAnimalPath());
        setImageView(imageViewHabitat, geofauna.getImageHabitatPath());
        setImageView(imageViewHost, geofauna.getImageHostPath());
    }

    private void setImageView(ImageView imageView, String path) {
        if (path != null && !path.isEmpty()) {
            Glide.with(this)
                    .load(Uri.fromFile(new File(path)))
                    .override(getResources().getDimensionPixelSize(R.dimen.preview_w_h),
                            getResources().getDimensionPixelSize(R.dimen.preview_w_h))
                    .into(imageView);
        }
    }

    private Intent saveDataOnRoom(Intent intent, Geofauna geofauna) {
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

        uniqueIdLayout.setError(null);
        serialNoLayout.setError(null);
        localityLayout.setError(null);
        collectorLayout.setError(null);

        geofauna.setDate(date_tv.getText().toString());
        geofauna.setTime(time_tv.getText().toString());
        geofauna.setLatitude(Double.valueOf(latitude_tv.getText().toString()));
        geofauna.setLongitude(Double.valueOf(longitude_tv.getText().toString()));
        geofauna.setAccuracy(accuracy_tv.getText().toString().substring(getResources().getInteger(R.integer.accuracy_string_trim_start)));


        if (TextUtils.isEmpty(uniqueId.getText())) {
            uniqueIdLayout.setError(getResources().getString(R.string.error_empty));
            uniqueIdLayout.requestFocus();
            error = true;
        } else {
            geofauna.setUniqueSurveyId(Objects.requireNonNull(uniqueId.getText()).toString());

            if (TextUtils.isEmpty(serialNo.getText())) {
                serialNoLayout.setError(getResources().getString(R.string.error_empty));
                serialNoLayout.requestFocus();
                error = true;
            } else {
                geofauna.setSerialNo(Objects.requireNonNull(serialNo.getText()).toString());

                if (TextUtils.isEmpty(locality.getText())) {
                    localityLayout.setError(getResources().getString(R.string.error_empty));
                    localityLayout.requestFocus();
                    error = true;
                } else {
                    geofauna.setLocality(Objects.requireNonNull(locality.getText()).toString());

                    if (TextUtils.isEmpty(collector.getText())) {
                        collectorLayout.setError(getResources().getString(R.string.error_empty));
                        collectorLayout.requestFocus();
                        error = true;
                    } else {
                        geofauna.setCollector(Objects.requireNonNull(collector.getText()).toString());
                    }
                }
            }
        }

        geofauna.setState(Objects.requireNonNull(stateSpinner.getSelectedItem().toString()));
        geofauna.setHabitat(Objects.requireNonNull(habitatSpinner.getSelectedItem().toString()));
        geofauna.setEntomofauna(Objects.requireNonNull(entomofoSpinner.getSelectedItem().toString()));
        geofauna.setOtherInvertebrate(Objects.requireNonNull(otherVerSpinner.getSelectedItem().toString()));
        geofauna.setVertebrate(Objects.requireNonNull(vertebrateSpinner.getSelectedItem().toString()));

        geofauna.setNoOfExamples(Objects.requireNonNull(examplesSpinner.getSelectedItem().toString()));
        geofauna.setTemperature(Objects.requireNonNull(temperature.getText()).toString());
        geofauna.setHumidity(Objects.requireNonNull(humidity.getText()).toString());

        /* Images */
        geofauna.setImageAnimal(imageFiles[0]);
        geofauna.setImageHabitat(imageFiles[1]);
        geofauna.setImageHost(imageFiles[2]);

        geofauna.setImageAnimalPath(imageUris[0]);
        geofauna.setImageHabitatPath(imageUris[1]);
        geofauna.setImageHostPath(imageUris[2]);

        geofauna.setTimestamp(new Date().getTime());

        intent.putExtra(DatabaseColumns.parcelGeofauna, geofauna);

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

    private String getImagePrefix() {
        switch (flag) {
            case 0:
                return "ImgAnimal";
            case 1:
                return "ImgHabitat";
            case 2:
                return "ImgHost";
            default:
                return "Img";
        }
    }

    public void setImageFileName(String fileName) {
        imageFiles[flag] = fileName;
    }

    public String getImageFileName() {
        return imageFiles[flag];
    }

    public void setImageFilePath(String filePath) {
        imageUris[flag] = filePath;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // After We Got the Required Permissions
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted
                capturePhoto();
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = getImagePrefix() + "_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();

        setImageFileName(imageFileName + ".jpg");
        String[] splitPath = currentPhotoPath.split("/", -1);
        splitPath[splitPath.length - 1] = getImageFileName();
        setImageFilePath(TextUtils.join("/", splitPath));

        return image;
    }


    private void capturePhoto() {
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
    public void onBackPressed() {
        super.onBackPressed();

        // Delete the photos it saved
        for (String image : imageUris) {
            if (image != null) {
                File file = new File(image);
                if (file.exists()) {
                    file.delete();
                    try {
                        file.getCanonicalFile().delete();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (file.exists()) {
                        getApplicationContext().deleteFile(file.getName());
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            if (photoUri.getPath() != null) {
                AppCompatImageView imageView = getImageView();

                Glide.with(this)
                        .load(Uri.fromFile(new File(currentPhotoPath)))
                        .override(getResources().getDimensionPixelSize(R.dimen.preview_w_h),
                                getResources().getDimensionPixelSize(R.dimen.preview_w_h))
                        .into(imageView);

                AppCompatImageView imageBtn = getCancelBtn();
                imageBtn.setVisibility(View.VISIBLE);

                // Resize the photo on a separate thread
                new ResizeImageTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }


    public static class ResizeImageTask extends AsyncTask<String, Void, Boolean> {

        private WeakReference<SurveyForm> activityReference;

        ResizeImageTask(SurveyForm context) {
            activityReference = new WeakReference<>(context);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Boolean doInBackground(String... strings) {
            SurveyForm activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return false;

            try {
                String[] splitPath = activity.currentPhotoPath.split("/", -1);
                splitPath[splitPath.length - 1] = activity.getImageFileName();
                savebitmap(scaleDown(BitmapFactory.decodeFile(activity.currentPhotoPath),
                        2000, false), activity.currentPhotoPath, String.join("/", splitPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    public static void savebitmap(Bitmap bmp, String path, String rename_path) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File f = new File(path);
//        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();

        // Rename file
        File new_f = new File(rename_path);
        f.renameTo(new_f);
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.max(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        return Bitmap.createScaledBitmap(realImage, width,
                height, filter);
    }

    /* Array Getters */

    private List<String> getHabitatList() {
        return new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.Habitat_items)));
    }

    private List<String> getNoOfExamplesList() {
        return new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.Examples_items)));
    }

    private List<String> getStateList() {
        return new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.indian_states)));
    }

    private List<String> getBinaryList() {
        return new LinkedList<>(Arrays.asList("No", "Yes"));
    }


}
