package com.anna.recyclerviewgesturedemo.gestureUseCase

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.anna.recyclerviewgesturedemo.R
import com.anna.recyclerviewgesturedemo.RVGestureUseCaseActivity.Companion.BUNDLE_KEY_ITEM_VALUE
import com.anna.recyclerviewgesturedemo.adapter.ItemTouchHelperAdapter
import com.anna.recyclerviewgesturedemo.databinding.ActivityDefultRecyclerViewBinding
import com.anna.recyclerviewgesturedemo.helper.SwipeChildDrawHelper

class RvSwipeLeftRightRemoveActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDefultRecyclerViewBinding.inflate(layoutInflater) }
    private val recyclerView get() = binding.recyclerView
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
        recyclerView.apply {
            adapter = ItemTouchHelperAdapter(
                gestureType = ItemTouchHelperAdapter.GestureType.SWIPE,
                itemList
            )
            layoutManager = LinearLayoutManager(this@RvSwipeLeftRightRemoveActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@RvSwipeLeftRightRemoveActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        val itemTouchHelper = ItemTouchHelper(SwipeChildDrawHelper(recyclerView))
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

}