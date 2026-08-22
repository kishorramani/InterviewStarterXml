package com.example.interviewstarter.presentation.feed

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.interviewstarter.R
import com.example.interviewstarter.databinding.FragmentFeedBinding
import com.example.interviewstarter.domain.model.Category
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FeedViewModel by viewModel()
    private lateinit var adapter: ArticleListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupCategoryChips()
        setupSearch()
        observeState()
        observeEffects()
    }

    private fun setupRecyclerView() {
        adapter = ArticleListAdapter(
            onItemClick = { article ->
                val bundle = bundleOf("articleId" to article.id)
                findNavController().navigate(R.id.action_feed_to_detail, bundle)
            },
            onBookmarkClick = { article ->
                viewModel.onBookmarkToggle(article.id)
            }
        )
        binding.rvArticles.layoutManager = LinearLayoutManager(requireContext())
        binding.rvArticles.adapter = adapter
    }

    private fun setupCategoryChips() {
        binding.chipGroupCategories.removeAllViews()
        Category.entries.forEach { category ->
            val chip = Chip(requireContext()).apply {
                text = category.displayName
                isCheckable = true
                id = View.generateViewId()
                setOnClickListener { viewModel.onCategorySelected(category) }
            }
            binding.chipGroupCategories.addView(chip)
            if (category == Category.ALL) {
                chip.isChecked = true
            }
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onSearchQueryChanged(s?.toString().orEmpty())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    if (state.isLoading) {
                        binding.shimmerViewContainer.startShimmer()
                        binding.shimmerViewContainer.visibility = View.VISIBLE
                        binding.rvArticles.visibility = View.GONE
                        binding.tvEmptyState.visibility = View.GONE
                    } else {
                        binding.shimmerViewContainer.stopShimmer()
                        binding.shimmerViewContainer.visibility = View.GONE

                        if (state.articles.isEmpty()) {
                            binding.rvArticles.visibility = View.GONE
                            binding.tvEmptyState.visibility = View.VISIBLE
                        } else {
                            binding.rvArticles.visibility = View.VISIBLE
                            binding.tvEmptyState.visibility = View.GONE
                            adapter.submitList(state.articles)
                        }
                    }
                }
            }
        }
    }

    private fun observeEffects() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEffect.collectLatest { effect ->
                    when (effect) {
                        is FeedUiEffect.ShowSnackbar -> {
                            Snackbar.make(binding.root, effect.message, Snackbar.LENGTH_SHORT).show()
                        }
                        is FeedUiEffect.NavigateToDetail -> {
                            val bundle = bundleOf("articleId" to effect.articleId)
                            findNavController().navigate(R.id.action_feed_to_detail, bundle)
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
