package ru.rznnike.fajita.cornersoverlay.device.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Binder
import android.os.IBinder
import android.view.View
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import ru.rznnike.fajita.cornersoverlay.R

class OverlayService : Service() {
    private val binder = LocalBinder()
    private var overlayView: View? = null

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        stopService()
        return super.onUnbind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val enableOverlay = intent?.getBooleanExtra(PARAM_ENABLE_OVERLAY, false) ?: false
        if (enableOverlay) {
            showOverlay()
        } else {
            stopService()
        }

        return START_NOT_STICKY
    }

    private fun showOverlay() {
        buildNotification()

        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        val metrics = DisplayMetrics()
//        windowManager.defaultDisplay.getMetrics(metrics)

        overlayView?.let {
            windowManager.removeView(overlayView)
            overlayView = null
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES

        overlayView = View.inflate(this, R.layout.overlay, null)

        windowManager.addView(overlayView, params)
    }

    override fun onDestroy() {
        stopService()
        super.onDestroy()
    }

    private fun stopService() {
        overlayView?.let {
            val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.removeView(overlayView)
            overlayView = null
        }
        stopForeground(true)
        stopSelf()
    }

    private fun buildNotification() {
        val manager = getNotificationManager(this)
        val channelId = getChannelId(manager)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setShowWhen(false)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setColor(ContextCompat.getColor(this, R.color.colorAccent))
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(baseContext.getString(R.string.app_name))

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getNotificationManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun getChannelId(manager: NotificationManager): String {
        createIfNotExistNotificationChannel(manager)
        return NOTIFICATION_CHANNEL_ID
    }

    private fun createIfNotExistNotificationChannel(notificationManager: NotificationManager) {
        var channel: NotificationChannel? = notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID)
        if (channel != null) {
            return
        }

        channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationManager.createNotificationChannel(channel)
    }


    inner class LocalBinder : Binder() {
        @Suppress("unused")
        fun getService(): OverlayService {
            return this@OverlayService
        }
    }

    companion object {
        const val PARAM_ENABLE_OVERLAY = "PARAM_ENABLE_OVERLAY"

        private const val NOTIFICATION_ID = 101
        private const val NOTIFICATION_CHANNEL_ID = "6TCornersOverlay"
        private const val NOTIFICATION_CHANNEL_NAME = "6TCornersOverlay"
    }
}