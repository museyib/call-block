package com.example.call_block;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grantPhonePermission();

        SwitchCompat enableCallBlockingSwitch = findViewById(R.id.enable_call_blocking);

        serviceIntent = new Intent(this, CallBlockingService.class);

        enableCallBlockingSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b)
                startService(serviceIntent);
            else
                stopService(serviceIntent);
        });
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