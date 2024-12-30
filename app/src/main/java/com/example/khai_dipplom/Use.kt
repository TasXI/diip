package com.example.khai_dipplom

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.Serializable

@kotlinx.serialization.Serializable
open class User(
    var username: String = "",
    var name1: String = "",
    var name2: String = "",
    var name3: String = "",
    var role: String = ""
) : Serializable

@kotlinx.serialization.Serializable
data class UserInfo(var id: String = "", val username: String)

@kotlinx.serialization.Serializable
data class UserPass(
    var username: String = "",
    var name1: String = "",
    var name2: String = "",
    var name3: String = "",
    var id: Int = 0,
    var password: String = "",
    var role: String = ""
)

@kotlinx.serialization.Serializable
open class UserClient(
    var user: User,
    var id: String,
    var token: String = ""
): Serializable

@kotlinx.serialization.Serializable
open class Message(
    var userIdSend: String,
    var username: String,
    var messageText: String
) : Serializable

@kotlinx.serialization.Serializable
open class ChatRecPut(
    var chatName: String,
    var userIds: List<String>,
    var messages: List<Message>
)

@kotlinx.serialization.Serializable
open class ChatRecGet(
    var chatId: String = "",
    var chatName: String,
    var userIds: List<String>,
    var lastMessage: Message? = null
): Serializable