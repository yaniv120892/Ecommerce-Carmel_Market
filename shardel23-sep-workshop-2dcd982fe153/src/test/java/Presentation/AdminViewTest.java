package Presentation;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.Assert.assertEquals;

public class AdminViewTest extends PresentationTest {

    @Before
    public void setUp() {
        super.setUp();
        super.loginAdmin();
    }

    @Test
    public void adminToolsVisible() {
        WebElement adminToolsOption = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div[2]/div/ul/li/a/b"));
        assertEquals(true, adminToolsOption.isDisplayed());
        assertEquals("Admin Tools", adminToolsOption.getText());
    }

}
