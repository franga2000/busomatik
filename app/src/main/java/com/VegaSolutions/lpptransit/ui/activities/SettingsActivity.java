package com.VegaSolutions.lpptransit.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;

import com.VegaSolutions.lpptransit.R;

public class SettingsActivity extends AppCompatActivity {


    public static final int SETTINGS_UPDATE = 0;

    RadioButton buttonWhite, buttonDark, buttonMin, buttonHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("settings", MODE_PRIVATE);
        boolean dark_theme = sharedPreferences.getBoolean("app_theme", false);
        boolean hour = sharedPreferences.getBoolean("hour", false);
        setTheme(dark_theme ? R.style.DarkTheme : R.style.WhiteTheme);
        setContentView(R.layout.activity_settings);

        buttonDark = findViewById(R.id.radio_dark);
        buttonWhite = findViewById(R.id.radio_white);
        buttonMin = findViewById(R.id.radio_minute);
        buttonHour = findViewById(R.id.radio_hour);

        if (dark_theme)
            buttonDark.setChecked(true);
        else
            buttonWhite.setChecked(true);

        if (hour)
            buttonHour.setChecked(true);
        else
            buttonMin.setChecked(true);

        setResult(1);

        buttonDark.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {

                setResult(SETTINGS_UPDATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("app_theme", true);
                editor.apply();

                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                finish();


            }

        });
        buttonWhite.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {

                setResult(SETTINGS_UPDATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("app_theme", false);
                editor.apply();

                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                finish();


            }

        });


        buttonMin.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("hour", false);
                editor.apply();

            }

        });

        buttonHour.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("hour", true);
                editor.apply();

            }

        });

    }
}
