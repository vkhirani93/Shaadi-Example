package shaadi.example.vrajesh.hirani.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.betaout.GOQii.room.dao.ProfileDao
import shaadi.example.vrajesh.hirani.room.entity.ProfileEntity

@Database(
        entities = [ProfileEntity::class],
        version = RoomConstants.DATABASE_VERSION,
        exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    internal abstract fun getOfflineProfilesDao(): ProfileDao

    companion object {
        /*
        * Singleton prevents multiple instances of database opening at the same time.
        * */
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            /*
            * If the INSTANCE is:
            * not null, then return it.
            * null, then create the database
            * */

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        RoomConstants.DATABASE_NAME
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}