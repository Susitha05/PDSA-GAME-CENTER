# Eight Queens Backend Startup Script
# This script starts the Spring Boot backend server

$ErrorActionPreference = "Stop"
$projectPath = "F:\HNDSE232\PDSA 1\PDSA-GAME-CENTER\Eight-Queens\Eight-Queens"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Starting Eight Queens Backend Server" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Step 1: Check MySQL
Write-Host "[1/3] Checking MySQL on port 4306..." -ForegroundColor Yellow
$mysqlRunning = Test-NetConnection -ComputerName localhost -Port 4306 -InformationLevel Quiet -WarningAction SilentlyContinue
if ($mysqlRunning) {
    Write-Host "✓ MySQL is RUNNING on port 4306" -ForegroundColor Green
} else {
    Write-Host "✗ ERROR: MySQL is NOT running on port 4306!" -ForegroundColor Red
    Write-Host "Please start MySQL first!" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

# Step 2: Change to project directory
Write-Host ""
Write-Host "[2/3] Navigating to project directory..." -ForegroundColor Yellow
Set-Location $projectPath
Write-Host "✓ In directory: $projectPath" -ForegroundColor Green

# Step 3: Start the application using Maven exec plugin
Write-Host ""
Write-Host "[3/3] Starting Spring Boot Application..." -ForegroundColor Yellow
Write-Host "Backend will start on http://localhost:8080" -ForegroundColor Cyan
Write-Host "MySQL connection: localhost:4306/eight_queens" -ForegroundColor Cyan
Write-Host ""
Write-Host "Press Ctrl+C to stop the server" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Try to run using exec:java which uses a different mechanism
& mvn exec:java "-Dexec.mainClass=com.PDSA.Eight_Queens.EightQueensApplication"

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "Maven exec failed. Trying alternative method..." -ForegroundColor Yellow
    Write-Host ""
    
    # Alternative: Try spring-boot:run with offline mode
    & mvn spring-boot:run -o
}

Write-Host ""
Read-Host "Press Enter to exit"
