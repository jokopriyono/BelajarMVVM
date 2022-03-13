package com.jokopriyono.belajarmvvm.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jokopriyono.belajarmvvm.data.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoViewModel @Inject constructor(
    private val repository: MainRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _characters = MutableLiveData<String>()
    val characters: LiveData<String> = _characters
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading
    private val _message: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> = _message

    suspend fun fetchAndLoadCharacters() {
        viewModelScope.launch(ioDispatcher) {
            repository.getAllCharacters(
                onStart = { _loading.postValue(true) },
                onComplete = { _loading.postValue(false) },
                onError = { _message.postValue(it) }
            ).collect {
                val textString = it
                    .joinToString(separator = ":")
                    .substring(0, 100)
                _characters.postValue(textString)
            }
        }
    }
}