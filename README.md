# Grocery Simulator for Android

Offline, text-only grocery shop simulator built with Kotlin, Jetpack Compose, MVVM, and Preferences DataStore.

## Build

From Command Prompt, run `setup-gradle.cmd` once, then run `build.cmd`. The setup script downloads Gradle locally into `.tools` so no Android Studio or system-wide Gradle installation is needed. The build script uses the Android SDK at `%LOCALAPPDATA%\Android\Sdk` by default and creates `app\build\outputs\apk\debug\app-debug.apk`. The project targets SDK 36 and supports Android 8.0 (API 26) and newer.

## Accessibility

All actions are buttons or text fields with TalkBack descriptions. The app uses logical vertical focus order, polite accessibility announcements for gameplay events, scrollable screens, and no drag-and-drop or image-only information.

## Sound placeholders

Gameplay never relies on sound to communicate information. The `app/src/main/assets/sounds/README.txt` file documents the intended short sound slots (click, purchase, cash, error, new day, reputation change, opening, and closing). They are intentionally placeholders so the project remains fully offline and dependency-free; the app communicates each event with visible text and Android accessibility announcements.
