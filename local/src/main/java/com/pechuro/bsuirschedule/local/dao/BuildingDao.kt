package com.pechuro.bsuirschedule.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pechuro.bsuirschedule.local.entity.building.AuditoryCached
import com.pechuro.bsuirschedule.local.entity.building.AuditoryTypeCached
import com.pechuro.bsuirschedule.local.entity.building.BuildingCached
import kotlinx.coroutines.flow.Flow

@Dao
interface BuildingDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(auditory: AuditoryCached): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(auditoryType: AuditoryTypeCached): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(building: BuildingCached): Long


    @Update
    suspend fun update(auditory: AuditoryCached)

    @Update
    suspend fun update(auditoryType: AuditoryTypeCached)

    @Update
    suspend fun update(building: BuildingCached)


    @Transaction
    suspend fun insertOrUpdate(auditory: AuditoryCached) {
        val id = insert(auditory)
        if (id == -1L) update(auditory)
    }

    @Transaction
    suspend fun insertOrUpdate(auditoryType: AuditoryTypeCached) {
        val id = insert(auditoryType)
        if (id == -1L) update(auditoryType)
    }

    @Transaction
    suspend fun insertOrUpdate(building: BuildingCached) {
        val id = insert(building)
        if (id == -1L) update(building)
    }

    @Query("SELECT * FROM auditory")
    fun getAllAuditories(): Flow<List<AuditoryCached>>

    @Query("SELECT * FROM building WHERE id = :id")
    suspend fun getBuildingById(id: Long): BuildingCached

    @Query("SELECT * FROM auditory_type WHERE id = :id")
    suspend fun getAuditoryTypeById(id: Long): AuditoryTypeCached

    @Query("SELECT * FROM auditory WHERE id = :id")
    suspend fun getAuditoryById(id: Long): AuditoryCached

    @Query("SELECT EXISTS(SELECT 1 FROM auditory)")
    suspend fun isAuditoriesNotEmpty(): Boolean
}