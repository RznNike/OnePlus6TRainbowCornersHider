package ru.rznnike.fajita.cornersoverlay.app.ui.item

import android.graphics.Typeface
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import ru.rznnike.fajita.cornersoverlay.R
import ru.rznnike.fajita.cornersoverlay.app.global.utils.dialog.BottomDialogAction

class BottomDialogButtonItem(
    val bottomDialogAction: BottomDialogAction
) : AbstractItem<BottomDialogButtonItem.ViewHolder>() {
    override val type: Int = R.id.bottomDialogButtonItem

    override val layoutRes: Int = R.layout.item_bottom_dialog_button

    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    class ViewHolder(view: View) : FastAdapter.ViewHolder<BottomDialogButtonItem>(view) {
        private val textViewName: AppCompatTextView = view.findViewById(R.id.textViewName)

        override fun bindView(item: BottomDialogButtonItem, payloads: MutableList<Any>) {
            textViewName.text = item.bottomDialogAction.text
            if (item.bottomDialogAction.selected == true) {
                textViewName.setTypeface(textViewName.typeface, Typeface.BOLD)
            } else {
                textViewName.setTypeface(textViewName.typeface, Typeface.NORMAL)
            }
        }

        override fun unbindView(item: BottomDialogButtonItem) { }
    }
}