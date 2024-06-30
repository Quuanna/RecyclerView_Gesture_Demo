package com.anna.recyclerviewgesturedemo.helper

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextPaint
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.anna.recyclerviewgesturedemo.adapter.ItemTouchHelperAdapter
import com.anna.recyclerviewgesturedemo.R


/**
 * 滑掉左滑右滑
 */
class SwipeChildDrawHelper(val recyclerView: RecyclerView) :
    ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.ACTION_STATE_IDLE,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {

    private val calculatedHorizontalMargin = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, 16f, recyclerView.context.resources.displayMetrics
    )

    override fun onMove(
        recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (viewHolder.adapterPosition != RecyclerView.NO_POSITION) {
            (recyclerView.adapter as? ItemTouchHelperAdapter)?.onItemSwipeRemove(viewHolder.adapterPosition)
        }
    }

    // 如果你想加入背景、文字、圖標，當用戶滑動時，這是開始裝飾的地方
    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

       // 滑動時 item 背景顏色、icon、判斷滑動手勢
        viewHolder.itemView.run {
            if (actionState != ItemTouchHelper.ACTION_STATE_SWIPE) return
            drawBackgroundColor(android.R.color.holo_blue_light, canvas, dX)
//            drawIcon(R.drawable.icon_remove, canvas, dX)
            drawIconAddText(R.drawable.ic_remove, "移除", canvas, dX)
//            drawText("刪除",canvas, calculatedHorizontalMargin, dX)
        }
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun View.drawBackgroundColor(@ColorRes color: Int, canvas: Canvas, dX: Float) {
        val colorLeft = ContextCompat.getColor(context, color)
        when (dX > 0.0) {
            true -> canvas.clipRect(left, top, left + dX.toInt(), bottom)
            false -> canvas.clipRect(right + dX.toInt(), top, right, bottom)
        }
        val cvLeftBackgroundColor = ColorDrawable(colorLeft)
        cvLeftBackgroundColor.bounds = canvas.clipBounds
        cvLeftBackgroundColor.draw(canvas) // 平移空間的長方形背景顏色
    }

    private fun View.drawIcon(@DrawableRes drawable: Int, canvas: Canvas, dX: Float) {
        val calculatedHorizontalMargin = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 16f, context.resources.displayMetrics
        )
        when {
            dX > calculatedHorizontalMargin -> {
                ContextCompat.getDrawable(context, drawable)?.run {
                    val iconSize = intrinsicHeight
                    val halfIcon = iconSize / 2
                    val top = top + ((bottom - top) / 2 - halfIcon)

                    setBounds(
                        (left + calculatedHorizontalMargin).toInt(), top,
                        (left + calculatedHorizontalMargin + iconSize).toInt(),
                        top + iconSize
                    )
                    colorFilter(Color.WHITE) // 設定icon背景色
                    draw(canvas) //在平移空間上畫上icon
                }
            }

            dX < -calculatedHorizontalMargin -> {
                ContextCompat.getDrawable(context, drawable)?.run {
                    val iconSize = intrinsicHeight
                    val halfIcon = iconSize / 2
                    val top = top + ((bottom - top) / 2 - halfIcon)
                    val imgEnd = (right - calculatedHorizontalMargin - halfIcon * 2).toInt()

                    setBounds(
                        imgEnd,
                        top,
                        (right - calculatedHorizontalMargin).toInt(),
                        top + iconSize
                    )
                    colorFilter(Color.WHITE) // 設定icon背景色
                    draw(canvas)
                }
            }
        }
    }

    private fun View.drawIconAddText(
        @DrawableRes drawable: Int,
        text: String,
        canvas: Canvas,
        dX: Float
    ) {
        val drawLeft = dX > calculatedHorizontalMargin
        val drawRight = dX < -calculatedHorizontalMargin
        when {
            drawLeft -> {
                ContextCompat.getDrawable(context, drawable)?.run {
                    val iconSize = intrinsicHeight
                    val halfIcon = (iconSize / 3.5).toInt()
                    val top = (top + ((bottom - top) / 3.5 - halfIcon)).toInt()

                    setBounds(
                        (left + calculatedHorizontalMargin).toInt(), top,
                        (left + calculatedHorizontalMargin + iconSize).toInt(),
                        top + iconSize
                    )
                    colorFilter(Color.WHITE) // 設定icon背景色
                    draw(canvas) //在平移空間上畫上icon

                    if (dX > calculatedHorizontalMargin + iconSize) {
                        drawBelowText(text, canvas, calculatedHorizontalMargin, iconSize)
                    }
                }
            }

            drawRight -> {
                ContextCompat.getDrawable(context, drawable)?.run {
                    val iconSize = intrinsicHeight
                    val halfIcon = iconSize / 2
                    val top = top + ((bottom - top) / 2 - halfIcon)
                    val imgEnd = (right - calculatedHorizontalMargin - halfIcon * 2).toInt()

                    setBounds(
                        imgEnd,
                        top,
                        (right - calculatedHorizontalMargin).toInt(),
                        top + iconSize
                    )
                    colorFilter(Color.WHITE) // 設定icon背景色
                    draw(canvas)
                }
            }
        }
    }

    private fun View.drawBelowText(
        text: String,
        canvas: Canvas,
        horizontalMargin: Float,
        iconSize: Int
    ) {
        TextPaint().apply {
            isAntiAlias = true
            textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                14f,
                context.resources.displayMetrics
            )
            color = Color.WHITE
            typeface = Typeface.SANS_SERIF
        }.also {
            // 上下中間位置：(bottom - top) / 1.5
            // 左右中間位置：left + horizontalMargin/ 2
            val textTop = (top + (bottom - top) / 1.5 + it.textSize / 2).toFloat()
            val textLeft =
                (left + horizontalMargin / 2) + (if (iconSize > 0) horizontalMargin / 2 else 0).toFloat()
            canvas.drawText(text, textLeft, textTop, it)
        }
    }

    private fun View.drawText(
        text: String,
        canvas: Canvas,
        horizontalMargin: Float,
        dX: Float
    ) {
        val drawLeft = dX > calculatedHorizontalMargin
        val drawRight = dX < -calculatedHorizontalMargin

        TextPaint().apply {
            isAntiAlias = true
            textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                14f,
                context.resources.displayMetrics
            )
            color = Color.WHITE
            typeface = Typeface.SANS_SERIF
        }.also {

            when {
                drawLeft -> {
                    val textTop = (top + (bottom - top) / 2 + it.textSize / 2) // 上下中間位置
                    val textLeft = (left + horizontalMargin / 2) + ( horizontalMargin / 1) // 左右中間位置
                    canvas.drawText(text, textLeft, textTop, it)
                }

                drawRight -> {
                    val textTop = (top + (bottom - top) / 2 + it.textSize / 2) // 上下中間位置
                    val textRight = (right - horizontalMargin * 2)  - (horizontalMargin * 1)// 左右中間位置
                    canvas.drawText(text, textRight, textTop, it)
                }
            }


        }
    }

    private fun Drawable.colorFilter(@ColorInt tintColor: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            this.setColorFilter(tintColor, PorterDuff.Mode.MULTIPLY)
        } else {
            this.colorFilter = BlendModeColorFilter(tintColor, BlendMode.SRC_ATOP)
        }
    }


}