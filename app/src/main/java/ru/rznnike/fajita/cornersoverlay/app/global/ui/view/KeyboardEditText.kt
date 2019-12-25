package ru.rznnike.fajita.cornersoverlay.app.global.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.appcompat.widget.AppCompatEditText

class KeyboardEditText: AppCompatEditText {
    var onImeCallback: ((Int, KeyEvent?) -> Boolean)? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        return (onImeCallback?.invoke(keyCode, event) ?: false) || super.onKeyPreIme(keyCode, event)
    }
}
