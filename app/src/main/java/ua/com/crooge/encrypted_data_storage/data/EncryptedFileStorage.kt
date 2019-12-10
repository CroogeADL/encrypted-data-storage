package ua.com.crooge.encrypted_data_storage.data

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.File

class EncryptedFileStorage(private val context: Context, val moshi: Moshi) {

    private val filesFolder = File("${context.filesDir}").also { fileFolder ->
        fileFolder.mkdirs()
    }

    inline fun <reified T> save(key: String, obj: T) = remove(key).also {
        getEncryptedFile(key)?.openFileOutput()?.bufferedWriter()?.use { writer ->
            writer.write(moshi.adapter<T>(T::class.java).toJson(obj))
        }
    }

    inline fun <reified T> load(key: String) = getEncryptedFile(key, write = false)?.let { file ->
        moshi.adapter<T>(T::class.java)
            .fromJson(String(file.openFileInput().readBytes()))
    }

    inline fun <reified T> saveList(key: String, obj: List<T>) = remove(key).also {
        getEncryptedFile(key)?.openFileOutput()?.bufferedWriter()?.use { writer ->
            writer.write(
                moshi.adapter<List<T>>(
                    Types.newParameterizedType(List::class.java, T::class.java)
                ).toJson(obj)
            )
        }
    }

    inline fun <reified T> loadList(key: String) =
        getEncryptedFile(key, write = false)?.let { file ->
            moshi.adapter<List<T>>(Types.newParameterizedType(List::class.java, T::class.java))
                .fromJson(String(file.openFileInput().readBytes()))
        }

    fun remove(key: String) = getFile(key).takeIf { file -> file.exists() }?.delete()

    fun clear() = filesFolder.listFiles()?.forEach { file ->
        file.takeIf { !file.isDirectory }?.delete()
    }

    fun getEncryptedFile(key: String, write: Boolean = true) = with(getFile(key)) {
        EncryptedFile.Builder(
            this,
            context,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build().takeIf { exists() || write }
    }

    private fun getFile(key: String) = File(filesFolder.absolutePath, key)
}