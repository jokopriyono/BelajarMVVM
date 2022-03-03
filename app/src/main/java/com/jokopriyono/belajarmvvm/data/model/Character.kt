package com.jokopriyono.belajarmvvm.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "characters")
@Parcelize
data class Character(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo var name: String,
    @ColumnInfo(name = "image_url") var imageUrl: String,
) : Parcelable
