package Presentation;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import static org.junit.Assert.assertEquals;

public class CheckoutViewTest extends PresentationTest{

    @Before
    public void setUp() {
        super.setUp();
        super.loginGuest();
    }

    @Test
    public void emptyCart() {
        WebElement checkout = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div[2]/div/ul/li[3]/a/b"));
        checkout.click();
        assertEquals("http://localhost:8080/MainMenu", driver.getCurrentUrl());
    }

    @Test
    public void badNoInputs() {
        addChocolateToCartNormal();
        WebElement checkout = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div[2]/div/ul/li[3]/a/b"));
        checkout.click();
        WebElement payButton = driver.findElement(By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[8]/td/input"));
        payButton.click();
        //TODO: check for errors on screen
    }

    @Test
    public void goodCheckout() {
        addChocolateToCartNormal();
        WebElement checkout = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div[2]/div/ul/li[3]/a/b"));
        checkout.click();
        WebElement creditNumberTextbox = driver.findElement(By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[1]/td/input"));
        creditNumberTextbox.sendKeys("1234567812345678");
        WebElement cvvCodeTextbox = driver.findElement(By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[3]/td/input"));
        cvvCodeTextbox.sendKeys("123");
        WebElement nameTextbox = driver.findElement(By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[4]/td/input"));
        nameTextbox.sendKeys("Username");
        WebElement idTextbox = driver.findElement(By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[5]/td/input"));
        idTextbox.sendKeys("123456789");
        WebElement addressTextbox = driver.findElement(By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[6]/td/input"));
        addressTextbox.sendKeys("TheStreet");
        WebElement payButton = driver.findElement(By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[8]/td/input"));
        payButton.click();
        WebElement notification = driver.findElement(By.xpath("//*[@id=\"message-green\"]/table/tbody/tr/td[1]"));
        assertEquals(true, notification.isDisplayed());
        assertEquals("payment process has been successfully completed", notification.getText());
    }

}
