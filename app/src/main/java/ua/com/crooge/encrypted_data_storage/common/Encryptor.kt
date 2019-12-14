package ua.com.crooge.encrypted_data_storage.common

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec

private const val TRANSFORMATION = "AES/CBC/PKCS7Padding"
private const val ANDROID_KEY_STORE = "AndroidKeyStore"
private const val INITIALIZATION_VECTOR_SIZE = 16

class Encryptor {

    private val keyStore: KeyStore by lazy {
        KeyStore.getInstance(ANDROID_KEY_STORE).also { keyStore -> keyStore.load(null) }
    }

    fun encrypt(alias: String, text: String) = with(Cipher.getInstance(TRANSFORMATION)) {
        init(Cipher.ENCRYPT_MODE, createSecretKey(alias))
        iv.plus(doFinal(Base64.encode(text.toByteArray(), Base64.DEFAULT)))
    }

    fun decrypt(alias: String, encryptedBytes: ByteArray) = with(Cipher.getInstance(TRANSFORMATION)) {
        val initializationVector = encryptedBytes.take(INITIALIZATION_VECTOR_SIZE).toByteArray()
        val encryptedData = encryptedBytes.drop(INITIALIZATION_VECTOR_SIZE).toByteArray()

        val secreteKey = (keyStore.getEntry(alias, null) as KeyStore.SecretKeyEntry).secretKey
        init(Cipher.DECRYPT_MODE, secreteKey, IvParameterSpec(initializationVector))

        String(Base64.decode(doFinal(encryptedData), Base64.DEFAULT))
    }

    private fun createSecretKey(alias: String) = with(
        KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
    ) {
        init(
            KeyGenParameterSpec.Builder(
                alias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build()
        )
        generateKey()
    }
}