package com.solution.mobile.utils;

import com.solution.common.utils.ApplicationProperties;
import com.solution.common.utils.SelenoidType;
import com.solution.common.utils.SelenoidValues;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

@Component
public class DriverManager {
    private static  ThreadLocal < AndroidDriver > appiumDriverThreadLocal = new ThreadLocal <> ( );
    private final Logger log = LoggerFactory.getLogger ( DriverManager.class );

    @Autowired
    private static ApplicationProperties applicationProperties;

    @Autowired
    private DriverWait driverWait;

    @Autowired
    private Environment environment;

    public void createAppiumDriver ( ) throws MalformedURLException {
        if ( getAppiumDriver ( ) == null ) {
            if ( Arrays.toString ( this.environment.getActiveProfiles ( ) ).contains ( "jenkins" ) ) {
                log.info ( "Remote URL for Appium: " + "applicationProperties.getAppiumGridUrl ( )" );
                setRemoteDriverAppium ( "http://127.0.0.1:4444/wd/hub" );
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

    /*
     * This method is using to set Selenoid Capabilities
     * https://github.com/aerokube/selenoid/blob/master/docs/special-capabilities.adoc
     *
     * */
    public synchronized static void setRemoteDriverAppium (String hubUrl) throws MalformedURLException {

        DesiredCapabilities capabilities = new DesiredCapabilities ( );

        /*Define Android Skin and Activate VNC*/
        capabilities.setCapability ( SelenoidType.ENABLE_VNC , true );
        capabilities.setCapability ( SelenoidType.SKIN , "WXGA720" );

        /*Set Run Environment*/
        capabilities.setCapability ( SelenoidType.SCREEN_RESOLUTION , "1280x1024x24" );
        capabilities.setCapability ( SelenoidType.TIME_ZONE , "Europe/Moscow" );
        capabilities.setCapability ( MobileCapabilityType.BROWSER_NAME , SelenoidValues.android );
        capabilities.setCapability ( MobileCapabilityType.PLATFORM_VERSION , "7" );
        capabilities.setCapability ( MobileCapabilityType.APP , "https://github.com/jesussalatiel/Appium-Cloud/raw/main/notepad.apk" );
        capabilities.setCapability ( MobileCapabilityType.AUTOMATION_NAME , AutomationName.ANDROID_UIAUTOMATOR2 );

        /*Load Waits*/
        capabilities.setCapability ( SelenoidType.ANDROID_INSTALL_TIMEOUT, 180000 );
        capabilities.setCapability ( SelenoidType.ACCEPT_INSECURE_CERTS , true );
        capabilities.setCapability ( SelenoidType.APP_WAIT_DURATION , 60000 );
        capabilities.setCapability ( SelenoidType.AVD_LAUNCH_TIMEOUT , 180000 );
        capabilities.setCapability ( SelenoidType.AVD_READY_TIMEOUT , 180000 );
        capabilities.setCapability ( MobileCapabilityType.NEW_COMMAND_TIMEOUT , 180000 );

        /*Reset app or environment*/
        capabilities.setCapability ( MobileCapabilityType.NO_RESET , false );
        capabilities.setCapability ( MobileCapabilityType.FULL_RESET , false );

        // Create Remote Connection to Selenoid
        appiumDriverThreadLocal.set ( new AndroidDriver <> ( new URL ( hubUrl ) , capabilities ) );
    }


    public synchronized static AndroidDriver getAppiumDriver ( ) {
        return appiumDriverThreadLocal.get ( );
    }
}
