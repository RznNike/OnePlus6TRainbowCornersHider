package ru.rznnike.fajita.cornersoverlay.app.global.ui.activity

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.koin.android.ext.android.inject
import ru.rznnike.fajita.cornersoverlay.app.global.ui.fragment.BaseFragment
import ru.rznnike.fajita.cornersoverlay.data.preference.Preferences


abstract class BaseActivity : MvpAppCompatActivity() {
    private val preferences: Preferences by inject()

    private val disposables = CompositeDisposable()

    private fun getCurrentFragment(): BaseFragment? {
        return supportFragmentManager.findFragmentById(ru.rznnike.fajita.cornersoverlay.R.id.container) as? BaseFragment
    }

    protected fun Disposable.unsubscribeOnDestroy() {
        disposables.add(this)
    }

    public override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    override fun onBackPressed() {
        val currentFragment = getCurrentFragment()
        currentFragment?.onBackPressed() ?: super.onBackPressed()
    }
}
