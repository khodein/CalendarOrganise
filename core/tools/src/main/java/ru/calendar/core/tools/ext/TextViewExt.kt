package ru.calendar.core.tools.ext

import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import ru.calendar.core.tools.color.ColorValue
import ru.calendar.core.tools.text.FontValue
import ru.calendar.core.tools.text.TextStyleValue
import ru.calendar.core.tools.text.TextValue

fun TextView.load(value: TextValue?) {
    bindTextOptional(value)
}

fun TextView.setTextStyle(value: TextStyleValue) {
    when (value) {
        is TextStyleValue.Res -> setAppearance(value.value)
        is TextStyleValue.Custom -> {
            setTextColorValue(value.color)
            setFontStyle(value.fontStyle)
            setTextSizeSp(value.size)
        }
    }
}

fun TextView.setAppearance(@StyleRes textAppearance: Int) =
    TextViewCompat.setTextAppearance(this, textAppearance)

fun TextView.setFontStyle(fontStyle: FontValue) {
    typeface = getFont(fontStyle.fontResId)
}

fun TextView.setTextColorValue(value: ColorValue) {
    val colorInt = when (value) {
        is ColorValue.Color -> value.value
        is ColorValue.Res -> getColor(value.value)
    }
    setTextColor(colorInt)
}

fun TextView.setTextSizeSp(size: Float) {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
}

private fun TextView.bindTextOptional(value: TextValue?) {
    isVisible = if (value != null) {
        text = value.value
        value.style?.let(::setTextStyle)
        gravity = value.gravity
        true
    } else {
        false
    }
}