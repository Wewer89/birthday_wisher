package pl.mwewerek;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.mwewerek.credentials.Credentials;
import pl.mwewerek.locators.Locators;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static WebDriver driver;
    public static WebDriverWait wait;

    public static void main(String[] args) throws InterruptedException {
        setUp();

        navigateToBirthdayPage();

        doLogin();

        List<WebElement> numberOfFieldsToInputWishes = waitForExpectedWebElements(Locators.INPUT_BIRTHDAY_WISHES);

        List<WebElement> linksWithNames = fetchLinksContainNames();

        makeBirthdayWishes(linksWithNames, numberOfFieldsToInputWishes);

        tearDown();
    }


    public static void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, 10);
    }

    public static void doLogin() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(Locators.ACCEPT_COOKIES_BUTTON))).click();
        driver.findElement(By.xpath(Locators.EMAIL_INPUT)).sendKeys(Credentials.EMAIL_ADDRESS);
        driver.findElement(By.xpath((Locators.PASSWORD_INPUT))).sendKeys(Credentials.PASSWORD);
        driver.findElement(By.xpath(Locators.LOGIN_BUTTON)).click();
    }

    public static void navigateToBirthdayPage() {
        String URL = "https://www.facebook.com/birthdays/";
        driver.navigate().to(URL);
    }

    public static List<WebElement> waitForExpectedWebElements(String locator) {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(locator)));
    }

    public static List<WebElement> fetchLinksContainNames() {
        WebElement birthdaySection = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(Locators.BIRTHDAY_SECTION)));
        return birthdaySection.findElements(By.xpath(Locators.LINK_WITH_NAME));
    }

    private static String extractNameFromLink(String link) {
        int groupContainsName = 1;
        String regex = "com.([A-z]+)\\.";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(link);
        matcher.find();
        try {
            return matcher.group(groupContainsName);
        } catch (Exception exception) {
            return "invalidName";
        }
    }

    public static void makeBirthdayWishes(List<WebElement> linksWithNames,
                                          List<WebElement> numberOfFieldsToInputWishes) {
        String birthdayWishes = "Wszystkiego najlepszego z okazji urodzin ";
        int indexOfFieldToInputWishes = 1;
        for (int linkIndex = 0; linkIndex < numberOfFieldsToInputWishes.size(); linkIndex++) {
            String indexConvertedToString = String.valueOf(indexOfFieldToInputWishes);
            String link = linksWithNames.get(linkIndex).getAttribute("href");
            String nameWithCapital = StringUtils.capitalize(extractNameFromLink(link));
            WebElement inputWishes = driver.findElement(By.xpath("(" + Locators.INPUT_BIRTHDAY_WISHES + ")" +
                    "[" + indexConvertedToString + "]"));
            if (nameWithCapital.equalsIgnoreCase("profile")) {
                continue;
            } else if (nameWithCapital.equalsIgnoreCase("invalidName")) {
                inputWishes.sendKeys(birthdayWishes + "!!!");
            } else {
                inputWishes.sendKeys(birthdayWishes + nameWithCapital + " !!!");
            }
            inputWishes.sendKeys(Keys.ENTER);
            indexOfFieldToInputWishes++;
        }
    }

    public static void tearDown() throws InterruptedException {
        Thread.sleep(3000);
        driver.quit();
    }
}
