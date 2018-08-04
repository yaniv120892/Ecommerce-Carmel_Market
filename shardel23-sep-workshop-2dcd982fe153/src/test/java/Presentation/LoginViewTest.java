package Presentation;

import org.junit.Test;
import org.openqa.selenium.By;

import static org.junit.Assert.assertEquals;

public class LoginViewTest extends PresentationTest{

    @Test
    public void loginTestGood() {
        String user = "u@gmail.com";
        String password = "u";
        driver.findElement(By.name("email")).sendKeys(user);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("user")).click();
        assertEquals("http://localhost:8080/MainMenu", driver.getCurrentUrl());
    }

    @Test
    public void loginTestBad() {
        String user = "badUser";
        String password = "badPassword";
        driver.findElement(By.name("email")).sendKeys(user);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("user")).click();
        assertEquals(true, driver.findElement(By.xpath("//*[@id=\"message-green\"]/table/tbody/tr/td[1]")).isDisplayed());
    }

    @Test
    public void guestLogin() {
        driver.findElement(By.xpath("//*[@id=\"myForm\"]/table/tbody/tr[3]/td/input[2]")).click();
        assertEquals("http://localhost:8080/MainMenu", driver.getCurrentUrl());
        driver.findElement(By.xpath("//*[@id=\"logout\"]/img")).click(); //logout
    }

}
