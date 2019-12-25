package ru.rznnike.fajita.cornersoverlay.app.global.navigation

import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.commands.BackTo

class AppRouter : Router() {
    fun startFlow(flow: AppScreen) {
        navigateTo(flow)
    }

    fun newRootFlow(flow: AppScreen) {
        newRootScreen(flow)
    }

    fun newRootFlowChain(vararg flows: AppScreen) {
        newRootChain(*flows)
    }

    fun finishFlow() {
        exit()
    }

    fun openFlow(flow: AppScreen) {
        executeCommands(ForwardTo(flow))
    }

    fun backTo(flow: AppScreen?) {
        executeCommands(BackTo(flow))
    }
}
