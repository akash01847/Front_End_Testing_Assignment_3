package pages;

import Util.Log;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import resources.Base;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import static Util.Log.log;

public class HomeFunction {

    WebDriver driver;

    WebDriverWait wait;

    Actions actions;

    Base base;


    public HomeFunction(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        actions = new Actions(driver);
        this.base = new Base();
    }

    @FindBy(css = "input[id=\"twotabsearchtextbox\"]")
    WebElement searchBar;

    @FindBy(css = "input[id=\"nav-search-submit-button\"]")
    WebElement searchButton;

    @FindBy(css = "#productDetails_detailBullets_sections1 > tbody > tr:nth-child(1) > td")
    WebElement standardIdentificationNumber;

    @FindBy(xpath = "//span[@class='a-size-medium a-color-success']")
    WebElement stockStatus;

    @FindBy(xpath = "//span[normalize-space()='39,999']")
    WebElement productRate;

    @FindBy(css = "div[class='a-section a-spacing-base']")
    WebElement plpTile;

    @FindBy(xpath = "//span[normalize-space()='OnePlus 11R 5G (Galactic Silver, 8GB RAM, 128GB Storage)']")
    WebElement searchResult;

    @FindBy(id = "buy-now-button")
    WebElement buyNowButton;

    @FindBy(xpath = "//td[@class='a-size-base']")
    WebElement customerRatings;
    @FindBy(css = "div[class=\"a-section a-spacing-mini vsx__headings\"]")
    WebElement offerHeading;
    @FindBy(css="div[id='anonCarousel1']")
    WebElement offerContainer;
    By ratingNumber = By.cssSelector("div[id='averageCustomerReviews_feature_div'] span[class='a-size-base a-color-base']");

    @FindBy(css = "a[id=\"breadcrumb-back-link\"]")
    WebElement backToResult;

    public void newWindow(WebDriver driver) {
        String currentWindowHandle = driver.getWindowHandle();
        Set<String> allWindowHandles = driver.getWindowHandles();
        for (String handle : allWindowHandles) {
            if (!handle.equals(currentWindowHandle)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }

    public void captureScreenshot(String functionName) {
        try {
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            String timestamp = String.valueOf(System.currentTimeMillis());
            String fileName = String.format("%s_%s.png", functionName, timestamp);
            String filePath = String.format("%s/screenshots/%s", System.getProperty("user.dir"), fileName);

            FileUtils.copyFile(screenshotFile, new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean textToSearch() throws NoSuchElementException, IOException {
        try {
            wait.until(ExpectedConditions.visibilityOf(searchBar));
//            String productId = System.getenv("PRODUCT_ID");
            searchBar.sendKeys(base.loadProperties("productID"));
            searchButton.click();
            return true;
        } catch (NoSuchElementException e) {
            captureScreenshot("textToSearch");
            return false;
        }
    }

    public boolean navigateToProductPage() throws TimeoutException, IOException {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(plpTile));
            plpTile.click();
            log.info("Clicked Plp");
            return true;
        } catch (TimeoutException e) {
            log.info("Plp not found");
            captureScreenshot("navigateToProductPage");
            throw e;
        }
    }

    public boolean validateBuyNowButton() throws TimeoutException {
        try {
                newWindow(driver);
                wait.until(ExpectedConditions.visibilityOf(buyNowButton));
                buyNowButton.isDisplayed();
                log.info("Buy Now Button Validated");
                return true;
            } catch (TimeoutException e) {
            log.info("Buy Now Button Not Visible");
            captureScreenshot("validateBuyNowButton");
        }
        return false;
    }


    public void checkCustomerRatings() throws AssertionError, NoSuchElementException {
        newWindow(driver);
        wait.until(ExpectedConditions.visibilityOf(offerHeading));
        WebElement ratingElement = driver.findElement(ratingNumber);
        String ratingText = ratingElement.getAttribute("innerHTML");
        double ratingValue = Double.parseDouble(ratingText.replaceAll("[^0-9.]", ""));
        log.info("Customer rating: " + ratingValue);
        if (ratingValue >= 4.0) {
            log.info("Product rating is greater than 4.");
        } else {
            log.info("Product rating is less than 4.");
            captureScreenshot("checkCustomerRatings");
        }
    }

    public boolean offerDetails() {
        try {
            newWindow(driver);
            wait.until(ExpectedConditions.visibilityOf(offerContainer));
            Log.info(offerContainer.getText());
            return true;
        } catch (Exception e) {
            captureScreenshot("offerDetails");
            throw e;
        }
    }
}
