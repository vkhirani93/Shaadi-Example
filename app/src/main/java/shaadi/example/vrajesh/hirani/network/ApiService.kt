package com.betaout.GOQii.network

import retrofit2.http.GET
import shaadi.example.vrajesh.hirani.model.ProfileModel

interface ApiService {
    @GET(NetworkConstants.EP_FETCH_PROFILES)
    suspend fun fetchProfiles(): ProfileModel
}