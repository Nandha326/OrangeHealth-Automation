# run-tests.ps1
# Usage: .\run-tests.ps1 @Smoke
# Usage: .\run-tests.ps1 "@Smoke and @HomePage"

param(
    [string]$tags = "@Smoke"
)

$env:JAVA_HOME = "C:\Program Files\Java\jdk-21.0.11"
if ($env:Path -notlike "*apache-maven*") {
    $env:Path += ";C:\Users\nandhakumar\maven\apache-maven-3.9.6\bin"
}

& mvn clean test "-Dcucumber.filter.tags=$tags"
