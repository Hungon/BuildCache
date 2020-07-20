package com.trials.sample001.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.trials.sample001.R
import com.trials.sample001.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this,
                R.layout.activity_main
            )
        val viewModel = ViewModelProvider(this).get(SectionsPagerViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        tabs.addOnTabSelectedListener(viewModel.tabListener)

        lifecycle.addObserver(viewModel)
        viewModel.run {
            getTabList.observe(this@MainActivity, Observer {
                sectionsPagerAdapter.setTitles(it)
            })
        }
    }
}