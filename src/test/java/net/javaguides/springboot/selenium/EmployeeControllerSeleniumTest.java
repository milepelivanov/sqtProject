package net.javaguides.springboot.selenium;


import net.javaguides.springboot.SpringbootThymeleafCrudWebAppApplication;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;


public class EmployeeControllerSeleniumTest {

    private WebDriver driver;
    private static ApplicationRunner applicationRunner;
    private static ConfigurableApplicationContext applicationContext;

    private static ApplicationArguments applicationArguments;

    @BeforeAll
    public static void setupApplicationContext() throws Exception {
        applicationArguments = new DefaultApplicationArguments(new String[]{});
        applicationRunner = args -> {
            applicationContext = SpringApplication.run(SpringbootThymeleafCrudWebAppApplication.class, args.getSourceArgs());
        };
        applicationRunner.run(applicationArguments);
    }

    @BeforeEach
    public void init() {


        System.setProperty("webdriver.gecko.driver", "./src/main/resources/driver/geckodriver.exe");
        driver = new FirefoxDriver();
    }

    @AfterEach
    public void destroy() {
        if (driver != null) {
            driver.quit();
        }

    }

    @AfterAll
    public static void closeApp() {
        if (applicationContext != null) {
            SpringApplication.exit(applicationContext, () -> 0);
        }
    }

    @Test
    public void saveEmployeeTest() throws Exception {
        driver.get("http://localhost:8080");
        List<WebElement> rows = driver.findElements(By.cssSelector("body > div > table > tbody > tr"));
        int start_number = rows.size();
        driver.findElement(By.linkText("Add Employee")).click();
        driver.findElement(By.id("firstName")).click();
        driver.findElement(By.id("firstName")).sendKeys("Test_Name");
        driver.findElement(By.id("lastName")).click();
        driver.findElement(By.id("lastName")).sendKeys("Test_LastName");
        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).sendKeys("test@gmail.com");
        driver.findElement(By.cssSelector(".btn")).click();
        rows = driver.findElements(By.cssSelector("body > div > table > tbody > tr"));

        boolean foundTest = false;

        for (WebElement row : rows) {
            WebElement tdFirstName = row.findElement(By.xpath("td[1]"));
            WebElement tdLastName = row.findElement(By.xpath("td[2]"));
            WebElement tdEmail = row.findElement(By.xpath("td[3]"));

            if (tdFirstName.getText().equals("Test_Name") && tdLastName.getText().equals("Test_LastName") && tdEmail.getText().equals("test@gmail.com")) {
                foundTest = true;
                break;
            }
        }

        assertEquals(rows.size(), start_number + 1);
        assertTrue(foundTest);

        for (WebElement row : rows) {
            WebElement first = row.findElement(By.xpath("td[1]"));
            WebElement second = row.findElement(By.xpath("td[2]"));
            WebElement third = row.findElement(By.xpath("td[3]"));
            if (first.getText().equals("Test_Name") && second.getText().equals("Test_LastName") && third.getText().equals("test@gmail.com")) {
                WebElement deleteButton = row.findElement(By.linkText("Delete"));
                deleteButton.click();
                break;
            }
        }
        driver.quit();
    }


    @Test
    public void deleteEmployeeTest() throws Exception {

        int number;
        boolean foundTest = false;
        int start_number_occurrence = 0, end_number_occurrence = 0;

        driver.get("http://localhost:8080");
        List<WebElement> rows = driver.findElements(By.cssSelector("body > div > table > tbody > tr"));
        number = rows.size();

        if (number == 0) {
            driver.findElement(By.linkText("Add Employee")).click();
            driver.findElement(By.id("firstName")).click();
            driver.findElement(By.id("firstName")).sendKeys("Test_Name");
            driver.findElement(By.id("lastName")).click();
            driver.findElement(By.id("lastName")).sendKeys("Test_LastName");
            driver.findElement(By.id("email")).click();
            driver.findElement(By.id("email")).sendKeys("test@gmail.com");
            driver.findElement(By.cssSelector(".btn")).click();
            driver.findElement(By.linkText("Delete")).click();
            rows = driver.findElements(By.cssSelector("body > div > table > tbody > tr"));


            for (WebElement row : rows) {
                WebElement tdFirstName = row.findElement(By.xpath("td[0]"));
                WebElement tdLastName = row.findElement(By.xpath("td[1]"));
                WebElement tdEmail = row.findElement(By.xpath("td[2]"));

                if (tdFirstName.getText().equals("Test_Name") && tdLastName.getText().equals("Test_LastName") && tdEmail.getText().equals("test@gmail.com")) {
                    foundTest = true;
                    break;
                }
            }
            assertEquals(rows.size(), number);
            assertFalse(foundTest);
        } else {
            String tdFirstName = "";
            String tdLastName = "";
            String tdEmail = "";
            WebElement deleteButton = driver.findElement(By.linkText("Delete"));
            if (deleteButton != null) {
                WebElement parentRow = deleteButton.findElement(By.xpath("./ancestor::tr"));

                List<WebElement> tdElements = parentRow.findElements(By.tagName("td"));

                tdFirstName = tdElements.get(0).getText();
                tdLastName = tdElements.get(1).getText();
                tdEmail = tdElements.get(2).getText();

                for (WebElement row : rows) {

                    WebElement FNameTD = row.findElement(By.xpath(".//td[1]"));
                    WebElement LNameTD = row.findElement(By.xpath(".//td[2]"));
                    WebElement EmailTD = row.findElement(By.xpath(".//td[3]"));
                    if (FNameTD.getText().equals(tdFirstName) && LNameTD.getText().equals(tdLastName) && EmailTD.getText().equals(tdEmail)) {
                        start_number_occurrence++;
                    }
                }
                deleteButton.click();

            }
            rows = driver.findElements(By.cssSelector("body > div > table > tbody > tr"));


            for (WebElement row : rows) {

                WebElement FNameTD = row.findElement(By.xpath(".//td[1]"));
                WebElement LNameTD = row.findElement(By.xpath(".//td[2]"));
                WebElement EmailTD = row.findElement(By.xpath(".//td[3]"));
                if (FNameTD.getText().equals(tdFirstName) && LNameTD.getText().equals(tdLastName) && EmailTD.getText().equals(tdEmail)) {
                    end_number_occurrence++;
                }
            }

            assertEquals(end_number_occurrence, start_number_occurrence - 1);
            assertEquals(rows.size(), number - 1);
        }


        driver.quit();
    }

    @Test
    public void editEmployeeTest() throws Exception {
        driver.get("http://localhost:8080");

        driver.findElement(By.linkText("Add Employee")).click();
        driver.findElement(By.id("firstName")).click();
        driver.findElement(By.id("firstName")).sendKeys("Test_Name");
        driver.findElement(By.id("lastName")).click();
        driver.findElement(By.id("lastName")).sendKeys("Test_LastName");
        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).sendKeys("test@gmail.com");
        driver.findElement(By.cssSelector(".btn")).click();


        List<WebElement> rows = driver.findElements(By.cssSelector("body > div > table > tbody > tr"));
        int start_number = rows.size();
        boolean foundTest = false;
        String td_FirstName = "Update_First_Name",
                td_LastName = "Update_Last_Name",
                td_Email = "Update_Email";

        WebElement updateButton = driver.findElement(By.linkText("Update"));

        updateButton.click();
        driver.findElement(By.id("firstName")).clear();
        driver.findElement(By.id("firstName")).click();
        driver.findElement(By.id("firstName")).sendKeys("Update_First_Name");

        driver.findElement(By.id("lastName")).clear();
        driver.findElement(By.id("lastName")).click();
        driver.findElement(By.id("lastName")).sendKeys("Update_Last_Name");

        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).sendKeys("Update_Email");

        driver.findElement(By.cssSelector(".btn")).click();

        rows = driver.findElements(By.cssSelector("body > div > table > tbody > tr"));

        for (WebElement row : rows) {

            WebElement FNameTD = row.findElement(By.xpath(".//td[1]"));
            WebElement LNameTD = row.findElement(By.xpath(".//td[2]"));
            WebElement EmailTD = row.findElement(By.xpath(".//td[3]"));
            if (FNameTD.getText().equals(td_FirstName) && LNameTD.getText().equals(td_LastName) && EmailTD.getText().equals(td_Email)) {
                foundTest = true;
                break;
            }
        }

        assertEquals(start_number, rows.size());
        assertTrue(foundTest);

        for (WebElement row : rows) {
            WebElement first = row.findElement(By.xpath("td[1]"));
            WebElement second = row.findElement(By.xpath("td[2]"));
            WebElement third = row.findElement(By.xpath("td[3]"));
            if (first.getText().equals("Update_First_Name") && second.getText().equals("Update_Last_Name") && third.getText().equals("Update_Email")) {
                WebElement deleteButton = row.findElement(By.linkText("Delete"));
                deleteButton.click();
                break;
            }
        }
        driver.quit();
    }


}


