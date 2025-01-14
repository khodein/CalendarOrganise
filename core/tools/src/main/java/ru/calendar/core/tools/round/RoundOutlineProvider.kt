package ru.calendar.core.tools.round

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

class RoundOutlineProvider(
    private val value: RoundValue
) : ViewOutlineProvider() {

    private val radiusValue: Int
        get() = value.radius.value

    private val topOffset: Int
        get() = when (value.mode) {
            RoundModeEntity.BOTTOM,
            RoundModeEntity.NONE -> radiusValue

            RoundModeEntity.ALL,
            RoundModeEntity.TOP -> 0
        }

    private val bottomOffset: Int
        get() = when (value.mode) {
            RoundModeEntity.BOTTOM,
            RoundModeEntity.ALL -> 0

            RoundModeEntity.TOP,
            RoundModeEntity.NONE -> radiusValue
        }

    override fun getOutline(view: View, outline: Outline) {
        outline.setRoundRect(
            0,
            0 - topOffset,
            view.width,
            view.height + bottomOffset,
            radiusValue.toFloat()
        )
    }
}