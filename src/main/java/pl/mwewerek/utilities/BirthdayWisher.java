package pl.mwewerek.utilities;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.mwewerek.locators.Locators;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BirthdayWisher {

    public static WebDriver driver;
    public static WebDriverWait wait;

    public static void makeBirthdayWishes() {
        int numberOfFieldsToInputWishes = countBirthdayWishesFields();
        List<WebElement> linksWithNames = fetchLinksContainNames();
        String birthdayWishes = "Wszystkiego najlepszego z okazji urodzin ";
        int indexOfFieldToInputWishes = 1;
        for (int linkIndex = 0; linkIndex < numberOfFieldsToInputWishes; linkIndex++) {
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

    private static int  countBirthdayWishesFields() {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy
                (By.xpath(Locators.INPUT_BIRTHDAY_WISHES))).size();
    }

    private static List<WebElement> fetchLinksContainNames() {
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
}
