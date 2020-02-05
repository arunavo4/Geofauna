package com.isro.geofauna;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.isro.geofauna.adapter.RecordAdapter;
import com.isro.geofauna.data.DatabaseColumns;
import com.isro.geofauna.data.Geofauna;
import com.isro.geofauna.data.GeofaunaViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final int ACTIVITY_REQUEST_CODE = 1;

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

        } else {
//            Snackbar.make((CoordinatorLayout) findViewById(R.id.main_layout), "Saved Successfully!", Snackbar.LENGTH_SHORT).show();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
