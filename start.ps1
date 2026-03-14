Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

$RootDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$FrontendDir = Join-Path $RootDir "frontend"
$BackendDir = Join-Path $RootDir "backend"

$backendProcess = $null
$frontendProcess = $null

function Stop-ChildProcesses {
    if ($script:backendProcess -and -not $script:backendProcess.HasExited) {
        Stop-Process -Id $script:backendProcess.Id -ErrorAction SilentlyContinue
    }

    if ($script:frontendProcess -and -not $script:frontendProcess.HasExited) {
        Stop-Process -Id $script:frontendProcess.Id -ErrorAction SilentlyContinue
    }
}

try {
    if (-not (Test-Path (Join-Path $FrontendDir "node_modules"))) {
        Write-Host "Installing frontend dependencies..."
        npm --prefix $FrontendDir install
    }

    Write-Host "Starting backend on http://localhost:8080"
    $script:backendProcess = Start-Process -FilePath "mvn.cmd" `
        -ArgumentList "-f", (Join-Path $BackendDir "pom.xml"), "spring-boot:run" `
        -PassThru `
        -NoNewWindow

    Write-Host "Starting frontend on http://localhost:4200"
    $script:frontendProcess = Start-Process -FilePath "npm.cmd" `
        -ArgumentList "--prefix", $FrontendDir, "start" `
        -PassThru `
        -NoNewWindow

    Wait-Process -Id $script:backendProcess.Id, $script:frontendProcess.Id
}
finally {
    Stop-ChildProcesses
}
