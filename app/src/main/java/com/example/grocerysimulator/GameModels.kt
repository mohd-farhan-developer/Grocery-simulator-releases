package com.example.grocerysimulator

data class GameState(
    val shopName: String,
    val day: Int = 1,
    val cash: Int = 5000,
    val soundEnabled: Boolean = true
)

enum class Screen(val title: String, val inGame: Boolean = true) {
    MAIN_MENU("Grocery Simulator", false),
    NEW_GAME("New Game", false),
    HUB("Main Hub"),
    SHOP_FLOOR("Shop Floor"),
    COMPUTER("Computer"),
    STORAGE_ROOM("Storage Room"),
    SETTINGS("Settings", false)
}
