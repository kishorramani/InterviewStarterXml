package com.example.interviewstarter.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun createWebSocketClient(): HttpClient =
    HttpClient(CIO) {
        install(WebSockets) {
            pingIntervalMillis = 20_000
        }
    }

fun chatMessages(
    client: HttpClient,
    host: String,
    path: String
): Flow<String> = flow {
    client.webSocket(host = host, path = path) {
        for (frame in incoming) {
            if (frame is Frame.Text) emit(frame.readText())
        }
    }
}
