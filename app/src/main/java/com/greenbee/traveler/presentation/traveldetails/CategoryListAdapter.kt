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

private val ADD_CATEGORY_TYPE = 1
private val CATEGORY_TYPE = 0

class CategoryListAdapter(val tripId: String, val interactors: Interactors) :
    ListAdapter<Category, RecyclerView.ViewHolder>(ItemDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ADD_CATEGORY_TYPE -> addCategoryViewHolder(inflater, parent)
            else -> categoryViewHolder(inflater, parent)
        }
    }

    private fun addCategoryViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): AddCategoryViewHolder {
        return AddCategoryViewHolder(
            inflater.inflate(
                R.layout.item_add_category, parent, false
            )
        )
    }

    private fun categoryViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): CategoryViewHolder {
        return CategoryViewHolder(
            inflater.inflate(
                R.layout.item_category, parent, false
            )
        )
    }


    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) ADD_CATEGORY_TYPE else CATEGORY_TYPE
    }

    class AddCategoryViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        val cardView: CardView = root.cardView
    }


    class CategoryViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        val title: TextView = root.title
        val recyclerView: RecyclerView = root.rec_list_fragment
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == itemCount - 1) {
            (holder as AddCategoryViewHolder)
            holder.cardView.setOnClickListener {
                interactors.addOrUpdateCategory(
                    AddOrUpdateCategory.Params(
                        tripId,
                        Category(title = "NEW")
                    )
                ) {}
            }

        } else {
            (holder as CategoryViewHolder)
            holder.title.text = getItem(position).title
            val adapter =
                ItemListAdapter(tripId, getItem(position).id, interactors)
            adapter.submitList(getItem(position).items)
            holder.recyclerView.adapter = adapter

        }
    }


    class ItemDiffUtilCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldCategory: Category, newCategory: Category): Boolean =
            oldCategory.id == newCategory.id

        override fun areContentsTheSame(oldCategory: Category, newCategory: Category): Boolean =
            oldCategory == newCategory
    }


}