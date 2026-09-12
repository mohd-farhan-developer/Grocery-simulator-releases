package com.example.grocerysimulator

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

class MainActivity : ComponentActivity() {
    private val viewModel: GameViewModel by viewModels { GameViewModel.Factory(GameRepository(applicationContext)) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { GroceryApp(viewModel, ::finish) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GroceryApp(vm: GameViewModel, onExit: () -> Unit) {
    val screen by vm.screen.collectAsStateWithLifecycle()
    val game by vm.game.collectAsStateWithLifecycle()
    val message by vm.message.collectAsStateWithLifecycle()
    var confirmBack by remember { mutableStateOf(false) }
    val view = LocalView.current

    LaunchedEffect(screen) { view.announceForAccessibility(screen.title) }
    LaunchedEffect(message) { message?.let(view::announceForAccessibility) }
    BackHandler(enabled = screen.inGame) { confirmBack = true }

    Scaffold(topBar = {
        TopAppBar(title = { Text(screen.title) }, navigationIcon = {
            if (screen.inGame) TextButton(onClick = { confirmBack = true }, modifier = Modifier.semantics { contentDescription = "Back to main menu" }) { Text("Back") }
        })
    }) { padding ->
        Surface(Modifier.fillMaxSize().padding(padding)) {
            when (screen) {
                Screen.MAIN_MENU -> MainMenu(vm, onExit)
                Screen.NEW_GAME -> NewGame(vm)
                Screen.HUB -> Hub(game, vm)
                Screen.SHOP_FLOOR -> ShopFloor()
                Screen.COMPUTER -> Computer()
                Screen.STORAGE_ROOM -> StorageRoom()
                Screen.SETTINGS -> Settings(game, vm)
            }
        }
    }

    if (confirmBack) AlertDialog(
        onDismissRequest = { confirmBack = false },
        title = { Text("Return to Main Menu?") },
        text = { Text("Your progress is already saved automatically.") },
        confirmButton = { Button(onClick = { confirmBack = false; vm.returnToMainMenu() }, modifier = Modifier.semantics { contentDescription = "Yes, return to main menu" }) { Text("Yes") } },
        dismissButton = { TextButton(onClick = { confirmBack = false }, modifier = Modifier.semantics { contentDescription = "No, stay in the current screen" }) { Text("No") } }
    )

    message?.let { text -> AlertDialog(onDismissRequest = vm::clearMessage, title = { Text("Grocery Simulator") }, text = { Text(text) }, confirmButton = { TextButton(onClick = vm::clearMessage) { Text("OK") } }) }
}

@Composable private fun MainMenu(vm: GameViewModel, onExit: () -> Unit) = Page {
    Text("Grocery Simulator", style = MaterialTheme.typography.headlineLarge)
    Text("A text-based supermarket simulator designed for TalkBack.")
    ActionButton("New Game", "Create a new supermarket game") { vm.navigate(Screen.NEW_GAME) }
    ActionButton("Load Game", "Load the saved supermarket game") { vm.loadGame() }
    ActionButton("Settings", "Open settings") { vm.navigate(Screen.SETTINGS) }
    ActionButton("Exit Game", "Exit Grocery Simulator", onExit)
}

@Composable private fun NewGame(vm: GameViewModel) = Page {
    var shopName by remember { mutableStateOf("") }
    Text("Name your supermarket", style = MaterialTheme.typography.headlineSmall)
    OutlinedTextField(shopName, { shopName = it }, label = { Text("Shop name") }, modifier = Modifier.fillMaxWidth().semantics { contentDescription = "Shop name text field" }, singleLine = true)
    ActionButton("Continue", "Continue and create an automatic save") { vm.beginNewGame(shopName) }
}

@Composable private fun Hub(game: GameState?, vm: GameViewModel) = Page {
    Text(game?.shopName ?: "Your supermarket", style = MaterialTheme.typography.headlineLarge)
    Text("Day ${game?.day ?: 1}")
    Text("Cash ₹${game?.cash ?: 5000}")
    Spacer(Modifier.width(1.dp))
    ActionButton("Go to Shop Floor", "Enter the shop floor") { vm.navigate(Screen.SHOP_FLOOR) }
    ActionButton("Go to Computer", "Enter the computer room") { vm.navigate(Screen.COMPUTER) }
    ActionButton("Go to Storage Room", "Enter the storage room") { vm.navigate(Screen.STORAGE_ROOM) }
}

@Composable private fun ShopFloor() = Page {
    Text("Shop Floor", style = MaterialTheme.typography.headlineSmall)
    ActionButton("Open / Close Store", "Open or close store placeholder") {}
    ActionButton("Shelves", "Shelves placeholder") {}
    ActionButton("Billing Counter", "Billing counter placeholder") {}
    ActionButton("Customers", "Customers placeholder") {}
    ActionButton("Cleaning", "Cleaning placeholder") {}
}

@Composable private fun Computer() = Page {
    Text("Computer", style = MaterialTheme.typography.headlineSmall)
    ActionButton("Market", "Market placeholder") {}
    ActionButton("Pricing", "Pricing placeholder") {}
    ActionButton("Licenses", "Licenses placeholder") {}
    ActionButton("Bank", "Bank placeholder") {}
    ActionButton("Employees", "Employees placeholder") {}
    ActionButton("Statistics", "Statistics placeholder") {}
}

@Composable private fun StorageRoom() = Page { Text("Storage Room", style = MaterialTheme.typography.headlineSmall) }

@Composable private fun Settings(game: GameState?, vm: GameViewModel) = Page {
    Text("Settings", style = MaterialTheme.typography.headlineSmall)
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("Sound effects")
        Switch(game?.soundEnabled ?: true, vm::setSoundEnabled, modifier = Modifier.semantics { contentDescription = "Enable or disable sound effects" })
    }
    Text("All gameplay information is shown as text and announced for TalkBack.")
}

@Composable private fun Page(content: @Composable ColumnScope.() -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { item { Column(verticalArrangement = Arrangement.spacedBy(12.dp), content = content) } }
}

@Composable private fun ActionButton(label: String, description: String, onClick: () -> Unit) {
    Button(onClick, modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp).semantics { contentDescription = description }) { Text(label) }
}
