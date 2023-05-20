package com.keiferstone.owlplayerstats.ui.screen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.keiferstone.owlplayerstats.model.Filter

@Composable
fun FilterScreen() {
    val selectedFilters = remember { mutableStateListOf<Filter>() }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)) {
        
    }
}