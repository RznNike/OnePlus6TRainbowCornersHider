package ru.rznnike.fajita.cornersoverlay.app.global.navigation

import androidx.lifecycle.ViewModel
import ru.terrakok.cicerone.Cicerone

class FlowCiceroneViewModel : ViewModel() {
    private var flowCicerone: Cicerone<FlowRouter>? = null

    fun getFlowCicerone(appRouter: AppRouter): Cicerone<FlowRouter> {
        if (flowCicerone == null) {
            flowCicerone = Cicerone.create(FlowRouter(appRouter))
        }

        return flowCicerone!!
    }
}
