package com.anna.recyclerviewgesturedemo.gestureUseCase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anna.recyclerviewgesturedemo.RVGestureUseCaseActivity
import com.anna.recyclerviewgesturedemo.adapter.ItemTouchHelperAdapter
import com.anna.recyclerviewgesturedemo.databinding.ActivityDefultRecyclerViewBinding

class RvSwipeBasicLeftRightRemoveActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDefultRecyclerViewBinding.inflate(layoutInflater) }
    private val recyclerView get() = binding.recyclerView
    private lateinit var itemTouchHelperAdapter: ItemTouchHelperAdapter
    private lateinit var itemList: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        (intent?.extras?.getStringArrayList(RVGestureUseCaseActivity.BUNDLE_KEY_ITEM_VALUE) as? ArrayList<String>)?.let {
            itemList = it
        }

        initView()
    }

    private fun initView() {
        itemTouchHelperAdapter =
            ItemTouchHelperAdapter(
                gestureType = ItemTouchHelperAdapter.GestureType.SWIPE,
                items = itemList
            )
        recyclerView.apply {
            adapter = itemTouchHelperAdapter
            layoutManager = LinearLayoutManager(this@RvSwipeBasicLeftRightRemoveActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@RvSwipeBasicLeftRightRemoveActivity,
                    RecyclerView.VERTICAL
                )
            )
        }
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.ACTION_STATE_IDLE,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (viewHolder.adapterPosition != RecyclerView.NO_POSITION) {
                    (recyclerView.adapter as? ItemTouchHelperAdapter)?.onItemSwipeRemove(viewHolder.adapterPosition)
                }
            }

        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}