package com.example.inventory.data


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LoginDao {

    // Insert a single entry into the database
    @Insert
    suspend fun insertDetails(data: LoginTable)

    // Retrieve all entries as LiveData
    @Query("SELECT * FROM LoginDetails")
    fun getDetails(): LiveData<List<LoginTable>>


    // Delete all data from the table
    @Query("DELETE FROM LoginDetails")
    suspend fun deleteAllData()
}
