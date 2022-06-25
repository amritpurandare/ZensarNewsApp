package com.amrit.zensarnewsapp.network

data class Response<out T>(val status: ApiStatus, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): Response<T> =
            Response(ApiStatus.SUCCESS, data, null)

        fun <T> error(data: T?, message: String?): Response<T> =
            Response(ApiStatus.ERROR, data, message)

        fun <T> loading(data: T?): Response<T> =
            Response(ApiStatus.LOADING, data, null)
    }
}
