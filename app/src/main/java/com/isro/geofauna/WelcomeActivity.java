package com.isro.geofauna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.isro.geofauna.data.DatabaseColumns;
import com.isro.geofauna.utils.PreferenceUtils;

import java.util.Objects;

public class WelcomeActivity extends AppCompatActivity {
    private boolean error = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(v -> {
            Intent welcomeIntent = saveDataOnPrefs(new Intent());
            if(!error){
                setResult(RESULT_OK, welcomeIntent);
                finish();
            }
        });

        //Check if collector name is there
        if(!PreferenceUtils.getCollector(this.getApplicationContext()).isEmpty()){
            final TextInputEditText collector = (TextInputEditText) findViewById(R.id.collector);
            collector.setText(PreferenceUtils.getCollector(this.getApplicationContext()));
        }
        if(!PreferenceUtils.getPhone(this.getApplicationContext()).isEmpty()){
            final TextInputEditText phone = (TextInputEditText) findViewById(R.id.phone);
            phone.setText(PreferenceUtils.getPhone(this.getApplicationContext()));
        }
    }

    private Intent saveDataOnPrefs(Intent intent) {
        error = false;  // Reset flag

        /* Mandatory fields*/
        final TextInputEditText collector = (TextInputEditText) findViewById(R.id.collector);
        final TextInputLayout collectorLayout = (TextInputLayout) findViewById(R.id.collector_layout);

        final TextInputEditText phone = (TextInputEditText) findViewById(R.id.phone);

        collectorLayout.setError(null);

        if(TextUtils.isEmpty(collector.getText())){
            collectorLayout.setError(getResources().getString(R.string.error_empty));
            error = true;
        }else{
            intent.putExtra(DatabaseColumns.collector, collector.getText().toString());
        }
        intent.putExtra(DatabaseColumns.phone, Objects.requireNonNull(phone.getText()).toString());

        return intent;
    }

}
