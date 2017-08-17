package io.github.freuvim.sipregister.listeners;

import android.net.sip.SipRegistrationListener;
import android.util.Log;
import static android.content.ContentValues.TAG;

public class SIPRegistrationListener implements SipRegistrationListener {
    @Override
    public void onRegistering(String s) {
        Log.d(TAG, "TESTE: registrando");
    }

    @Override
    public void onRegistrationDone(String s, long l) {
        Log.d(TAG, "TESTE: registrado");
    }

    @Override
    public void onRegistrationFailed(String s, int i, String s1) {
        Log.d(TAG, "TESTE: erro ao registrar");
    }
}
