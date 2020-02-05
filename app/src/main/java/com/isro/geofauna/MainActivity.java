package com.isro.geofauna;

import android.Manifest;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final int ACTIVITY_REQUEST_CODE = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 1;

    private GeofaunaViewModel mGeofaunaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final RecordAdapter adapter = new RecordAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mGeofaunaViewModel = new ViewModelProvider(this).get(GeofaunaViewModel.class);

        mGeofaunaViewModel.getAllByDate().observe(this, new Observer<List<Geofauna>>() {
            @Override
            public void onChanged(List<Geofauna> geofaunas) {
                adapter.setmGeofauna(geofaunas);
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(v -> startActivityForResult(new Intent(v.getContext(), SurveyForm.class), ACTIVITY_REQUEST_CODE));

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Geofauna geofauna = new Geofauna();

            geofauna.setDate(data.getStringExtra(DatabaseColumns.date));
            geofauna.setTime(data.getStringExtra(DatabaseColumns.time));
            geofauna.setLongitude(data.getStringExtra(DatabaseColumns.longitude));
            geofauna.setLatitude(data.getStringExtra(DatabaseColumns.latitude));

            geofauna.setUniqueSurveyId(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.uniqueSurveyId)));
            geofauna.setSerialNo(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.serialNo)));
            geofauna.setLocality(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.locality)));
            geofauna.setState(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.serialNo)));
            geofauna.setCollector(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.collector)));
            geofauna.setHabitat(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.habitat)));
            geofauna.setEntomofauna(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.entomofauna)));
            geofauna.setOtherInvertebrate(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.otherInvertebrate)));
            geofauna.setVertebrate(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.vertebrate)));
            geofauna.setNoOfExamples(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.noOfExamples)));
            geofauna.setTemperature(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.temperature)));
            geofauna.setHumidity(Objects.requireNonNull(data.getStringExtra(DatabaseColumns.humidity)));

            mGeofaunaViewModel.insertAll(geofauna);

            Snackbar.make((CoordinatorLayout) findViewById(R.id.main_layout), getResources().getString(R.string.saved_successfully), Snackbar.LENGTH_SHORT).show();

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
                Snackbar.make((CoordinatorLayout) findViewById(R.id.main_layout), getResources().getString(R.string.export_successful), Snackbar.LENGTH_SHORT).show();
                return true;
            }
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog ;
        private GeofaunaRoomDatabase userDatabase;
        Context context;

        public ExportDatabaseCSVTask(Context context) {
            this.userDatabase = userDatabase;
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

            File exportDir = new File(Environment.getExternalStorageDirectory(), "/geofauna/");
            Log.i("Environment.getExternal",Environment.getExternalStorageDirectory().toString());
            Log.i("exportDir",exportDir.toString());
            if (!exportDir.exists()) { boolean flag = exportDir.mkdir();
            Log.i("falg","" + flag);
            }

            File file = new File(exportDir, "Records.csv");
            try {
                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                String[] databaseColumns = DatabaseColumns.getDatabaseColumns();
                csvWrite.writeNext(databaseColumns);
                List<Geofauna> record = userDatabase.geofaunaDao().getAllByDateList();
                for(int i=0; i<record.size(); i++){
                    String[] StringArray ={String.valueOf(
                            record.get(i).getUniqueSurveyId()),
                            record.get(i).getSerialNo(),
                            record.get(i).getLocality(),
                            record.get(i).getState(),
                            record.get(i).getCollector(),
                            record.get(i).getHabitat(),
                            record.get(i).getEntomofauna(),
                            record.get(i).getOtherInvertebrate(),
                            record.get(i).getVertebrate(),
                            record.get(i).getNoOfExamples(),
                            record.get(i).getTemperature(),
                            record.get(i).getHumidity(),
                            record.get(i).getImageAnimal(),
                            record.get(i).getImageHabitat(),
                            record.get(i).getImageHost()
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
                Toast.makeText(context, "Export successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Export failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
