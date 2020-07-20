package com.trials.sample001.ui.main

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.google.android.material.tabs.TabLayout
import com.trials.sample001.record.Record
import com.trials.sample001.record.RecordRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class SectionsPagerViewModel(application: Application) : AndroidViewModel(application),
    LifecycleObserver {

    private val context: Context = application.baseContext
    private val recordRepository =
        RecordRepositoryImpl(context)

    private val arrayOfRecord = mutableListOf<Record.CategoryList>()
    private val _getTabList = MutableLiveData<List<String>>()
    private val _getCategoryList = MutableLiveData<List<Record.CategoryList>>()
    val getTabList = _getTabList
    val isLoading = ObservableField<Boolean>()
    val getCategoryList = _getCategoryList

    val tabListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab){
            tab.text?.let {
                onChangedSectionName = it.toString()
            }
        }
        override fun onTabUnselected(tab: TabLayout.Tab){}
        override fun onTabReselected(tab: TabLayout.Tab){}
    }

    var onChangedSectionName: String by Delegates.observable("", { _, old, new ->
        if (old != new) {
            updateList(new)
        }
    })

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        getRecords()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
    }

    fun onRefresh() {
        if (isLoading.get() == true) return
        getRecords()
    }

    private fun getRecords() = viewModelScope.launch {
        isLoading.set(true)
        when (val result = recordRepository.getRecordFromServer()) {
            is RecordRepositoryImpl.Result.FetchedRecords -> {
                _getTabList.value = result.value.topCategoryList.map { it.nameCategory }
                arrayOfRecord.clear()
                arrayOfRecord.addAll(result.value.topCategoryList)
                updateList(onChangedSectionName)
                isLoading.set(false)
            }
            is RecordRepositoryImpl.Result.Failure -> {
                showToast(result.message)
                isLoading.set(false)
            }
        }
    }


    private fun updateList(name: String) {
        val sortedList = arrayOfRecord.filter { it.nameCategory == name }.toList()
        if (sortedList.isNotEmpty()) {
            _getCategoryList.value = sortedList
        } else {
            _getCategoryList.value = arrayOfRecord
        }
    }

    private fun showToast(mes: String) = viewModelScope.launch(Dispatchers.Main) {
        Toast.makeText(context, mes, Toast.LENGTH_LONG).show()
    }
}