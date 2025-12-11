@echo off
REM MySQL Database Setup Script for Eight Queens
REM Run this to create the database and user on port 4306

echo ========================================
echo Eight Queens - MySQL Database Setup
echo Port: 4306
echo ========================================
echo.

REM Set MySQL connection details
set MYSQL_HOST=localhost
set MYSQL_PORT=4306
set MYSQL_BIN=e:\xampp server\mysql\bin\mysql.exe

echo Connecting to MySQL on port %MYSQL_PORT%...
echo Please enter your MySQL root password when prompted.
echo.

"%MYSQL_BIN%" -h %MYSQL_HOST% -P %MYSQL_PORT% -u root -p < database-schema.sql

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Database setup completed successfully!
    echo ========================================
    echo.
    echo Database: eight_queens
    echo User: eq_user
    echo Password: strong_password
    echo Port: 4306
    echo.
) else (
    echo.
    echo ========================================
    echo ERROR: Database setup failed!
    echo ========================================
    echo.
    echo Please check:
    echo 1. MySQL is running on port 4306
    echo 2. You have root access
    echo 3. The password is correct
    echo.
)

pause
