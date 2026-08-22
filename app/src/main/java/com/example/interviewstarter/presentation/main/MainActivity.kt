package com.example.interviewstarter.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.interviewstarter.databinding.ActivityMainBinding
import com.example.interviewstarter.presentation.users.UserPagingAdapter
import com.example.interviewstarter.presentation.users.UserViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: UserViewModel by viewModel()
    private val adapter = UserPagingAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.userRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.pagedUsers.collect { adapter.submitData(it) }
        }

        viewModel.loadUsers()
    }
}
