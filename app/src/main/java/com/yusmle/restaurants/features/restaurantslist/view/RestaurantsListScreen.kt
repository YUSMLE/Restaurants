package com.yusmle.restaurants.features.restaurantslist.view

import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yusmle.restaurants.R
import com.yusmle.restaurants.common.EndlessScrollListener
import com.yusmle.restaurants.common.autoCleared
import com.yusmle.restaurants.common.foundation.BaseFragment
import com.yusmle.restaurants.common.helper.util.onClickThrottled
import com.yusmle.restaurants.common.viewBinding
import com.yusmle.restaurants.databinding.FragmentRestaurantsListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class RestaurantsListScreen : BaseFragment(R.layout.fragment_restaurants_list) {

    private val binding by viewBinding(FragmentRestaurantsListBinding::bind)

    private val viewModel: RestaurantsListViewModel by viewModel()

    private var restaurantsListAdapter by autoCleared<RestaurantsListAdapter>()

    override fun setupViews() = with(binding) {
        restaurantsListAdapter = RestaurantsListAdapter(
            viewLifecycleOwner
        ).apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        val linearLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        with(restaurantsList) {
            adapter = restaurantsListAdapter
            layoutManager = linearLayoutManager
            addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
            addOnScrollListener(object : EndlessScrollListener(linearLayoutManager) {
                override fun onLoadMore(page: Int) {
                    viewModel.sendIntent(RestaurantsListUserIntention.GetMoreRestaurantsList)
                }
            })
        }

        swipeContainer.setOnRefreshListener {
            viewModel.sendIntent(RestaurantsListUserIntention.RefreshRestaurantsList)
        }

        retryView.retryButton.onClickThrottled {
            viewModel.sendIntent(RestaurantsListUserIntention.RetryGettingRestaurantsList)
        }
    }

    override fun setupObservers() = with(viewModel) {
        observeX(viewLifecycleOwner) {
            when (it) {
                RestaurantsListViewState.Init -> renderInitState()
                is RestaurantsListViewState.Loading -> renderLoadingState(it)
                is RestaurantsListViewState.Loaded -> renderLoadedState(it)
                is RestaurantsListViewState.Failed -> renderFailedState(it)
            }
        }
    }

    private fun renderInitState() {}

    private fun renderLoadingState(state: RestaurantsListViewState.Loading) = with(binding) {
        restaurantsList.isVisible = state.restaurants.isNotEmpty()
        loadingOverlay.root.isVisible = state.restaurants.isEmpty()
        retryView.root.isVisible = false
        emptyResultView.root.isVisible = false

        state.restaurants.toListItem().toMutableList().apply {
            if (isNotEmpty())
                add(RestaurantListItem.LoadingItem)
        }.also {
            restaurantsListAdapter.items = it
        }
    }

    private fun renderLoadedState(state: RestaurantsListViewState.Loaded) = with(binding) {
        restaurantsList.isVisible = state.restaurants.isNotEmpty()
        loadingOverlay.root.isVisible = false
        retryView.root.isVisible = false
        emptyResultView.root.isVisible = state.restaurants.isEmpty()

        state.restaurants.toListItem().let {
            restaurantsListAdapter.items = it
        }
    }

    private fun renderFailedState(state: RestaurantsListViewState.Failed) = with(binding) {
        restaurantsList.isVisible = state.restaurants.isNotEmpty()
        loadingOverlay.root.isVisible = false
        retryView.root.isVisible = state.restaurants.isEmpty()
        emptyResultView.root.isVisible = false

        state.restaurants.toListItem().toMutableList().apply {
            if (isNotEmpty())
                add(RestaurantListItem.RetryItem(getString(R.string.msg_failure_loading_data)))
        }.also {
            restaurantsListAdapter.items = it
        }
    }
}
