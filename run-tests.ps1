# run-tests.ps1
#
# Usage:
# .\run-tests.ps1
# .\run-tests.ps1 -tags "@Smoke"
# .\run-tests.ps1 -suiteXml "testng-crossbrowser.xml" -tags "@Smoke"
# .\run-tests.ps1 -suiteXml "testng-crossbrowser.xml" -browser1 chrome -browser2 edge -tags "@Smoke"
# .\run-tests.ps1 -suiteXml "testng-crossbrowser.xml" -browser1 chrome -browser2 edge -tags "@Smoke" -headless

param(
    [string]$tags = "@Smoke",
    [string]$suiteXml = "testng.xml",
    [string]$browser1 = "chrome",
    [string]$browser2 = "firefox",
    [switch]$headless
)

# Java
$env:JAVA_HOME = "C:\Users\nandh\.jdk\jdk-25.0.2"

# Maven
$mavenPath = "C:\Users\nandhakumar\maven\apache-maven-3.9.6\bin"

if ($env:Path -notlike "*$mavenPath*") {
    $env:Path += ";$mavenPath"
}

# Browser mode
if ($headless) {
    $headlessValue = "true"
    Write-Host "Running in HEADLESS mode" -ForegroundColor Yellow
}
else {
    $headlessValue = "false"
    Write-Host "Running in HEADED mode" -ForegroundColor Green
}

Write-Host "Suite   : $suiteXml"
Write-Host "Tags    : $tags"
Write-Host "Browser1: $browser1"
Write-Host "Browser2: $browser2"

# Run Maven
& mvn clean test `
    "-DsuiteXmlFile=$suiteXml" `
    "-Dcucumber.filter.tags=$tags" `
    "-Dbrowser1=$browser1" `
    "-Dbrowser2=$browser2" `
    "-Dheadless=$headlessValue"