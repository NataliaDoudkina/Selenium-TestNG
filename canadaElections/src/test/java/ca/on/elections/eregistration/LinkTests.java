package ca.on.elections.eregistration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class LinkTests {

	private WebDriver driver;

	@BeforeMethod(alwaysRun = true)
	private void setUp() {
		System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		// open test page
		String url = "https://eregistration.elections.on.ca/en/search";

		driver.get(url);
		System.out.println("Page is open");

	}
	
	@Parameters({"expected",  "expectedUrl"})
    @Test
	public void openLink(String expected,  String expectedUrl) {
		
		
		driver.findElement(By.linkText(expected)).click();
		for (String winHandle : driver.getWindowHandles()) {
		    driver.switchTo().window(winHandle); 
		}

		String actualUrl = driver.getCurrentUrl();
		
	//Assertions
	Assert.assertEquals(actualUrl, expectedUrl, "ERROR: Expected Url differs. Actual URL is " + actualUrl + " and expected is " + expectedUrl);
		
    //Close browser
		driver.quit();
	}
	
}
