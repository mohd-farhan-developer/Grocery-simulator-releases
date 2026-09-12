@echo off
setlocal
cd /d "%~dp0"

set "VERSION=8.10.2"
set "TOOLS=%~dp0.tools"
set "ZIP=%TOOLS%\gradle-%VERSION%-bin.zip"
set "INSTALL=%TOOLS%\gradle"

if exist "%INSTALL%\bin\gradle.bat" (
  echo Local Gradle %VERSION% is already installed.
  exit /b 0
)

if not exist "%TOOLS%" mkdir "%TOOLS%"
echo Downloading Gradle %VERSION%...
curl.exe -L --fail --output "%ZIP%" "https://services.gradle.org/distributions/gradle-%VERSION%-bin.zip"
if errorlevel 1 (
  echo Download failed. Check your internet connection and try again.
  exit /b 1
)

echo Extracting Gradle...
powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -LiteralPath '%ZIP%' -DestinationPath '%TOOLS%' -Force; if (Test-Path '%INSTALL%') { Remove-Item -LiteralPath '%INSTALL%' -Recurse -Force }; Move-Item -LiteralPath '%TOOLS%\gradle-%VERSION%' -Destination '%INSTALL%'; Remove-Item -LiteralPath '%ZIP%' -Force"
if errorlevel 1 exit /b 1

echo Gradle installed locally. Run build.cmd next.
endlocal
