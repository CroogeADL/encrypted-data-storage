package ua.com.crooge.encrypted_data_storage.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

private const val SHARED_PREFERENCES_STORAGE_NAME = "shared_preferences_storage"

class SharedPreferencesStorage(context: Context, val moshi: Moshi) :
    SharedPreferences by context.getSharedPreferences(
        SHARED_PREFERENCES_STORAGE_NAME,
        MODE_PRIVATE
    ) {

    inline fun <reified T> putObject(key: String, obj: T) =
        putString(key, moshi.adapter<T>(T::class.java).toJson(obj))

    inline fun <reified T> getObject(key: String) = getString(key)?.let { jsonObj ->
        moshi.adapter<T>(T::class.java).fromJson(jsonObj)
    }

    inline fun <reified T> putList(key: String, obj: List<T>) = putString(
        key, moshi.adapter<List<T>>(
            Types.newParameterizedType(List::class.java, T::class.java)
        ).toJson(obj)
    )

    inline fun <reified T> getList(key: String) = getString(key)?.let { jsonObj ->
        moshi.adapter<List<T>>(Types.newParameterizedType(List::class.java, T::class.java))
            .fromJson(jsonObj)
    }

    fun putString(key: String, value: String) = edit { putString(key, value) }

    fun getString(key: String) = getString(key, null)

    fun remove(key: String) = edit { remove(key) }

    fun clear() = edit { clear() }
}