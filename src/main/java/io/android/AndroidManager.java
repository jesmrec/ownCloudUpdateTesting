/**
 * @author Jesús Recio Rincón (@jesmrec)
 */

package io.android;

import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.logging.Level;

import io.appium.java_client.android.AndroidDriver;
import utils.LocProperties;
import utils.log.Log;

public class AndroidManager {

    private static AndroidDriver driver = null;
    private static File app;
    private static final String driverURL = LocProperties.getProperties().getProperty("appiumURL");
    private static final String appPackage = LocProperties.getProperties().getProperty("appPackage");

    private AndroidManager() {
    }

    public static void init() {

        File rootPath = new File(System.getProperty("user.dir"));
        File appDir = new File(rootPath, "src/test/resources");
        app = new File(appDir, LocProperties.getProperties().getProperty("apkName"));

        DesiredCapabilities capabilities = new DesiredCapabilities();
        setCapabilities(capabilities);

        try {
            driver = new AndroidDriver(new URL(driverURL), capabilities);
        } catch (MalformedURLException e) {
            Log.log(Level.SEVERE, "Driver could not be created: " + e.getMessage());
            e.printStackTrace();
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        Log.log(Level.FINE, "Device: " +
                driver.getCapabilities().getCapability("deviceManufacturer") + " " +
                driver.getCapabilities().getCapability("deviceModel"));
        Log.log(Level.FINE, "Platform: " +
                driver.getCapabilities().getCapability("platformName") + " " +
                driver.getCapabilities().getCapability("platformVersion"));
        Log.log(Level.FINE, "API Level: " +
                driver.getCapabilities().getCapability("deviceApiLevel") + "\n");

    }

    public static AndroidDriver getDriver() {
        if (driver == null) {
            init();
        }
        return driver;
    }

    //Check https://appium.io/docs/en/2.5/guides/caps/
    public static void setCapabilities(DesiredCapabilities capabilities) {

        // Name of the device or emulator (arbitrary for emulator)
        capabilities.setCapability("appium:deviceName", "test");

        // Absolute path to the APK to be installed on the emulator
        capabilities.setCapability("appium:app", app.getAbsolutePath());

        // Target platform (Android in this case)
        capabilities.setCapability("appium:platformName", "Android");

        // Automation engine to use (recommended for Android)
        capabilities.setCapability("appium:automationName", "UIAutomator2");

        // Application package to launch
        capabilities.setCapability("appium:appPackage", appPackage);

        // Entry activity to start the app
        capabilities.setCapability("appium:appActivity",
                "com.owncloud.android.ui.activity.SplashActivity");

        // Package to wait for after launching the app
        capabilities.setCapability("appium:appWaitPackage",
                LocProperties.getProperties().getProperty("appPackage"));

        // Wait until the app is fully launched before proceeding
        capabilities.setCapability("appium:appWaitForLaunch", "true");

        // Automatically grant all runtime permissions at install time
        capabilities.setCapability("appium:autoGrantPermissions", true);

        // Enable Unicode input for special characters (e.g., emojis, different scripts)
        capabilities.setCapability("appium:unicodeKeyboard", true);

        // Restore the original keyboard after the test session
        capabilities.setCapability("appium:resetKeyboard", true);

        // Disable window animations to reduce flakiness and improve speed
        capabilities.setCapability("appium:disableWindowAnimation", true);

        // Avoid resetting app data between sessions; preserves login and preferences
        capabilities.setCapability("appium:noReset", true);

        // Timeout in seconds for a new command to be sent before session is terminated
        capabilities.setCapability("appium:newCommandTimeout", 60);

        // Maximum time (in ms) to wait for the UiAutomator2 server to start on the device
        capabilities.setCapability("appium:uiautomator2ServerLaunchTimeout", 60000);

        // Maximum time (in ms) Appium will wait for an ADB command to complete
        capabilities.setCapability("appium:adbExecTimeout", 60000);

        // Max time (in ms) allowed for installing the APK on the emulator
        capabilities.setCapability("appium:androidInstallTimeout", 90000);
    }
}
