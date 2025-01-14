package ru.calendar.core.tools.dimension

import ru.calendar.core.tools.ext.dp

data class DimensionRect(
    private val left: Int,
    private val top: Int,
    private val right: Int,
    private val bottom: Int
) {
    val valueLeft = left.dp
    val valueRight = right.dp
    val valueBottom = bottom.dp
    val valueTop = top.dp


    companion object {
        val R_0_0_0_0 = DimensionRect(
            left = 0,
            top = 0,
            right = 0,
            bottom = 0,
        )
        val R_24_20_24_20 = DimensionRect(
            left = 24,
            top = 20,
            right = 24,
            bottom = 20
        )
    }
}