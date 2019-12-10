package ua.com.crooge.encrypted_data_storage.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

private const val SHARED_PREFERENCES_STORAGE_NAME = "encrypted_shared_preferences_storage"

class EncryptedSharedPreferencesStorage(context: Context, val moshi: Moshi) :
    SharedPreferences by EncryptedSharedPreferences.create(
        SHARED_PREFERENCES_STORAGE_NAME,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
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

    fun clear() = edit(commit = true) { clear() }
}