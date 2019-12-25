package ru.rznnike.fajita.cornersoverlay.app.global.ui.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.arellomobile.mvp.MvpDelegate

abstract class MvpAppCompatActivity : FragmentActivity() {
    private var mvpDelegate: MvpDelegate<out MvpAppCompatActivity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getMvpDelegate().onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        getMvpDelegate().onAttach()
    }

    override fun onResume() {
        super.onResume()
        getMvpDelegate().onAttach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        getMvpDelegate().onSaveInstanceState(outState)
        getMvpDelegate().onDetach()
    }

    override fun onStop() {
        super.onStop()
        getMvpDelegate().onDetach()
    }

    override fun onDestroy() {
        super.onDestroy()
        getMvpDelegate().onDestroyView()
        if (isFinishing) {
            getMvpDelegate().onDestroy()
        }
    }

    /**
     * @return The [MvpDelegate] being used by this Activity.
     */
    fun getMvpDelegate(): MvpDelegate<*> {
        if (mvpDelegate == null) {
            mvpDelegate = MvpDelegate(this)
        }
        return mvpDelegate as MvpDelegate<out MvpAppCompatActivity>
    }
}
