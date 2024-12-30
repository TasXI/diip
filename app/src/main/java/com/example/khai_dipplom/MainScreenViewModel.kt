package com.example.khai_dipplom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainScreenViewModel (private val apiService: KtorApi) : ViewModel() {


    private lateinit var user: UserClient

    fun getUserPr(): User{
        return user.user
    }

    var appClient: KtorClient? = null

    var curChat: ChatRecGet? = null

    private var _curUser = MutableSharedFlow<UserClient?>(replay = 1)
    var curUser: SharedFlow<UserClient?> = _curUser

    private var _userlist = MutableSharedFlow<List<UserInfo>?>(replay = 0)
    var userlist: SharedFlow<List<UserInfo>?> = _userlist

    private var _messagesFlow = MutableSharedFlow<List<Message>?>(replay = 0)
    var messagesFlow = _messagesFlow.asSharedFlow()

    private var _chatlist = MutableSharedFlow<List<ChatRecGet>?>(replay = 1)
    var chatlist: SharedFlow<List<ChatRecGet>?> = _chatlist

    private var _currentChat = MutableSharedFlow<ChatRecGet?>(replay = 1)
    var currentChat = _currentChat.asSharedFlow()

    private var _openChatFrag = MutableSharedFlow<ChatRecGet?>(replay = 0)
    var openChatFrag: SharedFlow<ChatRecGet?> = _openChatFrag

    fun openChatFragment(chat: ChatRecGet){
        viewModelScope.launch {
            _openChatFrag.emit(chat)
        }
    }

    var stateToUpdate: SharedFlow<String?> = MutableSharedFlow()

    fun sendkMessage(text: String) {
        viewModelScope.launch {
            if (curChat != null)
                apiService.addMessageById(
                    Message(user.id, user.user.username, text),
                    curChat!!,
                    user.token
                )
        }
    }

    fun setChatContent(chat: ChatRecGet){
        curChat = chat
        getMessages(chat)
        viewModelScope.launch {
            _currentChat.emit(chat)
        }
    }

    fun getMessages(chat: ChatRecGet){

        viewModelScope.launch {
        _messagesFlow.emit(apiService.getMessagesById(chat, user.token))
        }
    }



    fun getCurrentUserId() :String{
        return user.id
    }

    fun getChats(){
        viewModelScope.launch {
            _chatlist.emit(apiService.getUserChats(user.id, user.token))
        }
    }


    fun setUser(user: UserClient){
        this.user = user
        viewModelScope.launch {
            _curUser.emit(user)
        }



    }

//    var chatName: String,
//    var userIds: List<String>,
//    var messages: List<Message>

    fun createChat(chatName: String, userIds: List<String>, messages: List<Message>){

        viewModelScope.launch {
            apiService.createChat(ChatRecPut(chatName, userIds, messages), user.token)
        }
    }


    fun  getAllUsersByRole(){
        viewModelScope.launch {
            _userlist.emit(apiService.getAllUserRole(user.user.role, user.token))
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
                if (modelClass.isAssignableFrom(MainScreenViewModel::class.java)) {
                    return MainScreenViewModel(ktorApi) as T
                }
                else
                {
                    throw IllegalArgumentException("Unknown ViewModel class")
                }

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

}