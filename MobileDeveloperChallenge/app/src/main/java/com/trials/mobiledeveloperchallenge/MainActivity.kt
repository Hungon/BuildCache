package com.trials.mobiledeveloperchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.trials.mobiledeveloperchallenge.databinding.ActivityMainBinding
import com.trials.mobiledeveloperchallenge.view.ExchangeRateAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val viewModel = ViewModelProvider(this).get(ExchangeRateViewModel::class.java)
        binding.exchangeRateViewModel = viewModel
        binding.lifecycleOwner = this

        // set grid view
        val exchangeRateAdapter = ExchangeRateAdapter(this)
        gridview_exchange_rate.apply {
            adapter = exchangeRateAdapter
        }
        // set spinner
        val spinner: Spinner = findViewById(R.id.spinner_currencies)
        ArrayAdapter<String>(this, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        lifecycle.addObserver(viewModel)

        viewModel.run {
            getExchangeRate.observe(this@MainActivity, Observer {
                exchangeRateAdapter.update(it)
            })
        }
    }
}
