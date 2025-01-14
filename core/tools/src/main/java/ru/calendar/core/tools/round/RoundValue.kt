package ru.calendar.core.tools.round

import ru.calendar.core.tools.dimension.DimensionValue

data class RoundValue(
    val mode: RoundModeEntity,
    val radius: DimensionValue.Dp,
)