Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$projectRoot = $PSScriptRoot

function Invoke-Test {
    param (
        [Parameter(Mandatory)]
        [string]$Description,
        [Parameter(Mandatory)]
        [string]$Directory,
        [Parameter(Mandatory)]
        [string[]]$Command
    )

    Write-Host "Running $Description..."
    Push-Location $Directory
    try {
        $executable = $Command[0]
        $arguments =
            if ($Command.Length -gt 1) {
                $Command[1..($Command.Length - 1)]
            }
            else {
                @()
            }

        & $executable @arguments
    }
    finally {
        Pop-Location
    }
}

Invoke-Test `
    -Description 'backend tests' `
    -Directory (Join-Path $projectRoot 'backend') `
    -Command @('mvn', 'test', '-Dfmt.skip')

Invoke-Test `
    -Description 'frontend tests' `
    -Directory (Join-Path $projectRoot 'frontend') `
    -Command @('npm', 'run', 'test', '--', '--watch=false')
