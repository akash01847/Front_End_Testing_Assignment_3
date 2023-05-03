package stepDefinition;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import util.TestContextSetup;

import java.io.IOException;

public class HomeStepDef {

    TestContextSetup tcs;


    public HomeStepDef(TestContextSetup tcs) {
        this.tcs = tcs;
    }

    @Given("I am able to search specified productId")
    public void i_am_able_to_search_specified_product_id() throws IOException, PendingException {
        Assert.assertTrue(tcs.pom.getHome().textToSearch());
    }

    @When("I open the product page for specified productId")
    public void i_open_the_product_page_for_specified_product_id() throws PendingException, IOException {
        Assert.assertTrue(tcs.pom.getHome().navigateToProductPage());
    }

    @Then("I should see the Buy Now button")
    public void i_should_see_the_button() throws PendingException, IOException {
        Assert.assertTrue(tcs.pom.getHome().validateBuyNowButton());
    }

    @Then("I should check if customer rating for the product is over four")
    public void i_should_check_if_customer_rating_for_the_product_is_over_four() throws PendingException, IOException {
        Assert.assertTrue(tcs.pom.getHome().checkCustomerRatings());
    }

    @Then("Print all the offers available on the description page for the product")
    public void print_all_the_offers_available_on_the_description_page_for_the_product() throws PendingException, IOException {
        Assert.assertTrue(tcs.pom.getHome().offerDetails());
    }
}
