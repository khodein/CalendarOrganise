package ru.calendar.feature.calendar.ui.scheduler.base

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.Scroller
import androidx.annotation.CallSuper
import kotlin.math.abs

abstract class BaseScrollerItemView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val scroller by lazy { Scroller(context) }
    private val velocityTracker by lazy { VelocityTracker.obtain() }

    protected var offsetX = 0f
        private set

    protected var offsetY = 0f
        private set

    private var lastX = 0f
    private var lastY = 0f

    private var limitByX = 0f
    private var limitByY = 0f

    private var actualHeight: Int = 0
    abstract val contentHeight: Float
    abstract val contentWidth: Float

    override fun onTouchEvent(event: MotionEvent): Boolean {
        velocityTracker.addMovement(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!scroller.isFinished) {
                    scroller.abortAnimation()
                }

                lastX = event.x
                lastY = event.y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = lastX - event.x
                val deltaY = lastY - event.y

                // Горизонтальная прокрутка (только вправо)
                offsetX += deltaX
                offsetX = offsetX.coerceIn(0f, limitByX) // Нельзя скролить влево

                // Вертикальная прокрутка
                offsetY += deltaY
                offsetY = offsetY.coerceIn(0f, limitByY) // Ограничиваем скролл

                lastX = event.x
                lastY = event.y
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP -> {
                velocityTracker.computeCurrentVelocity(1000) // Вычисляем скорость

                val velocityX = velocityTracker.xVelocity
                val velocityY = velocityTracker.yVelocity

                // Если скорость достаточно высока, выполняем флинг
                if (abs(velocityX) > 50 || abs(velocityY) > 50) {
                    scroller.fling(
                        offsetX.toInt(), offsetY.toInt(),
                        -velocityX.toInt(), -velocityY.toInt(),
                        0, limitByX.toInt(), // Горизонтальные границы (можно скролить только вправо)
                        0, limitByY.toInt() // Вертикальные границы
                    )
                    invalidate()
                }
                velocityTracker.clear()
            }
        }
        return true
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            offsetX = scroller.currX.toFloat()
            offsetY = scroller.currY.toFloat()
            invalidate()
        }
    }

    private fun updateLimitMaxY() {
        if (contentHeight <= 0 || actualHeight <= 0) {
            return
        }
        limitByY = (contentHeight - actualHeight).coerceAtLeast(0f)
    }

    @CallSuper
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.actualHeight = h
        updateLimitMaxY()

        restoreScrollPosition(saveScrollPosition())
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        velocityTracker.recycle()
    }

    protected fun smoothScrollToTop(duration: Int = 500) {
        if (scroller.isFinished) { // Проверяем, что анимация не уже выполняется
            scroller.startScroll(
                offsetX.toInt(), // Текущее положение по X
                offsetY.toInt(), // Текущее положение по Y
                0,              // Не меняем положение по X
                -offsetY.toInt(), // Двигаемся к верху (offsetY = 0)
                duration            // Длительность анимации (в миллисекундах)
            )
            invalidate() // Запускаем перерисовку
        }
    }

    /**
     * Останавливает анимацию скролла.
     */
    protected fun stopScroll() {
        if (!scroller.isFinished) { // Проверяем, что анимация выполняется
            scroller.abortAnimation() // Останавливаем анимацию

            // Обновляем offsetX и offsetY до текущих значений Scroller
            offsetX = scroller.currX.toFloat()
            offsetY = scroller.currY.toFloat()

            invalidate() // Перерисовываем View
        }
    }

    protected fun saveScrollPosition(): Float {
        if (contentHeight <= 0 || actualHeight <= 0) {
            return 0f
        }

        // Вычисляем текущее положение скролла относительно контента
        return offsetY / (contentHeight - actualHeight).coerceAtLeast(1f)
    }

    protected fun restoreScrollPosition(savedPosition: Float) {
        if (contentHeight <= 0 || actualHeight <= 0) {
            return
        }

        // Вычисляем новое значение offsetY на основе сохранённого положения
        val maxOffsetY = (contentHeight - actualHeight).coerceAtLeast(0f)
        offsetY = (savedPosition * maxOffsetY).coerceIn(0f, maxOffsetY)

        // Обновляем UI
        invalidate()
    }
}