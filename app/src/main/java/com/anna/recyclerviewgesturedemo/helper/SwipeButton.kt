package com.anna.recyclerviewgesturedemo.helper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.view.MotionEvent
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.anna.recyclerviewgesturedemo.R


class SwipeButton(
    private val context: Context,
    private val title: String,
    textSize: Float,
    @ColorRes private val colorRes: Int,
    private val clickListener: UnderlayButtonClickListener
) {
    private var clickableRegion: RectF? = null
    private val textSizeInPixel: Float =
        textSize * context.resources.displayMetrics.density // dp to px
    private val horizontalPadding = 50.0f
    val intrinsicWidth: Float

    interface UnderlayButtonClickListener {
        fun onClick()
    }

    init {
        val paint = Paint()
        paint.textSize = textSizeInPixel
        paint.typeface = Typeface.DEFAULT_BOLD
        paint.textAlign = Paint.Align.LEFT
        val titleBounds = Rect()
        paint.getTextBounds(title, 0, title.length, titleBounds)
        intrinsicWidth = titleBounds.width() + 2 * horizontalPadding
    }

    fun draw(canvas: Canvas, rect: RectF) {
        val paint = Paint()

        // Draw background
        paint.color = ContextCompat.getColor(context, colorRes)
        canvas.drawRect(rect, paint)

        // Draw title
        paint.color = ContextCompat.getColor(context, R.color.white)
        paint.textSize = textSizeInPixel
        paint.typeface = Typeface.DEFAULT_BOLD
        paint.textAlign = Paint.Align.LEFT

        val titleBounds = Rect()
        paint.getTextBounds(title, 0, title.length, titleBounds)

        val y = rect.height() / 2 + titleBounds.height() / 2 - titleBounds.bottom
        canvas.drawText(title, rect.left + horizontalPadding, rect.top + y, paint)

        clickableRegion = rect
    }

    fun handle(event: MotionEvent) {
        clickableRegion?.let {
            if (it.contains(event.x, event.y)) {
                clickListener.onClick()
            }
        }
    }
}