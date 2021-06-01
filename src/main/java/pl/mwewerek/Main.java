package pl.mwewerek;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.mwewerek.credentials.Credentials;
import pl.mwewerek.locators.Locators;
import pl.mwewerek.utilities.BirthdayWisher;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends BirthdayWisher{

    public static void main(String[] args) throws InterruptedException {
        setUp();

        navigateToBirthdayPage();

        doLogin();

        checkIfBirthdaysToday();

        BirthdayWisher.makeBirthdayWishes();

        tearDown();
    }


    private static void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, 10);
    }

    private static void doLogin() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(Locators.ACCEPT_COOKIES_BUTTON))).click();
        } catch (NoSuchElementException exception) {
            System.out.println("There is no 'Accept Cookies' window");
        }
        driver.findElement(By.xpath(Locators.EMAIL_INPUT)).sendKeys(Credentials.EMAIL_ADDRESS);
        driver.findElement(By.xpath((Locators.PASSWORD_INPUT))).sendKeys(Credentials.PASSWORD);
        driver.findElement(By.xpath(Locators.LOGIN_BUTTON)).click();
    }

    private static void navigateToBirthdayPage() {
        String URL = "https://www.facebook.com/birthdays/";
        driver.navigate().to(URL);
    }

    private static void checkIfBirthdaysToday() throws InterruptedException {
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy
                    (By.xpath(Locators.INPUT_BIRTHDAY_WISHES)));
        } catch (TimeoutException exception) {
            System.out.println("There is no birthday today !!!");
            tearDown();
            Thread.currentThread().stop();
        }
    }

    private static void tearDown() throws InterruptedException {
        Thread.sleep(3000);
        driver.quit();
    }
}
