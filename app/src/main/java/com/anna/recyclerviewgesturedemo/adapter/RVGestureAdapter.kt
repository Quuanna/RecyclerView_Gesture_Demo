package com.anna.recyclerviewgesturedemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anna.recyclerviewgesturedemo.constant.UseCaseType
import com.anna.recyclerviewgesturedemo.databinding.ItemTextViewHolderBinding

class RVGestureAdapter(
    val useCaseList: Array<UseCaseType>,
    val onItemClickListener: ((UseCaseType) -> View.OnClickListener)
) :
    RecyclerView.Adapter<RVGestureAdapter.ViewHolder>() {

    override fun getItemCount(): Int = useCaseList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTextViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()
    inner class ViewHolder(val binding: ItemTextViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            if (adapterPosition == RecyclerView.NO_POSITION) return
            itemView.setOnClickListener(onItemClickListener.invoke(useCaseList[adapterPosition]))
            binding.textView.text = useCaseList[adapterPosition].title
            binding.imageBtnMove.visibility = View.GONE
        }
    }
}