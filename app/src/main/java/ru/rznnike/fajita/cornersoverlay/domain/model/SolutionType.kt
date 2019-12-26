package ru.rznnike.fajita.cornersoverlay.domain.model

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.android.parcel.Parcelize
import ru.rznnike.fajita.cornersoverlay.R

@Parcelize
enum class SolutionType(@StringRes val nameResId: Int) : Parcelable {
    APPLICATION(R.string.solution_application),
    ACCESSIBILITY(R.string.solution_accessibility)
}