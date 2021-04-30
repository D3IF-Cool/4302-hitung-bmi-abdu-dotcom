package org.d3if3067.hitungbmi.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BmiDao {
    @Insert
    fun insert(bmi: BmiEntity)

    @Query("SELECT * FROM bmi ORDER BY id DESC")
    fun getLastBmi(): LiveData<List<BmiEntity>>
    }
