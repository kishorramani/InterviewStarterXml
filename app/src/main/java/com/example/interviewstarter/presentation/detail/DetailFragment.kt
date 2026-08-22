package com.example.interviewstarter.presentation.detail

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.interviewstarter.R
import com.example.interviewstarter.databinding.FragmentDetailBinding
import com.example.interviewstarter.domain.model.Category
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val articleId = arguments?.getString("articleId").orEmpty()

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnBookmarkTop.setOnClickListener {
            viewModel.onBookmarkToggle()
        }

        binding.btnShareTop.setOnClickListener {
            viewModel.onShareClick()
        }

        binding.btnOpenLink.setOnClickListener {
            viewModel.onOpenInBrowserClick()
        }

        binding.btnShare.setOnClickListener {
            viewModel.onShareClick()
        }

        viewModel.loadArticle(articleId)
        observeState()
        observeEffects()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    if (state.isLoading) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
                        val article = state.article
                        if (article != null) {
                            binding.tvTitle.text = article.title
                            binding.tvAuthor.text = "By ${article.author}"
                            binding.tvReadTime.text = "${article.readTimeMinutes} min read"
                            binding.tvSummary.text = article.summary
                            binding.tvContent.text = article.content
                            binding.tvTags.text = article.tags.joinToString(" ") { "#$it" }

                            if (article.imageUrl.isNotEmpty()) {
                                binding.ivCover.load(article.imageUrl) {
                                    crossfade(true)
                                }
                            }

                            // Category badge
                            val badgeColorInt = when (article.category) {
                                Category.MOBILE -> ContextCompat.getColor(requireContext(), R.color.tag_mobile)
                                Category.AI_ML -> ContextCompat.getColor(requireContext(), R.color.tag_ai)
                                Category.WEB -> ContextCompat.getColor(requireContext(), R.color.tag_web)
                                Category.DEVOPS -> ContextCompat.getColor(requireContext(), R.color.tag_devops)
                                Category.ALL -> ContextCompat.getColor(requireContext(), R.color.primary_blue)
                            }
                            val badgeBackground = GradientDrawable().apply {
                                shape = GradientDrawable.RECTANGLE
                                cornerRadius = 16f
                                setColor(Color.argb(35, Color.red(badgeColorInt), Color.green(badgeColorInt), Color.blue(badgeColorInt)))
                            }
                            binding.tvCategoryBadge.background = badgeBackground
                            binding.tvCategoryBadge.setTextColor(badgeColorInt)
                            binding.tvCategoryBadge.text = article.category.displayName

                            // Bookmark button status
                            binding.btnBookmarkTop.setImageResource(
                                if (article.isBookmarked) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark_border
                            )
                            binding.btnBookmarkTop.setColorFilter(
                                if (article.isBookmarked) ContextCompat.getColor(requireContext(), R.color.primary_blue)
                                else ContextCompat.getColor(requireContext(), R.color.text_secondary)
                            )
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
                        is DetailUiEffect.ShowSnackbar -> {
                            Snackbar.make(binding.root, effect.message, Snackbar.LENGTH_SHORT).show()
                        }
                        is DetailUiEffect.OpenBrowser -> {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(effect.url))
                                startActivity(intent)
                            } catch (e: Exception) {
                                Snackbar.make(binding.root, "Unable to open URL", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                        is DetailUiEffect.ShareArticle -> {
                            try {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "${effect.title}\n${effect.url}")
                                    type = "text/plain"
                                }
                                startActivity(Intent.createChooser(sendIntent, "Share Article"))
                            } catch (e: Exception) {
                                Snackbar.make(binding.root, "Unable to share article", Snackbar.LENGTH_SHORT).show()
                            }
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
