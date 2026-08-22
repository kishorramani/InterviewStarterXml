package com.example.interviewstarter.presentation.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interviewstarter.domain.usecase.GetBookmarkedArticlesUseCase
import com.example.interviewstarter.domain.usecase.ToggleBookmarkUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookmarksViewModel(
    private val getBookmarkedArticlesUseCase: GetBookmarkedArticlesUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookmarksUiState())
    val uiState: StateFlow<BookmarksUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<BookmarksUiEffect>()
    val uiEffect: SharedFlow<BookmarksUiEffect> = _uiEffect.asSharedFlow()

    init {
        observeBookmarks()
    }

    private fun observeBookmarks() {
        getBookmarkedArticlesUseCase()
            .onEach { articles ->
                _uiState.update {
                    it.copy(
                        bookmarkedArticles = articles,
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onRemoveBookmark(articleId: String) {
        viewModelScope.launch {
            toggleBookmarkUseCase(articleId)
            _uiEffect.emit(BookmarksUiEffect.ShowSnackbar("Removed from bookmarks"))
        }
    }

    fun onArticleClick(articleId: String) {
        viewModelScope.launch {
            _uiEffect.emit(BookmarksUiEffect.NavigateToDetail(articleId))
        }
    }
}
