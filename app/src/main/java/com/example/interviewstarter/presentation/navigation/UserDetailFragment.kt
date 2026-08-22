package com.example.interviewstarter.presentation.navigation

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.interviewstarter.R

class UserDetailFragment : Fragment(R.layout.fragment_user_detail) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val userId = arguments?.getInt("userId", -1) ?: -1
        view.findViewById<TextView>(R.id.detailText).text =
            "User ID: $userId"
    }
}
