package com.anna.recyclerviewgesturedemo.gestureUseCase

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anna.recyclerviewgesturedemo.adapter.ItemTouchHelperAdapter
import com.anna.recyclerviewgesturedemo.RVGestureUseCaseActivity.Companion.BUNDLE_KEY_ITEM_VALUE
import com.anna.recyclerviewgesturedemo.databinding.ActivitySwipeDownRefreshBinding

class RvSwipeDownRefreshActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySwipeDownRefreshBinding.inflate(layoutInflater) }
    private val swipeRefreshLayout get() = binding.swipeRefreshLayout
    private val recyclerView get() = binding.recyclerView

    private lateinit var itemList: ArrayList<String>

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        (intent?.extras?.getStringArrayList(BUNDLE_KEY_ITEM_VALUE) as? ArrayList<String>)?.let {
            itemList = it
        }

        initView()
    }

    @SuppressLint("ResourceAsColor")
    private fun initView() {
        swipeRefreshLayout.apply {
            setColorSchemeColors(android.R.color.holo_blue_light)
            isEnabled = true
            setOnRefreshListener {
                isEnabled = false
                Toast.makeText(
                    this@RvSwipeDownRefreshActivity,
                    "swipeRefreshLayout",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val layout = LinearLayoutManager(this)
        layout.orientation = RecyclerView.VERTICAL
        recyclerView.apply {
            layoutManager = layout
            addItemDecoration(
                DividerItemDecoration(
                    this@RvSwipeDownRefreshActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = ItemTouchHelperAdapter(
                gestureType = ItemTouchHelperAdapter.GestureType.NONE,
                itemList
            )
        }
    }
}