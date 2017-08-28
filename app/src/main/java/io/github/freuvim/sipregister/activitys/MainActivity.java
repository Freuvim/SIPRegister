package io.github.freuvim.sipregister.activitys;

import android.Manifest;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import io.github.freuvim.sipregister.R;
import test.jinesh.easypermissionslib.EasyPermission;
import io.github.freuvim.sipregister.services.BackgroundService;

public class MainActivity extends AppCompatActivity implements EasyPermission.OnPermissionResult {

    final EasyPermission easyPermission = new EasyPermission();
    public Boolean permissions = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btServico = (Button) findViewById(R.id.btServico);
        final Intent serviceIntent = new Intent(MainActivity.this, BackgroundService.class);
        btServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easyPermission.requestPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissions) {
                    MainActivity.this.startService(serviceIntent);
                    Log.e("[SIPRegister] =>", "Serviço iniciado");
                } else {
                    Log.e("[SIPRegister] =>", "Uma ou mais permissão não foi concedida");
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        easyPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionResult(String permission, boolean isGranted) {
        switch (permission) {
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                if (isGranted) {
                    permissions = true;
                }
                break;
        }
    }
}
