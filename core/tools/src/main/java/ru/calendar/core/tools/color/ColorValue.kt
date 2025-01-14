package ru.calendar.core.tools.color

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import ru.calendar.core.res.R

sealed interface ColorValue {
    data class Res(@ColorRes val value: Int) : ColorValue
    data class Color(@ColorInt val value: Int) : ColorValue

    companion object {
        val transparent: ColorValue
            get() = Res(R.color.transparent)

        val primary30: ColorValue
            get() = Res(R.color.primary_30)

        val secondaryText: ColorValue
            get() = Res(R.color.secondaryText)

        val white: ColorValue
            get() = Res(R.color.white)
    }
}