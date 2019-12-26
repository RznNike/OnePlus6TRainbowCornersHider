package ru.rznnike.fajita.cornersoverlay.domain.model

import androidx.annotation.StringRes
import ru.rznnike.fajita.cornersoverlay.R

enum class SolutionType(@StringRes val nameResId: Int) {
    APPLICATION(R.string.solution_application),
    SYSTEM(R.string.solution_system),
    ACCESSIBILITY(R.string.solution_accessibility)
}