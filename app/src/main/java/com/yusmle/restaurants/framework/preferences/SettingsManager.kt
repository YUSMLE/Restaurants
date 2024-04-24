package com.yusmle.restaurants.framework.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.squareup.moshi.Moshi
import com.yusmle.restaurants.common.DispatcherProvider
import com.yusmle.restaurants.common.DispatcherProviderImpl
import com.yusmle.restaurants.framework.service.location.Location
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import kotlin.coroutines.CoroutineContext

/**
 * In order to use DataStore correctly always keep in mind the following rules:
 *
 *   1. Never create more than one instance of [DataStore] for a given file in the same process.
 *      Doing so can break all DataStore functionality. If there are multiple DataStores active for
 *      a given file in the same process, DataStore will throw [IllegalStateException] when reading
 *      or updating data.
 *
 *   2. The generic type of the DataStore must be immutable. Mutating a type used in DataStore
 *      invalidates any guarantees that DataStore provides and creates potentially serious,
 *      hard-to-catch bugs. It is strongly recommended that you use protocol buffers which provide
 *      immutability guarantees, a simple API and efficient serialization.
 *
 *   3. Never mix usages of [SingleProcessDataStore] and [MultiProcessDataStore] for the same file.
 *      If you intend to access the DataStore from more than one process always use
 *      [MultiProcessDataStore].
 *
 *   For more details, @see:
 *   [https://developer.android.com/topic/libraries/architecture/datastore#correct_usage]
 */

private val Context.appDataDataStore: DataStore<Preferences>
    by preferencesDataStore(PreferencesKeys.PREFERENCES_APP_DATA)

class SettingsManager(
    private val context: Context,
    private val dispatcherProvider: DispatcherProvider = DispatcherProviderImpl()
) {

    private val appDataStore by lazy { context.appDataDataStore }

    private val moshi: Moshi by lazy {
        Moshi.Builder().build()
    }

    val syncedLocationPreferences by lazy {
        StringPreferences(
            dispatcherProvider,
            appDataStore,
            PreferencesKeys.AppData.APP_DATA_SYNCED_LOCATION, {
                moshi.adapter(Location::class.java).toJson(it)
            }, {
                it?.let { moshi.adapter(Location::class.java).fromJson(it) }
            }
        )
    }
}

/// Base Preferences DataStore

abstract class PreferencesDataStore<T, PV>(
    private val dispatcherProvider: DispatcherProvider,
    private val dataStore: DataStore<Preferences>,
    private val preferencesKeyName: String
) : CoroutineScope {

    private var job: CompletableJob = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = dispatcherProvider.io() + job

    var value: T?
        get() {
            return runBlocking {
                // Gets a snapshot of the stored data without subscribing to the Flow.
                // You should also handle IOExceptions here.
                getOriginValue(dataStore.data.first()[preferencesKey])
            }
        }
        set(value) {
            launch {
                dataStore.edit { preferences ->
                    getPrimitiveValue(value)?.let {
                        preferences[preferencesKey] = it
                    } ?: preferences.clear()
                }
            }
        }

    val source: Flow<T?> by lazy {
        dataStore.data.catch {
            if (it is IOException) {
                // DEBUG
                it.printStackTrace()

                emit(emptyPreferences())
            }
            else throw it
        }.map { preferences ->
            getOriginValue(preferences[preferencesKey])
        }
    }

    protected abstract val preferencesKey: Preferences.Key<PV>

    protected abstract fun getPrimitiveValue(value: T?): PV?

    protected abstract fun getOriginValue(value: PV?): T?
}

/// Providing Primitive-type Preferences

class LongPreferences<T>(
    dispatcherProvider: DispatcherProvider,
    dataStore: DataStore<Preferences>,
    preferencesKeyName: String,
    private val toPrimitiveValue: (T?) -> Long?,
    private val toOriginValue: (Long?) -> T?
) : PreferencesDataStore<T, Long>(
    dispatcherProvider, dataStore, preferencesKeyName
) {

    override val preferencesKey = longPreferencesKey(preferencesKeyName)

    override fun getPrimitiveValue(value: T?) = toPrimitiveValue(value)

    override fun getOriginValue(value: Long?) = toOriginValue(value)
}

class DoublePreferences<T>(
    dispatcherProvider: DispatcherProvider,
    dataStore: DataStore<Preferences>,
    preferencesKeyName: String,
    private val toPrimitiveValue: (T?) -> Double?,
    private val toOriginValue: (Double?) -> T?
) : PreferencesDataStore<T, Double>(
    dispatcherProvider, dataStore, preferencesKeyName
) {

    override val preferencesKey = doublePreferencesKey(preferencesKeyName)

    override fun getPrimitiveValue(value: T?) = toPrimitiveValue(value)

    override fun getOriginValue(value: Double?) = toOriginValue(value)
}

class BooleanPreferences<T>(
    dispatcherProvider: DispatcherProvider,
    dataStore: DataStore<Preferences>,
    preferencesKeyName: String,
    private val toPrimitiveValue: (T?) -> Boolean?,
    private val toOriginValue: (Boolean?) -> T?
) : PreferencesDataStore<T, Boolean>(
    dispatcherProvider, dataStore, preferencesKeyName
) {

    override val preferencesKey = booleanPreferencesKey(preferencesKeyName)

    override fun getPrimitiveValue(value: T?) = toPrimitiveValue(value)

    override fun getOriginValue(value: Boolean?) = toOriginValue(value)
}

class StringPreferences<T>(
    dispatcherProvider: DispatcherProvider,
    dataStore: DataStore<Preferences>,
    preferencesKeyName: String,
    private val toPrimitiveValue: (T?) -> String?,
    private val toOriginValue: (String?) -> T?
) : PreferencesDataStore<T, String>(
    dispatcherProvider, dataStore, preferencesKeyName
) {

    override val preferencesKey = stringPreferencesKey(preferencesKeyName)

    override fun getPrimitiveValue(value: T?) = toPrimitiveValue(value)

    override fun getOriginValue(value: String?) = toOriginValue(value)
}
