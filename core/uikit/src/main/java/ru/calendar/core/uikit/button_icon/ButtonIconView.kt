package ru.calendar.core.uikit.button_icon

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import ru.calendar.core.recycler.RecyclerItemView
import ru.calendar.core.tools.dimension.DimensionValue
import ru.calendar.core.tools.ext.applyPadding
import ru.calendar.core.tools.ext.makeRipple
import ru.calendar.core.tools.ext.setBackgroundView
import ru.calendar.core.tools.size.SizeValue
import ru.calendar.core.uikit.databinding.ViewButtonIconBinding
import ru.calendar.core.uikit.image.ImageItem

class ButtonIconView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), RecyclerItemView<ButtonIconItem.State> {

    private val binding = ViewButtonIconBinding.inflate(LayoutInflater.from(context), this)
    private var onClick: (() -> Unit)? = null

    init {
        layoutParams = LayoutParams(
            WRAP_CONTENT,
            WRAP_CONTENT,
        )

        binding.buttonIconContentContainer.setOnClickListener {
            onClick?.invoke()
        }
    }

    override fun bindState(state: ButtonIconItem.State) {
        this.onClick = state.onClick
        applyPadding(state.paddings)
        binding.buttonIconContentContainer.makeRipple(
            ripple = state.ripple,
            shapeDrawable = GradientDrawable.OVAL,
        )
        binding.buttonIconContentContainer.setBackgroundView(state.background)
        binding.buttonIconContent.bindState(
            ImageItem.State(
                id = state.id,
                value = state.iconValue,
                sizeValue = SizeValue(
                    width = DimensionValue.Dp(20),
                    height = DimensionValue.Dp(20)
                )
            )
        )
    }
}