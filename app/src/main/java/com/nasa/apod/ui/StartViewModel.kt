package com.nasa.apod.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasa.apod.data.DataRepository
import com.nasa.apod.data.Result
import com.nasa.apod.data.model.Apod
import kotlinx.coroutines.launch

class StartViewModel(private val dataRepository: DataRepository) : ViewModel() {

    private val _state = MutableLiveData<Apod>()

    val state: LiveData<Apod>
        get() = _state

    init {
        viewModelScope.launch {
            when (val apod = dataRepository.getTodayPhoto()) {

                is Result.Success -> {
                    _state.value = apod.data
                }

                is Result.Failure -> {

                }
            }
        }
    }
}