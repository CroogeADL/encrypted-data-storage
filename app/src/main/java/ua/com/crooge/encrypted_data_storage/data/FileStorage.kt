package ua.com.crooge.encrypted_data_storage.data

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.File

class FileStorage(context: Context, val moshi: Moshi) {

    private val filesFolder = File("${context.filesDir}/data/").also { fileFolder ->
        fileFolder.mkdirs()
    }

    inline fun <reified T> save(key: String, obj: T) =
        getFile(key)?.writeText(moshi.adapter<T>(T::class.java).toJson(obj))

    inline fun <reified T> load(key: String) = getFile(key, write = false)?.let { file ->
        moshi.adapter<T>(T::class.java).fromJson(file.readText())
    }

    inline fun <reified T> saveList(key: String, obj: List<T>) = getFile(key)?.writeText(
        moshi.adapter<List<T>>(
            Types.newParameterizedType(List::class.java, T::class.java)
        ).toJson(obj)
    )

    inline fun <reified T> loadList(key: String) =
        getFile(key, write = false)?.let { file ->
            moshi.adapter<List<T>>(Types.newParameterizedType(List::class.java, T::class.java))
                .fromJson(String(file.readBytes()))
        }

    fun saveBytes(key: String, bytes: ByteArray) = getFile(key)?.writeBytes(bytes)

    fun loadBytes(key: String) = getFile(key, write = false)?.readBytes()

    fun remove(key: String) = getFile(key)?.delete()

    fun clear() = filesFolder.listFiles()?.forEach { file ->
        file.takeIf { !file.isDirectory }?.delete()
    }

    fun getFile(key: String, write: Boolean = true) =
        File(filesFolder.absolutePath, key).takeIf { file -> file.exists() || write }
}