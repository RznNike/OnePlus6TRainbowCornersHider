package ru.rznnike.fajita.cornersoverlay.app.presentation.main

import com.arellomobile.mvp.InjectViewState
import org.koin.core.inject
import ru.rznnike.fajita.cornersoverlay.app.global.presentation.BasePresenter
import ru.rznnike.fajita.cornersoverlay.data.preference.Preferences
import ru.rznnike.fajita.cornersoverlay.domain.model.SolutionType

@InjectViewState
class MainPresenter : BasePresenter<MainView>() {
    private val preferences: Preferences by inject()

    init {
        populateData()
    }

    private fun populateData() {
        viewState.populateData(
            preferences.getOverlayEnabledPreference().get(),
            preferences.getSolutionTypePreference().get()
        )
    }

    fun onSwitchEnableOverlay(checked: Boolean) {
        preferences.getOverlayEnabledPreference().set(checked)
        populateData()
    }

    fun getCurrentSolutionType(): SolutionType {
        return preferences.getSolutionTypePreference().get()
    }

    fun onSolutionTypeValueChanged(type: SolutionType) {
        preferences.getSolutionTypePreference().set(type)
        populateData()
    }
}
