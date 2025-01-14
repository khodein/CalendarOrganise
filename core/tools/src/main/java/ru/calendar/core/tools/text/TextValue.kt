package ru.calendar.core.tools.text

import android.view.Gravity
import androidx.annotation.GravityInt

data class TextValue(
    val value: CharSequence,
    val style: TextStyleValue? = null,
    @GravityInt val gravity: Int = Gravity.START,
)