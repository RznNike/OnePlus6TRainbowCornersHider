package ru.rznnike.fajita.cornersoverlay.device.service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import org.koin.android.ext.android.inject
import ru.rznnike.fajita.cornersoverlay.R
import ru.rznnike.fajita.cornersoverlay.app.global.notifier.Notifier
import ru.rznnike.fajita.cornersoverlay.app.ui.AppActivity
import ru.rznnike.fajita.cornersoverlay.data.preference.Preferences

class OverlayService : Service() {
    private val notifier: Notifier by inject()
    private val preferences: Preferences by inject()

    private val binder = LocalBinder()
    private var overlayView: View? = null
    private val screenSize = Point()

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        stopService()
        return super.onUnbind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val enableOverlay = preferences.getOverlayEnabledPreference().get()
        val debugMode = preferences.getDebugModePreference().get()

        if (enableOverlay) {
            showOverlay(debugMode)
        } else {
            stopService()
        }

        return START_NOT_STICKY
    }

    @SuppressLint("RtlHardcoded")
    private fun showOverlay(debugMode: Boolean) {
        buildNotification()

        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getRealSize(screenSize)

        overlayView?.let {
            windowManager.removeView(overlayView)
            overlayView = null
        }

        val params = WindowManager.LayoutParams().apply {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            flags = (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            format = PixelFormat.TRANSLUCENT
            layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            gravity = Gravity.TOP or Gravity.LEFT
            width = screenSize.x
            height = screenSize.y
        }

        overlayView = View.inflate(this, R.layout.overlay, null)
        val cornerViews = listOf(
            overlayView?.findViewById<AppCompatImageView>(R.id.imageViewLeftTopCorner),
            overlayView?.findViewById<AppCompatImageView>(R.id.imageViewRightTopCorner),
            overlayView?.findViewById<AppCompatImageView>(R.id.imageViewLeftBottomCorner),
            overlayView?.findViewById<AppCompatImageView>(R.id.imageViewRightBottomCorner)
        )
        val imageRes = if (debugMode) R.drawable.corner_left_top_debug else R.drawable.corner_left_top
        cornerViews.forEach {
            it?.setImageResource(imageRes)
        }

        try {
            windowManager.addView(overlayView, params)
        } catch (e: Exception) {
            notifier.sendMessage(R.string.error_unknown)
            Handler().postDelayed(::stopService, 1000)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val orientation = newConfig.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            overlayView?.layoutParams?.width = screenSize.x
            overlayView?.layoutParams?.height = screenSize.y
        } else {
            overlayView?.layoutParams?.width = screenSize.y
            overlayView?.layoutParams?.height = screenSize.x
        }
        windowManager.removeView(overlayView)
        windowManager.addView(overlayView, overlayView?.layoutParams)
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
        val notificationIntent = Intent(this, AppActivity::class.java)
        val intent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val manager = getNotificationManager(this)
        val channelId = getChannelId(manager)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setShowWhen(false)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setColor(ContextCompat.getColor(this, R.color.colorAccent))
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(getString(R.string.notification_title))
            .addAction(R.drawable.ic_open_app, getString(R.string.notification_settings), intent)

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
        private const val NOTIFICATION_ID = 101
        private const val NOTIFICATION_CHANNEL_ID = "6TCornersOverlay"
        private const val NOTIFICATION_CHANNEL_NAME = "6TCornersOverlay"
    }
}