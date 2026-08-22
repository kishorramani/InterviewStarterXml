package com.example.interviewstarter.data.remote.websocket

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll

class ChatWebSocketClient(
    private val client: HttpClient
) {
    fun messages(
        host: String,
        path: String
    ): Flow<String> = flow {
        client.webSocket(host = host, path = path) {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    emit(frame.readText())
                }
            }
        }
    }

    suspend fun send(
        host: String,
        path: String,
        message: String
    ) {
        client.webSocket(host = host, path = path) {
            send(Frame.Text(message))
        }
    }
}