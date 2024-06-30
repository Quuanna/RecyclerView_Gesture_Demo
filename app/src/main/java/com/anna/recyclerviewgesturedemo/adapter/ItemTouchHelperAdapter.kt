package com.anna.recyclerviewgesturedemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.anna.recyclerviewgesturedemo.R
import com.anna.recyclerviewgesturedemo.databinding.ItemTextViewHolderBinding
import java.util.Collections

class ItemTouchHelperAdapter(
    val gestureType: GestureType, private val items: ArrayList<String>
) : RecyclerView.Adapter<ItemTouchHelperAdapter.MyViewHolder>() {

    enum class GestureType {
        NONE, DRAG, SWIPE
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun onItemSwipeRemove(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun onItemShowButton(position: Int) {
        notifyItemChanged(position)
    }


    override fun getItemCount(): Int = items.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemTextViewHolderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
    }

    inner class MyViewHolder(val binding: ItemTextViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.apply {
                textView.text = items[adapterPosition]

                when (gestureType) {
                    GestureType.DRAG -> {
                        imageBtnMove.setImageDrawable(
                            ContextCompat.getDrawable(itemView.context, R.drawable.ic_dehaze)
                        )
                    }
                    GestureType.SWIPE, GestureType.NONE -> imageBtnMove.visibility = View.GONE
                }
            }
        }
    }


}