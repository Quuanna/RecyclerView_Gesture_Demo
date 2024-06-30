package com.anna.recyclerviewgesturedemo.gestureUseCase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.anna.recyclerviewgesturedemo.adapter.ItemTouchHelperAdapter
import com.anna.recyclerviewgesturedemo.RVGestureUseCaseActivity.Companion.BUNDLE_KEY_ITEM_VALUE
import com.anna.recyclerviewgesturedemo.databinding.ActivityDefultRecyclerViewBinding
import com.anna.recyclerviewgesturedemo.helper.SwipeButton
import com.anna.recyclerviewgesturedemo.helper.SwipeButtonDrawHelper

class RvSwipeLeftButtonActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDefultRecyclerViewBinding.inflate(layoutInflater) }
    private var toast: Toast? = null
    private lateinit var itemList: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        (intent?.extras?.getStringArrayList(BUNDLE_KEY_ITEM_VALUE) as? ArrayList<String>)?.let {
            itemList = it
        }
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = ItemTouchHelperAdapter(
            gestureType = ItemTouchHelperAdapter.GestureType.SWIPE, arrayListOf(
                "Item 0: No action",
                "Item 1: Delete",
                "Item 2: Delete & Mark as unread",
                "Item 3: Delete, Mark as unread & Archive"
            )
        )
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        val itemTouchHelper = ItemTouchHelper(object :
            SwipeButtonDrawHelper(binding.recyclerView, ItemTouchHelper.LEFT) {
            override fun instantiateButton(position: Int): List<SwipeButton> {
                return initSwipeButtons(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun initSwipeButtons(position: Int): List<SwipeButton> {
        var buttons = listOf<SwipeButton>()
        val deleteButton = deleteButton(position)
        val markAsUnreadButton = markAsUnreadButton(position)
        val archiveButton = archiveButton(position)
        when (position) {
            1 -> buttons = listOf(deleteButton)
            2 -> buttons = listOf(deleteButton, markAsUnreadButton)
            3 -> buttons = listOf(deleteButton, markAsUnreadButton, archiveButton)
            else -> Unit
        }
        return buttons
    }

    private fun toast(text: String) {
        toast?.cancel()
        toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        toast?.show()
    }

    private fun deleteButton(position: Int): SwipeButton {
        return SwipeButton(this,
            "Delete",
            14.0f,
            android.R.color.holo_red_light,
            object : SwipeButton.UnderlayButtonClickListener {
                override fun onClick() {
                    toast("Deleted item $position")
                }
            })
    }

    private fun markAsUnreadButton(position: Int): SwipeButton {
        return SwipeButton(this,
            "Mark as unread",
            14.0f,
            android.R.color.holo_green_light,
            object : SwipeButton.UnderlayButtonClickListener {
                override fun onClick() {
                    toast("Marked as unread item $position")
                }
            })
    }

    private fun archiveButton(position: Int): SwipeButton {
        return SwipeButton(this,
            "Archive",
            14.0f,
            android.R.color.holo_blue_light,
            object : SwipeButton.UnderlayButtonClickListener {
                override fun onClick() {
                    toast("Archived item $position")
                }
            })
    }
}





