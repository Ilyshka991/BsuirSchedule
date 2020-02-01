package com.pechuro.bsuirschedule.local.dao

import androidx.room.*
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


    @Transaction
    suspend fun deleteAll() {
        deleteAllBuildings()
        deleteAllAuditoriesTypes()
        deleteAllAuditories()
    }


    @Query("DELETE FROM auditory")
    suspend fun deleteAllAuditories()

    @Query("DELETE FROM building")
    suspend fun deleteAllBuildings()

    @Query("DELETE FROM auditory_type")
    suspend fun deleteAllAuditoriesTypes()

    @Query("SELECT * FROM auditory")
    fun getAllAuditories(): Flow<List<AuditoryCached>>

    @Query("SELECT * FROM building")
    fun getAllBuildings(): Flow<List<BuildingCached>>

    @Query("SELECT * FROM auditory_type")
    fun getAllAuditoryTypes(): Flow<List<AuditoryTypeCached>>

    @Query("SELECT * FROM building WHERE id = :id")
    suspend fun getBuildingById(id: Long): BuildingCached

    @Query("SELECT * FROM auditory_type WHERE id = :id")
    suspend fun getAuditoryTypeById(id: Long): AuditoryTypeCached

    @Query("SELECT EXISTS(SELECT 1 FROM auditory)")
    suspend fun isAuditoriesNotEmpty(): Boolean
}