# Solution: Cucumber-Java-Page-Factory-TestNG Template

[![Actions Status](https://github.com/jesussalatiel/framework-spring-cucumber-testng/actions/workflows/GeneralRegression.yml/badge.svg)](https://github.com/jesussalatiel/framework-spring-cucumber-testng/actions)

#### with Cucumber-java, Cucumber Spring, custom annotation @PageObject, lambda expression ready, Selenium PageFactory and webdriver manager (Selenium WebDriver) for:
* ##### Chrome
* ##### Firefox
* ##### Appium
* ##### RestAssured

# Index

<table> 
<tr>
  <th>Start</th>
  <td>
    | <a href="#maven">Maven</a> 
    | <a href="#quickstart">Quickstart</a> | 
  </td>
</tr>
<tr>
  <th>Run</th>
  <td>
     | <a href="#junit">TestNG</a>
    | <a href="#command-line">Command Line</a>
    | <a href="#ide-support">IDE Support</a>    
    | <a href="#java-jdk">Java JDK</a>    
    | <a href="#troubleshooting">Troubleshooting</a>    |
  </td>
</tr>
<tr>
  <th>Report</th> 
  <td>
     | <a href="#configuration">Configuration</a> 
    | <a href="#environment-switching">Environment Switching</a>
    | <a href="#logging">Logging</a> |
  </td>
</tr>
<tr>
  <th>Docker</th>
  <td>
     | <a href="https://www.docker.com/products/docker-desktop/">Docker</a>
    | <a href="https://docs.docker.com/compose/">Docker Compose</a>|
 | <a href="https://www.docker.com/products/docker-desktop/">Docker Desktop</a>|
  </td>
</tr>
<tr>
  <th>Advanced</th>
  <td>
    | <a href="#hooks">Before / After Hooks</a>
    | <a href="#json-transforms">JSON Transforms</a>
    | <a href="https://support.smartbear.com/testcomplete/docs/app-testing/mobile/device-cloud/configure-appium/android-on-windows.html">Install Appium - Local</a> |
    </td>
</tr>
</table>

# Customise Cucumber tests
* add your .feature files with scenarios
* create custom steps class / steps classes with @Autowired annotations for page objects class / classes
* generate steps- in .feature file press 'alt+enter' shortcut and choose 'Create step definition' option, choose created steps class / steps classes to paste steps
* create custom page objects classes with methods and with @Component and @Autowired annotations for webdriver manager
* to use PageFactory, extend page object class / classes with BasePage class and add annotation: @PageObject
* create assertions classes with methods and @Component annotation
* delete example .feature files, steps classes, page objects classes and assertion classes

# Maven

The Framework uses [Spring Boot Test](https://spring.io/guides/gs/testing-web/), [Cucumber](https://cucumber.io/)
, [Rest Assured](https://rest-assured.io/) and [Selenium](https://www.selenium.dev/) client implementations.

Spring `<dependencies>`:

```xml

<dependecies>
    ...
    <dependency>
        <groupId>org.springframework.amqp</groupId>
        <artifactId>spring-rabbit</artifactId>
        <version>${spring-rabbit.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-test</artifactId>
    </dependency>
    ...
</dependecies>
```

Cucumber & Rest Assured `<dependencies>`:

```xml

<dependecies>
    ...
    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>rest-assured</artifactId>
        <version>${restassured.version}</version>
    </dependency>
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-java</artifactId>
        <version>${cucumber.version}</version>
    </dependency>
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-spring</artifactId>
        <version>${cucumber.version}</version>
    </dependency>
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-testng</artifactId>
        <version>${cucumber.version}</version>
    </dependency>
    ...
</dependecies>
```

Selenium `<dependencies>`:

```xml

<dependecies>
    ...
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>${selenium-version}</version>
    </dependency>
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-server</artifactId>
        <version>${selenium-version}</version>
    </dependency>
    ...
</dependecies>
```

# Quickstart

- [Intellij IDE](https://www.jetbrains.com/idea/) - `Recommended`
- [Java JDK 11](https://jdk.java.net/java-se-ri/11)
- [Apache Maven 3.6.3](https://maven.apache.org/docs/3.6.3/release-notes.html)

# TestNG

By using the [TestNG Framework](https://junit.org/junit4/) we can utilize the [Cucumber Framework](https://cucumber.io/)
and the `@CucumberOptions` Annotation Type to execute the `*.feature` file tests

> Right click the `WikipediParallelRunner` class and select `Run`

```java

@CucumberOptions(
        features = {
                "src/test/resources/feature"
        },
        plugin = {
                "pretty",
                "json:target/cucumber/report.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        })
public class WikipediaParallelRunnerTest extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

}
```

# Lambda expression ready
Steps classes implements En interface and are prepared to use code by 'lambda-way'. Example of lambda expression is used in SignUpFormPageObjects class:
```
    private void sendKeysForInputWithAttributeName(String partValueName, String keyToSend){
        webDriverFactory.getDriver().findElements(By.cssSelector(USER_INPUT))
                .stream()
                .filter(elem->elem.getAttribute(USER_INPUT_ATTRIBUTE_NAME).contains(partValueName))
                .findFirst()
                .get()
                .sendKeys(keyToSend);
    }
```

# Example in template:
```
@PageObject
public class ActionPageObjects extends BasePage {

    public ActionPageObjects(WebDriverManager webDriverFactory) {
        super(webDriverFactory);
    }

    @FindBy (how= How.CSS,  using=".Header-nav-item[href*=jam]")
    private WebElement menuJam;

    public void clickJamMenu() {
        menuJam.click();
    }
}
```

# Command Line

Normally you will use your IDE to run a `*.feature` file directly or via the `*Test.java` class. With the `Test` class,
we can run tests from the command-line as well.

Note that the `mvn test` command only runs test classes that follow the `*Test.java` naming convention.

You can run a single test or a suite or tests like so :

```
mvn test -Dtest=WikipediaParallelRunnerTest
```

Note that the `mvn clean install` command runs all test Classes that follow the `*Test.java` naming convention

```
mvn clean install
```

You can recompile the modules with this command
```
mvn clean install -fae
```
# IDE Support

To minimize the discrepancies between IDE versions and Locales the `<sourceEncoding>` is set to `UTF-8`

```xml

<properties>
    ...
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    ...
</properties>
```

# Java JDK

The Java version to use is defined in the `maven-compiler-plugin`

```xml

<build>
    ...
    <pluginManagement>
        <plugins>
            ...
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
            ...
        </plugins>
    </pluginManagement>
    ...
</build>
```

# Configuration

The `AbstractTestDefinition` class is responsible for specifying each Step class as `@SpringBootTest` and
its `@ContextConfiguration`

```java

@ContextConfiguration(classes = {FrameworkContextConfiguration.class})
@SpringBootTest
public class AbstractTestDefinition {
}
```

The `FrameworkContextConfiguration` class is responsible for specifying the Spring `@Configuration`, modules to scan,
properties to use etc

```java

@EnableRetry
@Configuration
@ComponentScan({
        "com.solution.api", "com.solution.common",
})
@PropertySource("application.properties")
public class FrameworkContextConfiguration {
}
```

# Environment Switching

There is only one thing you need to do to switch the environment - which is to set `<activeByDefault>` property in the
Master POM.

> By default, the value of `spring.profiles.active` is defined in the `application.properties` file which inherits its value from the Master POM property `<activeByDefault>`

```xml

<profiles>
    ...
    <profile>
        <id>prod</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <activatedProperties>prod</activatedProperties>
        </properties>
    </profile>
    ...
</profiles>
```

You can then specify the profile to use when running Maven from the command line like so:

```
mvn clean install -DactiveProfile=dev
```

You can run specific module with Maven from command line
```
mvn clean install -pl <integration, common, functional>
```

Below is an example of the `application.properties` file.

```properties
spring.profiles.active=@activatedProperties@
```

# Extent Reports

The Framework uses [Extent Reports Framework](https://extentreports.com/) to generate the HTML Test Reports

The example below is a report generated automatically by Extent Reports open-source library.

<img src="https://github.com/cmccarthyIrl/spring-cucumber-testng-parallel-test-harness/blob/master/common/src/main/resources/demo/extent-report.jpg" height="400px"/>

# Allure Reports

The Framework uses [Allure Reports](https://docs.qameta.io/allure/) to generate the HTML Test Reports

The example below is a report generated by Allure Reports open-source library.

<img src="https://github.com/cmccarthyIrl/spring-cucumber-testng-parallel-test-harness/blob/master/common/src/main/resources/demo/allure-report.png" height="400px"/>

To generate the above report navigate to the root directory of the module under test and execute the following command

`mvn allure:serve`  or `mvn allure:generate` (for an offline report)

# Logging

The Framework uses [Log4j2](https://logging.apache.org/log4j/2.x/) You can instantiate the logging service in any Class
like so

```java
private final Logger logger=LoggerFactory.getLogger(WikipediaPageSteps.class);
```

you can then use the logger like so :

```java
        logger.info("This is a info message");
        logger.warn("This is a warning message");
        logger.debug("This is a info message");
        logger.error("This is a error message");
```

# Before / After Hooks

The [Logback](http://logback.qos.ch/) logging service is initialized from the `Hooks.class`

As the Cucumber Hooks are implemented by all steps we can configure the `@CucumberContextConfiguration` like so :

```java

@CucumberContextConfiguration
public class Hooks extends AbstractTestDefinition {

    private static boolean initialized = false;
    private static final Object lock = new Object();

    @Autowired
    private HookUtil hookUtil;
    @Autowired
    private DriverManager driverManager;

    @Before
    public void beforeScenario(Scenario scenario) {
        synchronized (lock) {
            if (!initialized) {
                if (!driverManager.isDriverExisting()) {
                    driverManager.downloadDriver();
                }
                initialized = true;
            }
        }
        driverManager.createDriver();
    }

    @After
    public void afterScenario(Scenario scenario) {
        hookUtil.endOfTest(scenario);
        WebDriverRunner.closeWebDriver();
    }
}
```

# JSON Transforms

[Rest Assured IO](https://rest-assured.io/) is used to map the `Response` Objects to their respective `POJO` Classes

```xml

<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>3.0.0</version>
</dependency>
```


# Docker Compose

To run Appium Grid --version 3
```
docker-compose -f appium-grid-compose.yml up -d
```

To run Selenoid By Tags
```
mvn clean install -DactiveProfile=jenkins -D cucumber.filter.tags="@Web"
```

To run by Tags
```
mvn test -D cucumber.filter.tags="@Example"
```

# Troubleshooting

- Execute the following commands inside the project to resolve any dependency issues
    1. `mvn clean install -DskipTests`
    2. `mvn clean install -fae`

# References 
Documentation about Appium and Selenium Grid
1. [Appium Tutorial Step by Step Appium Automation](https://www.swtestacademy.com/appium-tutorial/)
2. [Docker Android: Docker Solution with NoVNC Supported and Video](https://morioh.com/p/0ec7c1a6f9b0)
3. [Docker Android Repository](https://github.com/budtmo/docker-android?ref=morioh.com&utm_source=morioh.com)
4. [How to setting Appium Server and Selenium Grid](https://github.com/budtmo/docker-android/blob/master/README_APPIUM_AND_SELENIUM.md)
5. [Docker Compose Example](https://github.com/budtmo/docker-android/blob/master/docker-compose.yml#L70)
6. [Grid v4 - Docker Compose - Example](https://github.com/SeleniumHQ/docker-selenium/blob/trunk/docker-compose-v3-full-grid.yml)
7. [Docker images for the Selenium Grid Server](https://github.com/SeleniumHQ/docker-selenium#docker-compose)
8. [Github Actions - android-emulator-runner](https://github.com/marketplace/actions/android-emulator-runner)