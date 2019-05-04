package ca.on.elections.eregistration;



import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class SearchFieldsTests {
	private WebDriver driver;

	@BeforeMethod(alwaysRun = true)
	private void setUp() {
		System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		String url = "https://eregistration.elections.on.ca/en/search";
		driver.get(url);
		System.out.println("Page is open");
	}

////////Positive Tests

	@Test(priority = 1, groups = { "positiveTests", "smokeTests" })
	public void positiveSearchTests() {

		//enterfirstName
		WebElement firstName = driver.findElement(By.id("firstName"));
		firstName.sendKeys("Natalia");
		
		// enter lastname
		WebElement lastName = driver.findElement(By.id("lastName"));
		lastName.sendKeys("My last Name");

		// enter dateOfBirth
		WebElement month = driver.findElement(By.id("monthId"));
		month.sendKeys("January");
		WebElement day = driver.findElement(By.id("day"));
		day.sendKeys("12");
		WebElement year = driver.findElement(By.id("year"));
		year.sendKeys("1985");

		// enter postal code
		WebElement postalCode = driver.findElement(By.id("postalCode"));
		postalCode.sendKeys("M6G3Y6");
		sleep(3000);
		// enter address
		Select streetList = new Select(driver.findElement(By.id("streetList")));
		streetList.selectByVisibleText("ELLSWORTH AVE, TORONTO");

		WebElement addressNumber = driver.findElement(By.id("addressNumber"));
		addressNumber.sendKeys("1");
		
		
		WebElement unitNumber = driver.findElement(By.id("unitNumber"));
		unitNumber.sendKeys("1");
		
		// click search button
		WebElement searchBtn = driver.findElement(By.xpath(
				"/html/body[@class='language-en']/app-root/app-elector-search//section//div[@class='search-info']/div[2]/button[@class='btn-large-yellow']"));
		
		 searchBtn.click();

		// Verifications:
		 
		//Verify URL:
		//	String expectedUrl = "https://eregistration.elections.on.ca/en/search";
		//	 String currentUrl = driver.getCurrentUrl();
		//	 Assert.assertEquals(currentUrl, expectedUrl, "ERROR: Actual URL differs");
		    	
		WebElement alertBox = driver.findElement(By.xpath(
				"//app-root/app-elector-search//section//app-validation-summary/div[@class='alert-banner-multiple']/ol"));
		Assert.assertTrue(!alertBox.isDisplayed(), "Alert Box is present");

	}

	//////// Negative Tests

	@Parameters({ "firstName", "lastName", "month", "day", "year", "postalCode", "address", "addressNumber",
			"expectedMessage" })
	@Test(priority = 2, groups = { "negativeTests", "smokeTests" })
	public void testMandatoryFields(@Optional("Natalia") String firstName, String lastName, String month, String day,
			String year, String postalCode, String address, String addressNumber, String expectedMessage) {

		// Enter first Name
		WebElement firstNameElement = driver.findElement(By.id("firstName"));
		firstNameElement.sendKeys(firstName);

		// Enter Empty LastName
		WebElement lastNameElement = driver.findElement(By.id("lastName"));
		lastNameElement.sendKeys(lastName);

		// enter dateOfBirth
		WebElement monthElement = driver.findElement(By.id("monthId"));
		monthElement.sendKeys(month);
		WebElement dayElement = driver.findElement(By.id("day"));
		dayElement.sendKeys(day);
		WebElement yearElement = driver.findElement(By.id("year"));
		yearElement.sendKeys(year);

		// enter postal code
		WebElement postalCodeElement = driver.findElement(By.id("postalCode"));
		postalCodeElement.sendKeys(postalCode);
		sleep(2000);
		
		// enter address
		if (driver.findElement(By.id("streetList")).isDisplayed()) {
			Select streetList = new Select(driver.findElement(By.id("streetList")));
			streetList.selectByVisibleText(address);

			if (driver.findElement(By.id("addressNumber")).isDisplayed()) {
				WebElement addressNumberElement = driver.findElement(By.id("addressNumber"));
				addressNumberElement.sendKeys(addressNumber);

			}
		}
			
		// click search button
		WebElement searchBtn = driver.findElement(By.xpath(
				"/html/body[@class='language-en']/app-root/app-elector-search//section//div[@class='search-info']/div[2]/button[@class='btn-large-yellow']"));
		searchBtn.sendKeys(Keys.RETURN);
		

		// Verifications:
		    	
		WebElement errorMessage = driver.findElement(By.xpath(
				"//app-root/app-elector-search//section//app-validation-summary/div[@class='alert-banner-multiple']/ol/li[1]/a[@href='https://eregistration.elections.on.ca/en/search']"));
		String error = errorMessage.getText();
		Assert.assertTrue(error.contains(expectedMessage),
				"Expected message is different. Actual message is " + error + " and expected is " + expectedMessage);
	
	}

	@Test(priority = 3, groups = { "addressLinkTests" })
	public void openAddressLink() {
		driver.findElement(By.linkText("I have a different address type.")).click();
		WebElement anotherAddressFormat = driver.findElement(By.xpath(
				"//app-root/app-elector-search//section//app-elector-search-form/form/app-residential-address/div/div[1]"));
		Assert.assertTrue(anotherAddressFormat.isDisplayed(), "ERROR: There is no address box");

	}

	@Parameters({ "expected", "expectedUrl" })
	@Test(priority = 4, groups = { "headerLinksTests" })
	public void openLinkTest(String expected, String expectedUrl) {

		driver.findElement(By.linkText(expected)).click();
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}
		String actualUrl = driver.getCurrentUrl();

		//Assertions
		Assert.assertEquals(actualUrl, expectedUrl,
				"ERROR: Expected Url differs. Actual URL is " + actualUrl + " and expected is " + expectedUrl);

	}

	private void sleep(long m) {
		try {
			Thread.sleep(m);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		driver.close();
	}

}
