package com.keiferstone.owlplayerstats.ui.shape

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection


class RightSlantRectangle(private val tilt: Dp = Dp(30f)) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path().apply {
            lineTo(size.width, 0f)
            lineTo(size.width - tilt.value, size.height)
            lineTo(0f, size.height)
            close()
        }
        return Outline.Generic(path)
    }
}