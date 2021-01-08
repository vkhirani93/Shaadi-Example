package shaadi.example.vrajesh.hirani.util

import android.util.Log
import shaadi.example.vrajesh.hirani.model.ProfileData
import shaadi.example.vrajesh.hirani.room.entity.ProfileEntity
import kotlin.properties.Delegates

object Utility {
    private const val TAG = "Utility"

    internal var isNetworkConnected by Delegates.observable(false) { _, _, newValue ->
        Log.i(TAG, "$newValue")
    }

    /*
    * Usually a generic function is made in order to map the entity with the model
    * */
    internal fun modelToEntityMapper(listProfiles: List<ProfileData>): List<ProfileEntity> {
        val listOfflineProfiles = ArrayList<ProfileEntity>()

        for (i in listProfiles.indices) {
            val offlineProfile = ProfileEntity()

            offlineProfile.gender = listProfiles[i].gender
            offlineProfile.nameTitle = listProfiles[i].name.title
            offlineProfile.firstName = listProfiles[i].name.first
            offlineProfile.lastName = listProfiles[i].name.last
            offlineProfile.city = listProfiles[i].location.city
            offlineProfile.state = listProfiles[i].location.state
            offlineProfile.country = listProfiles[i].location.country
            offlineProfile.postalCode = listProfiles[i].location.postCode
            offlineProfile.age = listProfiles[i].dob.age
            offlineProfile.thumbnail = listProfiles[i].picture.thumbnail
            offlineProfile.imageMedium = listProfiles[i].picture.medium
            offlineProfile.imageLarge = listProfiles[i].picture.large

            listOfflineProfiles.add(offlineProfile)
        }

        return listOfflineProfiles
    }
}