@echo off
echo ========================================
echo Eight Queens Backend Startup Script
echo ========================================
echo.
echo Starting Spring Boot Application...
echo Database: H2 In-Memory (No MySQL required)
echo Port: 8080
echo.

cd /d "%~dp0"
cd Eight-Queens\Eight-Queens

echo Cleaning and compiling...
call mvn clean compile

echo.
echo Starting application...
call mvn spring-boot:run

pause
