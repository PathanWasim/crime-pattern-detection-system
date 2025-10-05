@echo off
echo ========================================
echo Crime Detection System Setup
echo ========================================
echo.

echo Checking Java installation...
java -version
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 23 or higher
    pause
    exit /b 1
)
echo ✓ Java is installed
echo.

echo Checking Maven installation...
mvn -version
if %errorlevel% neq 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven 3.6 or higher
    pause
    exit /b 1
)
echo ✓ Maven is installed
echo.

echo Cleaning and compiling project...
mvn clean compile
if %errorlevel% neq 0 (
    echo ERROR: Compilation failed
    pause
    exit /b 1
)
echo ✓ Project compiled successfully
echo.

echo Installing dependencies...
mvn clean install
if %errorlevel% neq 0 (
    echo ERROR: Dependency installation failed
    pause
    exit /b 1
)
echo ✓ Dependencies installed successfully
echo.

echo Creating directories...
if not exist "reports" mkdir reports
if not exist "logs" mkdir logs
echo ✓ Directories created
echo.

echo ========================================
echo Setup completed successfully!
echo ========================================
echo.
echo Next steps:
echo 1. Ensure MySQL server is running
echo 2. Run database_schema.sql to create database and tables
echo 3. Update database credentials in src/main/resources/application.properties
echo 4. Run the application with: mvn exec:java
echo.
echo Default login credentials:
echo Username: admin
echo Password: admin123
echo.
pause