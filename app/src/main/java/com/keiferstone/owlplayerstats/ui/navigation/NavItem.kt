package com.keiferstone.owlplayerstats.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class NavItem(
    @StringRes
    val nameResId: Int,
    @DrawableRes
    val iconResId: Int,
    val route: String)