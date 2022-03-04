package com.jokopriyono.belajarmvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jokopriyono.belajarmvvm.data.MainRepository

class MyViewModelFactory(
    private val repository: MainRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        try {
            return modelClass.getDeclaredConstructor(
                MainRepository::class.java
            ).newInstance(repository) as T
        } catch (e: Exception) {
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}