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
            saveDataOnPrefs();
            if(!error){
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });
    }

    private void saveDataOnPrefs() {
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
            PreferenceUtils.setCollector(this.getApplicationContext(), collector.getText().toString());
        }

        PreferenceUtils.setPhone(this.getApplicationContext(), Objects.requireNonNull(phone.getText()).toString());
    }

}
