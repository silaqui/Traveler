package com.greenbee.traveler.presentation.traveldetails

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import androidx.transition.*
import androidx.transition.Transition.TransitionListener
import com.greenbee.traveler.R
import com.greenbee.traveler.data.Interactors
import com.greenbee.traveler.databinding.TripDetailsFragmentBinding
import com.greenbee.traveler.presentation.InjectorUtils


class TripDetails : Fragment() {

    private lateinit var binding: TripDetailsFragmentBinding

    private val viewModel: TripDetailsViewModel by viewModels {
        InjectorUtils.provideTripDetailsViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val arguments = TripDetailsArgs.fromBundle(requireArguments())

        binding = TripDetailsFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        handleTransitionAnimation(arguments)
        setHasOptionsMenu(true)


        viewModel.setTripId(arguments.tripId)

        val adapter =
            CategoryListAdapter(arguments.tripId, Interactors.getInstance(requireContext()))
        binding.categoriesCard.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.categoriesCard)

        binding.categoriesCard.adapter = adapter

        viewModel.trip.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it.categories)
            }
        })


        binding.categoriesCard.visibility = viewModel.cardsVisibility
        binding.titleCard.visibility = viewModel.cardsVisibility


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.trip_details_menu, menu)
    }

    private fun handleTransitionAnimation(arguments: TripDetailsArgs) {
        val listener = object : TransitionListener {
            override fun onTransitionEnd(transition: Transition) {
                transition.removeListener(this)
                showTitleAndCategoriesCards()
                viewModel.cardsVisibility = View.VISIBLE
            }

            override fun onTransitionStart(transition: Transition) {}
            override fun onTransitionCancel(transition: Transition) {}
            override fun onTransitionPause(transition: Transition) {}
            override fun onTransitionResume(transition: Transition) {}
        }
        if (arguments.backgroundUrl != null) {
            sharedElementEnterTransition =
                TransitionInflater.from(context)
                    .inflateTransition(android.R.transition.move)
                    .addListener(listener)

            val resID: Int =
                requireContext().resources.getIdentifier(
                    arguments.backgroundUrl,
                    "drawable",
                    requireContext().packageName
                )

            binding.backgroundImage.setImageResource(resID)

            binding.backgroundImage.apply {
                transitionName = "TRANSITION_IMAGE_" + arguments.tripId
            }
        } else {
            showTitleAndCategoriesCards()
            viewModel.cardsVisibility = View.VISIBLE
        }
    }

    private fun showTitleAndCategoriesCards() {

        val duration: Long = 300

        val fade = Slide()
        fade.duration = duration
        fade.slideEdge = Gravity.END
        fade.addTarget(binding.titleCard)

        val slide = Slide()
        slide.duration = duration
        slide.slideEdge = Gravity.BOTTOM
        slide.addTarget(binding.categoriesCard)

        TransitionManager.beginDelayedTransition(binding.realative, TransitionSet().apply {
            ordering = TransitionSet.ORDERING_TOGETHER
            addTransition(fade)
            addTransition(slide)
        })
        binding.categoriesCard.visibility = View.VISIBLE
        binding.titleCard.visibility = View.VISIBLE

    }

}
