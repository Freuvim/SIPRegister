package io.github.freuvim.sipregister.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.github.freuvim.sipregister.services.BackgroundService;


public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
            Intent serviceIntent = new Intent(Intent.ACTION_MAIN);
            serviceIntent.setClass(context, BackgroundService.class);

            context.startService(serviceIntent);
    }
}
