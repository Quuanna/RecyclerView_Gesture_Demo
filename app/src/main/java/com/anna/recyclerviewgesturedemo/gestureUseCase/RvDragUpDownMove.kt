package com.anna.recyclerviewgesturedemo.gestureUseCase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anna.recyclerviewgesturedemo.adapter.ItemTouchHelperAdapter
import com.anna.recyclerviewgesturedemo.RVGestureUseCaseActivity.Companion.BUNDLE_KEY_ITEM_VALUE
import com.anna.recyclerviewgesturedemo.databinding.ActivityDefultRecyclerViewBinding

class RvDragUpDownMove : AppCompatActivity() {

    private val binding by lazy { ActivityDefultRecyclerViewBinding.inflate(layoutInflater) }
    private val recyclerView get() = binding.recyclerView
    private lateinit var itemTouchHelperAdapter: ItemTouchHelperAdapter
    private lateinit var itemList: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        (intent?.extras?.getStringArrayList(BUNDLE_KEY_ITEM_VALUE) as? ArrayList<String>)?.let {
            itemList = it
        }

        initView()
    }
    private fun initView() {
        itemTouchHelperAdapter =
            ItemTouchHelperAdapter(gestureType = ItemTouchHelperAdapter.GestureType.DRAG, items = itemList)
        recyclerView.apply {
            adapter = itemTouchHelperAdapter
            layoutManager = LinearLayoutManager(this@RvDragUpDownMove)
            addItemDecoration(
                DividerItemDecoration(this@RvDragUpDownMove, RecyclerView.VERTICAL)
            )
        }
        val itemTouchHelper = ItemTouchHelper(dragCallback())
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun dragCallback() = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.ACTION_STATE_IDLE
    ) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return if (viewHolder.adapterPosition == RecyclerView.NO_POSITION
                && target.adapterPosition == RecyclerView.NO_POSITION) {
                false
            } else {
                itemTouchHelperAdapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
                true
            }
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    }
}