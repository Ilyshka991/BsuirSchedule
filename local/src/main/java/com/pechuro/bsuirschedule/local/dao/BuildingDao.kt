package com.pechuro.bsuirschedule.local.dao

import androidx.room.*
import com.pechuro.bsuirschedule.local.entity.AuditoryDB
import com.pechuro.bsuirschedule.local.entity.AuditoryTypeDB
import com.pechuro.bsuirschedule.local.entity.BuildingDB
import kotlinx.coroutines.flow.Flow

@Dao
interface BuildingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg auditoryDB: AuditoryDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg auditoryTypeDB: AuditoryTypeDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg buildingDB: BuildingDB)

    @Transaction
    suspend fun deleteAll() {
        deleteBuildings()
        deleteAuditoriesType()
        deleteAuditories()
    }

    @Query("DELETE FROM auditory")
    suspend fun deleteAuditories()

    @Query("DELETE FROM building")
    suspend fun deleteBuildings()

    @Query("DELETE FROM auditory_type")
    suspend fun deleteAuditoriesType()

    @Query("SELECT * FROM auditory")
    fun getAllAuditories(): Flow<List<AuditoryDB>>

    @Query("SELECT * FROM building")
    fun getAllBuildings(): Flow<List<BuildingDB>>

    @Query("SELECT * FROM auditory_type")
    fun getAllAuditoryTypes(): Flow<List<AuditoryTypeDB>>

    @Query("SELECT * FROM building WHERE id = :id")
    suspend fun getBuildingById(id: Long): BuildingDB

    @Query("SELECT * FROM auditory_type WHERE id = :id")
    suspend fun getAuditoryTypeById(id: Long): AuditoryTypeDB

    @Query("SELECT EXISTS(SELECT 1 FROM auditory)")
    suspend fun isAuditoriesNotEmpty(): Boolean
}