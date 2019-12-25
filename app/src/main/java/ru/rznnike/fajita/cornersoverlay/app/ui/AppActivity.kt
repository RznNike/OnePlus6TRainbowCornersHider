package ru.rznnike.fajita.cornersoverlay.app.ui

import android.app.ActivityManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.transition.Fade
import com.andrognito.flashbar.Flashbar
import com.arellomobile.mvp.presenter.InjectPresenter
import io.reactivex.disposables.Disposable
import org.koin.android.ext.android.inject
import ru.rznnike.fajita.cornersoverlay.R
import ru.rznnike.fajita.cornersoverlay.app.Screens
import ru.rznnike.fajita.cornersoverlay.app.global.navigation.AppNavigator
import ru.rznnike.fajita.cornersoverlay.app.global.navigation.AppRouter
import ru.rznnike.fajita.cornersoverlay.app.global.notifier.Notifier
import ru.rznnike.fajita.cornersoverlay.app.global.notifier.SystemMessage
import ru.rznnike.fajita.cornersoverlay.app.global.ui.activity.BaseActivity
import ru.rznnike.fajita.cornersoverlay.app.global.ui.fragment.FlowFragment
import ru.rznnike.fajita.cornersoverlay.app.global.ui.fragment.SystemDialogFragment
import ru.rznnike.fajita.cornersoverlay.app.presentation.AppPresenter
import ru.rznnike.fajita.cornersoverlay.app.presentation.AppView
import ru.rznnike.fajita.cornersoverlay.domain.global.SchedulersProvider
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.cicerone.commands.Replace

class AppActivity : BaseActivity(), AppView {
    @InjectPresenter
    lateinit var presenter: AppPresenter

    private val navigatorHolder: NavigatorHolder by inject()
    private val notifier: Notifier by inject()
    private val schedulersProvider: SchedulersProvider by inject()
    private val router: AppRouter by inject()

    private var notifierDisposable: Disposable? = null

    private val navigator = object : AppNavigator(this, supportFragmentManager, R.id.container) {
        private var doubleBackToExitPressedOnce: Boolean = false

        override fun setupFragmentTransaction(
            command: Command,
            currentFragment: Fragment?,
            nextFragment: Fragment?,
            fragmentTransaction: FragmentTransaction
        ) {
            //fix incorrect order lifecycle callback of MainFlowFragment
            fragmentTransaction.setReorderingAllowed(true)

            if (command is Forward || command is Replace) {
                currentFragment?.exitTransition = Fade()
                nextFragment?.enterTransition = Fade()
            }
        }

        override fun activityBack() {
            if (doubleBackToExitPressedOnce) {
                super.activityBack()
                return
            }
            doubleBackToExitPressedOnce = true
            notifier.sendMessage(R.string.double_back_to_exit)
            val exitDuration = resources.getInteger(R.integer.exit_duration)
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, exitDuration.toLong())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity)
        initWindowFlags()
        updateTaskDescription()

        if (savedInstanceState == null) {
            router.newRootFlow(Screens.MainFlow())
        } else {
            window.setBackgroundDrawableResource(R.color.colorBackground)
        }
    }

    private fun initWindowFlags() {
        var flags = window.decorView.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        flags = flags or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.decorView.systemUiVisibility = flags
    }

    @Suppress("DEPRECATION")
    private fun updateTaskDescription() {
        val taskDesc = if (Build.VERSION.SDK_INT >= 28)
            ActivityManager.TaskDescription(
                resources.getString(R.string.app_name),
                R.mipmap.ic_launcher,
                ContextCompat.getColor(this, R.color.colorTextWhite)
            )
        else
            ActivityManager.TaskDescription(
                resources.getString(R.string.app_name),
                BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher),
                ContextCompat.getColor(this, R.color.colorTextWhite)
            )

        setTaskDescription(taskDesc)
    }

    override fun onStart() {
        super.onStart()
        subscribeOnSystemMessages()
    }

    private fun subscribeOnSystemMessages() {
        notifierDisposable = notifier.notifier
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .subscribe(this::onNextMessageNotify)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    private fun onNextMessageNotify(systemMessage: SystemMessage) {
        val text = if (systemMessage.textRes == null) {
            systemMessage.text ?: return
        } else {
            getString(systemMessage.textRes)
        }
        val actionText = if (systemMessage.actionTextRes == null) {
            systemMessage.actionText ?: ""
        } else {
            getString(systemMessage.actionTextRes)
        }

        when(systemMessage.type) {
            SystemMessage.Type.BAR -> {
                showBarMessage(text, systemMessage.level)
            }
            SystemMessage.Type.ALERT -> {
                showAlertMessage(text)
            }
            SystemMessage.Type.ACTION -> {
                showActionMessage(
                    text,
                    actionText,
                    systemMessage.actionCallback,
                    systemMessage.level
                )
            }
        }
    }

    private fun showBarMessage(
        text: String,
        level: SystemMessage.Level
    ) {
        val textColor = if (level == SystemMessage.Level.ERROR) R.color.colorRed else R.color.colorBlack

        val flashbarBuilder = Flashbar.Builder(this)
            .gravity(Flashbar.Gravity.TOP)
            .title(text)
            .backgroundColorRes(R.color.colorBackgroundSnackbar)
            .titleColorRes(textColor)
            .messageColorRes(textColor)
            .castShadow(true, FLASHBAR_SHADOW_STRENGTH)
            .duration(FLASHBAR_DURATION_MS)
            .enableSwipeToDismiss()

        flashbarBuilder.show()
    }

    private fun showAlertMessage(text: String) {
        val params = SystemDialogFragment.Params(message = text)
        SystemDialogFragment.newInstance(params).show(supportFragmentManager, null)
    }

    private fun showActionMessage(
        message: String,
        action: String?,
        actionCallback: (() -> Unit?)?,
        level: SystemMessage.Level
    ) {
        val textColor = if (level == SystemMessage.Level.ERROR) R.color.colorRed else R.color.colorBlack

        val flashbarBuilder = Flashbar.Builder(this)
            .gravity(Flashbar.Gravity.TOP)
            .title(message)
            .backgroundColorRes(R.color.colorBackgroundSnackbar)
            .titleColorRes(textColor)
            .messageColorRes(textColor)
            .castShadow(true, FLASHBAR_SHADOW_STRENGTH)
            .duration(FLASHBAR_DURATION_MS)
            .enableSwipeToDismiss()
        if (!TextUtils.isEmpty(action) && actionCallback != null) {
            flashbarBuilder
                .primaryActionText(action ?: "")
                .primaryActionTextColorRes(textColor)
                .primaryActionTapListener(object : Flashbar.OnActionTapListener {
                    override fun onActionTapped(bar: Flashbar) {
                        actionCallback.invoke()
                        bar.dismiss()
                    }
                })
                .primaryActionTextColor(R.color.colorAccent)
        }

        flashbarBuilder.show()
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onStop() {
        unsubscribeOnSystemMessages()
        super.onStop()
    }

    private fun unsubscribeOnSystemMessages() {
        notifierDisposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (currentFocus != null) {
            currentFocus!!.clearFocus()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = supportFragmentManager.findFragmentById(R.id.container)

        if (fragment is FlowFragment) {
            val currentFragment = fragment.getCurrentFragment()
            currentFragment?.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        const val PARAM_TYPE = "type"
        const val PARAM_ID = "id"
        const val FLASHBAR_DURATION_MS = 3000L
        const val FLASHBAR_SHADOW_STRENGTH = 2
    }
}
