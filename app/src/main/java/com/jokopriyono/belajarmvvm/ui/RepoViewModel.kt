package com.jokopriyono.belajarmvvm.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jokopriyono.belajarmvvm.data.MainRepository

class RepoViewModel(private val repository: MainRepository) : ViewModel() {

    var view: RepoView? = null

    private val _characters = MutableLiveData<String>()
    val characters: LiveData<String> = _characters

    suspend fun fetchAndLoadCharacters() {
        view?.showLoading()
        // fetch data from API
        val result = repository.getAllCharacters()
        result?.let {
            // if data not null, save or replace to table characters
            repository.saveAllCharacters(it.characterData)
        }

        // get latest data characters from local db
        val loadFromDatabase = repository.loadAllCharacters()
        val textString = loadFromDatabase
            .joinToString(separator = ":")
            .substring(0, 100)
        _characters.postValue(textString)
        view?.hideLoading()
    }

}