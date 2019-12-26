package ru.rznnike.fajita.cornersoverlay.data.preference

import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences
import ru.rznnike.fajita.cornersoverlay.domain.model.SolutionType

class Preferences(private val prefs: RxSharedPreferences) {
    fun getOverlayEnabledPreference(): Preference<Boolean> = prefs.getBoolean(OVERLAY_ENABLED, false)

    fun getDebugModePreference(): Preference<Boolean> = prefs.getBoolean(DEBUG_MODE, false)

    fun getSolutionTypePreference(): Preference<SolutionType> = prefs.getObject(SOLUTION_TYPE, SolutionType.APPLICATION, SolutionTypeConverter())

    companion object {
        private const val OVERLAY_ENABLED = "OVERLAY_ENABLED"
        private const val DEBUG_MODE = "DEBUG_MODE"
        private const val SOLUTION_TYPE = "SOLUTION_TYPE"
    }
}
