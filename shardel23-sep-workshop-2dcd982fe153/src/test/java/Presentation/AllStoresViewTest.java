package Presentation;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AllStoresViewTest extends PresentationTest {

    @Before
    public void setUp() {
        super.setUp();
        super.loginUser();
        Actions builder = new Actions(driver);
        WebElement storeOptions = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div[2]/div/ul/li[1]/a/b"));
        WebElement showAllStores = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div[2]/div/ul/li[1]/div/ul/li[1]/a"));
        builder.moveToElement(storeOptions).build().perform();
        showAllStores.click();
    }

    @Test
    public void searchEmpty() {
        driver.findElement(By.xpath("//*[@id=\"search2\"]/input")).click();
        List<WebElement> rows = driver.findElements(By.xpath("//*[@id=\"product-table\"]/tbody/tr"));
        assertEquals(1, rows.size());
    }

    @Test
    public void searchChocolateMilk() {
        String itemName = "Chocolate Milk";
        driver.findElement(By.xpath("//*[@id=\"itemName\"]")).sendKeys(itemName);
        driver.findElement(By.xpath("//*[@id=\"search2\"]/input")).click();
        List<WebElement> rows = driver.findElements(By.xpath("//*[@id=\"product-table\"]/tbody/tr"));
        assertTrue(rows.size() > 1);
        WebElement nameCell = driver.findElement(By.xpath("//*[@id=\"product-table\"]/tbody/tr[2]/td[1]"));
        assertEquals(itemName, nameCell.getText());
    }

}
