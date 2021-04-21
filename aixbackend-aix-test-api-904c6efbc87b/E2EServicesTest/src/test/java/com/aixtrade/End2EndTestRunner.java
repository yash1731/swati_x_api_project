package com.aixtrade;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        dryRun = false
        , monochrome = true
        , plugin = {"html:target/cucumber.html",
        "json:target/cucumber.json",
        "pretty:target/cucumber-pretty.txt",
        "usage:target/cucumber-usage.json",
        "junit:target/cucumber-results.xml",
        "io.qameta.allure.cucumber6jvm.AllureCucumber6Jvm"
        }
        , features = {"src/test/resources"}
        , snippets = CucumberOptions.SnippetType.CAMELCASE
        , glue = {"com.aixtrade", "com.aixtrade.hooks", "com.aixtrade.rules"}
        , tags = "@regression6879a"
)
public class End2EndTestRunner {

}
