package com.pemmob.eksplorasianime.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.pemmob.eksplorasianime.data.model.Anime
import com.pemmob.eksplorasianime.ui.components.AnimeCard
import com.pemmob.eksplorasianime.ui.components.ErrorView
import com.pemmob.eksplorasianime.ui.components.LoadingView
import com.pemmob.eksplorasianime.ui.state.UiState
import com.pemmob.eksplorasianime.ui.viewmodel.AnimeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: AnimeViewModel,
    onAnimeClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val animeListState by viewModel.animeListState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Eksplorasi Anime",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.fetchAnimeList() }) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = "Muat Ulang"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Kolom Pencarian Anime Dinamis
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = {
                    Text("Cari judul anime (misal: Naruto)...")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Search Icon"
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            viewModel.onSearchQueryChanged("")
                            viewModel.fetchAnimeList("")
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = "Clear Search"
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                        viewModel.fetchAnimeList()
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )

            // Penanganan State UI: Loading, Error, dan Data (Success)
            when (val state = animeListState) {
                is UiState.Loading -> {
                    LoadingView(message = "Memuat daftar anime...")
                }
                is UiState.Error -> {
                    ErrorView(
                        message = state.message,
                        onRetry = { viewModel.fetchAnimeList() }
                    )
                }
                is UiState.Success -> {
                    val animeList = state.data
                    if (animeList.isEmpty()) {
                        EmptyListState()
                    } else {
                        // Implementasi LazyColumn
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = animeList,
                                key = { it.malId }
                            ) { anime ->
                                AnimeCard(
                                    anime = anime,
                                    onClick = { onAnimeClick(anime.malId) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyListState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Rounded.SearchOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Tidak Ada Anime Ditemukan",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Coba kata kunci pencarian anime yang lain.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
