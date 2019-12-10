# Encrypted Data Storage

Example of using [`EncryptedFile`](https://developer.android.com/topic/security/data#read-files) and [`EncryptedSharedPreferences`](https://developer.android.com/topic/security/data#edit-shared-preferences).

Considered 2 types of storage which intended for persisting of `Objects` and `Lists`. First one based on `SharedPreferences` and another - on `File`. The [`Android Jetpack Security library`](https://developer.android.com/topic/security/data) is used for data encryption.