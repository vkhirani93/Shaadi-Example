package com.betaout.GOQii.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import shaadi.example.vrajesh.hirani.room.RoomConstants
import shaadi.example.vrajesh.hirani.room.entity.ProfileEntity

@Dao
interface ProfileDao {
    @Insert
    suspend fun insertProfile(profileEntity: ProfileEntity)

    @Insert
    suspend fun insertAllProfiles(listProfileEntities: List<ProfileEntity>)

    @Query("SELECT * FROM ${RoomConstants.ENTITY_PROFILE} ORDER BY ${RoomConstants.COL_CREATED_TIME} DESC LIMIT 10")
    suspend fun getAllProfiles(): List<ProfileEntity>

    @Query("UPDATE ${RoomConstants.ENTITY_PROFILE} SET ${RoomConstants.COL_SELECTION_STATUS} = :selectionStatus WHERE id = :id")
    suspend fun updateProfile(id: Long, selectionStatus: Int): Int
}