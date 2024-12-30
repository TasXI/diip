package com.example.khai_dipplom

import android.app.Application
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class KtorClient : Application() {

    lateinit var client: HttpClient

    val baseURI: String = "10.0.2.2"
    val _sss : MutableSharedFlow<String?> = MutableSharedFlow()
    val sss = _sss.asSharedFlow()

    fun getUpdFlow() = sss
    override fun onCreate() {
        super.onCreate()
        client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15000
            }
            install(WebSockets) {
                pingIntervalMillis = 15000
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
            }
        }
    }

    private var isconnect = false

    fun isConnected() = isconnect

    suspend fun connectToSocket(usid: String){
        var ss = runBlocking {
            try {
                client.webSocket(
                    method = HttpMethod.Get,
                    host = baseURI,
                    port = 8080,
                    path = "/eventssub"

                ) {

                    try {

                        launch {
                            while (true) {
                                send(Frame.Ping(byteArrayOf()))
                                delay(5_000)
                            }
                        }

                        send(Frame.Text(usid))
                        while (true) {
                            when (val frame = incoming.receive()) {
                                is Frame.Text -> {
                                    when (val text = frame.readText()) {

                                        "\"connect successful\"" -> {

                                            isconnect = true

                                            Log.d("mySoc", "CONNECT!")

                                        }

                                        else -> {

                                            Log.d("mySoc", "send $text")

                                            _sss.emit(text)

                                        }
                                    }

                                }

                                else -> {
                                    Log.d("mySoc", "-99999999")

                                }
                            }

                        }
                    } catch (exa: Exception) {
                        Log.d("mySoc", exa.stackTraceToString())
                        isconnect = false
                    }
                }
            }
            catch (ex: Exception){
                Log.d("mySoc", ex.message!!)
            }
        }
    }



    override fun onTerminate() {
        super.onTerminate()
        client.close()
    }



//    suspend fun receiveMes() {
//        try {
//            Log.e("mytag", "start")
//            val response = client.get("http://10.0.2.2:8080/")
//            Log.e("mytag", "Response: ${response.bodyAsText()}")
//        } catch (e: Exception) {
//            Log.e("mytag", e.message!!)
//            e.printStackTrace()
//        } finally {
//            client.close()
//        }
//    }
}