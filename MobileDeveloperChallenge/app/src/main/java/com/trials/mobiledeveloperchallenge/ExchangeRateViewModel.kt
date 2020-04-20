package com.trials.mobiledeveloperchallenge

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import kotlinx.coroutines.*
import java.lang.NumberFormatException
import kotlin.properties.Delegates


class ExchangeRateViewModel(application: Application) : AndroidViewModel(application),
    LifecycleObserver, Observable {

    private val context = application.applicationContext
    private val repository = ExchangeRateRepositoryImpl(context)
    private val _getExchangeRate = MutableLiveData<List<Pair<String, Double>>>()
    private val updateJob by lazy {
        viewModelScope.launch {
            while (isActive) {
                delay(INTERVAL_TO_UPDATE_IN_MINUTES)
                getRealTimeRate(onChangedAmount, onChangedCurrency)
            }
        }
    }
    private var onChangedAmount: Int by Delegates.observable(1) { property, oldValue, newValue ->
        if (oldValue != newValue) {
            onChangedCurrency ?: return@observable
            getRealTimeRate(newValue, onChangedCurrency)
        }
    }
    private var onChangedCurrency: String by Delegates.observable("") { property, oldValue, newValue ->
        if (newValue.isNotEmpty()&& oldValue != newValue) {
            getRealTimeRate(onChangedAmount, newValue)
        }
    }
    val getExchangeRate: LiveData<List<Pair<String, Double>>> = _getExchangeRate
    val currencyList = ObservableField<List<String>>()

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        getCurrencyList()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        updateJob.start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        updateJob.cancel()
    }

    fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        try {
            onChangedAmount = s.toString().toInt()
        } catch (e: NumberFormatException) {}
    }

    fun onItemSelected(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ) {
        onChangedCurrency = parent?.selectedItem.toString()
    }

    private fun getCurrencyList() = viewModelScope.launch {
        when (val result = repository.getCurrencyList()) {
            is ExchangeRateRepositoryImpl.Result.FetchedCurrency -> {
                currencyList.set(result.value.currencies.keys.toList())
            }
            is ExchangeRateRepositoryImpl.Result.Failure -> {
                showToast(result.message)
            }
        }
    }

    private fun getRealTimeRate(amount: Int, source: String) = viewModelScope.launch {
        when (val result = repository.getRealTimeRate(source)) {
            is ExchangeRateRepositoryImpl.Result.FetchedRate -> {
                _getExchangeRate.postValue(result.value.quotes.map {
                    Pair(
                        it.key,
                        it.value * amount
                    )
                })
            }
            is ExchangeRateRepositoryImpl.Result.Failure -> {
                showToast(result.message)
            }
        }
    }

    private fun showToast(message: String) {
        viewModelScope.launch(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val INTERVAL_TO_UPDATE_IN_MINUTES = 1_000L * 60 * 30
    }
}
