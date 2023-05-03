package util;

import pages.PageObjectManager;
import resources.Base;

import java.io.IOException;

public class TestContextSetup {
public Base base;
public PageObjectManager pom;
public TestContextSetup() throws IOException {
    base=new Base();
    pom=new PageObjectManager(base.initializeDriver());
}
}
