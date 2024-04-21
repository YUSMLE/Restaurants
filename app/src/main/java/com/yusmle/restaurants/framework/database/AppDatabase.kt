package com.yusmle.restaurants.framework.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yusmle.restaurants.features.restaurantslist.data.RestaurantDao
import com.yusmle.restaurants.features.restaurantslist.data.RestaurantEntity

@Database(
    version = 1,
    exportSchema = true,
    entities = [RestaurantEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    // Define the DAOs that work with the database.
    // Provide an abstract "getter" method for each @Dao.

    abstract fun restaurantDao(): RestaurantDao

    companion object {

        private const val DATABASE_NAME = "app_database"

        // Make the `AppDatabase` a singleton to prevent having
        // multiple instances of the database opened at the same time.

        private val LOCK = Any()

        @Volatile
        private var instance: AppDatabase? = null

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            ).allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()

        // Consider preparing callback for when the database is opened.
        // To delete all content and repopulate the database whenever the app is started,
        // you create a `RoomDatabase.Callback` and override the `onOpen()` method.
        // Because you cannot do Room database operations on the UI thread, `onOpen()` creates and
        // executes an `AsyncTask` to add content to the database.
    }
}
