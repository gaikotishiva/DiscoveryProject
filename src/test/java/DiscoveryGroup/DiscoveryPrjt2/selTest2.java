package DiscoveryGroup.DiscoveryPrjt2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.codehaus.plexus.util.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.annotations.Test;


/** 
 * @author Siva Gaikoti
 * @category Selenium Automation
 * @version 1.0
 * Description: Automation of Discover website and adding the videos to favorite list
 * Conventions: None
 *
 **/

public class selTest2 {
	
	public WebDriver driver = null;
	public File src = null;
	public String upath;
	public static String URL = "https://go.discovery.com/";

	//Home Page
	
	@FindBy(xpath="//div[@class='carousel__arrowWrapper popularShowsCarousel__controlsProp']//i[@class='icon-arrow-right']")
	public WebElement HomePg_RightArrow_elem;
	
	@FindBy(xpath="//a[@href='/tv-shows/moonshiners/']//button[contains(text(),'Explore the Show')]")
	public WebElement HomePg_ExploreTheShow_elem;
	
	@FindBy(xpath="//button[@class='episodeList__showMoreButton']")
	public WebElement HomePg_ShowMore_elem;
	
	@FindBy(xpath="//li[@class='episodeVideoTile__wrapper  ']")
	public List<WebElement> HomePg_EpisodeTitle_elem;
	
	@Parameters("browser")
	@Test
	public void test1(@Optional("chrome") String browser) throws IOException {
		
		try {
			
			upath = System.getProperty("user.dir");
			
			if(browser.equalsIgnoreCase("chrome")) {
				System.setProperty("webdriver.chrome.driver", upath+"/Resources/chromedriver_v66");
				//WebDriverManager.chromedriver().setup();				
				driver = new ChromeDriver();
			}else {
				throw new Exception("Browser should be chrome or firefox but provided as "+browser);
			}
			
			PageFactory.initElements(driver, selTest2.this);
			
			driver.manage().window().maximize();
			
			driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);
			
			WebDriverWait wait = new WebDriverWait(driver,90);			
			//Wait<WebDriver> fwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(90000)).pollingEvery(Duration.ofSeconds(10000));
	
			driver.get(URL);
			
			//driver.manage().window().fullscreen();
			// or we can use this url to move directly to videos section so that no switching is required : driver.get("https://go.discovery.com/my-videos");
			
			JavascriptExecutor js = (JavascriptExecutor)driver;
			//WebElement elem = driver.findElement(By.xpath("//h2[@class='popularShowsCarousel__header']"));
			js.executeScript("window.scrollBy(0,1000)");
			//js.executeScript("arguments[0].scrollIntoView();", elem);
				
			while(driver.findElements(By.xpath("//div[@class='carousel__arrowWrapper popularShowsCarousel__controlsProp']//i[@class='icon-arrow-right']")).size()>0) {
				wait.until(ExpectedConditions.visibilityOf(HomePg_RightArrow_elem));
				HomePg_RightArrow_elem.click();								
			}
			
			wait.until(ExpectedConditions.visibilityOf(HomePg_ExploreTheShow_elem));			
			HomePg_ExploreTheShow_elem.click();
			
			wait.until(ExpectedConditions.visibilityOf(HomePg_ShowMore_elem));			
			HomePg_ShowMore_elem.click();			
			wait.until(ExpectedConditions.visibilityOf(HomePg_ShowMore_elem));			
			HomePg_ShowMore_elem.click();
			
			int count = HomePg_EpisodeTitle_elem.size();			
			System.out.println("Elements count: "+count);
			
			HashMap<String, String> hm = new HashMap<>();
			Thread.sleep(2000);
			for(int i=1;i<=count;i++) {
				hm.put(driver.findElement(By.xpath("//li["+i+"]//p[@class='episodeTitle']")).getText(),driver.findElement(By.xpath("//li["+i+"]//p[@class='minutes']")).getText());
			}
			
			System.out.println("Hashmap size: "+hm.size() );			
			for(Entry<String, String> e : hm.entrySet()) {
				System.out.println("Title: "+e.getKey()+" ,  Duration: "+e.getValue());
			}
				
			driver.quit();
			System.out.println("Test Case Passed");
			
		}catch(Exception e) {
			System.out.println("Test Case Failed due to Exception : "+e.getMessage());
			
			if(driver!=null) {
				src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(src, new File(upath+"/Screenshots/"+System.currentTimeMillis()+".png"));
				driver.quit();
			}
		}
		
	}

}

