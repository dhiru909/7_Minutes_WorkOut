package com.justdevelopers.a7minutesworkout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.justdevelopers.a7minutesworkout.databinding.ItemExerciseStatusBinding

class ExerciseStatusAdapter(val items:List<ExerciseModel>):
    RecyclerView.Adapter<ExerciseStatusAdapter.MainHolder>() {

    inner class MainHolder(binding: ItemExerciseStatusBinding): RecyclerView.ViewHolder(binding.root) {
        val tvItem = binding.tvItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(ItemExerciseStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val task:ExerciseModel = items[position]
        holder.tvItem.text = task.getId().toString()

        when{
            task.getIsSelected()->{
                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context,R.drawable.item_circular_color_white_background)
                holder.tvItem.setTextColor(Color.parseColor("#212121"))
            }
            task.getIsCompleted()->{
                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context,R.drawable.item_circular_color_accent_background)
                holder.tvItem.setTextColor(Color.parseColor("#FFFFFF"))
            }
            else->{
                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context,R.drawable.item_circular_color_gray_background)
                holder.tvItem.setTextColor(Color.parseColor("#212121"))

            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}