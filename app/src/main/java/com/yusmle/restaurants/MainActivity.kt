package com.yusmle.restaurants

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.yusmle.restaurants.common.viewBinding
import com.yusmle.restaurants.databinding.ActivityMainBinding
import com.yusmle.restaurants.common.foundation.BaseActivity

class MainActivity : BaseActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun initializeActivity(savedInstanceState: Bundle?) {}

    override fun extractIntentParams(intent: Intent?) {}

    override fun setupViews() {
        setContentView(binding.root)

        binding.refresh.setOnClickListener { view ->
            Snackbar.make(view, getString(R.string.msg_pull_to_refresh), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.action), null)
                .show()
        }
    }

    override fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
    }

    override fun setupNavigation() {}

    override fun setupObservers() {}
}
