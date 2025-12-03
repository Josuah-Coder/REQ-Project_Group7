package org.example;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {

                "src/test/resources/features/customer/billing-transactions.feature",
                "src/test/resources/features/customer/charging-process.feature",
                "src/test/resources/features/customer/find-charging-point.feature",


                "src/test/resources/features/operator/billing-view.feature",
                "src/test/resources/features/operator/charging-point-management.feature",
                "src/test/resources/features/operator/location-management.feature",
                "src/test/resources/features/operator/price-management.feature",
                "src/test/resources/features/operator/status-monitoring.feature"
        },
        glue = "org.example",
        plugin = {
                "pretty",
                "html:target/cucumber-reports.html",
                "json:target/cucumber.json"
        },
        tags = "@MVP2"
)
public class RunCucumberTest {
}