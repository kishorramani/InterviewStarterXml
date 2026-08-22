package com.example.interviewstarter.data.local.mapper

import com.example.interviewstarter.data.local.entity.UserEntity
import com.example.interviewstarter.domain.model.User

fun UserEntity.toDomain() = User(id, name, username, email)
fun User.toEntity() = UserEntity(id, name, username, email)
