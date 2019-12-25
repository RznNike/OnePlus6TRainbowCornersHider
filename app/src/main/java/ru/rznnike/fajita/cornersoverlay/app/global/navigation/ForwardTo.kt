package ru.rznnike.fajita.cornersoverlay.app.global.navigation

import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.commands.Command

/**
 * Opens new screen if screen not exists on stack top
 */
class ForwardTo(val screen: Screen) : Command
