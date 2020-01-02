package com.pechuro.bsuirschedule.local.dao

import androidx.room.*
import com.pechuro.bsuirschedule.local.entity.AuditoryCached
import com.pechuro.bsuirschedule.local.entity.AuditoryTypeCached
import com.pechuro.bsuirschedule.local.entity.BuildingCached
import kotlinx.coroutines.flow.Flow

@Dao
interface BuildingDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg auditory: AuditoryCached)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg auditoryType: AuditoryTypeCached)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg building: BuildingCached)

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