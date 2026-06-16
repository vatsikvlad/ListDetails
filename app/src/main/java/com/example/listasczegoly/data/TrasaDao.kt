package com.example.listasczegoly.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrasaDao {
    @Query("SELECT * FROM trasy")
    fun getAllTrasy(): Flow<List<Trasa>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(trasy: List<Trasa>)

    @Query("SELECT COUNT(*) FROM trasy")
    suspend fun getCount(): Int
}
