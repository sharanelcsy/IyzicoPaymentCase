package com.project.tests.Runner;


import com.saf.framework.TestNGBase;
import io.cucumber.testng.CucumberFeatureWrapper;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.PickleEventWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import io.qameta.allure.Attachment;
import org.apiguardian.api.API;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@CucumberOptions(

        features = "src/test/features/Test.feature",
         tags="@Test",
        plugin = {"pretty", "io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm"},
        glue = {"com.project.stepdefs"})

public class TestRunnerBase extends TestNGBase {
    @API(
            status = API.Status.STABLE
    )
    private io.cucumber.testng.TestNGCucumberRunner testNGCucumberRunner;


    //String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    @BeforeClass(alwaysRun = true)
    public void setUpClass() {
        this.testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    }

    @Test(
            groups = "cucumber",
            description = "Runs Cucumber Feature",
            dataProvider = "features")
    public void feature(PickleEventWrapper pickleWrapper, CucumberFeatureWrapper featureWrapper) throws Throwable {
        this.testNGCucumberRunner.runScenario(pickleWrapper.getPickleEvent());
    }

    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] saveScreenshot(WebDriver oDriver) {
        return ((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES);
    }

    @DataProvider
    public Object[][] features() {
        return this.testNGCucumberRunner == null ? new Object[0][0] : this.testNGCucumberRunner.provideScenarios();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        testNGCucumberRunner.finish();
    }
}
