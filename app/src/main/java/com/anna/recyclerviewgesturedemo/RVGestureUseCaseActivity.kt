package com.anna.recyclerviewgesturedemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anna.recyclerviewgesturedemo.adapter.RVGestureAdapter
import com.anna.recyclerviewgesturedemo.constant.UseCaseType
import com.anna.recyclerviewgesturedemo.databinding.ActivityDefultRecyclerViewBinding
import com.anna.recyclerviewgesturedemo.gestureUseCase.RvDragUpDownMove
import com.anna.recyclerviewgesturedemo.gestureUseCase.RvSwipeAndDragActivity
import com.anna.recyclerviewgesturedemo.gestureUseCase.RvSwipeBasicLeftRightRemoveActivity
import com.anna.recyclerviewgesturedemo.gestureUseCase.RvSwipeDownRefreshActivity
import com.anna.recyclerviewgesturedemo.gestureUseCase.RvSwipeLeftButtonActivity
import com.anna.recyclerviewgesturedemo.gestureUseCase.RvSwipeLeftRightRemoveActivity

class RVGestureUseCaseActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDefultRecyclerViewBinding.inflate(layoutInflater) }
    private val recyclerView get() = binding.recyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@RVGestureUseCaseActivity)
            adapter = RVGestureAdapter(
                UseCaseType.entries.toTypedArray(),
                useCaseItemClickListener()
            )
            addItemDecoration(
                DividerItemDecoration(
                    this@RVGestureUseCaseActivity,
                    RecyclerView.VERTICAL
                )
            )
        }
    }

    private fun useCaseItemClickListener() =
        object : (UseCaseType) -> View.OnClickListener {
            override fun invoke(useCase: UseCaseType): View.OnClickListener {
                return View.OnClickListener {
                    when (useCase) {
                        UseCaseType.SWIPE_DOWN_REFRESH -> {
                            startActivity(getActivityIntent(RvSwipeDownRefreshActivity::class.java))
                        }

                        UseCaseType.BASIC_DRAG_POSITION_UP_DOWN_MOVE -> {
                            startActivity(getActivityIntent(RvDragUpDownMove::class.java))
                        }

                        UseCaseType.BASIC_SWIPE_LEFT_RIGHT_REMOVE -> {
                            startActivity(getActivityIntent(RvSwipeBasicLeftRightRemoveActivity::class.java))
                        }
                        UseCaseType.CUSTOM_SWIPE_LEFT_RIGHT_ADD_BACKGROUND -> {
                            startActivity(getActivityIntent(RvSwipeLeftRightRemoveActivity::class.java))
                        }

                        UseCaseType.BASIC_SWIPE_AND_DRAG -> {
                            startActivity(getActivityIntent(RvSwipeAndDragActivity::class.java))
                        }

                        UseCaseType.BASIC_SWIPE_LEFT_RIGHT_SHOW_BUTTON_EVENT -> {
                            startActivity(getActivityIntent(RvSwipeLeftButtonActivity::class.java))
                        }



                    }
                }
            }
        }

    private fun getItemNumList(): ArrayList<String> {
        val items = arrayListOf<String>()
        repeat(10) {
            items.add("item $it")
        }
        return items
    }

    private fun getActivityIntent(cls: Class<*>?): Intent {
        val bundle = Bundle()
        bundle.putStringArrayList(BUNDLE_KEY_ITEM_VALUE, getItemNumList())
        return Intent(this@RVGestureUseCaseActivity, cls).putExtras(bundle)
    }

    companion object {
        const val BUNDLE_KEY_ITEM_VALUE = "BUNDLE_KEY_ITEM_VALUE"
    }
}