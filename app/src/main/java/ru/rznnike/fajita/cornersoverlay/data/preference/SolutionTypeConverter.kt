package ru.rznnike.fajita.cornersoverlay.data.preference

import com.f2prateek.rx.preferences2.Preference
import ru.rznnike.fajita.cornersoverlay.domain.model.SolutionType

class SolutionTypeConverter : Preference.Converter<SolutionType> {
    override fun deserialize(serialized: String): SolutionType {
        return SolutionType.valueOf(serialized)
    }

    override fun serialize(value: SolutionType): String {
        return value.toString()
    }
}