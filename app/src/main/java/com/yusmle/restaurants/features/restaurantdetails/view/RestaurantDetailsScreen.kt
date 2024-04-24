package com.yusmle.restaurants.features.restaurantdetails.view

import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.yusmle.restaurants.R
import com.yusmle.restaurants.common.foundation.BaseFragment
import com.yusmle.restaurants.common.viewBinding
import com.yusmle.restaurants.databinding.FragmentRestaurantsDetailsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RestaurantDetailsScreen : BaseFragment(R.layout.fragment_restaurants_details) {

    private val binding by viewBinding(FragmentRestaurantsDetailsBinding::bind)

    private val args by navArgs<RestaurantDetailsScreenArgs>()

    private val viewModel: RestaurantDetailsViewModel by viewModel {
        // Retrieve [restaurantName] to initialize the view model
        parametersOf(args.restaurantName)
    }

    override fun setupViews() {
        viewModel.sendIntent(RestaurantDetailsUserIntention.GetRestaurantDetails)
    }

    override fun setupObservers() = with(viewModel) {
        observeX(viewLifecycleOwner) {
            binding.restaurantDetailsViewGroup.isVisible =
                it is RestaurantDetailsViewState.Loaded && it.restaurant != null
            binding.loadingOverlay.root.isVisible = it == RestaurantDetailsViewState.Loading
            binding.retryView.root.isVisible = it is RestaurantDetailsViewState.Failed
            binding.emptyResultView.root.isVisible =
                it is RestaurantDetailsViewState.Loaded && it.restaurant == null

            if (it is RestaurantDetailsViewState.Loaded) {
                binding.name.text = it.restaurant?.name
                binding.phoneNumber.text = it.restaurant?.phoneNumber
                binding.address.text = it.restaurant?.address
                binding.distance.text = it.restaurant?.distance
                binding.reviewRate.rating = it.restaurant?.reviewRate ?: 0f
            }
        }
    }
}
