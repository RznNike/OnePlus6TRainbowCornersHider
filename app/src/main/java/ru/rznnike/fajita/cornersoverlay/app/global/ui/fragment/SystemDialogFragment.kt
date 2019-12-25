package ru.rznnike.fajita.cornersoverlay.app.global.ui.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import kotlinx.android.parcel.Parcelize
import ru.rznnike.fajita.cornersoverlay.R

class SystemDialogFragment : DialogFragment() {

    private var params: Params? = null
    private var clickListener: OnClickListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initClickListener()
        params = arguments?.getParcelable(ARG_PARAMS) as Params?
        return createDialog()
    }

    private fun createDialog(): AlertDialog {
        val positiveText = if (TextUtils.isEmpty(params?.positiveText))
            getString(android.R.string.ok)
        else
            params?.positiveText
        
        var builder: AlertDialog.Builder = AlertDialog.Builder(context!!, R.style.AppTheme_Dialog_Alert)
            .setTitle(params?.title)
            .setMessage(params?.message)
            .setPositiveButton(positiveText) { _, _ ->
                dismissAllowingStateLoss()
                clickListener?.dialogPositiveClicked(params?.tag, params?.data)
            }
        if (!TextUtils.isEmpty(params?.negativeText)) {
            builder = builder.setNegativeButton(params?.negativeText) { _, _ ->
                dismissAllowingStateLoss()
                clickListener?.dialogNegativeClicked(params?.tag, params?.data)
            }
        }

        return builder.create()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        clickListener?.dialogCanceled(params?.tag)
    }

    private fun initClickListener() = when {
        parentFragment is OnClickListener -> clickListener = parentFragment as OnClickListener?
        activity is OnClickListener -> clickListener = activity as OnClickListener?
        else -> clickListener = object : OnClickListener {
        }
    }

    interface OnClickListener {
        fun dialogPositiveClicked(tag: String?, data: Parcelable?) {}

        fun dialogNegativeClicked(tag: String?, data: Parcelable?) {}

        fun dialogCanceled(tag: String?) {}
    }

    @Parcelize
    data class Params (
        val tag: String? = null,
        val title: String? = null,
        val message: String? = null,
        val positiveText: String? = null,
        val negativeText: String? = null,
        val data: Parcelable? = null) : Parcelable

    companion object {
        private const val ARG_PARAMS = "arg_params"

        fun newInstance(params: Params): SystemDialogFragment {
            val fragment = SystemDialogFragment()
            fragment.arguments = bundleOf(ARG_PARAMS to params)
            return fragment
        }
    }
}
