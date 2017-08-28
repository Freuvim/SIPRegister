package io.github.freuvim.sipregister.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.github.freuvim.sipregister.services.BackgroundService;

public class BroadcastReceiverOnBootCompleted extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            Intent serviceIntent = new Intent(context, BackgroundService.class);
            context.stopService(serviceIntent);
            context.startService(serviceIntent);

        }
    }
}
