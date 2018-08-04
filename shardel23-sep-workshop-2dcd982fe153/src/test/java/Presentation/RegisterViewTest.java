package Presentation;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import static org.junit.Assert.assertEquals;

public class RegisterViewTest extends PresentationTest {

    @Before
    public void setUp() {
        super.setUp();
        driver.findElement(By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[4]/td/input")).click();
    }

    @Test
    public void RegisterGood() {
        String email = "registerGood@gmail.com";
        String password = "12345678";
        driver.findElement((By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[1]/td/input"))).sendKeys(email);
        driver.findElement((By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[2]/td/input"))).sendKeys(password);
        Select dropdown = new Select (driver.findElement(By.xpath("//*[@id=\"nationalities\"]")));
        dropdown.selectByIndex(1);
        driver.findElement((By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[4]/td/input"))).click();
        assertEquals("http://localhost:8080/MainMenu", driver.getCurrentUrl());
    }

    @Test
    public void RegisterBadEmpty() {
        String email = "";
        String password = "";
        driver.findElement((By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[1]/td/input"))).sendKeys(email);
        driver.findElement((By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[2]/td/input"))).sendKeys(password);
        Select dropdown = new Select (driver.findElement(By.xpath("//*[@id=\"nationalities\"]")));
        dropdown.selectByIndex(0);
        driver.findElement((By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[4]/td/input"))).click();
        assertEquals(true, driver.findElement(By.xpath("//*[@id=\"message-green\"]/table/tbody/tr/td[1]")).isDisplayed());
        assertEquals("http://localhost:8080/doRegister", driver.getCurrentUrl());
    }

    @Test
    public void RegisterBadInvalidEmail() {
        String email = "badEmail";
        String password = "12341234";
        driver.findElement((By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[1]/td/input"))).sendKeys(email);
        driver.findElement((By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[2]/td/input"))).sendKeys(password);
        Select dropdown = new Select (driver.findElement(By.xpath("//*[@id=\"nationalities\"]")));
        dropdown.selectByIndex(0);
        driver.findElement((By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[4]/td/input"))).click();
        assertEquals(true, driver.findElement(By.xpath("//*[@id=\"message-green\"]/table/tbody/tr/td[1]")).isDisplayed());
        assertEquals("http://localhost:8080/doRegister", driver.getCurrentUrl());
    }

    @Test
    public void RegisterBadInvalidPassword() {
        String email = "InvalidPassword@gmail.com";
        String password = "123412";
        driver.findElement((By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[1]/td/input"))).sendKeys(email);
        driver.findElement((By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[2]/td/input"))).sendKeys(password);
        Select dropdown = new Select (driver.findElement(By.xpath("//*[@id=\"nationalities\"]")));
        dropdown.selectByIndex(0);
        driver.findElement((By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[4]/td/input"))).click();
        assertEquals(true, driver.findElement(By.xpath("//*[@id=\"message-green\"]/table/tbody/tr/td[1]")).isDisplayed());
        assertEquals("http://localhost:8080/doRegister", driver.getCurrentUrl());
    }

    @Test
    public void RegisterBadAlreadyExists() {
        String email = "a@gmail.com";
        String password = "12341234";
        driver.findElement((By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[1]/td/input"))).sendKeys(email);
        driver.findElement((By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[2]/td/input"))).sendKeys(password);
        Select dropdown = new Select (driver.findElement(By.xpath("//*[@id=\"nationalities\"]")));
        dropdown.selectByIndex(0);
        driver.findElement((By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[4]/td/input"))).click();
        assertEquals(true, driver.findElement(By.xpath("//*[@id=\"message-green\"]/table/tbody/tr/td[1]")).isDisplayed());
        assertEquals("http://localhost:8080/doRegister", driver.getCurrentUrl());
    }

}
