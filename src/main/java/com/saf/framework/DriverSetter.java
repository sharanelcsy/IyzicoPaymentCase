package com.saf.framework;


import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;



public class DriverSetter {

	private static DriverSetter instance = null;
	ThreadLocal<WebDriver> oDriver = new ThreadLocal<WebDriver>();

	private DriverSetter() {

	}

	public static DriverSetter getInstance() {
		if (instance == null) {
			instance = new DriverSetter();
		}
		return instance;
	}


	public final void setWebDriver(String sDriverName) throws Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();

		if (sDriverName.equalsIgnoreCase("chrome")) {

			//Use a higher value if your mobile elements take time to show up
			oDriver.get().manage().timeouts().implicitlyWait(35, TimeUnit.SECONDS);
		}

	}

	public WebDriver getWebDriver() {
		return oDriver.get();
	}


}
