package io.github.freuvim.sipregister.activitys;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.github.freuvim.sipregister.R;
import io.github.freuvim.sipregister.SIPRegister;
import io.github.freuvim.sipregister.services.BackgroundService;
import test.jinesh.easypermissionslib.EasyPermission;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements EasyPermission.OnPermissionResult {

    final EasyPermission easyPermission = new EasyPermission();
    public Boolean permissions = false;
    private TelephonyManager TelephonyMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button btServico = (Button) findViewById(R.id.btServico);
        final Intent serviceIntent = new Intent(MainActivity.this, BackgroundService.class);
        TelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        btServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               easyPermission.requestPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE);
                if (permissions && !etPassword.getText().toString().equals("")) {
                    String imsi = TelephonyMgr.getSubscriberId();
                    SIPRegister sipRegister = new SIPRegister(MainActivity.this);
                    sipRegister.registerSIP(imsi, etPassword.getText().toString());
                    if (sipRegister.getSipResult()){
                        MainActivity.this.startService(serviceIntent);
                        Log.d(TAG, "TESTE: inicou serviço");
                    }
                } else {
                    Log.d(TAG, "TESTE: permissões não concedidas ou senha vazia");
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
            case Manifest.permission.READ_PHONE_STATE:
                if (isGranted) {
                    easyPermission.requestPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                break;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                if (isGranted) {
                    permissions = true;
                }
                break;
        }
    }
}
