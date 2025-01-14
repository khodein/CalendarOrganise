package ru.calendar.core.tools.ext

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import ru.calendar.core.tools.dimension.DimensionValue
import ru.calendar.core.tools.color.ColorValue
import ru.calendar.core.tools.image.ImageValue
import ru.calendar.core.tools.round.RoundModeEntity
import ru.calendar.core.tools.round.RoundValue

fun ImageView.load(value: ImageValue?) {
    bindImageOptional(value)
}

fun ImageView.setTint(colorValue: ColorValue?) {
    imageTintList = colorValue?.let {
        ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_enabled)),
            intArrayOf(getColor(colorValue))
        )
    }
}

fun ImageView.setTint(@ColorInt colorInt: Int) {
    imageTintList = ColorStateList(
        arrayOf(intArrayOf(android.R.attr.state_enabled)),
        intArrayOf(colorInt)
    )
}


private fun ImageView.bindImageOptional(value: ImageValue?) {
    isVisible = value?.let {
        setImageResource(value.value)
        setTint(value.tint)
        scaleType = value.scaleType
        makeRound(
            value.roundValue ?: RoundValue(
                mode = RoundModeEntity.NONE,
                radius = DimensionValue.Dp(0)
            )
        )
        true
    } ?: false
}