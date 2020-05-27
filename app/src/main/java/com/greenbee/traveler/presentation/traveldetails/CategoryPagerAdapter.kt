package com.greenbee.traveler.presentation.traveldetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.greenbee.traveler.databinding.ItemAddCategoryBinding
import com.greenbee.traveler.databinding.ItemCategoryBinding
import com.greenbee.traveler.domain.entities.Category

class CategoryPagerAdapter(val items: List<Category>, val context: Context) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int = items.size + 1

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return if (position == 0) {
            instantiateNewCategoryCard(container)
        } else {
            instantiateCategoryCard(container, position - 1)
        }
    }


    private fun instantiateNewCategoryCard(container: ViewGroup): View {
        val binding = ItemAddCategoryBinding.inflate(LayoutInflater.from(context), container, false)
        container.addView(binding.root)
        return binding.root
    }

    private fun instantiateCategoryCard(container: ViewGroup, position: Int): View {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(context), container, false)
        binding.title.text = items[position].title
        val adapter = ItemListAdapter(ItemListAdapter.ItemListener {
        })
        adapter.submitList(items[position].items)
        binding.recListFragment.adapter = adapter
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

}