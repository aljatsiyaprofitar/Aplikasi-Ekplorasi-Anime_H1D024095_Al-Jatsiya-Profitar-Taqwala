package com.pemmob.eksplorasianime.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.eksplorasianime.data.model.Anime
import com.pemmob.eksplorasianime.data.repository.AnimeRepository
import com.pemmob.eksplorasianime.data.repository.AnimeRepositoryImpl
import com.pemmob.eksplorasianime.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel untuk mengelola state dan business logic aplikasi Anime Exploration.
 * Menerapkan pola MVVM dan mematuhi prinsip Unidirectional Data Flow (UDF).
 */
class AnimeViewModel(
    private val repository: AnimeRepository = AnimeRepositoryImpl()
) : ViewModel() {

    // UI State untuk daftar anime (HomeScreen)
    private val _animeListState = MutableStateFlow<UiState<List<Anime>>>(UiState.Loading)
    val animeListState: StateFlow<UiState<List<Anime>>> = _animeListState.asStateFlow()

    // UI State untuk detail anime (DetailScreen)
    private val _animeDetailState = MutableStateFlow<UiState<Anime>>(UiState.Loading)
    val animeDetailState: StateFlow<UiState<Anime>> = _animeDetailState.asStateFlow()

    // State untuk text input query pencarian
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        // Load data awal saat ViewModel dibuat
        fetchAnimeList()
    }

    /**
     * Memperbarui query teks pencarian anime.
     */
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    /**
     * Mengambil daftar anime dari repository.
     * Mengelola state: Loading -> Success (Data) atau Error.
     */
    fun fetchAnimeList(query: String? = _searchQuery.value) {
        viewModelScope.launch {
            _animeListState.value = UiState.Loading
            repository.getAnimeList(query)
                .onSuccess { list ->
                    // Memanfaatkan fitur collection Kotlin (filter untuk memastikan validitas data jika diperlukan)
                    val validList = list.filter { it.title != null || it.titleEnglish != null }
                    _animeListState.value = UiState.Success(validList)
                }
                .onFailure { throwable ->
                    val errorMessage = throwable.localizedMessage ?: "Terjadi kesalahan saat mengambil data anime."
                    _animeListState.value = UiState.Error(errorMessage)
                }
        }
    }

    /**
     * Mengambil detail anime berdasarkan ID (mal_id).
     */
    fun fetchAnimeDetail(id: Int) {
        viewModelScope.launch {
            _animeDetailState.value = UiState.Loading
            repository.getAnimeDetail(id)
                .onSuccess { anime ->
                    _animeDetailState.value = UiState.Success(anime)
                }
                .onFailure { throwable ->
                    val errorMessage = throwable.localizedMessage ?: "Gagal memuat detail anime."
                    _animeDetailState.value = UiState.Error(errorMessage)
                }
        }
    }

    /**
     * Mereset state detail saat navigasi keluar dari screen detail.
     */
    fun clearDetailState() {
        _animeDetailState.value = UiState.Loading
    }
}
