package pages;

import org.openqa.selenium.WebDriver;

public class PageObjectManager {
    WebDriver driver;
    HomeFunction home;

    public PageObjectManager(WebDriver driver){
        this.driver=driver;
    }

    public HomeFunction getHome() {
        home = new HomeFunction(driver);
        return home;
    }

}
