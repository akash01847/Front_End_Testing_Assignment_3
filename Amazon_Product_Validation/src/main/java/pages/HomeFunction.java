package pages;

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
    @FindBy(css = "div[id=\"averageCustomerReviews\"]")
    WebElement ratingValue;
    By plp = By.cssSelector("div[data-component-type=\"s-search-result\"]");
    @FindBy(css = "div[data-component-type=\"s-search-result\"]")
    WebElement plpTile;
    @FindBy(id = "buy-now-button")
    WebElement buyNowButton;
    @FindBy(css = "div[id=\"vsxoffers_feature_div\"] div.a-carousel-viewport")
    WebElement offerBox;
    By byOfferContainer = By.cssSelector("div.a-cardui-body li.a-carousel-card");
    By ratingNumber = By.cssSelector("div[id='averageCustomerReviews_feature_div'] span[class='a-size-base a-color-base']");

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
            String productId = System.getenv("PRODUCT_ID");
            searchBar.sendKeys(productId);
            searchButton.click();
            return true;
        } catch (NoSuchElementException e) {
            captureScreenshot("textToSearch");
            return false;
        }
    }

        public boolean navigateToProductPage() throws TimeoutException {
            try {
                wait.until(ExpectedConditions.visibilityOf(plpTile));
                List<WebElement> productListing = driver.findElements(plp);
                productListing.get(0).click();
                log.info("Clicked Plp");
                return true;
            } catch (TimeoutException e) {
                captureScreenshot("navigateToProductPage");
                throw e;
            }
        }

    public boolean validateBuyNowButton() throws TimeoutException {
        try {
                newWindow(driver);
                driver.navigate().refresh();
                wait.until(ExpectedConditions.visibilityOf(buyNowButton));
                return buyNowButton.isDisplayed();
            } catch (TimeoutException e) {
            log.info("Buy Now Button Not Visible");
            captureScreenshot("validateBuyNowButton");
        }
        return false;
    }

    public void checkCustomerRatings() throws AssertionError, NoSuchElementException {
        try {
            newWindow(driver);
            wait.until(ExpectedConditions.visibilityOf(ratingValue));
            WebElement ratingElement = driver.findElement(ratingNumber);
            String ratingText = ratingElement.getAttribute("innerHTML");
            double ratingValue = Double.parseDouble(ratingText.replaceAll("[^0-9.]", ""));
            log.info("Customer rating: " + ratingValue);
            if (ratingValue >= 4.0) {
                log.info("Product rating is greater than 4.");
            } else {
                log.info("Product rating is less than 4.");
            }
        } catch (TimeoutException e) {
            captureScreenshot("checkCustomerRatings");
            throw e;
        }
    }

    public boolean offerDetails() {
        try {
            newWindow(driver);
            List<WebElement> offerText = offerBox.findElements(byOfferContainer);
            for (WebElement offer : offerText) {
                log.info(offer.getText());
            }
            return true;
        } catch (Exception e) {
            captureScreenshot("offerDetails");
            throw e;
        }
    }
}
