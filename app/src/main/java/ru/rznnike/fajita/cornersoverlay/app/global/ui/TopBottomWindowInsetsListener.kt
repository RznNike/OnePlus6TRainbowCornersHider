package ru.rznnike.fajita.cornersoverlay.app.global.ui

import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import ru.rznnike.fajita.cornersoverlay.R

class TopBottomWindowInsetsListener : View.OnApplyWindowInsetsListener {
    internal var topMargin = true
    internal var bottomMargin = true
    private val topOffsetPx = 0
    private val bottomOffsetPx = 0

    override fun onApplyWindowInsets(view: View, windowInsets: WindowInsets): WindowInsets {
        val params = view.layoutParams as ViewGroup.MarginLayoutParams
        if (topMargin) {
            params.topMargin = topOffsetPx + windowInsets.systemWindowInsetTop
        }
        if (bottomMargin) {
            val bottomNavigationHeight = view.resources.getDimension(R.dimen.bottom_navigation_height) +
                    view.resources.getDimension(R.dimen._1dp)
            val bottomMargin = bottomOffsetPx + windowInsets.systemWindowInsetBottom - bottomNavigationHeight.toInt()
            params.bottomMargin = if (bottomMargin < 0) 0 else bottomMargin

        }
        view.layoutParams = params
        view.parent.requestLayout()
        return windowInsets
    }
}
