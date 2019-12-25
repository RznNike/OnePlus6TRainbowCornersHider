package ru.rznnike.fajita.cornersoverlay.data.preference

import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences

class Preferences(private val prefs: RxSharedPreferences) {
    fun getOverlayEnabledPreference(): Preference<Boolean> = prefs.getBoolean(OVERLAY_ENABLED, false)

    companion object {
        private const val OVERLAY_ENABLED = "OVERLAY_ENABLED"
    }
}
