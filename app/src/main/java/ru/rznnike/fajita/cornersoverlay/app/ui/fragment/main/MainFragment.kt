package ru.rznnike.fajita.cornersoverlay.app.ui.fragment.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.os.bundleOf
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.ext.android.inject
import ru.rznnike.fajita.cornersoverlay.BuildConfig
import ru.rznnike.fajita.cornersoverlay.R
import ru.rznnike.fajita.cornersoverlay.app.global.notifier.Notifier
import ru.rznnike.fajita.cornersoverlay.app.global.ui.TopBottomWindowInsetsListener
import ru.rznnike.fajita.cornersoverlay.app.global.ui.fragment.BaseFragment
import ru.rznnike.fajita.cornersoverlay.app.presentation.main.MainPresenter
import ru.rznnike.fajita.cornersoverlay.app.presentation.main.MainView
import ru.rznnike.fajita.cornersoverlay.device.service.OverlayService
import java.util.*

class MainFragment : BaseFragment(), MainView {
    @InjectPresenter
    lateinit var presenter: MainPresenter
    private val windowInsetsListener: TopBottomWindowInsetsListener by inject()
    private val notifier: Notifier by inject()

    override val contentView: Int = R.layout.fragment_main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        content.setOnApplyWindowInsetsListener(windowInsetsListener)
        content.requestApplyInsets()
        initOnClickListeners()
        initVersionView()
    }

    private fun initOnClickListeners() {
        switchEnableOverlay.setOnClickListener {
            presenter.onSwitchEnableOverlay(switchEnableOverlay.isChecked)
        }
    }

    private fun initVersionView() {
        @Suppress("ConstantConditionIf") val buildType =
            if (BuildConfig.BUILD_TYPE == "release") "" else BuildConfig.BUILD_TYPE

        textViewVersion.text = String.format(
            Locale.getDefault(), "%s %s(%d)%s",
            getString(R.string.version), BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE, buildType
        )
    }

    override fun populateData(overlayEnabled: Boolean) {
        switchEnableOverlay.isChecked = overlayEnabled

        if (overlayEnabled) {
            checkOverlayPermission()
        } else {
            sendCommandToOverlayService(false)
        }
    }

    private fun checkOverlayPermission() {
        if (Settings.canDrawOverlays(requireContext())) {
            sendCommandToOverlayService(true)
        } else {
            requestOverlayPermission()
        }
    }

    private fun requestOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:" + requireContext().packageName)
        )
        startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            if (Settings.canDrawOverlays(requireContext())) {
                presenter.onSwitchEnableOverlay(true)
            } else {
                presenter.onSwitchEnableOverlay(false)
                view?.post {
                    notifier.sendActionMessage(R.string.overlay_permission_denied_message, R.string.settings) {
                        requestOverlayPermission()
                    }
                }
            }
        }
    }

    private fun sendCommandToOverlayService(overlayEnabled: Boolean) {
        val intent = Intent(requireContext(), OverlayService::class.java)
        intent.putExtra(OverlayService.PARAM_ENABLE_OVERLAY, overlayEnabled)
        requireContext().startService(intent)
    }

    companion object {
        private const val REQUEST_OVERLAY_PERMISSION = 42
        fun newInstance(): MainFragment {
            val fragment = MainFragment()
            fragment.arguments = bundleOf()
            return fragment
        }
    }
}
