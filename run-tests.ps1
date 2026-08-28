# run-tests.ps1
# Usage: .\run-tests.ps1 -tags "@Smoke"
# Usage: .\run-tests.ps1 -suiteXml "testng-crossbrowser.xml" -tags "@Smoke"

param(
    [string]$tags = "@Smoke",
    [string]$suiteXml = "testng.xml"
)

$env:JAVA_HOME = "C:\Users\nandh\.jdk\jdk-25.0.2"
if ($env:Path -notlike "*apache-maven*") {
    $env:Path += ";C:\Users\nandhakumar\maven\apache-maven-3.9.6\bin"
}

& mvn clean test "-DsuiteXmlFile=$suiteXml" "-Dcucumber.filter.tags=$tags"

