package com.example.interviewstarter.data.remote.api

import com.example.interviewstarter.data.remote.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url

class UserApi(
    private val client: HttpClient
) {
    suspend fun getUsers(): List<UserDto> =
        client.get {
            url("${com.example.interviewstarter.core.network.NetworkConfig.BASE_URL}users")
        }.body()

    suspend fun getUser(id: Int): UserDto =
        client.get {
            url("${com.example.interviewstarter.core.network.NetworkConfig.BASE_URL}users/$id")
        }.body()
}
