package com.pemmob.eksplorasianime.ui.state

/**
 * Sealed interface untuk merepresentasikan keadaan UI (State Management).
 * Sesuai ketentuan minimal: Loading, Data (Success), dan Error.
 */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<out T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}
