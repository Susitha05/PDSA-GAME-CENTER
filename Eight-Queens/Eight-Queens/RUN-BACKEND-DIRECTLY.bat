@echo off
echo ========================================
echo Starting Eight Queens Backend Server
echo ========================================
echo.

cd /d "F:\HNDSE232\PDSA 1\PDSA-GAME-CENTER\Eight-Queens\Eight-Queens"

echo [1/3] Checking MySQL on port 4306...
powershell -Command "$result = Test-NetConnection -ComputerName localhost -Port 4306 -InformationLevel Quiet; if ($result) { Write-Host 'MySQL is RUNNING on port 4306' -ForegroundColor Green } else { Write-Host 'ERROR: MySQL is NOT running on port 4306!' -ForegroundColor Red; exit 1 }"

echo.
echo [2/3] Compiling Java classes (if needed)...
call mvn compile -o -q
if errorlevel 1 (
    echo Compile failed! Trying without offline mode...
    call mvn compile -q
)

echo.
echo [3/3] Starting Spring Boot Application...
echo Backend will start on http://localhost:8080
echo Press Ctrl+C to stop the server
echo.

REM Run using Maven's dependency classpath
call mvn exec:java -Dexec.mainClass="com.PDSA.Eight_Queens.EightQueensApplication"

pause
