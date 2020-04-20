package com.trials.mobiledeveloperchallenge.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.trials.mobiledeveloperchallenge.R


class ExchangeRateAdapter(context: Context) :
    ArrayAdapter<Pair<String, Double>>(context, R.layout.exchange_rate_item) {

    private val mutableList = mutableListOf<Pair<String, Double>>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return if (convertView != null) {
            if (mutableList.isNotEmpty()) {
                with(convertView) {
                    val nameTextView: TextView = findViewById(R.id.text_name)
                    val rateTextView: TextView = findViewById(R.id.text_exchange_rate)
                    val item = mutableList[position]
                    nameTextView.text = item.first
                    rateTextView.text = item.second.toString()
                }
            }
            convertView
        } else {
            LayoutInflater.from(context).inflate(R.layout.exchange_rate_item, parent, false)
        }
    }

    override fun getCount(): Int = mutableList.size

    fun update(list: List<Pair<String, Double>>) {
        if (!mutableList.containsAll(list)) {
            mutableList.clear()
            mutableList.addAll(list)
            notifyDataSetChanged()
        }
    }
}