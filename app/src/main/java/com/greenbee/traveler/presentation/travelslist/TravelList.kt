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
import com.greenbee.traveler.databinding.TravelsListFragmentBinding
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
            TravelListAdapter(TravelListAdapter.TripListener { id, imageView ->
                viewModel.onTripClicked(id, imageView)
            })

        binding.recListFragment.adapter = adapter

        viewModel.tripList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.addFab.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
//                viewModel.add()
//                viewModel.refresh()
            }
        }

        viewModel.navigateToTripDetails.observe(viewLifecycleOwner, Observer { tripId ->
            tripId?.let {
                val extras = FragmentNavigatorExtras(
                    viewModel.imageView!! to "TRANSITION_IMAGE_" + tripId.toString()
                )
                this.findNavController()
                    .navigate(TravelListDirections.actionTravelsListToTripDetails(tripId), extras)
                viewModel.onTripNavigated()
            }
        })

        return binding.root
    }


}


