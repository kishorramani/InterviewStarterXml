package com.example.interviewstarter.presentation.feed

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.interviewstarter.R
import com.example.interviewstarter.databinding.ItemArticleBinding
import com.example.interviewstarter.domain.model.Article
import com.example.interviewstarter.domain.model.Category

class ArticleListAdapter(
    private val onItemClick: (Article) -> Unit,
    private val onBookmarkClick: (Article) -> Unit
) : ListAdapter<Article, ArticleListAdapter.ArticleViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ArticleViewHolder(
        private val binding: ItemArticleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            val context = binding.root.context

            binding.tvTitle.text = article.title
            binding.tvSummary.text = article.summary
            binding.tvAuthor.text = "By ${article.author}"
            binding.tvReadTime.text = "${article.readTimeMinutes} min read"

            if (article.tags.isNotEmpty()) {
                binding.tvTags.text = article.tags.take(3).joinToString(" ") { "#$it" }
                binding.tvTags.visibility = android.view.View.VISIBLE
            } else {
                binding.tvTags.visibility = android.view.View.GONE
            }

            // Category badge styling
            val badgeColorInt = when (article.category) {
                Category.MOBILE -> ContextCompat.getColor(context, R.color.tag_mobile)
                Category.AI_ML -> ContextCompat.getColor(context, R.color.tag_ai)
                Category.WEB -> ContextCompat.getColor(context, R.color.tag_web)
                Category.DEVOPS -> ContextCompat.getColor(context, R.color.tag_devops)
                Category.ALL -> ContextCompat.getColor(context, R.color.primary_blue)
            }

            val badgeBackground = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 16f
                setColor(Color.argb(35, Color.red(badgeColorInt), Color.green(badgeColorInt), Color.blue(badgeColorInt)))
            }
            binding.tvCategoryBadge.background = badgeBackground
            binding.tvCategoryBadge.setTextColor(badgeColorInt)
            binding.tvCategoryBadge.text = article.category.displayName

            // Bookmark icon
            binding.btnBookmark.setImageResource(
                if (article.isBookmarked) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark_border
            )
            binding.btnBookmark.setColorFilter(
                if (article.isBookmarked) ContextCompat.getColor(context, R.color.primary_blue)
                else ContextCompat.getColor(context, R.color.text_secondary)
            )

            // Image loading with Coil
            if (article.imageUrl.isNotEmpty()) {
                binding.ivCover.load(article.imageUrl) {
                    crossfade(true)
                }
            }

            binding.root.setOnClickListener { onItemClick(article) }
            binding.btnBookmark.setOnClickListener { onBookmarkClick(article) }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }
}
