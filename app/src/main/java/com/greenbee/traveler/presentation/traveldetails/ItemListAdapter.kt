package com.greenbee.traveler.presentation.traveldetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.greenbee.traveler.R
import com.greenbee.traveler.domain.entities.Item
import kotlinx.android.synthetic.main.item_item.view.*

class ItemListAdapter(var clickListener: ItemListener) :
    ListAdapter<Item, ItemListAdapter.ItemViewHolder>(ItemDiffUtilCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(
            inflater.inflate(
                R.layout.item_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        getItem(position).let {
            holder.name.text = it.name
            holder.checkBox.isChecked = it.isDone
        }
    }


    class ItemDiffUtilCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem == newItem
    }

    class ItemViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        val name: TextView = root.item_name
        val checkBox: CheckBox = root.item_check_box
    }

    class ItemListener(val clickListener: (item: Item) -> Unit) {
        fun onClick(item: Item) =
            clickListener(item)
    }
}
