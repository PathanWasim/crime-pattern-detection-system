@echo off
echo ========================================
echo Starting Crime Detection System
echo ========================================
echo.

echo Testing database connection...
java -cp "target/classes;target/dependency/*" com.crimedetect.utils.DatabaseConnection
if %errorlevel% neq 0 (
    echo.
    echo ERROR: Database connection failed!
    echo Please check your MySQL server and database configuration.
    pause
    exit /b 1
)
echo.

echo Starting application...
mvn exec:java
pause