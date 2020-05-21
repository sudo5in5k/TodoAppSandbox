package com.example.todoappsandbox.data

sealed class State<out T> {
    object Loading : State<Nothing>()
    data class Success<out T>(val data: List<T>): State<T>()
    data class Error(val exception: Exception): State<Nothing>()
}