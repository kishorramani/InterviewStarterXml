package com.example.interviewstarter.data.remote.mapper

import com.example.interviewstarter.data.remote.dto.UserDto
import com.example.interviewstarter.domain.model.User

fun UserDto.toDomain(): User =
    User(
        id = id,
        name = name,
        username = username,
        email = email
    )
