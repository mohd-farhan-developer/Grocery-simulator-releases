@echo off
setlocal
cd /d "%~dp0"

if "%ANDROID_HOME%"=="" set "ANDROID_HOME=%LOCALAPPDATA%\Android\Sdk"
if "%ANDROID_SDK_ROOT%"=="" set "ANDROID_SDK_ROOT=%ANDROID_HOME%"

set "GRADLE_CMD=gradle"
if exist "%~dp0.tools\gradle\bin\gradle.bat" set "GRADLE_CMD=%~dp0.tools\gradle\bin\gradle.bat"
if "%GRADLE_CMD%"=="gradle" (
  where gradle >nul 2>nul
  if errorlevel 1 (
    echo Gradle was not found. Run setup-gradle.cmd first.
    echo Android SDK detected at: %ANDROID_HOME%
    exit /b 1
  )
)

echo Building Grocery Simulator debug APK...
call "%GRADLE_CMD%" --no-daemon assembleDebug
if errorlevel 1 exit /b %errorlevel%

echo.
echo BUILD SUCCESSFUL
echo APK: %~dp0app\build\outputs\apk\debug\app-debug.apk
endlocal
