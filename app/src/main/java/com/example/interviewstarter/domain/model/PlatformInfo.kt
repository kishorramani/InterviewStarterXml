package com.example.interviewstarter.domain.model

data class PlatformInfo(
    val osName: String,
    val osVersion: String,
    val deviceModel: String,
    val cpuArchitecture: String,
    val memoryInfo: String,
    val isSimulator: Boolean,
    val kotlinVersion: String = "2.3.20",
    val composeVersion: String = "N/A (Android XML Views)"
)
