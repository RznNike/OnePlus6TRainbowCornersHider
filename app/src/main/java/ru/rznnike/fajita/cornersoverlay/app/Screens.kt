package ru.rznnike.fajita.cornersoverlay.app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.Fragment
import ru.rznnike.fajita.cornersoverlay.R
import ru.rznnike.fajita.cornersoverlay.app.global.navigation.AppScreen
import ru.rznnike.fajita.cornersoverlay.app.ui.fragment.main.MainFlowFragment
import ru.rznnike.fajita.cornersoverlay.app.ui.fragment.main.MainFragment

class Screens {
    class MainFlow : AppScreen() {
        override fun getFragment(): Fragment = MainFlowFragment.newInstance()
    }

    class MainScreen : AppScreen() {
        override fun getFragment(): Fragment = MainFragment.newInstance()
    }


    // STANDART ACTIONS

    @Suppress("unused")
    class AppSettingsActionScreen : AppScreen() {
        override fun getActivityIntent(context: Context): Intent? {
            val myAppSettings = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + context.packageName)
            )
            myAppSettings.addCategory(Intent.CATEGORY_DEFAULT)
            myAppSettings.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            return myAppSettings
        }
    }

    @Suppress("unused")
    class ActionOpenLinkScreen(
        private val link: String
    ) : AppScreen() {
        override fun getActivityIntent(context: Context): Intent? {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(link)
            return intent
        }
    }

    @Suppress("unused")
    class ActionOpenDialScreen(private val phone: String) : AppScreen() {
        override fun getActivityIntent(context: Context): Intent? {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.fromParts("tel", phone, null)
            return intent
        }
    }

    @Suppress("unused")
    class ActionShareTextScreen(
        private val text: String,
        private val header: String? = null
    ) : AppScreen() {
        override fun getActivityIntent(context: Context): Intent? {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, text)
            intent.putExtra("sms_body", text)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
            intent.type = "text/plain"

            return Intent.createChooser(
                intent,
                header ?: context.resources.getString(R.string.share))
        }
    }
}
