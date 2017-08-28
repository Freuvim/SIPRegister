package io.github.freuvim.sipregister.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import io.github.freuvim.sipregister.services.BackgroundService;

public class BroadcastReceiverOnBootCompleted extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.registerReceiver(this, new IntentFilter(intent.getAction()));
        Intent serviceIntent = new Intent(context, BackgroundService.class);
        context.stopService(serviceIntent);
    }
}
