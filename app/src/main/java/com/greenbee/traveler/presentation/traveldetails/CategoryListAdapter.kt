package com.greenbee.traveler.presentation.traveldetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.greenbee.traveler.R
import com.greenbee.traveler.data.Interactors
import com.greenbee.traveler.domain.entities.Category
import com.greenbee.traveler.features.usecases.AddOrUpdateCategory
import kotlinx.android.synthetic.main.item_add_category.view.cardView
import kotlinx.android.synthetic.main.item_add_category.view.title
import kotlinx.android.synthetic.main.item_category.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ADD_CATEGORY_TYPE = 1
private const val CATEGORY_TYPE = 0

class CategoryListAdapter(val tripId: String, val interactors: Interactors) :
    ListAdapter<CategoryListAdapter.DataItem, RecyclerView.ViewHolder>(ItemDiffUtilCallback()) {

    private val viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ADD_CATEGORY_TYPE -> AddCategoryViewHolder(
                inflater.inflate(R.layout.item_add_category, parent, false)
            )
            CATEGORY_TYPE -> CategoryViewHolder(
                inflater.inflate(R.layout.item_category, parent, false)
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.AddCategory -> ADD_CATEGORY_TYPE
            is DataItem.CategoryHolder -> CATEGORY_TYPE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val listItem = getItem(position)) {
            is DataItem.AddCategory -> bindAddCategory((holder as AddCategoryViewHolder))
            is DataItem.CategoryHolder -> bindCategory((holder as CategoryViewHolder), listItem)
        }
    }

    private fun bindCategory(holder: CategoryViewHolder, categoryHolder: DataItem.CategoryHolder) {
        holder.title.text = categoryHolder.category.id
        holder.recyclerView.setRecycledViewPool(viewPool)

        val adapter = ItemListAdapter(tripId, categoryHolder.id, interactors)



        adapter.addAddItemAndSubmitList(categoryHolder.category.items)
        holder.recyclerView.adapter = adapter
    }

    private fun bindAddCategory(holder: AddCategoryViewHolder) {
        holder.cardView.setOnClickListener {
            interactors.addOrUpdateCategory(
                AddOrUpdateCategory.Params(
                    tripId,
                    Category(title = "NEW")
                )
            ) {}
        }
    }

    class AddCategoryViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        val cardView: CardView = root.cardView
    }

    class CategoryViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        val title: TextView = root.title
        val recyclerView: RecyclerView = root.rec_list_fragment
    }

    fun addAddCategoryAndSubmitList(list: List<Category>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.AddCategory)
                else -> list.map { DataItem.CategoryHolder(it) } + listOf(DataItem.AddCategory)
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
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

        data class CategoryHolder(val category: Category) : DataItem() {
            override val id = category.id
        }

        object AddCategory : DataItem() {
            override val id = Long.MIN_VALUE.toString()
        }

    }


}