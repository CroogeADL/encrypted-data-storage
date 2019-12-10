package ua.com.crooge.encrypted_data_storage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import ua.com.crooge.encrypted_data_storage.data.EncryptedFileStorage
import ua.com.crooge.encrypted_data_storage.data.EncryptedSharedPreferencesStorage
import ua.com.crooge.encrypted_data_storage.model.User

class MainActivity : AppCompatActivity() {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* ============== Users ============== */

        val billie = User("Billie", "Eilish", 17, "Los Angeles")
        val elizabeth = User("Elizabeth", "Woolridge Grant", 34, "New York City")
        val angelina = User("Angelina", "Jolie", 44, "Los Angeles")

        /* =================================== */

        /* ============== EncryptedFileStorage ============== */

        val encryptedFileStorage = EncryptedFileStorage(applicationContext, moshi)

        encryptedFileStorage.save("Angelina Jolie", angelina)
        val angelinaJolie = encryptedFileStorage.load<User>("Angelina Jolie")

        encryptedFileStorage.saveList("artists", listOf(billie, elizabeth, angelina))
        val artists = encryptedFileStorage.loadList<User>("artists")

        println(artists)

        /* ================================================== */

        /* ============== EncryptedSharedPreferencesStorage ============== */

        val encryptedSharedPreferencesStorage = EncryptedSharedPreferencesStorage(applicationContext, moshi)

        encryptedSharedPreferencesStorage.putObject("Angelina Jolie", angelina)
        val _angelinaJolie = encryptedSharedPreferencesStorage.getObject<User>("Angelina Jolie")

        encryptedSharedPreferencesStorage.putList("artists", listOf(billie, elizabeth, angelina))
        val _artists = encryptedSharedPreferencesStorage.getList<User>("artists")

        println(_artists)

        /* =============================================================== */
    }
}
