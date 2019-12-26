package ru.rznnike.fajita.cornersoverlay.app.global.utils

import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.IItem

fun createFastAdapter(vararg adapter: IAdapter<*>) =
    FastAdapter.with<IItem<*>, IAdapter<out IItem<*>>>(adapter.toList())
