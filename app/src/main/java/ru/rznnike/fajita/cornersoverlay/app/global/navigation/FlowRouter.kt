package ru.rznnike.fajita.cornersoverlay.app.global.navigation

import ru.terrakok.cicerone.Router

class FlowRouter(private val appRouter: AppRouter) : Router() {
    fun startFlow(flows: AppScreen) {
        appRouter.navigateTo(flows)
    }

    fun replaceFlow(flow: AppScreen) {
        appRouter.replaceScreen(flow)
    }

    fun newRootFlow(flow: AppScreen) {
        appRouter.newRootScreen(flow)
    }

    fun newRootFlowChain(vararg flows: AppScreen) {
        appRouter.newRootChain(*flows)
    }

    fun finishFlow() {
        appRouter.exit()
    }

    fun backTo(flow: AppScreen?) {
        appRouter.backTo(flow)
    }
}
