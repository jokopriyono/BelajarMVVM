package com.jokopriyono.belajarmvvm.data

import android.util.Log
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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService,
    private val charactersDao: CharactersDao,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun uploadFileAndGetResult(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
        url: String,
        file: MultipartBody.Part,
        token: RequestBody,
    ) = flow {
        val response = apiService.uploadFile(url, file, token)
        response.suspendOnSuccess {
            emit(this.data)
        }.onError {
            Log.e("pesan", this.message())
            onError(this.message())
        }.onException {
            Log.e("pesan", this.message())
            onError(message)
        }
    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

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
                onError(this.message())
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