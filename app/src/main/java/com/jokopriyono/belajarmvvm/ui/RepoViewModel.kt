package com.jokopriyono.belajarmvvm.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jokopriyono.belajarmvvm.data.MainRepository

class RepoViewModel(
    private val repository: MainRepository,
    private val view: RepoView
) : ViewModel() {

    private val _characters = MutableLiveData<String>()
    val characters: LiveData<String> = _characters

    suspend fun fetchAndLoadCharacters() {
        view.showLoading()
        val result = repository.getAllCharacters()
        result?.let {
            repository.saveAllCharacters(it.characterData)
        }

        val loadFromDatabase = repository.loadAllCharacters()
        _characters.postValue(loadFromDatabase.joinToString(separator = ":"))
        view.hideLoading()
    }

}