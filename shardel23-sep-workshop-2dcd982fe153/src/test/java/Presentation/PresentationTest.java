package Presentation;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class PresentationTest {

    WebDriver driver;

    public PresentationTest() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Shard\\Desktop\\Workshop\\SEP\\src\\test\\resources\\chromedriver.exe");
        driver = new ChromeDriver();
    }

    @Before
    public void setUp() {
        driver.get("http://localhost:8080/");
        //driver.manage().window().maximize();
    }

    @After
    public void tearDown() {
        driver.close();
    }

    protected void loginUser() {
        String user = "u@gmail.com";
        String password = "u";
        driver.findElement(By.name("email")).sendKeys(user);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("user")).click();
    }

    protected void loginGuest() {
        driver.findElement(By.name("guest")).click();
    }

    protected void loginAdmin() {
        String user = "a@gmail.com";
        String password = "a";
        driver.findElement(By.name("email")).sendKeys(user);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("user")).click();
    }

    protected void addChocolateToCartNormal() {
        Actions builder = new Actions(driver);
        WebElement storeOptions = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div[2]/div/ul/li[1]/a/b"));
        WebElement showAllStores = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div[2]/div/ul/li[1]/div/ul/li[1]/a"));
        builder.moveToElement(storeOptions).build().perform();
        showAllStores.click();
        WebElement store1ItemsLink = driver.findElement(By.xpath("//*[@id=\"product-table\"]/tbody/tr[2]/td[3]/a"));
        store1ItemsLink.click();
        WebElement firstItemAddToCartLink = driver.findElement(By.xpath("//*[@id=\"product-table\"]/tbody/tr[2]/td[6]/a"));
        firstItemAddToCartLink.click();
        WebElement amountTextBox = driver.findElement(By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[1]/td/input"));
        amountTextBox.sendKeys("1");
        WebElement addButton = driver.findElement(By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[2]/td/input"));
        addButton.click();
    }

}
