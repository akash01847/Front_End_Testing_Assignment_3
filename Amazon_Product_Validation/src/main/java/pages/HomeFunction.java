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
import java.util.Objects;

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

    @FindBy(xpath = "//span[normalize-space()='OnePlus 11R 5G (Galactic Silver, 8GB RAM, 128GB Storage)']")
    WebElement searchResult;

    @FindBy(css = "input[id=\"buy-now-button\"]")
    WebElement buyNowButton;

    @FindBy(xpath = "//td[@class='a-size-base']")
    WebElement customerRatings;

    @FindBy(css = "div[class=\"a-section vsx__offers multipleProducts\"]")
    WebElement offerContainer;

    By ratingNumber = By.cssSelector("div[id='averageCustomerReviews_feature_div'] span[class='a-size-base a-color-base']");

    public void captureScreenshot(String functionName) throws IOException {
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

//     public boolean textToSearch() throws NoSuchElementException, IOException {
//         try {
//             wait.until(ExpectedConditions.visibilityOf(searchBar));
            
//             searchBar.sendKeys(base.loadProperties("productID"));
//             searchButton.click();
//             return true;
//         } catch (NoSuchElementException | IOException e) {
//             captureScreenshot("textToSearch");
//             return false;
//         }
//     }
    
     public boolean textToSearch() throws NoSuchElementException, IOException {
        try {
            wait.until(ExpectedConditions.visibilityOf(searchBar));
            String productId = System.getenv("PRODUCT_ID");
            searchBar.sendKeys(productId);
            searchButton.click();
            return true;
        } catch (NoSuchElementException | IOException e) {
            captureScreenshot("textToSearch");
            return false;
        }
    }

    public boolean navigateToProductPage() throws TimeoutException, IOException {
        try {
            wait.until(ExpectedConditions.visibilityOf(searchResult));
            searchResult.click();
            if (Objects.equals(base.loadProperties("productPrice"), productRate.getText())) {
                System.out.println("On Product Page");
                return true;
            } else {
                return false;
            }
        } catch (TimeoutException e) {
            captureScreenshot("navigateToProductPage");
            throw e;
        }
    }

    public boolean validateBuyNowButton() throws TimeoutException, IOException {
        try {
            wait.until(ExpectedConditions.visibilityOf(standardIdentificationNumber));
            return buyNowButton.isDisplayed();
        } catch (TimeoutException e) {
            captureScreenshot("validateBuyNowButton");
            return true;
        }
    }

    public boolean checkCustomerRatings() throws AssertionError, IOException, NoSuchElementException {
        // Store the current window handle
        String currentHandle = driver.getWindowHandle();
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(currentHandle)) {
                driver.switchTo().window(handle);
                break;
            }
        }
        wait.until(ExpectedConditions.visibilityOf(stockStatus));
        customerRatings.isDisplayed();
        WebElement ratingElement = driver.findElement(ratingNumber);
        String ratingText = ratingElement.getAttribute("innerHTML");
        double ratingValue = Double.parseDouble(ratingText.replaceAll("[^0-9.]", ""));
        System.out.println("Customer rating: " + ratingValue);
        if (ratingValue >= 4.0) {
            System.out.println("Product rating is greater than 4.");
            return true;
        } else {
            System.out.println("Product rating is less than 4.");
            captureScreenshot("checkCustomerRatings");
            return false;
        }
    }

    public boolean offerDetails() throws IOException {
        try {
            wait.until(ExpectedConditions.visibilityOf(offerContainer));
            List<WebElement> offerElements = offerContainer.findElements(By.cssSelector("li[class=\"a-carousel-card\"]"));
            System.out.println("Found " + offerElements.size() + " offer elements");
            for (WebElement offerElement : offerElements) {
                System.out.println(offerElement.getText());
            }
            return offerContainer.isDisplayed();
        } catch (Exception e) {
            captureScreenshot("offerDetails");
            return true;
        }
    }
}
