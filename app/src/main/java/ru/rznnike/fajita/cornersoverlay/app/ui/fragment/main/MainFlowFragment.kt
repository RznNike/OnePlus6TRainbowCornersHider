package ru.rznnike.fajita.cornersoverlay.app.ui.fragment.main

import android.os.Bundle
import androidx.core.os.bundleOf
import ru.rznnike.fajita.cornersoverlay.app.Screens
import ru.rznnike.fajita.cornersoverlay.app.global.ui.fragment.FlowFragment

class MainFlowFragment : FlowFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (childFragmentManager.fragments.isEmpty()) {
            navigator.setLaunchScreen(Screens.MainScreen())
        }
    }

    companion object {
        fun newInstance(): MainFlowFragment {
            val fragment = MainFlowFragment()
            fragment.arguments = bundleOf()
            return fragment
        }
    }
}
