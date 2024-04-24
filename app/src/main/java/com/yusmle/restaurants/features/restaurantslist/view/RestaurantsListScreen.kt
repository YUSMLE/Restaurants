package com.yusmle.restaurants.features.restaurantslist.view

import android.Manifest
import android.content.Intent
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yusmle.restaurants.R
import com.yusmle.restaurants.common.EndlessScrollListener
import com.yusmle.restaurants.common.autoCleared
import com.yusmle.restaurants.common.foundation.BaseFragment
import com.yusmle.restaurants.common.helper.util.isAnyLocationProviderEnabled
import com.yusmle.restaurants.common.helper.util.isFineLocationPermissionGranted
import com.yusmle.restaurants.common.helper.util.navigateSafe
import com.yusmle.restaurants.common.helper.util.onClickThrottled
import com.yusmle.restaurants.common.observeX
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

        // Initialize loading data
        checkLocationPermissionRequirement()
    }

    override fun setupObservers(): Unit = with(viewModel) {
        observeX(viewLifecycleOwner) {
            when (it) {
                RestaurantsListViewState.Init -> renderInitState()
                is RestaurantsListViewState.Loading -> renderLoadingState(it)
                is RestaurantsListViewState.Loaded -> renderLoadedState(it)
                is RestaurantsListViewState.Failed -> renderFailedState(it)
            }
        }

        restaurantsListAdapter.viewActions.observeX(viewLifecycleOwner) {
            if (it is RestaurantActionEvent.RestaurantItemClicked) {
                navigateToRestaurantDetailsScreen(it.name)
            }
            else if (it == RestaurantActionEvent.RetryItemClicked) {
                sendIntent(RestaurantsListUserIntention.RetryGettingMoreRestaurantsList)
            }
        }
    }

    private fun renderInitState() {}

    private fun renderLoadingState(state: RestaurantsListViewState.Loading) = with(binding) {
        restaurantsList.isVisible = state.restaurants.isNotEmpty()
        loadingOverlay.root.isVisible = state.restaurants.isEmpty() && state.refreshing.not()
        swipeContainer.isRefreshing = state.refreshing
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
        swipeContainer.isRefreshing = false
        retryView.root.isVisible = false
        emptyResultView.root.isVisible = state.restaurants.isEmpty()

        state.restaurants.toListItem().let {
            restaurantsListAdapter.items = it
        }
    }

    private fun renderFailedState(state: RestaurantsListViewState.Failed) = with(binding) {
        restaurantsList.isVisible = state.restaurants.isNotEmpty()
        loadingOverlay.root.isVisible = false
        swipeContainer.isRefreshing = false
        retryView.root.isVisible = state.restaurants.isEmpty()
        emptyResultView.root.isVisible = false

        state.restaurants.toListItem().toMutableList().apply {
            if (isNotEmpty())
                add(RestaurantListItem.RetryItem(getString(R.string.msg_failure_loading_data)))
        }.also {
            restaurantsListAdapter.items = it
        }
    }

    private fun navigateToRestaurantDetailsScreen(restaurantName: String) =
        navigateSafe(
            RestaurantsListScreenDirections.openRestaurantDetailsScreen(restaurantName)
        )

    /**
     * Before you perform the actual permission request, check whether your app already has the
     * permissions, and whether your app needs to show a permission rationale dialog.
     *
     * Note: If precise location is needed, then add both ACCESS_COARSE_LOCATION and
     * ACCESS_FINE_LOCATION permissions to the app's manifest file.
     * On Android 12 (API level 31) or higher, users can request that your app retrieve only
     * approximate location information even when your app requests the ACCESS_FINE_LOCATION runtime
     * permission. To handle this, both ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION permissions
     * should be requested in a single runtime request.
     *
     * Read more: [https://developer.android.com/develop/sensors-and-location/location/permissions]
     */
    private fun checkLocationPermissionRequirement() {
        if (requireContext().isFineLocationPermissionGranted().not())
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        else
            checkLocationProvidersRequirement()
    }

    private fun checkLocationProvidersRequirement() {
        if (requireContext().isAnyLocationProviderEnabled().not())
            locationProvidersRequest.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        else
            viewModel.sendIntent(RestaurantsListUserIntention.LocationServiceRequirementsGranted)
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                // Precise location access granted.
                checkLocationProvidersRequirement()
            }
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                // Only approximate location access granted - Not Bad!
                checkLocationProvidersRequirement()
            }
            else -> {
                // No location access granted - Exit the app!
                requireActivity().finish()
            }
        }
    }

    private val locationProvidersRequest = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (requireContext().isAnyLocationProviderEnabled()) {
            // A location provider is enabled.
            viewModel.sendIntent(RestaurantsListUserIntention.LocationServiceRequirementsGranted)
        }
        else {
            // Not enabled any location provider - Exit the app!
            requireActivity().finish()
        }
    }
}
