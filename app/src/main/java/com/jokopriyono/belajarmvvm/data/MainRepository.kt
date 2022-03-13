package com.jokopriyono.belajarmvvm.data

import com.jokopriyono.belajarmvvm.data.local.CharactersDao
import com.jokopriyono.belajarmvvm.data.model.Character
import com.jokopriyono.belajarmvvm.data.remote.ApiService
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService,
    private val charactersDao: CharactersDao,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getAllCharacters(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        val characters = charactersDao.getAllCharacters()

        if (characters.isEmpty()) {
            val response = apiService.getAllDisneyCharacters()
            response.suspendOnSuccess {
                val charactersConvert = data.characterData.map {
                    Character(it.id.toLong(), it.name, it.imageUrl)
                }
                charactersDao.insertAllCharacters(charactersConvert)
                emit(charactersConvert)
            }.onError {
                onError(this.message().toString())
            }.onException {
                onError(message)
            }
        } else {
            emit(charactersDao.getAllCharacters())
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

}