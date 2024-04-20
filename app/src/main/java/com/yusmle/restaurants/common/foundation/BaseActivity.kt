package com.yusmle.restaurants.common.foundation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

abstract class BaseActivity : AppCompatActivity {

    constructor()

    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    /****************************************************
     * ACTIVITY LIFECYCLE
     ***************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DEBUG
        Log.v(TAG, "Lifecycle - onCreate")

        // FIRST, initialize the Activity with [savedInstanceState]
        initializeActivity(savedInstanceState)

        // Setup Views
        setupViews()

        // Set Action Bar
        setupActionBar()

        // Init fragments and then, open home fragment
        setupNavigation()

        // Setup Observers
        setupObservers()

        // LAST, extract required params from incoming Intent
        lifecycleScope.launchWhenResumed {
            extractIntentParams(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        // DEBUG
        Log.v(TAG, "Lifecycle - onStart")
    }

    override fun onRestart() {
        super.onRestart()

        // DEBUG
        Log.v(TAG, "Lifecycle - onRestart")
    }

    override fun onResume() {
        super.onResume()

        // DEBUG
        Log.v(TAG, "Lifecycle - onResume")
    }

    override fun onPause() {
        // DEBUG
        Log.v(TAG, "Lifecycle - onPause")

        super.onPause()
    }

    override fun onStop() {
        // DEBUG
        Log.v(TAG, "Lifecycle - onStop")

        super.onStop()
    }

    override fun onDestroy() {
        // DEBUG
        Log.v(TAG, "Lifecycle - onDestroy")

        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // DEBUG
        Log.v(TAG, "Lifecycle - onSaveInstanceState")

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // DEBUG
        Log.v(TAG, "Lifecycle - onRestoreInstanceState")
    }

    override fun onBackPressed() {
        // DEBUG
        Log.v(TAG, "Lifecycle - onBackPressed")

        super.onBackPressed()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        // DEBUG
        Log.v(TAG, "Lifecycle - onNewIntent")

        // Extract required params from incoming Intent
        lifecycleScope.launchWhenResumed {
            extractIntentParams(intent)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        // DEBUG
        Log.v(TAG, "Lifecycle - onWindowFocusChanged($hasFocus)")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // DEBUG
        Log.v(TAG, "Lifecycle - onActivityResult")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        /* menuInflater.inflate(R.menu.menu_main, menu) */

        return true
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return true
    }

    /****************************************************
     * ACTIVITY STATE
     ***************************************************/

    /**
     * If [savedInstanceState] equals to null, initialize state from incoming Intent,
     * else restore state from savedInstanceState.
     */
    protected abstract fun initializeActivity(savedInstanceState: Bundle?)

    /**
     * Extract data params from incoming Intent.
     */
    protected abstract fun extractIntentParams(intent: Intent?)

    /****************************************************
     * VIEW/DATA BINDING
     *
     * Set Content View,
     * Set Action Bar,
     * Init fragments and then, open HomeFragment as default,
     * Bottom Navigation View,
     * ...
     ***************************************************/

    protected abstract fun setupViews()

    protected abstract fun setupActionBar()

    protected abstract fun setupNavigation()

    /****************************************************
     * OBSERVERS
     ***************************************************/

    protected abstract fun setupObservers()

    companion object {
        val TAG: String = BaseActivity::class.java.simpleName
    }
}
