package com.keiferstone.owlplayerstats.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.keiferstone.owlplayerstats.extension.findActivity

@Composable
inline fun <reified VM : ViewModel> hiltActivityViewModel(): VM {
    return hiltViewModel(LocalContext.current.findActivity())
}