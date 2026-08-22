package com.example.interviewstarter.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interviewstarter.domain.usecase.GetArticlesUseCase
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

class DetailViewModel(
    private val getArticlesUseCase: GetArticlesUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<DetailUiEffect>()
    val uiEffect: SharedFlow<DetailUiEffect> = _uiEffect.asSharedFlow()

    fun loadArticle(articleId: String) {
        _uiState.update { it.copy(isLoading = true) }
        getArticlesUseCase.getById(articleId)
            .onEach { article ->
                _uiState.update {
                    it.copy(
                        article = article,
                        isLoading = false,
                        errorMessage = if (article == null) "Article not found" else null
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onBookmarkToggle() {
        val currentArticle = _uiState.value.article ?: return
        viewModelScope.launch {
            val isBookmarked = toggleBookmarkUseCase(currentArticle.id)
            val message = if (isBookmarked) "Saved to bookmarks" else "Removed from bookmarks"
            _uiEffect.emit(DetailUiEffect.ShowSnackbar(message))
        }
    }

    fun onShareClick() {
        val article = _uiState.value.article ?: return
        viewModelScope.launch {
            _uiEffect.emit(DetailUiEffect.ShareArticle(article.title, article.url))
        }
    }

    fun onOpenInBrowserClick() {
        val article = _uiState.value.article ?: return
        viewModelScope.launch {
            _uiEffect.emit(DetailUiEffect.OpenBrowser(article.url))
        }
    }
}
