package DiscoveryGroup.DiscoveryPrjt2;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.codehaus.plexus.util.FileUtils;
import org.openqa.selenium.By;
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

public class selTest1 {
	
	public WebDriver driver = null;
	public File src = null;
	public String upath;
	public static String URL = "https://go.discovery.com/";
	public static String Video_URL = "https://go.discovery.com/my-videos";
	public Actions actions = null;

	
	//Home Page
	
	@FindBy(xpath="//span[contains(text(),'Shows')]")
	public WebElement HomePg_Show_elem;
	
	@FindBy(xpath="//div[@id='show-drop-desktop']//a[@class='dscShowsDropContent__seeAllShows'][contains(text(),'See All Shows')]")
	public WebElement HomePg_SeeAllShows_elem;
	
	@FindBy(xpath="//a[contains(@href,'apollo')]")
	public List<WebElement> HomePg_ApolloVideos_elem;
	
	@FindBy(xpath="//div[@class='showHero__showBrand showHero__showLogoNoClips']//i[contains(@class,'flipIconCore__icon icon-plus')]")
	public WebElement HomePg_ApolloAdd_elem;
	
	@FindBy(xpath="//i[contains(@class,'flipIconCore__icon icon-minus')]")
	public WebElement HomePg_ApolloRemove_elem;
	
	@FindBy(xpath="//h2[contains(text(),'Favorite Shows')]")
	public WebElement HomePg_FavShowSection_elem;
	
	@FindBy(xpath="//div[contains(text(),'Apollo: The Forgotten Films')]")
	public List<WebElement> HomePg_ApolloVideos1_elem;
	
	@FindBy(xpath="//div[contains(text(),'Confessions From Space: Apollo')]")
	public List<WebElement> HomePg_ApolloVideos2_elem;
	
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
			
			PageFactory.initElements(driver, selTest1.this);
			
			driver.manage().window().maximize();
			
			driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(120, TimeUnit.SECONDS);
			
			WebDriverWait wait = new WebDriverWait(driver,90);			
			//Wait<WebDriver> fwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(90000)).pollingEvery(Duration.ofSeconds(10000));
	
			driver.get(URL);
			
			driver.manage().window().fullscreen();
			// or we can use this url to move directly to videos section so that no switching is required : driver.get("https://go.discovery.com/my-videos");
			
			actions = new Actions(driver);			
			wait.until(ExpectedConditions.visibilityOf(HomePg_Show_elem));		
			actions.moveToElement(HomePg_Show_elem).click().build().perform();		
			wait.until(ExpectedConditions.visibilityOf(HomePg_SeeAllShows_elem));			
			actions.moveToElement(HomePg_SeeAllShows_elem).click().build().perform();
			
			int sCount = HomePg_ApolloVideos_elem.size();
			List<WebElement> apolloList1 = null;
			System.out.println("Total Apollo Elements: "+sCount);
			
			for(int i=0;i<sCount;i++) {
				apolloList1 = driver.findElements(By.xpath("//a[contains(@href,'apollo')]"));
				apolloList1.get(i).click();
				if(driver.findElements(By.xpath("//div[@class='showHero__showBrand showHero__showLogoNoClips']//i[contains(@class,'flipIconCore__icon icon-plus')]")).size()>0){
					HomePg_ApolloAdd_elem.click();
				}else {
					HomePg_ApolloRemove_elem.click();	
				}
				wait.until(ExpectedConditions.visibilityOf(HomePg_Show_elem));		
				actions.moveToElement(HomePg_Show_elem).click().build().perform();		
				wait.until(ExpectedConditions.visibilityOf(HomePg_SeeAllShows_elem));
				actions.moveToElement(HomePg_SeeAllShows_elem).click().build().perform();
				
				apolloList1=null;
			}

			
			driver.get(Video_URL);
			
			String favShowSection = HomePg_FavShowSection_elem.getText();
			
			if(favShowSection.equalsIgnoreCase("FAVORITE SHOWS")) {
				if(HomePg_ApolloVideos1_elem.size()>0){
					System.out.println("Apollo: The Forgotten Films is added properly");
				}
				
				if(HomePg_ApolloVideos2_elem.size()>0)
				{
					System.out.println("Confessions From Space: Apollo is added properly");	
				}
			}else {
				throw new Exception("Fav Show Section does not exist");
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
