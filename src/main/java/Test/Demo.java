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
		Duration fifteenSeconds = Duration.ofSeconds(15);
		Duration oneSecond = Duration.ofSeconds(1);
		Wait<EdgeDriver> wait = new FluentWait(driver).withTimeout(fifteenSeconds).pollingEvery(oneSecond)
				.ignoring(NoSuchElementException.class);
		Date date = new Date();

		// Logging IN
		driver.findElement(By.linkText("Log in")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='Enter email']")));
		driver.findElement(By.xpath("//input[@placeholder='Enter email']")).sendKeys(prop.getProperty("username"));
		driver.findElement(By.xpath("//input[@value='Continue']")).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='Enter password']")));
		driver.findElement(By.xpath("//input[@placeholder='Enter password']")).sendKeys(prop.getProperty("password"));
		driver.findElement(By.xpath("//span[text()='Log in']")).click();

		// Creating New Board
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Create new board']")));
		driver.findElement(By.xpath("//span[text()='Create new board']")).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Board title']")));
		driver.findElement(By.xpath("//div[text()='Board title']/following-sibling::input"))
				.sendKeys("Board " + date.toString());
		driver.findElement(By.xpath("//button[@data-testid='create-board-submit-button']")).click();

		// Adding List
		wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//span[text()='Add a list']/ancestor::a/following-sibling::input")));
		driver.findElement(By.xpath("//span[text()='Add a list']/ancestor::a/following-sibling::input"))
				.sendKeys("List A");
		driver.findElement(By.xpath("//input[@value='Add list']")).click();
		driver.findElement(By.xpath("//input[@placeholder='Enter list title…']")).sendKeys("List B");
		driver.findElement(By.xpath("//input[@value='Add list']")).click();

		// Adding List Item
		driver.findElement(By.xpath("//h2[text()='List A']/ancestor::div[1]/following-sibling::div[2]/a[1]")).click();
		driver.manage().timeouts().implicitlyWait(oneSecond);
		driver.findElement(By.xpath("//h2[text()='List A']/ancestor::div[1]/following-sibling::div[1]//textarea[1]"))
				.sendKeys(date.toString());
		driver.findElement(By.xpath("//input[@value='Add card']")).click();

		// Dragging and dropping list item from List A to List B
		Actions action = new Actions(driver);
		WebElement ele1 = driver
				.findElement(By.xpath("//h2[text()='List A']/ancestor::div[1]/following-sibling::div[1]/a[1]"));
		Point p1 = ele1.getLocation();
		int x1 = p1.getX();
		int y1 = p1.getY();
		WebElement ele2 = driver
				.findElement(By.xpath("//h2[text()='List B']/ancestor::div[1]/following-sibling::div[2]/a[1]"));
		action.dragAndDrop(ele1, ele2).build().perform();

		// Printing X and Y co-ordinates
		Point p2 = driver.findElement(By.xpath("//h2[text()='List B']/ancestor::div[1]/following-sibling::div[2]/a[1]"))
				.getLocation();
		int x2 = p2.getX();
		int y2 = p2.getY();
		System.out.println("Card was moved from (" + x1 + "," + y1 + ") to (" + x2 + "," + y2 + ")");

	}

	@AfterClass
	public void logOutAndClose() {
		Duration twoSecond = Duration.ofSeconds(2);
		driver.findElement(By.xpath("//button[@data-testid= 'header-member-menu-button']")).click();
		driver.manage().timeouts().implicitlyWait(twoSecond);
		driver.findElement(By.xpath("//span[text()='Log out']")).click();
		driver.manage().timeouts().implicitlyWait(twoSecond);
		driver.findElement(By.xpath("//span[text()='Log out']")).click();
		driver.close();
	}
}
