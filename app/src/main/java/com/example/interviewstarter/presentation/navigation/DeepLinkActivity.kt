package com.example.interviewstarter.presentation.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.interviewstarter.R

class DeepLinkActivity : AppCompatActivity(R.layout.activity_nav_host) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findNavController(R.id.nav_host).handleDeepLink(intent)
    }
}
