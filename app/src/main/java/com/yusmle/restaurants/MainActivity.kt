package com.yusmle.restaurants

import android.content.Intent
import android.os.Bundle
import com.yusmle.restaurants.common.viewBinding
import com.yusmle.restaurants.databinding.ActivityMainBinding
import com.yusmle.restaurants.foundation.BaseActivity

class MainActivity : BaseActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun initializeActivity(savedInstanceState: Bundle?) {}

    override fun extractIntentParams(intent: Intent?) {}

    override fun setupViews() {
        setContentView(binding.root)
    }

    override fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
    }

    override fun setupNavigation() {}

    override fun setupObservers() {}
}
