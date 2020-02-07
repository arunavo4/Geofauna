package com.isro.geofauna;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.isro.geofauna.adapter.RecordAdapter;
import com.isro.geofauna.data.DatabaseColumns;
import com.isro.geofauna.data.Geofauna;
import com.isro.geofauna.data.GeofaunaRoomDatabase;
import com.isro.geofauna.data.GeofaunaViewModel;
import com.isro.geofauna.utils.CSVWriter;
import com.isro.geofauna.utils.PreferenceUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_NEW_RECORD = 10;
    public static final int REQUEST_COLLECTOR_DATA = 11;
    public static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 1;

    private GeofaunaViewModel mGeofaunaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Check if collector name is there
        if(PreferenceUtils.getCollector(this.getApplicationContext()).isEmpty()){
            // Go to Welcome Activity
            startActivityForResult(new Intent(this, WelcomeActivity.class), REQUEST_COLLECTOR_DATA);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final RecordAdapter adapter = new RecordAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FrameLayout no_record = (FrameLayout) findViewById(R.id.img_no_record);

        mGeofaunaViewModel = new ViewModelProvider(this).get(GeofaunaViewModel.class);

        mGeofaunaViewModel.getAllByDate().observe(this, geofaunas -> {
            adapter.setmGeofauna(geofaunas);
            if(adapter.getItemCount() > 0){
                recyclerView.setVisibility(View.VISIBLE);
                no_record.setVisibility(View.INVISIBLE);
            }else {
                if (recyclerView.getVisibility() == View.VISIBLE && no_record.getVisibility() == View.INVISIBLE){
                    recyclerView.setVisibility(View.INVISIBLE);
                    no_record.setVisibility(View.VISIBLE);
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(v -> startActivityForResult(new Intent(v.getContext(), SurveyForm.class), REQUEST_NEW_RECORD));

        // Collector Edit Button
        AppCompatImageView collector_edit = (AppCompatImageView) findViewById(R.id.collector_edit);
        collector_edit.setOnClickListener(
                v -> startActivityForResult(new Intent(v.getContext(), WelcomeActivity.class), REQUEST_COLLECTOR_DATA)
        );
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_NEW_RECORD && resultCode == RESULT_OK) {
            Geofauna geofauna = new Geofauna();

            geofauna.setDate(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.date)));
            geofauna.setTime(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.time)));
            geofauna.setLongitude(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.longitude)));
            geofauna.setLatitude(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.latitude)));
            geofauna.setAccuracy(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.accuracy)));

            geofauna.setUniqueSurveyId(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.uniqueSurveyId)));
            geofauna.setSerialNo(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.serialNo)));
            geofauna.setLocality(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.locality)));
            geofauna.setState(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.state)));
            geofauna.setCollector(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.collector)));
            geofauna.setPhone(Objects.requireNonNull(PreferenceUtils.getPhone(MainActivity.this)));
            geofauna.setHabitat(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.habitat)));
            geofauna.setEntomofauna(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.entomofauna)));
            geofauna.setOtherInvertebrate(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.otherInvertebrate)));
            geofauna.setVertebrate(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.vertebrate)));
            geofauna.setNoOfExamples(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.noOfExamples)));
            geofauna.setTemperature(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.temperature)));
            geofauna.setHumidity(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.humidity)));

            geofauna.setImageAnimal(data.getStringExtra(DatabaseColumns.imageAnimal));
            geofauna.setImageHabitat(data.getStringExtra(DatabaseColumns.imageHabitat));
            geofauna.setImageHost(data.getStringExtra(DatabaseColumns.imageHost));

            geofauna.setImageAnimalPath(data.getStringExtra(DatabaseColumns.imageAnimalPath));
            geofauna.setImageHabitatPath(data.getStringExtra(DatabaseColumns.imageHabitatPath));
            geofauna.setImageHostPath(data.getStringExtra(DatabaseColumns.imageHostPath));

            geofauna.setTimestamp(data.getLongExtra(DatabaseColumns.timestamp, new Date().getTime()));

            mGeofaunaViewModel.insertAll(geofauna);

            Snackbar.make((CoordinatorLayout) findViewById(R.id.main_layout), getResources().getString(R.string.saved_successfully), Snackbar.LENGTH_SHORT).show();
        }
        else if(requestCode == REQUEST_COLLECTOR_DATA && resultCode == RESULT_OK){
            PreferenceUtils.setCollector(this.getApplicationContext(), Objects.requireNonNull(
                    data.getStringExtra(DatabaseColumns.collector)));
            PreferenceUtils.setPhone(this.getApplicationContext(), Objects.requireNonNull(
                    data.getStringExtra(DatabaseColumns.phone)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_export) {

            if ((ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)){

                // Permission is not granted
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_STORAGE);
            } else {
                // Permission has already been granted
                new ExportDatabaseCSVTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                return true;
            }
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // After We Got the Required Permissions
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_STORAGE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted
                new ExportDatabaseCSVTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }

    public class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog ;
        private GeofaunaRoomDatabase userDatabase;
        Context context;

        ExportDatabaseCSVTask(Context context) {
            this.dialog = new ProgressDialog(context);
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Exporting database...");
            this.dialog.show();
            userDatabase = GeofaunaRoomDatabase.getDatabase(context);
        }

        protected Boolean doInBackground(final String... args) {
            File exportDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            if (!exportDir.exists()) { boolean flag = exportDir.mkdir();}

            File file = new File(exportDir, getResources().getString(R.string.export_filename_default)+ ".csv");
            try {
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                String[] databaseColumns = DatabaseColumns.getDatabaseColumns();
                csvWrite.writeNext(databaseColumns);
                List<Geofauna> record = userDatabase.geofaunaDao().getAllByDateList();
                for(int i=0; i<record.size(); i++){
                    String[] StringArray ={
                            record.get(i).getUniqueSurveyId(),
                            record.get(i).getSerialNo(),
                            record.get(i).getLocality(),
                            record.get(i).getState(),
                            record.get(i).getCollector(),
                            record.get(i).getPhone(),
                            record.get(i).getHabitat(),
                            record.get(i).getEntomofauna(),
                            record.get(i).getOtherInvertebrate(),
                            record.get(i).getVertebrate(),
                            record.get(i).getNoOfExamples(),
                            record.get(i).getTemperature(),
                            record.get(i).getHumidity(),
                            record.get(i).getImageAnimal(),
                            record.get(i).getImageHabitat(),
                            record.get(i).getImageHost(),
                            record.get(i).getDate(),
                            record.get(i).getTime(),
                            record.get(i).getLatitude(),
                            record.get(i).getLongitude(),
                            record.get(i).getAccuracy()
                    };
                    csvWrite.writeNext(StringArray);
                }
                csvWrite.close();
                return true;
            } catch (IOException e) {
                Log.i("IOException", Objects.requireNonNull(e.getMessage()));
                return false;
            }
        }

        protected void onPostExecute(final Boolean success) {
            if (this.dialog.isShowing()) { this.dialog.dismiss(); }
            if (success) {
                Toast.makeText(context, getResources().getString(R.string.export_successful), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, getResources().getString(R.string.export_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
