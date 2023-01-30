package Test;

import java.time.Duration;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

public class Demo {

	public static void main(String args[]) throws InterruptedException {
		//opening the browser and URL
		System.setProperty("webdriver.edge.driver","C:\\msedgedriver.exe");
		EdgeDriver driver = new EdgeDriver();
		driver.get("https://trello.com/");
		driver.manage().window().maximize();
		driver.findElement(By.linkText("Log in")).click();
		Duration fifteenSeconds = Duration.ofSeconds(15);
		Duration oneSecond  = Duration.ofSeconds(1);
		Wait<EdgeDriver> wait = new FluentWait(driver).withTimeout(fifteenSeconds).pollingEvery(oneSecond).ignoring(NoSuchElementException.class);
		
		//logging IN
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='Enter email']")));
		driver.findElement(By.xpath("//input[@placeholder='Enter email']")).sendKeys("srikathir124@gmail.com");
		driver.findElement(By.xpath("//input[@value='Continue']")).click();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='Enter password']")));
		driver.findElement(By.xpath("//input[@placeholder='Enter password']")).sendKeys("FullCreative@30Jan2023");
		driver.findElement(By.xpath("//span[text()='Log in']")).click();
		
		//Creating New Board
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Create new board']")));
		driver.findElement(By.xpath("//span[text()='Create new board']")).click();
		
		Date date = new Date();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Board title']")));
		driver.findElement(By.xpath("//div[text()='Board title']/following-sibling::input")).sendKeys("Board "+date.toString());
		driver.findElement(By.xpath("//button[@data-testid='create-board-submit-button']")).click();
		
		//Adding List
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Add a list']/ancestor::a/following-sibling::input")));
		driver.findElement(By.xpath("//span[text()='Add a list']/ancestor::a/following-sibling::input")).sendKeys("List A");
		driver.findElement(By.xpath("//input[@value='Add list']")).click();
		driver.findElement(By.xpath("//input[@placeholder='Enter list title…']")).sendKeys("List B");
		driver.findElement(By.xpath("//input[@value='Add list']")).click();
		
		//Adding List Item
		driver.findElement(By.xpath("//h2[text()='List A']/ancestor::div[1]/following-sibling::div[2]/a[1]")).click();
		driver.findElement(By.xpath("//h2[text()='List A']/ancestor::div[1]/following-sibling::div[1]//textarea[1]")).sendKeys(date.toString());
		driver.findElement(By.xpath("//input[@value='Add card']")).click();
		
		//dragging and dropping list item from List A to List B
		Actions action = new Actions(driver);
		WebElement ele1 = driver.findElement(By.xpath("//h2[text()='List A']/ancestor::div[1]/following-sibling::div[1]/a[1]"));
		WebElement ele2 = driver.findElement(By.xpath("//h2[text()='List B']/ancestor::div[1]/following-sibling::div[2]/a[1]"));
		action.dragAndDrop(ele1, ele2).build().perform();
		
		//Logging OUT
		driver.findElement(By.xpath("//button[@data-testid= 'header-member-menu-button']")).click();
		driver.manage().timeouts().implicitlyWait(oneSecond);
		driver.findElement(By.xpath("//span[text()='Log out']")).click();
		driver.manage().timeouts().implicitlyWait(oneSecond);
		driver.findElement(By.xpath("//span[text()='Log out']")).click();
	}
}
