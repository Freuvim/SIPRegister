package io.github.freuvim.sipregister;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class SIPRegister {

    private SipManager mSipManager;
    private SipProfile mSipProfile;
    private Context ctx = null;
    private Boolean sipResult = false;

    public SIPRegister(Context context) {
        ctx = context;
    }

    public void registerSIP(String imsi, String password) {
        if (mSipManager == null) {
            mSipManager = SipManager.newInstance(ctx);
        }
        String domain = "ims.mnc0" + imsi.substring(3, 5) + ".mcc" + imsi.substring(0, 3) + ".3gppnetwork.org";
        try {
            Log.d(TAG, "TESTE: suporte a voip -" + SipManager.isVoipSupported(ctx));
            Log.d(TAG, "TESTE: suporte a api sip -" + SipManager.isApiSupported(ctx));

            SipProfile.Builder builder = new SipProfile.Builder(imsi, domain);
            builder.setPassword(password);
            builder.setAuthUserName(imsi+"@"+domain);
            builder.setOutboundProxy("255.255.255.0");
            builder.setProfileName(imsi + "@" + domain );
            builder.setDisplayName("Test");
            builder.setProtocol("TCP");
            builder.setAutoRegistration(true);
            mSipProfile = builder.build();
            Intent intent = new Intent();
            intent.setAction("android.SipDemo.INCOMING_CALL");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, Intent.FILL_IN_DATA);
            mSipManager.open(mSipProfile, pendingIntent, null);
            mSipManager.setRegistrationListener(mSipProfile.getUriString(), new SipRegistrationListener() {
                @Override
                public void onRegistering(String s) {
                    Log.d(TAG, "TESTE: registrando");
                }

                @Override
                public void onRegistrationDone(String s, long l) {
                    Log.d(TAG, "TESTE: registrado");
                    setSipResult(true);
                }

                @Override
                public void onRegistrationFailed(String s, int i, String s1) {
                    Log.d(TAG, "TESTE: erro ao registrar - " + s + s1);
                    setSipResult(false);

                }
            });

        } catch (SipException se) {
            Log.d(TAG, "TESTE: " + se);
        } catch (NullPointerException npe) {
            Log.d(TAG, "TESTE: " + npe + " - " + mSipProfile.getDisplayName());
        } catch (java.text.ParseException pe) {
            Log.d(TAG, "TESTE: " + pe + "");
        }
    }

    public Boolean getSipResult() {
        return sipResult;
    }

    public void setSipResult(Boolean sipResult) {
        this.sipResult = sipResult;
    }
}
