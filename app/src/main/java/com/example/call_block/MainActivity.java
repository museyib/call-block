package com.example.call_block;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private Intent serviceIntent;
    private SwitchCompat cellularCallBlockingSwitch;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        grantPhonePermission();
        cellularCallBlockingSwitch = findViewById(R.id.cellular_call_blocking_switch);

        serviceIntent = new Intent(this, CallBlockingService.class);

        cellularCallBlockingSwitch.setOnCheckedChangeListener((compoundButton, b) -> switchCellularCallBlocking(b));
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        cellularCallBlockingSwitch.setChecked(serviceActive());
    }

    private boolean serviceActive()
    {
        return preferences.getBoolean("serviceRunning", false);
    }

    private void switchCellularCallBlocking(boolean b)
    {
        if (b)
        {
            if (!serviceActive())
            {
                startForegroundService(serviceIntent);
                preferences.edit().putBoolean("serviceRunning", true).apply();
            }
            cellularCallBlockingSwitch.setText(getString(R.string.disable_call_blocking));
        }
        else
        {
            if (serviceActive())
            {
                stopService(serviceIntent);
                preferences.edit().putBoolean("serviceRunning", false).apply();
            }
            cellularCallBlockingSwitch.setText(getString(R.string.enable_call_blocking));
        }
    }

    private void grantPhonePermission()
    {
        int phonePermissionCheck = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (phonePermissionCheck == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.MODIFY_PHONE_STATE,
                    Manifest.permission.ANSWER_PHONE_CALLS
            }, 1);
        }
    }
}