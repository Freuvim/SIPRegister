package io.github.freuvim.sipregister.Listeners;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import io.github.freuvim.sipregister.Database.BeanSettings;
import io.github.freuvim.sipregister.Database.DAOSettings;
import static android.content.ContentValues.TAG;

public class TeleListener extends PhoneStateListener {

    private Context ctx;

    public TeleListener(Context context){
        ctx = context;
    }

    @Override
    public void onDataConnectionStateChanged(int state, int networkType) {
        Boolean cheio = true;
        BeanSettings beanSettings = new BeanSettings();
        DAOSettings dao = new DAOSettings(ctx);
        dao.open();
        beanSettings.setIdSetting(0);
        if(dao.selectUm(beanSettings) == null){
            cheio = false;
        }
        beanSettings.setIdSetting(0);
        beanSettings.setName_setting("Dados");
        beanSettings.setValue_setting(getNetworkClass(networkType));
        if(cheio){
            dao.update(beanSettings);
        } else {
            dao.insert(beanSettings);
        }
        dao.close();
        super.onDataConnectionStateChanged(state, networkType);
    }

    private String getNetworkClass(int networkType) {
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "EDGE";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "HSPAP";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "LTE";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "HSPA";
            default:
                return "Unknown";
        }
    }
}
