package com.example.interviewstarter.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interviewstarter.domain.model.Category
import com.example.interviewstarter.domain.usecase.GetArticlesUseCase
import com.example.interviewstarter.domain.usecase.SearchArticlesUseCase
import com.example.interviewstarter.domain.usecase.ToggleBookmarkUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class FeedViewModel(
    private val getArticlesUseCase: GetArticlesUseCase,
    private val searchArticlesUseCase: SearchArticlesUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<FeedUiEffect>()
    val uiEffect: SharedFlow<FeedUiEffect> = _uiEffect.asSharedFlow()

    private val selectedCategoryFlow = MutableStateFlow(Category.ALL)
    private val searchQueryFlow = MutableStateFlow("")

    init {
        observeArticles()
    }

    private fun observeArticles() {
        combine(
            selectedCategoryFlow,
            searchQueryFlow.debounce(300).distinctUntilChanged()
        ) { category, query ->
            Pair(category, query)
        }.flatMapLatest { (category, query) ->
            _uiState.update { it.copy(isLoading = true) }
            if (query.isBlank()) {
                getArticlesUseCase(category)
            } else {
                searchArticlesUseCase(query)
            }
        }.onEach { articles ->
            _uiState.update { current ->
                current.copy(
                    articles = articles,
                    isLoading = false,
                    selectedCategory = selectedCategoryFlow.value,
                    searchQuery = searchQueryFlow.value
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onCategorySelected(category: Category) {
        selectedCategoryFlow.value = category
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onSearchQueryChanged(query: String) {
        searchQueryFlow.value = query
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun clearSearch() {
        searchQueryFlow.value = ""
        _uiState.update { it.copy(searchQuery = "") }
    }

    fun onBookmarkToggle(articleId: String) {
        viewModelScope.launch {
            val isBookmarked = toggleBookmarkUseCase(articleId)
            val message = if (isBookmarked) "Article saved to bookmarks" else "Removed from bookmarks"
            _uiEffect.emit(FeedUiEffect.ShowSnackbar(message))
        }
    }

    fun onArticleClick(articleId: String) {
        viewModelScope.launch {
            _uiEffect.emit(FeedUiEffect.NavigateToDetail(articleId))
        }
    }
}
