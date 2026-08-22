package com.example.interviewstarter.presentation.bookmarks

import android.os.Bundle
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
import com.example.interviewstarter.databinding.FragmentBookmarksBinding
import com.example.interviewstarter.presentation.feed.ArticleListAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookmarksFragment : Fragment() {

    private var _binding: FragmentBookmarksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookmarksViewModel by viewModel()
    private lateinit var adapter: ArticleListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeState()
        observeEffects()
    }

    private fun setupRecyclerView() {
        adapter = ArticleListAdapter(
            onItemClick = { article ->
                val bundle = bundleOf("articleId" to article.id)
                findNavController().navigate(R.id.action_bookmarks_to_detail, bundle)
            },
            onBookmarkClick = { article ->
                viewModel.onRemoveBookmark(article.id)
            }
        )
        binding.rvBookmarks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBookmarks.adapter = adapter
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    if (state.bookmarkedArticles.isEmpty()) {
                        binding.rvBookmarks.visibility = View.GONE
                        binding.tvEmptyState.visibility = View.VISIBLE
                    } else {
                        binding.rvBookmarks.visibility = View.VISIBLE
                        binding.tvEmptyState.visibility = View.GONE
                        adapter.submitList(state.bookmarkedArticles)
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
                        is BookmarksUiEffect.ShowSnackbar -> {
                            Snackbar.make(binding.root, effect.message, Snackbar.LENGTH_SHORT).show()
                        }
                        is BookmarksUiEffect.NavigateToDetail -> {
                            val bundle = bundleOf("articleId" to effect.articleId)
                            findNavController().navigate(R.id.action_bookmarks_to_detail, bundle)
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
