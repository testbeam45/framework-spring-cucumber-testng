package com.solution.common.utils;

import java.time.Duration;

public class Constants {

    public static final Duration timeoutLong = Duration.ofSeconds(30);

    public static final Duration pollingLong = Duration.ofMillis(200);

    public static final Duration timeoutShort = Duration.ofSeconds(10);

    public static final Duration pollingShort = Duration.ofMillis(100);

    public static String DRIVER_DIRECTORY = System.getProperty("user.dir") + "/../common/src/main/resources/drivers";

    public static String COMMON_RESOURCES = System.getProperty("user.dir") + "/../common/src/main/resources";

}
