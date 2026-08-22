package com.example.interviewstarter.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.interviewstarter.databinding.FragmentSettingsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnClearCache.setOnClickListener {
            viewModel.clearCache()
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleDarkMode(isChecked)
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        observeState()
        observeEffects()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    val info = state.platformInfo
                    if (info != null) {
                        binding.tvOsVersion.text = info.osVersion
                        binding.tvDeviceModel.text = info.deviceModel
                        binding.tvCpuArch.text = info.cpuArchitecture
                        binding.tvMemory.text = info.memoryInfo
                    }

                    binding.btnClearCache.isEnabled = !state.isClearingCache
                    binding.btnClearCache.text = if (state.isClearingCache) "Clearing..." else "Clear Unsaved Cache"
                }
            }
        }
    }

    private fun observeEffects() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEffect.collectLatest { effect ->
                    when (effect) {
                        is SettingsUiEffect.ShowSnackbar -> {
                            Snackbar.make(binding.root, effect.message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
