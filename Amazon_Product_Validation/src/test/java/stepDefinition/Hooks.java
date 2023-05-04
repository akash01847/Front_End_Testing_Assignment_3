package stepDefinition;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import util.TestContextSetup;

import java.io.IOException;

public class Hooks {
   TestContextSetup tcs;
    public Hooks(TestContextSetup tcs){
        this.tcs=tcs;
    }

    @Before
    public void setUp(){
        tcs.pom.getHome();
    }

    @After
    public void tearDown() throws IOException {
        tcs.base.initializeDriver().quit();
    }
}
