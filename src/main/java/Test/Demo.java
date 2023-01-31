package Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import org.testng.annotations.*;

public class Demo {
	
	private WebDriver driver;
	private Properties prop;
	
	@BeforeClass
	public void initializeURL() throws IOException {
		prop = new Properties();
		FileInputStream propFile = new FileInputStream("src\\test\\resources\\trello.properties");
		prop.load(propFile);
		
		System.setProperty("webdriver.edge.driver", "src\\test\\resources\\msedgedriver.exe");
		driver = new EdgeDriver();
		driver.get(prop.getProperty("URL"));
		driver.manage().window().maximize();
		
	}
	
	@Test
	public void RunTest() {
		Duration tenSeconds = Duration.ofSeconds(10);
		Duration oneSecond = Duration.ofSeconds(1);
		Wait<EdgeDriver> wait = new FluentWait(driver).withTimeout(tenSeconds).pollingEvery(oneSecond)
				.ignoring(NoSuchElementException.class);
		Date date = new Date();

		// Logging IN
		driver.findElement(By.linkText("Log in")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("loginPage.username.xpath"))));
		driver.findElement(By.xpath(prop.getProperty("loginPage.username.xpath"))).sendKeys(prop.getProperty("username"));
		driver.findElement(By.xpath(prop.getProperty("loginPage.continue.xpath"))).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("loginPage.password.xpath"))));
		driver.findElement(By.xpath(prop.getProperty("loginPage.password.xpath"))).sendKeys(prop.getProperty("password"));
		driver.findElement(By.xpath(prop.getProperty("loginPage.login.xpath"))).click();

		// Creating New Board
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("homePage.createBoard.xpath"))));
		driver.findElement(By.xpath(prop.getProperty("homePage.createBoard.xpath"))).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("homePage.boardTitle.xpath"))));
		driver.findElement(By.xpath(prop.getProperty("homePage.boardTitle.xpath")))
				.sendKeys("Board " + date.toString());
		driver.findElement(By.xpath(prop.getProperty("homePage.createBoardSubmit.xpath"))).click();

		// Adding List
		wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath(prop.getProperty("listPage.listAHeading.xpath"))));
		driver.findElement(By.xpath(prop.getProperty("listPage.listAHeading.xpath")))
				.sendKeys("List A");
		driver.findElement(By.xpath(prop.getProperty("listPage.addList.xpath"))).click();
		driver.findElement(By.xpath(prop.getProperty("listPage.listBHeading.xpath"))).sendKeys("List B");
		driver.findElement(By.xpath(prop.getProperty("listPage.addList.xpath"))).click();

		// Adding List Item
		driver.findElement(By.xpath(prop.getProperty("listPage.addListItem.xpath"))).click();
		driver.manage().timeouts().implicitlyWait(oneSecond);
		driver.findElement(By.xpath(prop.getProperty("listPage.listItemTextbox.xpath")))
				.sendKeys(date.toString());
		driver.findElement(By.xpath(prop.getProperty("listPage.addCard.xpath"))).click();

		// Dragging and dropping list item from List A to List B
		Actions action = new Actions(driver);
		WebElement ele1 = driver
				.findElement(By.xpath(prop.getProperty("listPage.listAItem.xpath")));
		Point p1 = ele1.getLocation();
		int x1 = p1.getX();
		int y1 = p1.getY();
		WebElement ele2 = driver
				.findElement(By.xpath(prop.getProperty("listPage.listBBox.xpath")));
		action.dragAndDrop(ele1, ele2).build().perform();

		// Printing X and Y co-ordinates
		Point p2 = driver.findElement(By.xpath(prop.getProperty("listPage.listBItem.xpath")))
				.getLocation();
		int x2 = p2.getX();
		int y2 = p2.getY();
		System.out.println("Card was moved from (" + x1 + "," + y1 + ") to (" + x2 + "," + y2 + ")");

	}

	@AfterClass
	public void logOutAndClose() {
		Duration twoSecond = Duration.ofSeconds(2);
		driver.findElement(By.xpath(prop.getProperty("headerMenu.profileIcon.xpath"))).click();
		driver.manage().timeouts().implicitlyWait(twoSecond);
		driver.findElement(By.xpath(prop.getProperty("headerMenu.logOut.xpath"))).click();
		driver.manage().timeouts().implicitlyWait(twoSecond);
		driver.findElement(By.xpath(prop.getProperty("headerMenu.logOut.xpath"))).click();
		driver.close();
	}
}
