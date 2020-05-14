package com.greenbee.traveler.presentation.travelslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.greenbee.traveler.R
import com.greenbee.traveler.domain.entities.Trip
import kotlinx.android.synthetic.main.item_travel.view.*
import java.text.SimpleDateFormat
import java.util.*

class TravelListAdapter(val clickListener: TripListener) :
    ListAdapter<Trip, TravelListAdapter.TripViewHolder>(TripDiffUtilCallback()) {

    private val format = SimpleDateFormat("dd/MM/yyy")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TripViewHolder(inflater.inflate(R.layout.item_travel, parent, false))
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        getItem(position).let { trip ->
            holder.title.text = trip.title
            holder.date.text = format.format(Date(trip.date))
            holder.root.setOnClickListener {
                clickListener.onClick(
                    trip,
                    holder.listImage
                )
            }

            holder.listImage.apply { transitionName = "TRANSITION_IMAGE_" + trip.id.toString() }
        }
    }

    class TripDiffUtilCallback : DiffUtil.ItemCallback<Trip>() {
        override fun areItemsTheSame(oldItem: Trip, newItem: Trip): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Trip, newItem: Trip): Boolean = oldItem == newItem
    }

    class TripViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        val title: TextView = root.travel_title
        val date: TextView = root.travel_date
        val root: CardView = root.root_item_travel
        val listImage: ImageView = root.list_image
    }

    class TripListener(val clickListener: (travelId: Long, imageView: ImageView) -> Unit) {
        fun onClick(trip: Trip, imageView: ImageView) =
            clickListener(trip.id, imageView)
    }
}