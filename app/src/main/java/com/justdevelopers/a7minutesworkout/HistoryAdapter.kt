package com.justdevelopers.a7minutesworkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.justdevelopers.a7minutesworkout.databinding.ItemsRowBinding

class HistoryAdapter(private val items:ArrayList<HistoryEntity>): RecyclerView.Adapter<HistoryAdapter.MainHolder>() {

    inner class MainHolder(itemView:ItemsRowBinding):RecyclerView.ViewHolder(itemView.root){
        val llMain=itemView.llMain
        val tvDate=itemView.tvDate
        val tvId=itemView.tvId
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.MainHolder {
        return MainHolder(ItemsRowBinding.inflate(LayoutInflater
            .from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HistoryAdapter.MainHolder, position: Int) {
        val context = holder.itemView.context
        val item=items[position]
        holder.tvDate.text = item.createdAt
        holder.tvId.text = (position+1).toString()
        if (position%2==0){
            holder.llMain.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context,
                    R.color.colorLightGray))
        }else{
            holder.llMain.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context,
                R.color.white))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}