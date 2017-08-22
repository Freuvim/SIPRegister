package io.github.freuvim.sipregister;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.net.sip.SipSession;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
            builder.setAuthUserName(imsi + "@" + domain);
            builder.setOutboundProxy("sip.atenainformatica.com.br");
            builder.setProfileName(domain);
            builder.setDisplayName("localizacao");
            builder.setPort(5060);
            builder.setAutoRegistration(true);
            mSipProfile = builder.build();
            Intent intent = new Intent();
            intent.setAction("android.SipDemo.INCOMING_CALL");
            //Bundle b = new Bundle();
            //b.putInt("gsm", 123);
            //intent.putExtras(b);
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


            PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, Intent.FILL_IN_DATA);
            mSipManager.open(mSipProfile, pendingIntent, null);
            SipAudioCall.Listener clistener = new SipAudioCall.Listener() {
                @Override
                public void onCalling(SipAudioCall call) {
                    Log.d(TAG, "TESTE: chamando");
                }

                @Override
                public void onCallEnded(SipAudioCall call) {
                    Toast.makeText(ctx, "Display Name: " + call.getPeerProfile().getDisplayName()
                            + "\n Uri String" + call.getPeerProfile().getUriString(), Toast.LENGTH_SHORT).show();
                    call.getPeerProfile().getDisplayName();
                    //downloadAndSaveFile()
                }

            };
            SipAudioCall testCall = new SipAudioCall(ctx, mSipProfile);
            testCall.setListener(clistener);
            SipSession.Listener sessionListener = new SipSession.Listener() {
                @Override
                public void onCalling(SipSession session) {
                    String callId = session.getCallId();
                    Log.d(TAG, "onCalling. call ID: " + callId);
                }

                @Override
                public void onRegistering(SipSession session) {
                    super.onRegistering(session);
                    Log.d(TAG, "TESTE: registrando");
                }

                @Override
                public void onRegistrationDone(SipSession session, int duration) {
                    super.onRegistrationDone(session, duration);
                    Log.d(TAG, "TESTE: registrado");
                    setSipResult(true);
                }

                @Override
                public void onRegistrationFailed(SipSession session, int errorCode, String errorMessage) {
                    super.onRegistrationFailed(session, errorCode, errorMessage);
                    Log.d(TAG, "TESTE: não registrado " + errorMessage);
                    setSipResult(false);
                }
            };
            SipSession ss = mSipManager.createSipSession(mSipProfile, sessionListener);
            String sipAddress = "sip:anupam90@sip2sip.info";
            mSipManager.makeAudioCall(mSipProfile.getUriString(), sipAddress, clistener, 30);
            Log.d(TAG, "iD: " + ss.getCallId());


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

    private Boolean downloadAndSaveFile(String server, int portNumber,
                                        String user, String password, String filename, File localFile)
            throws IOException {
        FTPClient ftp = null;

        try {
            ftp = new FTPClient();
            ftp.connect(server, portNumber);

            ftp.login(user, password);
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();

            OutputStream outputStream = null;
            boolean success = false;
            try {
                outputStream = new BufferedOutputStream(new FileOutputStream(
                        localFile));
                success = ftp.retrieveFile(filename, outputStream);
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }

            return success;
        } finally {
            if (ftp != null) {
                ftp.logout();
                ftp.disconnect();
            }
        }
    }
}
