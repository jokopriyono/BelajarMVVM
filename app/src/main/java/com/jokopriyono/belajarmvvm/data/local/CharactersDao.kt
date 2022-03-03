package com.jokopriyono.belajarmvvm.data.local

import androidx.room.*
import com.jokopriyono.belajarmvvm.data.model.Character

@Dao
interface CharactersDao {

    @Query("select * from characters")
    fun getAllCharacters(): List<Character>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCharacters(characters: List<Character>)

}