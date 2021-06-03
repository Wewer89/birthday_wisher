package pl.mwewerek;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.mwewerek.Base.Base;
import pl.mwewerek.resources.Credentials;
import pl.mwewerek.resources.Locators;
import pl.mwewerek.resources.UserMessages;
import pl.mwewerek.utilities.BirthdayWisher;

public class Main extends Base{

    public static void main(String[] args) throws InterruptedException {
        setUp();
        navigateToBirthdayPage();
        doLogin();
        checkIfBirthdaysToday();
        BirthdayWisher.makeBirthdayWishes();
        tearDown();
    }

    private static void doLogin() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(Locators.ACCEPT_COOKIES_BUTTON))).click();
        } catch (NoSuchElementException exception) {
            System.out.println(UserMessages.NO_ACCEPT_COOKIES_WINDOW);
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
            System.out.println(UserMessages.NO_BIRTHDAY);
            tearDown();
            Thread.currentThread().stop();
        }
    }
}
