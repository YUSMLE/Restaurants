package com.yusmle.restaurants.features.restaurantslist.view

import androidx.fragment.app.Fragment
import com.yusmle.restaurants.R
import com.yusmle.restaurants.common.foundation.BaseFragment
import com.yusmle.restaurants.common.viewBinding
import com.yusmle.restaurants.databinding.FragmentRestaurantsListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class RestaurantsListScreen : BaseFragment(R.layout.fragment_restaurants_list) {

    private val binding by viewBinding(FragmentRestaurantsListBinding::bind)

    private val viewModel: RestaurantsListViewModel by viewModel()

    override fun setupViews() = with(binding) {
        // TODO("Not yet implemented")
    }

    override fun setupObservers() = with(viewModel) {
        observeX(viewLifecycleOwner) {
            // TODO("Not yet implemented")
        }
    }
}
