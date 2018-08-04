package Presentation;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PurchasePolicyTest extends PresentationTest {

    @Before
    public void setUp() {
        super.setUp();
        super.loginUser();
        Actions builder = new Actions(driver);
        WebElement storeOptions = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div[2]/div/ul/li[1]/a/b"));
        WebElement myStores = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div[2]/div/ul/li[1]/div/ul/li[2]/a"));
        builder.moveToElement(storeOptions).build().perform();
        myStores.click();
        WebElement purchasePolicies = driver.findElement(By.xpath("//*[@id=\"product-table\"]/tbody/tr[2]/td[9]/a"));
        purchasePolicies.click();
    }

    @Test
    public void showRulesChocolateMilkNoRules() {
        WebElement showRulesChocolateMilk = driver.findElement(By.xpath("//*[@id=\"items-purchase-policies-table\"]/tbody[2]/tr[3]/td[2]/a"));
        showRulesChocolateMilk.click();
        WebElement error = driver.findElement(By.xpath("//*[@id=\"message-green\"]/table/tbody/tr/td[1]"));
        String expectedError = "the item Chocolate Milk doesn't have any purchase rule";
        assertEquals(expectedError, error.getText());
    }

    @Test
    public void addRuleDarkChocolateNoRules() {
        WebElement addRuleDarkChocolate = driver.findElement(By.xpath("//*[@id=\"items-purchase-policies-table\"]/tbody[2]/tr[2]/td[3]/a"));
        addRuleDarkChocolate.click();
        WebElement conditionTextBox = driver.findElement(By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[3]/td/input"));
        conditionTextBox.sendKeys("10");
        WebElement addButton = driver.findElement(By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[4]/td/input"));
        addButton.click();
        WebElement message = driver.findElement(By.xpath("//*[@id=\"message-green\"]/table/tbody/tr/td[1]"));
        String expectedError = "Rule was added successfully";
        assertEquals(expectedError, message.getText());
    }

}
