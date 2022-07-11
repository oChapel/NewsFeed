package ua.com.foxminded.newsfeed.util

sealed class Result<T> {

    override fun toString(): String = when(this) {
        is Success<*> -> "Success[data=" + this.data.toString() + "]"
        is Error<*> -> "Error[exception=" + this.error.toString() + "]"
    }

    val isSuccessful: Boolean
        get() = this is Success<*>

    class Success<T>(val data: T) : Result<T>()
    class Error<T>(val error: Throwable?) : Result<T>()
}
