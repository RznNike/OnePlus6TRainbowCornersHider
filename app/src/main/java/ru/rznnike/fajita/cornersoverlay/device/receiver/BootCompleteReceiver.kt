package ru.rznnike.fajita.cornersoverlay.device.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.rznnike.fajita.cornersoverlay.device.service.OverlayService

class BootCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            val myIntent = Intent(context, OverlayService::class.java)
            context.startForegroundService(myIntent)
        }
    }
}