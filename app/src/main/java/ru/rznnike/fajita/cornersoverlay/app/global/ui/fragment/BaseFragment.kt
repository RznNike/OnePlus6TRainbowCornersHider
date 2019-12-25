package ru.rznnike.fajita.cornersoverlay.app.global.ui.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ru.rznnike.fajita.cornersoverlay.app.global.navigation.AppRouter
import ru.rznnike.fajita.cornersoverlay.app.global.navigation.AppScreen
import ru.rznnike.fajita.cornersoverlay.app.global.navigation.FlowCiceroneViewModel
import ru.rznnike.fajita.cornersoverlay.app.global.navigation.FlowRouter
import org.koin.android.ext.android.inject

abstract class BaseFragment : MvpAppCompatFragment() {
    private val disposables = CompositeDisposable()
    open var isLightStatusBar: Boolean = true
        protected set

    val appRouter: AppRouter by inject()

    protected val router: FlowRouter?
        get() {
            val flowParent = getParent(this) ?: return null

            return ViewModelProviders.of(flowParent)
                .get(FlowCiceroneViewModel::class.java)
                .getFlowCicerone(appRouter).router
        }

    private fun getParent(fragment: Fragment): FlowFragment? {
        return when {
            fragment is FlowFragment -> fragment
            fragment.parentFragment == null -> null
            else -> getParent(fragment.parentFragment!!)
        }
    }

    @get:LayoutRes
    protected abstract val contentView: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(contentView, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    protected fun unsubscribeOnDestroy(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }


    open fun onBackPressed() {
        router?.exit()
    }

    override fun onResume() {
        super.onResume()
        initStatusBar()
    }

    private fun initStatusBar() {
        if (this is FlowFragment || (childFragmentManager.fragments.isNotEmpty())) {
            return
        }
        applyStatusBarMode()
    }

    fun setStatusBarMode(isLightStatusBar: Boolean) {
        this.isLightStatusBar = isLightStatusBar
        applyStatusBarMode()
    }

    private fun applyStatusBarMode() {
        val darkMode = isLightStatusBar

        miUISetStatusBarLightMode(darkMode)
        flymeSetStatusBarLightMode(darkMode)
        setDefaultStatusBarLightMode(darkMode)
    }

    private fun setDefaultStatusBarLightMode(darkMode: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = activity!!.window.decorView.systemUiVisibility

            flags = if (darkMode) {
                flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }

            activity!!.window.decorView.systemUiVisibility = flags
        }
    }

    @SuppressLint("PrivateApi")
    private fun miUISetStatusBarLightMode(darkMode: Boolean): Boolean {
        var result = false
        val window = activity!!.window
        if (window != null) {
            val clazz = window.javaClass
            try {
                val darkModeFlag: Int
                val layoutParams =
                    Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod(
                    "setExtraFlags",
                    Int::class.javaPrimitiveType, Int::class.javaPrimitiveType
                )
                extraFlagField.invoke(window, if (darkMode) darkModeFlag else 0, darkModeFlag)
                result = true
            } catch (ignored: Exception) {
            }

        }
        return result
    }

    private fun flymeSetStatusBarLightMode(light: Boolean): Boolean {
        val window = activity!!.window
        var result = false
        if (window != null) {
            try {
                val lp = window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                    .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)

                value = if (light)
                    value or bit
                else
                    value and bit.inv()

                meizuFlags.setInt(lp, value)
                window.attributes = lp
                result = true
            } catch (ignored: Exception) {
            }

        }
        return result
    }

    fun routerExit() {
        router?.exit()
    }

    fun routerStartFlow(flow: AppScreen) {
        router?.startFlow(flow)
    }

    fun routerReplaceFlow(flow: AppScreen) {
        router?.replaceFlow(flow)
    }

    fun routerNewRootFlow(flow: AppScreen) {
        router?.newRootFlow(flow)
    }

    fun routerNewRootFlowChain(vararg flows: AppScreen) {
        router?.newRootFlowChain(*flows)
    }

    fun routerFinishFlow() {
        router?.finishFlow()
    }

    fun routerBackTo(flow: AppScreen?) {
        router?.backTo(flow)
    }
}
