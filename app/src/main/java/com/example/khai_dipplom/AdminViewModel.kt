package com.example.khai_dipplom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.ktor.http.content.PartData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel(private val apiService: KtorApi) : ViewModel(){

    var curAdmin: UserClient? = null

    fun doSignUp(username: String,password: String, name1: String, name2: String, name3: String) {

        if (curAdmin != null) {
            viewModelScope.launch {
                _isSucces.emit(
                    apiService.signUp(
                        username,
                        password,
                        name1,
                        name2,
                        name3,
                        curAdmin!!.token
                    )
                )
            }
        }
        else{
            viewModelScope.launch {
                _isSucces.emit(
                    -999
                )
            }
        }
    }

    var _isSucces: MutableSharedFlow<Int> = MutableSharedFlow(replay = 0)
    var isSucces: SharedFlow<Int> = _isSucces




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
                    if (modelClass.isAssignableFrom(AdminViewModel::class.java)) {
                        return AdminViewModel(ktorApi) as T
                    }
                    else
                    {
                        throw IllegalArgumentException("Unknown ViewModel class")
                    }

                }
            }
        }


}