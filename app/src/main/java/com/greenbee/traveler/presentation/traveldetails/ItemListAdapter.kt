package com.greenbee.traveler.presentation.traveldetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.greenbee.traveler.data.Interactors
import com.greenbee.traveler.databinding.ItemListAddItemBinding
import com.greenbee.traveler.databinding.ItemListItemBinding
import com.greenbee.traveler.domain.entities.Item
import com.greenbee.traveler.features.usecases.AddOrUpdateItem
import com.greenbee.traveler.features.usecases.ToggleItemDone
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ADD_ITEM_TYPE = 1
private const val ITEM_TYPE = 0

class ItemListAdapter(
    val tripId: String,
    val categoryId: String,
    private val interactors: Interactors
) :
    ListAdapter<ItemListAdapter.DataItem, RecyclerView.ViewHolder>(ItemDiffUtilCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    val handler: Handler = Handler(tripId, categoryId, interactors)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ADD_ITEM_TYPE ->
                CustomViewHolder(ItemListAddItemBinding.inflate(inflater, parent, false))
            ITEM_TYPE ->
                CustomViewHolder(ItemListItemBinding.inflate(inflater, parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    class Handler(val tripId: String, val categoryId: String, val interactors: Interactors) {

        var newItemName: String = ""
        private var _showAddNew: MutableLiveData<Boolean> = MutableLiveData(true)
        val showAddNew: LiveData<Boolean> = _showAddNew

        val addElementsShown =
            Transformations.map(_showAddNew) { if (it) View.VISIBLE else View.GONE }
        val addElementsHidden =
            Transformations.map(_showAddNew) { if (it) View.GONE else View.VISIBLE }

        fun toggleItemDone(item: Item) {
            interactors.toggleItemDone(ToggleItemDone.Params(tripId, categoryId, item)) {}
        }

        fun addItem(name: String) {
            interactors.addOrUpdateItem(
                AddOrUpdateItem.Params(tripId, categoryId, Item(name = name))
            ) {}
        }

        fun toggleShowAddNew() {
            _showAddNew.postValue(!_showAddNew.value!!)
        }

    }

    class CustomViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.AddItem -> ADD_ITEM_TYPE
            is DataItem.ItemHolder -> ITEM_TYPE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val listItem = getItem(position)) {
            is DataItem.AddItem -> bindAddNewItem((holder as CustomViewHolder))
            is DataItem.ItemHolder -> bindItem((holder as CustomViewHolder), listItem)
        }
    }

    private fun bindItem(holder: CustomViewHolder, itemHolder: DataItem.ItemHolder) {
        val binding = holder.binding as ItemListItemBinding
        binding.item = itemHolder.item
        binding.handler = handler
        binding.executePendingBindings()
    }

    private fun bindAddNewItem(holder: CustomViewHolder) {
        val binding = holder.binding as ItemListAddItemBinding
        binding.handler = handler

//        binding.imageButton.setOnClickListener {
//            binding.imageButton.visibility = View.GONE
//            binding.newItemName.visibility = View.VISIBLE
//            binding.confirm.visibility = View.VISIBLE
//            binding.cancel.visibility = View.VISIBLE
//        }
//        binding.cancel.setOnClickListener {
//            binding.imageButton.visibility = View.VISIBLE
//            binding.newItemName.visibility = View.GONE
//            binding.confirm.visibility = View.GONE
//            binding.cancel.visibility = View.GONE
//        }

        binding.executePendingBindings()
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
