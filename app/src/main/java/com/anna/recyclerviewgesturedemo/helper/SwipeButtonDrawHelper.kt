package com.anna.recyclerviewgesturedemo.helper

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.RectF
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.anna.recyclerviewgesturedemo.adapter.ItemTouchHelperAdapter
import kotlin.math.abs
import kotlin.math.max

@SuppressLint("ClickableViewAccessibility")
abstract class SwipeButtonDrawHelper(
    private val recyclerView: RecyclerView, swipe: Int
) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.ACTION_STATE_IDLE, swipe
) {

    private var swipedPosition = -1

    // 儲存與每個滑動項目位置關聯的按鈕
    private val buttonsBuffer: MutableMap<Int, List<SwipeButton>> = mutableMapOf()
    // 儲存與每個滑動項目位置關聯的按鈕
    private val recoverQueue: ArrayList<Int> = arrayListOf()

    // 定義在給定位置的每個項目要顯示的 SwipeButton 物件列表
    abstract fun instantiateButton(position: Int): List<SwipeButton>

    init {
        recyclerView.setOnTouchListener(touchListener())
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        recoverQueue.add(position)
        swipedPosition = position
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val position = viewHolder.adapterPosition
        val maxDX: Float
        val itemView = viewHolder.itemView

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                if (!buttonsBuffer.containsKey(position)) {
                    buttonsBuffer[position] = instantiateButton(position)
                }
                val buttons = buttonsBuffer[position] ?: return
                if (buttons.isEmpty()) return

                maxDX = max(-buttons.intrinsicWidth(), dX)
                drawButtons(c, buttons, itemView, maxDX)
                // 使用 maxDX 限制左滑距離
                super.onChildDraw(c, recyclerView, viewHolder, maxDX, dY, actionState, isCurrentlyActive)
            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        } else {
            // 其他情況，保持原來的行為
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        swipedPosition = -1
        recoverSwipedItem()
    }

    private fun drawButtons(
        canvas: Canvas,
        buttons: List<SwipeButton>,
        itemView: View,
        maxDX: Float
    ) {
        var right = itemView.right
        buttons.forEach { button ->
            val width = button.intrinsicWidth / buttons.intrinsicWidth() * abs(maxDX)
            val left = right - width
            button.draw(
                canvas,
                RectF(left, itemView.top.toFloat(), right.toFloat(), itemView.bottom.toFloat())
            )
            right = left.toInt()
        }
    }

    private fun touchListener() = View.OnTouchListener { _, event ->
        if (swipedPosition < 0) return@OnTouchListener false
        buttonsBuffer[swipedPosition]?.forEach { it.handle(event) }
        swipedPosition = -1
        recoverSwipedItem()
        false
    }

    private fun recoverSwipedItem() {
        if (recoverQueue.isNotEmpty()) {
            val position = recoverQueue.removeFirst()
            (recyclerView.adapter as? ItemTouchHelperAdapter)?.onItemShowButton(position)
        }
    }
}

private fun List<SwipeButton>.intrinsicWidth(): Float {
    if (isEmpty()) return 0.0f
    return map { it.intrinsicWidth }.reduce { acc, fl -> acc + fl }
}