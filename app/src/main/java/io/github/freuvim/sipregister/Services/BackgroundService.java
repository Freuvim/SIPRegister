package io.github.freuvim.sipregister.Services;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.app.Service;
import android.util.Log;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import io.github.freuvim.sipregister.Database.BeanSettings;
import io.github.freuvim.sipregister.Database.DAOSettings;
import io.github.freuvim.sipregister.Listeners.TeleListener;

import static android.content.ContentValues.TAG;


public class BackgroundService extends Service {

    TelephonyManager TelephonyMgr;
    public static final int notify = 10000;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;

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

    public void makeDir() {
        Log.d(TAG, "TESTE: chamou makedir");
        String path = null;
        DAOSettings dao = new DAOSettings(getBaseContext());
        BeanSettings bean = new BeanSettings();
        dao.open();
        bean.setIdSetting(1);
        if (dao.selectUm(bean).getValue_setting().equals("true")) {
            path = "Wifi";
            Log.d(TAG, "TESTE: tem wifi");
        } else {
            bean.setIdSetting(0);
            if (!dao.selectUm(bean).getValue_setting().equals("Unknown")) {
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
                if (!Directory.exists()) {
                    if (!Directory.mkdirs()) {
                        throw new Exception();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "makeDir");
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
        makeDir();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
