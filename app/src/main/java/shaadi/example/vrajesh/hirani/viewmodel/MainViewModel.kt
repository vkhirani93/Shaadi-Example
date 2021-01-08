package shaadi.example.vrajesh.hirani.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betaout.GOQii.state.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import shaadi.example.vrajesh.hirani.repository.MainRepository
import shaadi.example.vrajesh.hirani.room.AppDatabase
import shaadi.example.vrajesh.hirani.room.entity.ProfileEntity

class MainViewModel : ViewModel() {
    private val mDataStateProfiles: MutableLiveData<DataState<List<ProfileEntity>>> =
            MutableLiveData()

    val dataStateProfiles: LiveData<DataState<List<ProfileEntity>>>
        get() = mDataStateProfiles

    @ExperimentalCoroutinesApi
    internal fun fetchProfiles(appDatabase: AppDatabase) {
        viewModelScope.launch {
            MainRepository.fetchProfiles(appDatabase)
                    .onEach { dataStateProfiles ->
                        mDataStateProfiles.value = dataStateProfiles
                    }
                    .launchIn(viewModelScope)
        }
    }
}