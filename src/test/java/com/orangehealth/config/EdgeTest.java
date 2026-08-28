package com.orangehealth.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.File;

public class EdgeTest {
    public static void main(String[] args) {
        try {
            System.out.println("Testing Edge fallback...");
            String edgeBinary = "C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe";
            String driverPath = "C:\\Users\\nandh\\.cache\\selenium\\chromedriver\\win64\\151.0.7922.138\\chromedriver.exe";

            System.out.println("Edge binary exists: " + new File(edgeBinary).exists());
            System.out.println("Driver exists: " + new File(driverPath).exists());

            ChromeOptions options = new ChromeOptions();
            options.setBinary(edgeBinary);
            options.addArguments("--headless=new");
            options.addArguments("--remote-allow-origins=*");

            ChromeDriverService service = new ChromeDriverService.Builder()
                    .usingDriverExecutable(new File(driverPath))
                    .build();

            WebDriver driver = new ChromeDriver(service, options);
            System.out.println("Driver created! Navigating...");
            driver.get("https://www.orangehealth.in");
            System.out.println("Title: " + driver.getTitle());
            driver.quit();
            System.out.println("SUCCESS!");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
