package com.example.todoappsandbox.data

sealed class ResponseResult<T> {
    class Success<T>(var data: List<T>) : ResponseResult<T>()
    class Empty<T> : ResponseResult<T>()
    class Error<T>(val message: String?) : ResponseResult<T>()
}