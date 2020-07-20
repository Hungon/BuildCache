package com.trials.sample001.ui.detail

import android.app.Application
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.trials.sample001.R
import com.trials.sample001.db.ContentEntity
import com.trials.sample001.record.RecordRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application),
    LifecycleObserver {

    private val context: Context = application.baseContext
    private val recordRepository = RecordRepositoryImpl(context)
    var currentContentEntity: ContentEntity? = null
    val isPurchased = ObservableField(false)
    val isRegistered = ObservableField(false)

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        currentContentEntity?.let {
            retrieveFromDB(it.idBook)
        }
    }


    fun onClick(v: View) {
        when (v.id) {
            R.id.button_register -> {
                isRegistered.set(!(isRegistered.get() ?: false))
                currentContentEntity?.let {
                    val copiedContent = it.copy(hasRegistered = isRegistered.get() ?: false)
                    currentContentEntity = copiedContent
                    insertOrUpdate(copiedContent)
                }
            }
            R.id.button_purchase -> {

            }
        }
    }

    private fun retrieveFromDB(id: String) = viewModelScope.launch {
        when (val result = recordRepository.getContentFromDbById(id)) {
            is RecordRepositoryImpl.Result.FetchedContents -> {
                isPurchased.set(result.value.hasPurchased)
                isRegistered.set(result.value.hasRegistered)
            }
            is RecordRepositoryImpl.Result.Failure -> {
                showToast(result.message)
            }
        }
    }

    private fun insertOrUpdate(contentEntity: ContentEntity) = viewModelScope.launch {
        when (val result = recordRepository.insertOrUpdate(contentEntity)) {
            is RecordRepositoryImpl.Result.InsertedContent -> {

            }
            is RecordRepositoryImpl.Result.Failure -> {
                showToast(result.message)
            }
        }
    }

    private fun showToast(mes: String) = viewModelScope.launch(Dispatchers.Main) {
        Toast.makeText(context, mes, Toast.LENGTH_LONG).show()
    }
}