package com.jokopriyono.belajarmvvm.data

import android.content.Context
import com.jokopriyono.belajarmvvm.data.local.MyDatabase
import com.jokopriyono.belajarmvvm.data.model.Character
import com.jokopriyono.belajarmvvm.data.model.CharacterData
import com.jokopriyono.belajarmvvm.data.model.CharactersResponse
import com.jokopriyono.belajarmvvm.data.remote.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.withContext

class MainRepository(context: Context) {
    private val retrofit = ApiClient.instance
    private val database = MyDatabase.instance(context)

    suspend fun getAllCharacters(): CharactersResponse? {
        var result: CharactersResponse?

        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            result = try {
                retrofit.getAllDisneyCharacters().body()
            } catch (e: Exception) {
                null
            }
        }
        return result
    }

    suspend fun saveAllCharacters(characterData: List<CharacterData>): Boolean {
        var successInsert: Boolean
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            val characters = characterData.map {
                Character(it.id.toLong(), it.name, it.imageUrl)
            }
            successInsert = try {
                database.character().insertAllCharacters(characters)
                true
            } catch (e: Exception) {
                false
            }
        }
        return successInsert
    }

    suspend fun loadAllCharacters(): List<Character> {
        var result: List<Character>
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            result = database.character().getAllCharacters()
        }
        return result
    }

}