package ru.rznnike.fajita.cornersoverlay.app.global.presentation

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.rznnike.fajita.cornersoverlay.app.global.navigation.AppScreen

interface NavigationMvpView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun routerExit()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun routerStartFlow(flow: AppScreen)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun routerReplaceFlow(flow: AppScreen)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun routerNewRootFlow(flow: AppScreen)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun routerNewRootFlowChain(vararg flows: AppScreen)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun routerFinishFlow()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun routerBackTo(flow: AppScreen?)
}
