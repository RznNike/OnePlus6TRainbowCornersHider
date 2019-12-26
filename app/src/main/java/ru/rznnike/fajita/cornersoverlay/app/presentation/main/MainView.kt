package ru.rznnike.fajita.cornersoverlay.app.presentation.main

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.rznnike.fajita.cornersoverlay.domain.model.SolutionType

interface MainView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun populateData(
        overlayEnabled: Boolean,
        debugMode: Boolean,
        solutionType: SolutionType
    )
}
