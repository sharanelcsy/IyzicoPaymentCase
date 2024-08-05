package com.saf.framework;


import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;



public class DriverSetter {

	private static DriverSetter instance = null;
	ThreadLocal<WebDriver> oDriver = new ThreadLocal<WebDriver>();







	public WebDriver getWebDriver() {
		return oDriver.get();
	}


}
