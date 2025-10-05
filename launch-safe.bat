@echo off
echo ========================================
echo Crime Detection System (Safe Mode)
echo ========================================
echo.

echo Setting up classpath...
set CLASSPATH=target/classes;target/lib/*

echo Starting Crime Detection System in Safe Mode...
echo (Using system default theme to avoid compatibility issues)
echo Please wait while the application loads...
echo.

java -Dapp.theme=system -cp "%CLASSPATH%" com.crimedetect.App

echo.
echo Application has closed.
pause