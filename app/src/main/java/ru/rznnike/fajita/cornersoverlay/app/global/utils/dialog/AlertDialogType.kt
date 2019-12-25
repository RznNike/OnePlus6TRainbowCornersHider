package ru.rznnike.fajita.cornersoverlay.app.global.utils.dialog

enum class AlertDialogType(
    val optionsCount: Int
) {
    ALERT_HORIZONTAL_2_OPTIONS_LEFT_ACCENT(2),
    ALERT_HORIZONTAL_2_OPTIONS_RIGHT_ACCENT(2),
    ALERT_VERTICAL_2_OPTIONS_TOP_ACCENT(2),
    ALERT_VERTICAL_1_OPTION_ACCENT(1),
    ALERT_VERTICAL_1_OPTION_NO_ACCENT(1)
}
