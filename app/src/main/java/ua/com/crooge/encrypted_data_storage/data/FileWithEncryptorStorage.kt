package ua.com.crooge.encrypted_data_storage.data

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import ua.com.crooge.encrypted_data_storage.common.Encryptor
import java.io.File

class FileWithEncryptorStorage(context: Context, val moshi: Moshi, val encryptor: Encryptor) {

    private val filesFolder = File("${context.filesDir}/encrypted_data_with_encryptor/").also { fileFolder ->
        fileFolder.mkdirs()
    }

    inline fun <reified T> save(key: String, obj: T) =
        saveBytes(key, encryptor.encrypt(key, moshi.adapter<T>(T::class.java).toJson(obj)))

    inline fun <reified T> load(key: String) = loadBytes(key)?.let { encryptedBytes ->
        moshi.adapter<T>(T::class.java).fromJson(encryptor.decrypt(key, encryptedBytes))
    }

    inline fun <reified T> saveList(key: String, obj: List<T>) = saveBytes(
        key, encryptor.encrypt(
            key, moshi.adapter<List<T>>(
                Types.newParameterizedType(List::class.java, T::class.java)
            ).toJson(obj)
        )
    )

    inline fun <reified T> loadList(key: String) = loadBytes(key)?.let { encryptedBytes ->
        moshi.adapter<List<T>>(Types.newParameterizedType(List::class.java, T::class.java))
            .fromJson(encryptor.decrypt(key, encryptedBytes))
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