package com.example.grocerysimulator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(private val repository: GameRepository) : ViewModel() {
    private val _game = MutableStateFlow<GameState?>(null)
    val game: StateFlow<GameState?> = _game.asStateFlow()
    private val _screen = MutableStateFlow(Screen.MAIN_MENU)
    val screen: StateFlow<Screen> = _screen.asStateFlow()
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    init { viewModelScope.launch { _game.value = repository.load() } }

    fun beginNewGame(shopName: String): Boolean {
        val cleanName = shopName.trim()
        if (cleanName.isEmpty()) { _message.value = "Enter a shop name before continuing."; return false }
        val newState = GameState(cleanName)
        _game.value = newState
        save(newState)
        _screen.value = Screen.HUB
        return true
    }

    fun loadGame() {
        if (_game.value == null) _message.value = "No saved game found. Choose New Game to begin."
        else _screen.value = Screen.HUB
    }

    fun navigate(destination: Screen) {
        _screen.value = destination
        _game.value?.let(::save)
    }

    fun setSoundEnabled(enabled: Boolean) {
        _game.value?.let { updated ->
            val saved = updated.copy(soundEnabled = enabled)
            _game.value = saved
            save(saved)
        }
    }

    fun returnToMainMenu() { _game.value?.let(::save); _screen.value = Screen.MAIN_MENU }
    fun clearMessage() { _message.value = null }
    private fun save(state: GameState) { viewModelScope.launch { repository.save(state) } }

    class Factory(private val repository: GameRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = GameViewModel(repository) as T
    }
}
