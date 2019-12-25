package ru.rznnike.fajita.cornersoverlay.app.global.presentation

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.ViewCommand
import com.arellomobile.mvp.viewstate.strategy.StateStrategy

class AddToEndSingleByTagStateStrategy : StateStrategy {
    override fun <View : MvpView> beforeApply(state: MutableList<ViewCommand<View>>, command: ViewCommand<View>) {
        val iterator = state.iterator()

        while (iterator.hasNext()) {
            val entry = iterator.next()

            if (entry.tag == command.tag) {
                iterator.remove()
                break
            }
        }

        state.add(command)
    }

    override fun <View : MvpView> afterApply(
        state: List<ViewCommand<View>>, command: ViewCommand<View>
    ) {
    }
}