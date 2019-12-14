package ua.com.crooge.encrypted_data_storage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.android.synthetic.main.activity_main.start
import ua.com.crooge.encrypted_data_storage.common.Encryptor
import ua.com.crooge.encrypted_data_storage.common.getMethodExecutionTime
import ua.com.crooge.encrypted_data_storage.data.EncryptedFileStorage
import ua.com.crooge.encrypted_data_storage.data.EncryptedSharedPreferencesStorage
import ua.com.crooge.encrypted_data_storage.data.FileStorage
import ua.com.crooge.encrypted_data_storage.data.FileStorageWithEncryptor
import ua.com.crooge.encrypted_data_storage.data.SharedPreferencesStorage
import ua.com.crooge.encrypted_data_storage.model.User

class MainActivity : AppCompatActivity() {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val encryptor = Encryptor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        start.setOnClickListener {

            /* ============== Users ============== */

            val billie = User("Billie", "Eilish", 17, "Los Angeles")
            val elizabeth = User("Elizabeth", "Woolridge Grant", 34, "New York City")
            val angelina = User("Angelina", "Jolie", 44, "Los Angeles")

            /* =================================== */



            /* ============== EncryptedFileStorage ============== */

            val encryptedFileStorage = EncryptedFileStorage(applicationContext, moshi)

//        encryptedFileStorage.save("Angelina Jolie", angelina)
            var time = getMethodExecutionTime { encryptedFileStorage.save("Angelina Jolie", angelina) }

//        val angelinaJolieEFS = encryptedFileStorage.load<User>("Angelina Jolie")
            time = getMethodExecutionTime { encryptedFileStorage.load<User>("Angelina Jolie") }

//        encryptedFileStorage.saveList("artists", listOf(billie, elizabeth, angelina))
            time = getMethodExecutionTime { encryptedFileStorage.saveList("artists", listOf(billie, elizabeth, angelina)) }

//        val artistsEFS = encryptedFileStorage.loadList<User>("artists")
            time = getMethodExecutionTime { encryptedFileStorage.loadList<User>("artists") }

            /* ================================================== */



            /* ============== EncryptedSharedPreferencesStorage ============== */

            val encryptedSharedPreferencesStorage = EncryptedSharedPreferencesStorage(applicationContext, moshi)

//        encryptedSharedPreferencesStorage.putObject("Angelina Jolie", angelina)
            time = getMethodExecutionTime { encryptedSharedPreferencesStorage.putObject("Angelina Jolie", angelina) }

//        val angelinaJolieESP = encryptedSharedPreferencesStorage.getObject<User>("Angelina Jolie")
            time = getMethodExecutionTime { encryptedSharedPreferencesStorage.getObject<User>("Angelina Jolie") }

//        encryptedSharedPreferencesStorage.putList("artists", listOf(billie, elizabeth, angelina))
            time = getMethodExecutionTime { encryptedSharedPreferencesStorage.putList("artists", listOf(billie, elizabeth, angelina)) }

//        val artistsESP = encryptedSharedPreferencesStorage.getList<User>("artists")
            time = getMethodExecutionTime { encryptedSharedPreferencesStorage.getList<User>("artists") }

            /* =============================================================== */



            /* ============== FileStorageWithEncryptor ============== */

            val fileStorageWithEncryptor = FileStorageWithEncryptor(applicationContext, moshi, encryptor)

//        fileStorageWithEncryptor.save("Angelina Jolie", angelina)
            time = getMethodExecutionTime { fileStorageWithEncryptor.save("Angelina Jolie", angelina) }

//        val angelinaJolieFSWE = fileStorageWithEncryptor.load<User>("Angelina Jolie")
            time = getMethodExecutionTime { fileStorageWithEncryptor.load<User>("Angelina Jolie") }

//        fileStorageWithEncryptor.saveList("artists", listOf(billie, elizabeth, angelina))
            time = getMethodExecutionTime { fileStorageWithEncryptor.saveList("artists", listOf(billie, elizabeth, angelina)) }

//        val artistsFSWE = fileStorageWithEncryptor.loadList<User>("artists")
            time = getMethodExecutionTime { fileStorageWithEncryptor.loadList<User>("artists") }

            /* ================================================== */



            /* ============== FileStorage ============== */

            val fileStorage = FileStorage(applicationContext, moshi)

//        fileStorage.save("Angelina Jolie", angelina)
            time = getMethodExecutionTime { fileStorage.save("Angelina Jolie", angelina) }

//        val angelinaJolieFS = fileStorage.load<User>("Angelina Jolie")
            time = getMethodExecutionTime { fileStorage.load<User>("Angelina Jolie") }

//        fileStorage.saveList("artists", listOf(billie, elizabeth, angelina))
            time = getMethodExecutionTime { fileStorage.saveList("artists", listOf(billie, elizabeth, angelina)) }

//        val artistsFS = fileStorage.loadList<User>("artists")
            time = getMethodExecutionTime { fileStorage.loadList<User>("artists") }

            /* ================================================== */



            /* ============== SharedPreferencesStorage ============== */

            val sharedPreferencesStorage = SharedPreferencesStorage(applicationContext, moshi)

//        sharedPreferencesStorage.putObject("Angelina Jolie", angelina)
            time = getMethodExecutionTime { sharedPreferencesStorage.putObject("Angelina Jolie", angelina) }

//        val angelinaJolieSP = sharedPreferencesStorage.getObject<User>("Angelina Jolie")
            time = getMethodExecutionTime { sharedPreferencesStorage.getObject<User>("Angelina Jolie") }

//        sharedPreferencesStorage.putList("artists", listOf(billie, elizabeth, angelina))
            time = getMethodExecutionTime { sharedPreferencesStorage.putList("artists", listOf(billie, elizabeth, angelina)) }

//        val artistsSP = sharedPreferencesStorage.getList<User>("artists")
            time = getMethodExecutionTime { sharedPreferencesStorage.getList<User>("artists") }

            /* =============================================================== */
        }
    }
}
