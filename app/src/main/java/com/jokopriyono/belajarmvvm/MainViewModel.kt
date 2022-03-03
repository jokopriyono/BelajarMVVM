package com.jokopriyono.belajarmvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    val counter: MutableLiveData<Int> = MutableLiveData(0)

    fun increment() {
        counter.value = counter.value?.plus(1)
    }

    fun decrement() {
        counter.value = counter.value?.minus(1)
    }

}