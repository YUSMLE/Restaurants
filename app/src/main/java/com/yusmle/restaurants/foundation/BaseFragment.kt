package com.yusmle.restaurants.foundation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavBackStackEntry
import androidx.navigation.fragment.findNavController

abstract class BaseFragment(
    @LayoutRes contentLayoutId: Int,
    private val isFullScreen: Boolean = false
) : Fragment(contentLayoutId) {

    /****************************************************
     * FRAGMENT LIFECYCLE
     ***************************************************/

    /**
     * Is called when a fragment is connected to an activity.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)

        // DEBUG
        Log.v(TAG, "Lifecycle - onAttach")
    }

    /**
     * Is called to do initial creation of the fragment.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DEBUG
        Log.v(TAG, "Lifecycle - onCreate")

        // TODO("Initialize ViewModel here")
    }

    /**
     * Is called by Android once the Fragment should inflate a view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // DEBUG
        Log.v(TAG, "Lifecycle - onCreateView")

        // TODO("Defines the xml file for the fragment here")

        return super.onCreateView(inflater, parent, savedInstanceState)
    }

    /**
     * Is called after onCreateView() and ensures that the fragment's root view is non-null.
     * Any view setup should happen here. E.g., view lookups, attaching listeners.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // TODO("Make StatusBar transparent and / or full screen here based on [isFullScreen]")

        super.onViewCreated(view, savedInstanceState)

        // DEBUG
        Log.v(TAG, "Lifecycle - onViewCreated")

        // TODO("Setup any handles to view objects here")

        setupViews()
        setupObservers()
    }

    /**
     * Is called once the fragment is ready to be displayed on screen.
     */
    override fun onStart() {
        super.onStart()

        // DEBUG
        Log.v(TAG, "Lifecycle - onStart")
    }

    /**
     * Allocate “expensive” resources such as registering for location, sensor updates, etc.
     */
    override fun onResume() {
        super.onResume()

        // DEBUG
        Log.v(TAG, "Lifecycle - onResume")
    }

    /**
     * Release “expensive” resources. Commit any changes.
     */
    override fun onPause() {
        // DEBUG
        Log.v(TAG, "Lifecycle - onPause")

        super.onPause()
    }

    /**
     * All resources released.
     */
    override fun onStop() {
        // DEBUG
        Log.v(TAG, "Lifecycle - onStop")

        super.onStop()
    }

    /**
     * Is called when fragment's view is being destroyed, but the fragment is still kept around.
     */
    override fun onDestroyView() {
        // DEBUG
        Log.v(TAG, "Lifecycle - onDestroyView")

        super.onDestroyView()
    }

    /**
     * Is called when fragment is no longer in use.
     */
    override fun onDestroy() {
        // DEBUG
        Log.v(TAG, "Lifecycle - onDestroy")

        super.onDestroy()
    }

    /**
     * Is called when fragment is no longer connected to the activity.
     */
    override fun onDetach() {
        // DEBUG
        Log.v(TAG, "Lifecycle - onDetach")

        super.onDetach()
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return false
    }

    /****************************************************
     * VIEW/DATA BINDING
     ***************************************************/

    protected abstract fun setupViews()

    /****************************************************
     * OBSERVERS
     ***************************************************/

    protected abstract fun setupObservers()

    protected fun observeNavBackStackEntryLifecycle(navigationFragmentId: Int) {
        val navController = findNavController()
        val navBackStackEntry = navController.getBackStackEntry(navigationFragmentId)

        // Create our observer and add it to the NavBackStackEntry's lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // Do stuff on Lifecycle RESUME
                checkNavBackStackEntry(navBackStackEntry)
            }
            /// else if (event == Lifecycle.Event.ON_DESTROY) { /* Do stuff on Lifecycle DESTROY */ }
        }

        navBackStackEntry.lifecycle.addObserver(observer)

        // As addObserver() does not automatically remove the observer, we should call the
        // removeObserver() manually when the view lifecycle is destroyed.
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
    }

    protected open fun checkNavBackStackEntry(navBackStackEntry: NavBackStackEntry) {
        /* Nothing by default */
    }

    companion object {
        val TAG: String = BaseFragment::class.java.simpleName
    }
}
