@echo off
echo ========================================
echo Eight Queens Frontend Startup Script
echo ========================================
echo.
echo Starting React Development Server...
echo Port: 3000
echo.

cd /d "%~dp0"
cd gameinterfaces

echo Installing dependencies (if needed)...
call npm install

echo.
echo Starting development server...
call npm start

pause
