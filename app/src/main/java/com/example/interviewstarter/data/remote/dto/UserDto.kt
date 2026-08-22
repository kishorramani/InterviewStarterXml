package com.example.interviewstarter.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val name: String,
    val username: String = "",
    val email: String = ""
)
