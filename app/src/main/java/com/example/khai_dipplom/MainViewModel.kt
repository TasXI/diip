package com.example.khai_dipplom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val apiService: KtorApi) : ViewModel() {


    private val _userFlow: MutableSharedFlow<UserClient?> = MutableSharedFlow(replay = 0)
    val userFlow: SharedFlow<UserClient?> = _userFlow

    fun signIn(username: String,password: String){

        viewModelScope.launch {
            _userFlow.emit(apiService.signIn(username, password))
        }

    }





    companion object {
        lateinit var ktorApi: KtorApi

        public fun setApi(api: KtorApi){
            ktorApi = api
        }

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                    return MainViewModel(ktorApi) as T
                }
                else
                {
                    throw IllegalArgumentException("Unknown ViewModel class")
                }

            }
        }
    }
}