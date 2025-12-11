@echo off
echo ========================================
echo Starting Eight Queens Backend Server
echo ========================================
echo.
echo NOTE: This script runs the application using VS Code Java Extension
echo If this fails, please use the "Run Java" button in VS Code on EightQueensApplication.java
echo.
cd /d "f:\HNDSE232\PDSA 1\PDSA-GAME-CENTER\Eight-Queens\Eight-Queens"

REM Try to use VS Code's Java Extension to run
echo Attempting to start the application...
echo Please click the "Run" button above the main() method in EightQueensApplication.java
echo Or right-click on the file and select "Run Java"
echo.
echo The server should start on http://localhost:8080
echo.
pause
