package ru.calendar.core.tools.dimension

import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import ru.calendar.core.tools.ext.dp
import ru.calendar.core.tools.ext.spToPx

sealed interface DimensionValue {

    val value: Int

    data class Px(val pxValue: Float) : DimensionValue {
        override val value: Int = pxValue.dp.toInt()
    }

    data class SpToPx(val spValue: Float): DimensionValue {
        override val value: Int = spValue.spToPx
    }

    data class Dp(val dpValue: Int) : DimensionValue {
        override val value: Int = dpValue.dp
    }

    data object WrapContent : DimensionValue {
        override val value: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    data object MatchParent : DimensionValue {
        override val value: Int = ViewGroup.LayoutParams.MATCH_PARENT
    }

    data object MatchConstraint : DimensionValue {
        override val value: Int = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
    }
}