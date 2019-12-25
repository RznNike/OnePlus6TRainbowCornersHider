package ru.rznnike.fajita.cornersoverlay.app.global.utils.dialog

import androidx.appcompat.app.AlertDialog

data class AlertDialogAction(
    val text: String,
    val callback: (dialog: AlertDialog) -> Unit
)
