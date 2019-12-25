package ru.rznnike.fajita.cornersoverlay.app.global.utils

import android.view.View

fun View?.setVisible() {
    this?.let {
        it.visibility = View.VISIBLE
    }
}

fun View?.setInvisible() {
    this?.let {
        it.visibility = View.INVISIBLE
    }
}

fun View?.setGone() {
    this?.let {
        it.visibility = View.GONE
    }
}
