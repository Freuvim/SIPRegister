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
        Log.e("[SIPRegister] =>", "Serviço criado");
        super.onCreate();
    }

    private class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    isConnectedToInternet();
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
        Log.e("[SIPRegister] =>", "Serviço criado");
        return super.onStartCommand(intent, flags, startId);
    }

    private void isConnectedToInternet() {
        Log.e("[SIPRegister] =>", "Verificando o tipo de conexão com a internet");
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
                Log.e("[SIPRegister] =>", "A conexão é através do WIFI");
            } else {
                bean.setValue_setting("false");
                Log.e("[SIPRegister] =>", "A conexão é através da rede móvel");
            }
        } else {
            bean.setValue_setting("false");
        }
        Log.e("[SIPRegister] =>", "Gravando o tipo de conexão...");
        if (dao.selectUm(bean) == null) {
            dao.insert(bean);
        } else {
            dao.update(bean);
        }
        dao.close();
        registrar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void registrar() {
        Log.e("[SIPRegister] =>", "Enviando requisições HTTP");
        Imsi imsi = new Imsi();
        imsi.setImsi(724051111111111L);
        imsi.setCgi(9999);
        imsi.setLat(9999);
        imsi.setLon(9999);
        Call<ImageModel> call = new RetrofitBuilder().getImageService().enviarRegistro(imsi);
        call.enqueue(new Callback<ImageModel>() {
            @Override
            public void onResponse(@NonNull Call<ImageModel> call, @NonNull Response<ImageModel> response) {
                ImageModel image = response.body();
                assert image != null;
                byte[] decode = Base64.decode(image.getArquivo(), Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                makeFile(bitmap);
            }

            @Override
            public void onFailure(@NonNull Call<ImageModel> call, @NonNull Throwable t) {
                Log.e("[SIPRegister] =>", "Erro ao tentar requisitar o servidor: " + t.getMessage());
            }
        });
    }

    public void makeFile(Bitmap image) {
        Log.e("[SIPRegister] =>", "Gravando a imagem...");
        FileOutputStream fos = null;
        String path = null;
        DAOSettings dao = new DAOSettings(getBaseContext());
        BeanSettings bean = new BeanSettings();
        String imsi = TelephonyMgr.getSubscriberId();
        File Directory;
        dao.open();
        bean.setIdSetting(1);
        if ("true".equals(dao.selectUm(bean).getValue_setting())) {
            path = "Wifi";
            Log.e("[SIPRegister] =>", "A conexão é via WIFI");
        } else {
            bean.setIdSetting(0);
            if (!"Unknown".equals(dao.selectUm(bean).getValue_setting())) {
                path = "Dados";
                Log.e("[SIPRegister] =>", "A conexão é via dados móveis");
            }
        }
        dao.close();
        if (path != null) {
            Directory = new File(Environment.getExternalStorageDirectory().getPath() + "/SIPRegister/" + imsi + path);
            File file = new File(Directory, "image.jpg");
            if (!file.exists()) {
                try {
                    if (!Directory.exists()) {
                        if (!Directory.mkdirs()) {
                            throw new Exception();
                        }
                    }
                    fos = new FileOutputStream(file);
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
                Log.e("[SIPRegister] =>", "Aquivo de imagem gerado");
            }
        }
    }
}
