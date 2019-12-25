package ru.rznnike.fajita.cornersoverlay.app.global.ui.fragment

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.transition.Fade
import androidx.transition.Slide
import ru.rznnike.fajita.cornersoverlay.R
import ru.rznnike.fajita.cornersoverlay.app.global.navigation.AppNavigator
import ru.rznnike.fajita.cornersoverlay.app.global.navigation.FlowCiceroneViewModel
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.cicerone.commands.Replace

abstract class FlowFragment : BaseFragment() {
    private var navigatorHolder: NavigatorHolder? = null

    //fix incorrect order lifecycle callback of MainFlowFragment
    val navigator: AppNavigator by lazy {
        object : AppNavigator(activity!!, childFragmentManager, R.id.container) {
            override fun setupFragmentTransaction(
                command: Command,
                currentFragment: Fragment?,
                nextFragment: Fragment?,
                fragmentTransaction: FragmentTransaction
            ) {
                fragmentTransaction.setReorderingAllowed(true)

                this@FlowFragment.setupFragmentTransaction(
                    command,
                    currentFragment,
                    nextFragment!!,
                    fragmentTransaction
                )
            }

            override fun activityBack() {
                onExit()
            }
        }
    }

    override val contentView: Int = R.layout.layout_container

    internal fun getCurrentFragment() = childFragmentManager.findFragmentById(R.id.container) as? BaseFragment

    protected fun onExit() {
        router?.finishFlow()
    }

    @Suppress("UNUSED_PARAMETER")
    protected fun setupFragmentTransaction(
        command: Command,
        currentFragment: Fragment?,
        nextFragment: Fragment,
        fragmentTransaction: FragmentTransaction
    ) {
        if (command is Forward || command is Replace) {
            if (currentFragment != null) {
                currentFragment.exitTransition = Fade(Fade.OUT)
            }
            nextFragment.enterTransition = Slide(Gravity.END)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val flowCiceroneModel = ViewModelProviders.of(this).get(FlowCiceroneViewModel::class.java)
        navigatorHolder = flowCiceroneModel.getFlowCicerone(appRouter).navigatorHolder
    }

    override fun onBackPressed() {
        val currentFragment = getCurrentFragment()
        if (currentFragment != null) {
            currentFragment.onBackPressed()
        } else {
            router?.exit()
        }
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder!!.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder!!.removeNavigator()
        super.onPause()
    }
}
