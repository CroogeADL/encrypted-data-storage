package ua.com.crooge.encrypted_data_storage.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Base64
import androidx.core.content.edit
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import ua.com.crooge.encrypted_data_storage.common.Encryptor

private const val SHARED_PREFERENCES_WITH_ENCRYPTOR_STORAGE_NAME = "shared_preferences_with_encryptor_storage_name"

class SharedPreferencesWithEncryptorStorage(context: Context, val moshi: Moshi, val encryptor: Encryptor) :
    SharedPreferences by context.getSharedPreferences(
        SHARED_PREFERENCES_WITH_ENCRYPTOR_STORAGE_NAME,
        MODE_PRIVATE
    ) {

    inline fun <reified T> putObject(key: String, obj: T) =
        saveBytes(key, encryptor.encrypt(key, moshi.adapter<T>(T::class.java).toJson(obj)))

    inline fun <reified T> getObject(key: String) = loadBytes(key)?.let { encryptedBytes ->
        moshi.adapter<T>(T::class.java).fromJson(encryptor.decrypt(key, encryptedBytes))
    }

    inline fun <reified T> putList(key: String, obj: List<T>) = saveBytes(
        key, encryptor.encrypt(
            key, moshi.adapter<List<T>>(
                Types.newParameterizedType(List::class.java, T::class.java)
            ).toJson(obj)
        )
    )

    inline fun <reified T> getList(key: String) = loadBytes(key)?.let { encryptedBytes ->
        moshi.adapter<List<T>>(Types.newParameterizedType(List::class.java, T::class.java))
            .fromJson(encryptor.decrypt(key, encryptedBytes))
    }

    fun saveBytes(key: String, bytes: ByteArray) = putString(key, Base64.encodeToString(bytes, Base64.DEFAULT))

    fun loadBytes(key: String) = Base64.decode(getString(key), Base64.DEFAULT)

    fun putString(key: String, value: String) = edit { putString(key, value) }

    fun getString(key: String) = getString(key, null)

    fun remove(key: String) = edit { remove(key) }

    fun clear() = edit { clear() }
}