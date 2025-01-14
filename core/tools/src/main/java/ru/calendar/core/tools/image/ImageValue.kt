package ru.calendar.core.tools.image

import android.widget.ImageView
import androidx.annotation.DrawableRes
import ru.calendar.core.tools.color.ColorValue
import ru.calendar.core.tools.round.RoundValue

data class ImageValue(
    @DrawableRes val value: Int,
    val tint: ColorValue? = null,
    val roundValue: RoundValue? = null,
    val scaleType: ImageView.ScaleType = ImageView.ScaleType.FIT_CENTER,
)