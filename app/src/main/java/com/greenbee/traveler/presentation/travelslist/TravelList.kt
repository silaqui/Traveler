package com.greenbee.traveler.presentation.travelslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.greenbee.traveler.LIST_DETAIL_HERO_TAG
import com.greenbee.traveler.R
import com.greenbee.traveler.databinding.TravelsListFragmentBinding
import com.greenbee.traveler.domain.entities.Trip
import com.greenbee.traveler.presentation.InjectorUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TravelList : Fragment() {

    private lateinit var binding: TravelsListFragmentBinding

    private val viewModel: TravelListViewModel by viewModels {
        InjectorUtils.provideTravelListViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TravelsListFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setUpListAdapter()
        setUpSwipeToDeleteOnList()
        setUpAddFab()
        setUpNavigateToDetails()

        return binding.root
    }

    private fun setUpListAdapter() {
        val adapter =
            TravelListAdapter(TravelListAdapter.TripListener { id, imageView, backgroundUrl ->
                viewModel.onTripClicked(id, imageView, backgroundUrl)
            })

        binding.recListFragment.adapter = adapter

        viewModel.tripList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
    }

    private fun setUpSwipeToDeleteOnList() {
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteAtPosition(viewHolder.adapterPosition, ::showUndoSnackBar)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recListFragment)
    }

    fun showUndoSnackBar(trip: Trip) {
        Snackbar
            .make(
                binding.travelListConstraintLayout,
                R.string.Travel_removed,
                Snackbar.LENGTH_LONG
            )
            .setAction(R.string.UNDO) { viewModel.undoDelete(trip) }
            .show()
    }

    private fun setUpAddFab() {
        binding.addFab.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val newItemTitle = binding.title.text.toString()
                viewModel.add(newItemTitle)
            }
        }
    }

    private fun setUpNavigateToDetails() {
        viewModel.navigateToTripDetails.observe(viewLifecycleOwner, Observer { tripId ->
            tripId?.let {
                val actionTravelsListToTripDetails =
                    TravelListDirections.actionTravelsListToTripDetails(
                        tripId,
                        viewModel.backgroundUrl
                    )
                if (viewModel.imageView != null) {
                    val extras = FragmentNavigatorExtras(
                        viewModel.imageView!! to "$LIST_DETAIL_HERO_TAG$tripId"
                    )
                    this.findNavController().navigate(actionTravelsListToTripDetails, extras)
                } else {
                    this.findNavController().navigate(actionTravelsListToTripDetails)
                }
                viewModel.onTripNavigated()
            }
        })
    }

}


