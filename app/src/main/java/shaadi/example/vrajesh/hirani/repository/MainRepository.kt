package shaadi.example.vrajesh.hirani.repository

import com.betaout.GOQii.network.ApiService
import com.betaout.GOQii.network.NetworkConstants
import com.betaout.GOQii.network.RetrofitService
import com.betaout.GOQii.state.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import shaadi.example.vrajesh.hirani.room.AppDatabase
import shaadi.example.vrajesh.hirani.room.entity.ProfileEntity
import shaadi.example.vrajesh.hirani.util.Utility

object MainRepository {
    private var apiService: ApiService = RetrofitService.createService(ApiService::class.java)

    private const val NO_INTERNET_TOAST = "No Internet Connection..."

    internal suspend fun fetchProfiles(appDatabase: AppDatabase): Flow<DataState<List<ProfileEntity>>> = flow {
        emit(DataState.Loading)
        try {
            val offlineProfilesDao = appDatabase.getOfflineProfilesDao()
            var listOfflineProfiles = offlineProfilesDao.getAllProfiles()

            if (listOfflineProfiles.isNullOrEmpty() && !Utility.isNetworkConnected) {
                emit(DataState.Failure(NO_INTERNET_TOAST))
            } else if (listOfflineProfiles.isNullOrEmpty() && Utility.isNetworkConnected) {
                val profileResponse = apiService.fetchProfiles()
                val listProfiles = profileResponse.listProfiles
                if (!listProfiles.isNullOrEmpty()) {
                    listOfflineProfiles = Utility.modelToEntityMapper(listProfiles)
                    saveProfilesOffline(appDatabase, listOfflineProfiles)
                    listOfflineProfiles = offlineProfilesDao.getAllProfiles()
                    emit(DataState.Success(listOfflineProfiles))
                } else {
                    emit(DataState.Failure(NetworkConstants.FAILURE_REASON))
                }
            } else {
                emit(DataState.Success(listOfflineProfiles))
            }
            emit(DataState.NoState)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            e.printStackTrace()
        }
    }

    private suspend fun saveProfilesOffline(
            appDatabase: AppDatabase,
            listProfileEntities: List<ProfileEntity>
    ) {
        val offlineProfilesDao = appDatabase.getOfflineProfilesDao()
        offlineProfilesDao.insertAllProfiles(listProfileEntities)
    }
}