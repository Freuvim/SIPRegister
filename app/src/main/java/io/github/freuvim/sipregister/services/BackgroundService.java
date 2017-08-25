package io.github.freuvim.sipregister.services;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.app.Service;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import io.github.freuvim.sipregister.database.BeanSettings;
import io.github.freuvim.sipregister.database.DAOSettings;
import io.github.freuvim.sipregister.listeners.TeleListener;
import io.github.freuvim.sipregister.model.ImageModel;
import io.github.freuvim.sipregister.model.Imsi;
import io.github.freuvim.sipregister.retrofit.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class BackgroundService extends Service {

    TelephonyManager TelephonyMgr;
    public static final int notify = 10000;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    private Bitmap bitmap;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        TelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        TelephonyMgr.listen(new TeleListener(getBaseContext()), PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
        super.onCreate();
    }

    private class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    isConnectedToInternet();
                    Log.d(TAG, "TESTE: time display");
                }
            });
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            mTimer = new Timer();
        }
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);
        Log.d(TAG, "TESTE: criou servico");
        return super.onStartCommand(intent, flags, startId);
    }

    private void isConnectedToInternet() {
        Log.d(TAG, "TESTE: chamou ictt");
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        DAOSettings dao = new DAOSettings(getBaseContext());
        BeanSettings bean = new BeanSettings();
        dao.open();
        bean.setIdSetting(1);
        bean.setName_setting("Wifi");
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                bean.setValue_setting("true");
            } else {
                bean.setValue_setting("false");
                Log.d(TAG, "TESTE: sem wifi");
            }
        } else {
            bean.setValue_setting("false");
        }
        if (dao.selectUm(bean) == null) {
            dao.insert(bean);
            Log.d(TAG, "TESTE: insert");
        } else {
            dao.update(bean);
            Log.d(TAG, "TESTE: update");
        }
        dao.close();
        registrar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void registrar() {
        Imsi imsi = new Imsi();
        imsi.setImsi(724051111111111L);
        Call<ImageModel> call = new RetrofitBuilder().getImageService().registrar(imsi);
        call.enqueue(new Callback<ImageModel>() {
            @Override
            public void onResponse(@NonNull Call<ImageModel> call, @NonNull Response<ImageModel> response) {
                ImageModel image = response.body();
                assert image != null;
                Log.d("[Sip Service] => ", "Sip registrado com sucesso: " + image.getArquivo());
                byte[] decode = Base64.decode(image.getArquivo(), Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                makeFile(bitmap);
            }

            @Override
            public void onFailure(@NonNull Call<ImageModel> call, @NonNull Throwable t) {
                Log.e("[Sip Service =>", "Erro ao tentar registrar o SIP: " + t.getMessage());
            }
        });
    }

    public void makeFile(Bitmap image) {
        Log.d(TAG, "TESTE: chamou makeFile");
        FileOutputStream fos = null;
        String path = null;
        DAOSettings dao = new DAOSettings(getBaseContext());
        BeanSettings bean = new BeanSettings();
        dao.open();
        bean.setIdSetting(1);
        if ("true".equals(dao.selectUm(bean).getValue_setting())) {
            path = "Wifi";
            Log.d(TAG, "TESTE: tem wifi");
        } else {
            bean.setIdSetting(0);
            if (!"Unknown".equals(dao.selectUm(bean).getValue_setting())) {
                path = "Dados";
                Log.d(TAG, "TESTE: tem dados");
            }
        }
        dao.close();
        if (path != null) {
            File Directory;
            try {
                String imsi = TelephonyMgr.getSubscriberId();
                Directory = new File(Environment.getExternalStorageDirectory().getPath() + "/SIPRegister/" + imsi + path);
                File mypath = new File(Directory, "image.jpg");
                if (!Directory.exists()) {
                    if (!Directory.mkdirs()) {
                        throw new Exception();
                    }
                }
                fos = new FileOutputStream(mypath);
                image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    assert fos != null;
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(TAG, "TESTE: makeFile");
    }

}
