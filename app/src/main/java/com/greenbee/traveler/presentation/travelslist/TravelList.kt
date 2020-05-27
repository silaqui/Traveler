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


        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteAtPosition(viewHolder.adapterPosition, ::showUndoSnackBar)
            }
        }


        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recListFragment)

        binding.addFab.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val newItemTitle = binding.title.text.toString()
                viewModel.add(newItemTitle)


            }
        }

        viewModel.navigateToTripDetails.observe(viewLifecycleOwner, Observer { tripId ->
            tripId?.let {
                if (viewModel.imageView != null) {
                    val extras = FragmentNavigatorExtras(
                        viewModel.imageView!! to "TRANSITION_IMAGE_" + tripId.toString()
                    )
                    this.findNavController()
                        .navigate(
                            TravelListDirections.actionTravelsListToTripDetails(
                                tripId,
                                viewModel.backgroundUrl
                            ),
                            extras
                        )
                } else {
                    this.findNavController()
                        .navigate(
                            TravelListDirections.actionTravelsListToTripDetails(
                                tripId,
                                viewModel.backgroundUrl
                            )
                        )
                }
                viewModel.onTripNavigated()
            }
        })

        return binding.root
    }


    fun showUndoSnackBar(trip: Trip) {
        Snackbar
            .make(
                binding.travelListConstraintLayout,
                "Travel removed",
                Snackbar.LENGTH_LONG
            )
            .setAction("UNDO", View.OnClickListener {
                viewModel.undoDelete(trip)
            })
            .show()
    }

}


