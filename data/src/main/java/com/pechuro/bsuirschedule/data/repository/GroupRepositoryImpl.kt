package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.domain.entity.Group
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import kotlinx.coroutines.flow.Flow

class GroupRepositoryImpl : IGroupRepository {

    override fun getAll(): Flow<List<Group>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getByNumber(number: String): Group {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun update() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun isStored(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}