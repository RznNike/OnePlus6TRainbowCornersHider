package ru.rznnike.fajita.cornersoverlay.app.global.utils.dialog

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mikepenz.fastadapter.ClickListener
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.IItem
import com.mikepenz.fastadapter.adapters.ItemAdapter
import ru.rznnike.fajita.cornersoverlay.R
import ru.rznnike.fajita.cornersoverlay.app.global.ui.EmptyDividerDecoration
import ru.rznnike.fajita.cornersoverlay.app.global.utils.createFastAdapter
import ru.rznnike.fajita.cornersoverlay.app.ui.item.BottomDialogButtonItem

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

fun Context.showBottomDialog(
    cancellable: Boolean = true,
    onCancelListener: (() -> Unit)? = null,
    actions: List<BottomDialogAction>
) {
    val bottomSheetDialog = BottomSheetDialog(this, R.style.CustomBottomSheetDialog)
    val rootView = View.inflate(this, R.layout.bottom_dialog, null)

    val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerViewDialog)
    val itemAdapter: ItemAdapter<BottomDialogButtonItem> = ItemAdapter()
    val adapterActions: FastAdapter<IItem<*>> = createFastAdapter(itemAdapter)
    adapterActions.setHasStableIds(true)
    adapterActions.onClickListener = object : ClickListener<IItem<*>> {
        override fun invoke(v: View?, adapter: IAdapter<IItem<*>>, item: IItem<*>, position: Int): Boolean {
            return if (item is BottomDialogButtonItem) {
                item.bottomDialogAction.callback.invoke(bottomSheetDialog)
                true
            } else {
                false
            }
        }
    }
    recyclerView.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = adapterActions
        itemAnimator = null
        addItemDecoration(EmptyDividerDecoration(this@showBottomDialog, R.dimen.baseline_grid_medium, false))
    }
    itemAdapter.setNewList(actions.map { BottomDialogButtonItem(it) })

    rootView.setOnClickListener { bottomSheetDialog.cancel() }

    bottomSheetDialog.setContentView(rootView)

    val behavior = BottomSheetBehavior.from(rootView.parent as View)
    behavior.state = BottomSheetBehavior.STATE_EXPANDED
    behavior.skipCollapsed = true
    behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(view: View, state: Int) {
            if (state == BottomSheetBehavior.STATE_HIDDEN) {
                bottomSheetDialog.cancel()
            }
        }

        override fun onSlide(view: View, v: Float) {}
    })

    bottomSheetDialog.setCancelable(cancellable)
    bottomSheetDialog.setOnCancelListener {
        onCancelListener?.invoke()
    }

    bottomSheetDialog.show()
}
