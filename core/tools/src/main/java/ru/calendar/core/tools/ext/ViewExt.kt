package ru.calendar.core.tools.ext

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import ru.calendar.core.tools.color.ColorValue
import ru.calendar.core.tools.dimension.DimensionRect
import ru.calendar.core.tools.dimension.DimensionValue
import ru.calendar.core.tools.round.RoundOutlineProvider
import ru.calendar.core.tools.round.RoundValue
import ru.calendar.core.tools.size.SizeValue

fun View.makeRound(value: RoundValue) {
    outlineProvider = RoundOutlineProvider(value)
    clipToOutline = true
}

fun View.getDrawable(@DrawableRes id: Int) =
    ResourcesCompat.getDrawable(context.resources, id, context.theme)
fun View.getColor(@ColorRes id: Int) = context.getColorCompat(id)
fun View.getColorStateList(@ColorRes id: Int) = ContextCompat.getColorStateList(context, id)
fun View.getString(@StringRes id: Int) = ContextCompat.getString(context, id)
fun View.getFont(@FontRes id: Int) = context.getFont(id)
fun View.getColor(value: ColorValue?): Int {
    val colorInt = when (value) {
        is ColorValue.Color -> value.value
        is ColorValue.Res -> getColor(value.value)
        else -> Color.TRANSPARENT
    }
    return colorInt
}

fun View.setBackgroundView(value: ColorValue?) {
    setBackgroundColor(getColor(value))
}

fun View.applyPadding(
    dpRect: DimensionRect?
) {
    setPadding(
        dpRect?.valueLeft ?: 0,
        dpRect?.valueTop ?: 0,
        dpRect?.valueRight ?: 0,
        dpRect?.valueBottom ?: 0
    )
}

fun View.applyPadding(
    left: Int = this.paddingLeft,
    top: Int = this.paddingTop,
    right: Int = this.paddingRight,
    bottom: Int = this.paddingBottom
) {
    setPadding(left, top, right, bottom)
}

fun View.applyMargin(
    dpRect: DimensionRect?
) {
    applyMargin(
        left = dpRect?.valueLeft ?: 0,
        top = dpRect?.valueTop ?: 0,
        bottom = dpRect?.valueBottom ?: 0,
        right = dpRect?.valueRight ?: 0
    )
}

fun View.applyMargin(
    left: Int,
    top: Int,
    right: Int,
    bottom: Int
) {
    val lp = this.layoutParams
    if (lp !is ViewGroup.MarginLayoutParams) return

    val isNeedChange = lp.leftMargin != left ||
            lp.topMargin != top ||
            lp.rightMargin != right ||
            lp.bottomMargin != bottom

    if (isNeedChange) {
        lp.leftMargin = left
        lp.topMargin = top
        lp.rightMargin = right
        lp.bottomMargin = bottom
        this.layoutParams = lp
    }
}

fun View.setSizeValue(value: SizeValue) {
    val lp = layoutParams
    lp.width = value.width?.value ?: DimensionValue.WrapContent.value
    lp.height = value.height?.value ?: DimensionValue.WrapContent.value
    layoutParams = lp
}

fun View.setWithValue(value: DimensionValue) {
    setWidth(value.value)
}

fun View.setWidth(width: Int) {
    if (this.layoutParams != null && (this.width != width || this.layoutParams.width != width)) {
        val lp = layoutParams
        lp.width = width
        layoutParams = lp
    }
}

fun View.setHeightValue(value: DimensionValue) {
    setHeight(value.value)
}

fun View.setHeight(height: Int) {
    if (this.layoutParams != null && (this.height != height || this.layoutParams.height != height)) {
        val lp = layoutParams
        lp.height = height
        layoutParams = lp
    }
}

fun View.makeRipple(
    ripple: ColorValue,
    cornerRadius: DimensionValue.Dp = DimensionValue.Dp(0),
    shapeDrawable: Int = GradientDrawable.RECTANGLE,
) {
    val mask = GradientDrawable().apply {
        shape = shapeDrawable
        setCornerRadius(cornerRadius.value.toFloat())
        setColor(getColor(ColorValue.secondaryText))
    }

    foreground = RippleDrawable(
        ColorStateList.valueOf(getColor(ripple)),
        null,
        mask
    )
}