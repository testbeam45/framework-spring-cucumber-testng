package com.solution.mobile.utils;

import com.solution.common.utils.ApplicationProperties;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

@Component
public class DriverManager {
    private static final ThreadLocal < AndroidDriver > appiumDriverThreadLocal = new ThreadLocal <> ( );
   // private final Logger log = LoggerFactory.getLogger ( DriverManager.class );

    private WebDriverWait wait;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private DriverWait driverWait;

    @Autowired
    private Environment environment;

    public void createAppiumDriver ( ) throws MalformedURLException {
        if ( getAppiumDriver ( ) == null ) {
            if ( Arrays.toString ( this.environment.getActiveProfiles ( ) ).contains ( "jenkins" ) ) {
                //startEnvironment ();
                System.out.println ("--------------------------->"+applicationProperties.getAppiumGridUrl ( ) );
                setRemoteDriverAppium ( applicationProperties.getAppiumGridUrl ( ) );
            } else {

            }
        }
    }

    public boolean startEnvironment ( ) {
        String path = "C:\\Users\\jesus.bustamante\\Desktop\\spring-cucumber-testng-parallel-test-harness\\Selenoid-Grid-Remote-Docker.yml";
        boolean isUpDisplayed = false;
        boolean isEnvironmentUp = false;
        int times = 0;
        while (!isEnvironmentUp) {
            try {
                Process container = Runtime.getRuntime ( ).exec ( "docker ps" );
                BufferedReader reader = new BufferedReader ( new InputStreamReader ( container.getInputStream ( ) ) );
                String line = null;
                try {
                    while ((line = reader.readLine ( )) != null) {
                        isUpDisplayed = line.contains ( " Up" );
                        if ( !isUpDisplayed ) {
                            if ( times == 0 ) {
                                System.out.println ( "Starting Environment" );
                                Runtime.getRuntime ( ).exec ( "docker-compose -f " + path + " up -d" );
                                Thread.sleep ( 9000 );
                                times++;
                            }
                        } else {
                            System.out.println ( "Environment Works!" );
                            isEnvironmentUp = true;
                            return true;
                        }
                        System.out.println ( "Waiting to starting containers..." );
                    }
                    System.out.println ( "¡Environment Started!" );

                } catch (IOException e) {
                    e.getLocalizedMessage ( );
                }
            } catch (Exception e) {
                e.getLocalizedMessage ( );
            }
        }
        return false;
    }

    public boolean isHostReachable (String host) {
        try {
            URL url = new URL ( host );
            HttpURLConnection huc = (HttpURLConnection) url.openConnection ( );
            return ((huc.getResponseCode ( ) == HttpURLConnection.HTTP_OK));
        } catch (IOException e) {
            e.getLocalizedMessage ( );
        }
        return false;
    }


    public void setRemoteDriverAppium (String hubUrl) throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities ( );
        if ( true ) {
            capabilities.setCapability ( MobileCapabilityType.DEVICE_NAME , applicationProperties.getDeviceName ( ) );
            capabilities.setCapability ( "enableVNC" , true );
        } else {
            capabilities.setCapability ( MobileCapabilityType.DEVICE_NAME , "Android Emulator" );
            //capabilities.setCapability ( MobileCapabilityType.PLATFORM_NAME , "Android" );
            capabilities.setCapability ( MobileCapabilityType.AUTOMATION_NAME , "UIAutomator1" );
        }
        capabilities.setCapability ( "app" , applicationProperties.getFullPathAppName ( ) );
        capabilities.setCapability ( "androidInstallTimeout" , 180000 );
        capabilities.setCapability ( "newCommandTimeout" , 180000 );
        capabilities.setCapability ( MobileCapabilityType.NO_RESET , false );
        capabilities.setCapability ( MobileCapabilityType.FULL_RESET , false );

        // Create Remote Connection to Appium Grid
        appiumDriverThreadLocal.set ( new AndroidDriver ( new URL ( hubUrl ) , capabilities ) );
    }


    public AndroidDriver getAppiumDriver ( ) {
        return appiumDriverThreadLocal.get ( );
    }
}
