package com.example.grocerysimulator

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import org.json.JSONObject

private val Context.gameDataStore by preferencesDataStore("grocery_simulator_save")

class GameRepository(private val context: Context) {
    private val saveKey = stringPreferencesKey("game_state")

    suspend fun load(): GameState? {
        val raw = context.gameDataStore.data.first()[saveKey] ?: return null
        return runCatching {
            val json = JSONObject(raw)
            GameState(json.getString("shopName"), json.optInt("day", 1), json.optInt("cash", 5000), json.optBoolean("soundEnabled", true))
        }.getOrNull()
    }

    suspend fun save(state: GameState) {
        val json = JSONObject().apply {
            put("shopName", state.shopName)
            put("day", state.day)
            put("cash", state.cash)
            put("soundEnabled", state.soundEnabled)
        }
        context.gameDataStore.edit { it[saveKey] = json.toString() }
    }
}
