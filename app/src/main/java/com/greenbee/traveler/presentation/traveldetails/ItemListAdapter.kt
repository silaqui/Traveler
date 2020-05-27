package com.greenbee.traveler.presentation.traveldetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.greenbee.traveler.R
import com.greenbee.traveler.domain.entities.Item
import kotlinx.android.synthetic.main.item_list_add_item.view.*
import kotlinx.android.synthetic.main.item_list_item.view.*

private val ADD_ITEM_TYPE = 1
private val ITEM_TYPE = 0

class ItemListAdapter(var clickListener: ItemListener) :
    ListAdapter<Item, RecyclerView.ViewHolder>(ItemDiffUtilCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            ADD_ITEM_TYPE -> AddItemViewHolder(
                inflater.inflate(
                    R.layout.item_list_add_item,
                    parent,
                    false
                )
            )
            else -> ItemViewHolder(
                inflater.inflate(
                    R.layout.item_list_item,
                    parent,
                    false
                )
            )
        }

    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) ADD_ITEM_TYPE else ITEM_TYPE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == itemCount - 1) {
            (holder as AddItemViewHolder)
            holder.newButton.setOnClickListener {
                holder.newButton.visibility = View.GONE
                holder.name.visibility = View.VISIBLE
                holder.confirm.visibility = View.VISIBLE
                holder.cancel.visibility = View.VISIBLE
            }
            holder.confirm.setOnClickListener {
                holder.newButton.visibility = View.VISIBLE
                holder.name.visibility = View.GONE
                holder.confirm.visibility = View.GONE
                holder.cancel.visibility = View.GONE
            }
            holder.cancel.setOnClickListener {
                holder.newButton.visibility = View.VISIBLE
                holder.name.visibility = View.GONE
                holder.confirm.visibility = View.GONE
                holder.cancel.visibility = View.GONE
            }


        } else {
            getItem(position).let {
                (holder as ItemViewHolder).name.text = it.name
                holder.checkBox.isChecked = it.isDone
            }
        }
    }


    class ItemDiffUtilCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem == newItem
    }

    class AddItemViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        val newButton: ImageButton = root.imageButton
        val name: EditText = root.new_item_name
        val confirm: ImageButton = root.confirm
        val cancel: ImageButton = root.cancel
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
