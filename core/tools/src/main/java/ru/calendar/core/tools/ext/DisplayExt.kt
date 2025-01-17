package ru.calendar.core.tools.ext

import android.content.res.Resources
import android.util.TypedValue

val Float.dp: Float
    get() = (this * Resources.getSystem().displayMetrics.density)

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Float.spToPx: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        Resources.getSystem().displayMetrics
    ).toInt()

val screenHeight: Int
    get() = Resources.getSystem().displayMetrics.heightPixels

val screenWidth: Int
    get() = Resources.getSystem().displayMetrics.widthPixels

