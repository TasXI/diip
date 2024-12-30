package com.example.khai_dipplom

import android.annotation.SuppressLint
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.math.log

class KtorApi (private val client: HttpClient) {

    val baseURI: String = "10.0.2.2"



    suspend fun addMessageById(message: Message ,chat: ChatRecGet, accessToken: String): Boolean{
        try {
            val response = client.post("http://$baseURI:8080/chatmessagesaddone") {
                contentType(ContentType.Application.Json)
                headers.append(HttpHeaders.Authorization, "Bearer $accessToken")
                setBody(Pair(message,chat))
            }


            if (response.status == HttpStatusCode.OK)
            return true
            else return false

        }catch (ex: Exception){
            return false
        }
    }

    suspend fun getMessagesById(chat: ChatRecGet, accessToken: String): List<Message>?{

        try {
            val response = client.post("http://$baseURI:8080/chatmessagesgets") {
                contentType(ContentType.Application.Json)
                headers.append(HttpHeaders.Authorization, "Bearer $accessToken")
                setBody(chat)
            }

            val messages: List<Message>? = response.body()

            return messages

        }catch (ex: Exception){
            return null
        }
    }


    suspend fun getAllUserRole(role: String, accessToken: String): List<UserInfo>?{
        try {
            val response = client.post("http://$baseURI:8080/userrole") {
                contentType(ContentType.Application.Json)
                headers.append(HttpHeaders.Authorization, "Bearer $accessToken")
                setBody(role)
            }

            val userList: List<UserInfo> = response.body()

            return userList
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun getUserChats(userId: String, accessToken: String): List<ChatRecGet>?{
        try {
            val response = client.post("http://$baseURI:8080/chatgets") {
                contentType(ContentType.Application.Json)
                headers.append(HttpHeaders.Authorization, "Bearer $accessToken")
                setBody(userId)
            }


            if (response.status == HttpStatusCode.OK) {
                val userList: List<ChatRecGet> = response.body()
                return userList
            }
            else{
                return null
            }
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun createChat(chat: ChatRecPut, accessToken: String): Boolean{
        try {
            val response = client.post("http://$baseURI:8080/chatcreate") {
                contentType(ContentType.Application.Json)
                headers.append(HttpHeaders.Authorization, "Bearer $accessToken")
                setBody(chat)
            }


            if (response.status == HttpStatusCode.OK) {
                val userList: Boolean = response.body()
                return userList
            }
            else{
                return false
            }
        } catch (e: Exception) {
            return false
        }
    }

    suspend fun signIn(username: String, password: String): UserClient? {
        try {
            val response = client.post("http://$baseURI:8080/signin") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(username, password))
            }


            if (response.status == HttpStatusCode.Accepted) {
                val user: UserClient = response.body()
                return user
            }

        return null
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun signUp(
        login: String,
        password: String,
        name1: String,
        name2: String,
        name3: String,
        mytoken: String
    ) : Int {

        try {
            val response = client.post("http://$baseURI:8080/signup") {
                contentType(ContentType.Application.Json)
                headers.append(HttpHeaders.Authorization, "Bearer $mytoken")
                setBody(UserPass(login, name1, name2, name3, 0, password, ""))
            }



            return response.status.value
        } catch (e: Exception) {
            return -1
        }
    }
}

@Serializable
data class LoginRequest(val username: String, val password: String)
