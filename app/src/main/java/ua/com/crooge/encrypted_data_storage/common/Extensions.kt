package ua.com.crooge.encrypted_data_storage.common

fun getMethodExecutionTime(action: () -> Unit): Long {
    val startTime = System.nanoTime()
    action()
    val endTime = System.nanoTime()

    return (endTime - startTime) / 1000000
}