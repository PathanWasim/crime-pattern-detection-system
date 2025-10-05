@echo off
echo ========================================
echo Crime Detection System Launcher
echo ========================================
echo.

echo Setting up classpath...
set CLASSPATH=target/classes;target/lib/*

echo Starting Crime Detection System...
echo Please wait while the application loads...
echo.

java -cp "%CLASSPATH%" com.crimedetect.App

echo.
echo Application has closed.
pause