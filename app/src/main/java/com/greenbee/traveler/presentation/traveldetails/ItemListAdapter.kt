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
import com.greenbee.traveler.data.Interactors
import com.greenbee.traveler.domain.entities.Item
import com.greenbee.traveler.features.usecases.AddOrUpdateItem
import kotlinx.android.synthetic.main.item_list_add_item.view.*
import kotlinx.android.synthetic.main.item_list_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ADD_ITEM_TYPE = 1
private const val ITEM_TYPE = 0

class ItemListAdapter(val tripId: String, val categoryId: String, val interactors: Interactors) :
    ListAdapter<ItemListAdapter.DataItem, RecyclerView.ViewHolder>(ItemDiffUtilCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ADD_ITEM_TYPE -> AddItemViewHolder(
                inflater.inflate(R.layout.item_list_add_item, parent, false)
            )
            ITEM_TYPE -> ItemViewHolder(
                inflater.inflate(R.layout.item_list_item, parent, false)
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.AddItem -> ADD_ITEM_TYPE
            is DataItem.ItemHolder -> ITEM_TYPE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val listItem = getItem(position)) {
            is DataItem.AddItem -> bindAddNewItem((holder as AddItemViewHolder))
            is DataItem.ItemHolder -> bindItem((holder as ItemViewHolder), listItem)
        }
    }

    private fun bindItem(holder: ItemViewHolder, itemHolder: DataItem.ItemHolder) {
        holder.name.text = itemHolder.item.name
        holder.checkBox.isChecked = itemHolder.item.isDone
        holder.checkBox.setOnClickListener {
            interactors
                .addOrUpdateItem(
                    AddOrUpdateItem.Params(
                        tripId,
                        categoryId,
                        itemHolder.item.copy(isDone = !itemHolder.item.isDone)
                    )
                ) {}
        }
    }

    private fun bindAddNewItem(holder: AddItemViewHolder) {
        holder.newButton.setOnClickListener {
            holder.newButton.visibility = View.GONE
            holder.name.visibility = View.VISIBLE
            holder.confirm.visibility = View.VISIBLE
            holder.cancel.visibility = View.VISIBLE
        }
        holder.confirm.setOnClickListener {
            Interactors.getInstance(holder.confirm.context).addOrUpdateItem(
                AddOrUpdateItem.Params(
                    tripId,
                    categoryId,
                    Item(name = "New Item")
                )
            ) {}
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

    fun addAddItemAndSubmitList(list: List<Item>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.AddItem)
                else -> list.map { DataItem.ItemHolder(it) } + listOf(DataItem.AddItem)
            }
            withContext(Dispatchers.Main) { submitList(items) }
        }
    }

    class ItemDiffUtilCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean =
            oldItem == newItem

    }

    sealed class DataItem {
        abstract val id: String

        data class ItemHolder(val item: Item) : DataItem() {
            override val id = item.id
        }

        object AddItem : DataItem() {
            override val id = Long.MIN_VALUE.toString()
        }

    }
}
