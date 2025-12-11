@echo off
echo ========================================
echo  Starting Eight Queens Backend Server
echo ========================================
echo.

cd /d "f:\HNDSE232\PDSA 1\PDSA-GAME-CENTER\Eight-Queens\Eight-Queens"

echo [1/3] Checking MySQL on port 4306...
powershell -Command "if ((Test-NetConnection -ComputerName localhost -Port 4306 -InformationLevel Quiet)) { Write-Host 'MySQL is running on port 4306 [OK]' -ForegroundColor Green } else { Write-Host 'ERROR: MySQL is not running on port 4306!' -ForegroundColor Red; Write-Host 'Please start MySQL/XAMPP first.' -ForegroundColor Yellow; pause; exit 1 }"

echo.
echo [2/3] Compiling Java classes...
call mvn compile
if errorlevel 1 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo.
echo [3/3] Starting Spring Boot application...
echo NOTE: If this fails with plugin errors, please use VS Code's Run button instead
echo.
echo The application will start on: http://localhost:8080
echo Press Ctrl+C to stop the server
echo.

start "Eight Queens Backend" cmd /k "cd /d %CD% && mvn spring-boot:run"

echo.
echo ========================================
echo Backend server starting in new window...
echo Wait for "Started EightQueensApplication" message
echo Then refresh your browser at http://localhost:3000
echo ========================================
echo.

timeout /t 3 >nul
echo Opening backend health check in 15 seconds...
timeout /t 15 >nul
start http://localhost:8080/api/eight-queens/health

pause
