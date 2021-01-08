package shaadi.example.vrajesh.hirani.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.betaout.GOQii.state.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import shaadi.example.vrajesh.hirani.R
import shaadi.example.vrajesh.hirani.room.AppDatabase
import shaadi.example.vrajesh.hirani.room.entity.ProfileEntity
import shaadi.example.vrajesh.hirani.ui.adapter.ProfilesAdapter
import shaadi.example.vrajesh.hirani.ui.decorator.ProfileItemDecorator
import shaadi.example.vrajesh.hirani.util.ItemClickListener
import shaadi.example.vrajesh.hirani.viewmodel.MainViewModel

class MainActivity : AppCompatActivity(), ItemClickListener {
    private lateinit var rvProfile: RecyclerView
    private lateinit var listProfiles: MutableList<ProfileEntity>

    private val mainViewModel: MainViewModel by viewModels()
    private val appDatabase: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        subscribeObservers()

        mainViewModel.fetchProfiles(appDatabase)
    }

    override fun onItemClick(position: Int, item: ProfileEntity) {
        val offlineProfilesDao = appDatabase.getOfflineProfilesDao()

        coroutineScope.launch {
            offlineProfilesDao.updateProfile(item.id, item.selectionStatus)
        }

        rvProfile.adapter!!.notifyItemChanged(position)
    }

    private fun initViews() {
        rvProfile = findViewById(R.id.main_rv_profiles)

        listProfiles = ArrayList()

        rvProfile.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvProfile.adapter = ProfilesAdapter(this, listProfiles)
        val cardWidthPixels = resources.displayMetrics.widthPixels * 0.80f
        val cardHintPercent = 0.01f
        rvProfile.addItemDecoration(
                ProfileItemDecorator(
                        this,
                        cardWidthPixels.toInt(),
                        cardHintPercent
                )
        )
    }

    private fun subscribeObservers() {
        mainViewModel.dataStateProfiles.observe(this, { dataState ->
            when (dataState) {
                is DataState.Success<List<ProfileEntity>> -> {
                    loadData(dataState.data)
                }

                is DataState.Failure -> {
                    Toast.makeText(this, dataState.message, Toast.LENGTH_LONG).show()
                }

                is DataState.Error -> {
                    logError(dataState.exception.message)
                }

                else -> {
                    /* Do Nothing */
                }
            }
        })
    }

    private fun loadData(listProfileEntities: List<ProfileEntity>) {
        if (!listProfileEntities.isNullOrEmpty()) {
            val index = listProfiles.size
            listProfiles.addAll(index, listProfileEntities)
            rvProfile.adapter!!.notifyItemRangeChanged(index, listProfileEntities.count())
        }
    }

    private fun logError(message: String?) {
        if (message != null) {
            Log.d(TAG, message)
        }
    }

    private companion object {
        private const val TAG = "MainActivity"
    }
}