package ru.rznnike.fajita.cornersoverlay.app.global.utils.dialog

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import ru.rznnike.fajita.cornersoverlay.R

fun Context.showAlertDialog(
    type: AlertDialogType,
    message: String,
    cancellable: Boolean = true,
    onCancelListener: (() -> Unit)? = null,
    actions: List<AlertDialogAction>
) {
    val layout = when (type) {
        AlertDialogType.ALERT_HORIZONTAL_2_OPTIONS_LEFT_ACCENT -> R.layout.dialog_horizontal_2_options_left_accent
        AlertDialogType.ALERT_HORIZONTAL_2_OPTIONS_RIGHT_ACCENT -> R.layout.dialog_horizontal_2_options_right_accent
        AlertDialogType.ALERT_VERTICAL_2_OPTIONS_TOP_ACCENT -> R.layout.dialog_vertical_2_options_top_accent
        AlertDialogType.ALERT_VERTICAL_1_OPTION_ACCENT -> R.layout.dialog_vertical_1_option_accent
        AlertDialogType.ALERT_VERTICAL_1_OPTION_NO_ACCENT -> R.layout.dialog_vertical_1_option_no_accent
    }
    val dialogView = View.inflate(this, layout, null)

    val alertDialogBuilder = AlertDialog.Builder(this, R.style.AppTheme_Dialog_Alert)
    alertDialogBuilder.setView(dialogView)
    alertDialogBuilder.setCancelable(cancellable)
    alertDialogBuilder.setOnCancelListener {
        onCancelListener?.invoke()
    }
    val alertDialog = alertDialogBuilder.create()

    val textViewDialogMessage = dialogView.findViewById<AppCompatTextView>(R.id.textViewDialogMessage)
    textViewDialogMessage.text = message

    val buttonIds = listOf(
        R.id.buttonDialogFirstAction,
        R.id.buttonDialogSecondAction
    )
    for (i in (0..type.optionsCount)) {
        if ((actions.size - 1) >= i) {
            val button = dialogView.findViewById<AppCompatTextView>(buttonIds[i])
            button.text = actions[i].text
            button.setOnClickListener {
                actions[i].callback.invoke(alertDialog)
            }
        }
    }

    alertDialog.show()
}
