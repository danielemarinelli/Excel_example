package tests;

import core.ExcelUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.io.IOException;


public class reservation {

    public static void main (String[] args) throws IOException, InterruptedException {
        //WebDriverManager.firefoxdriver().setup();
        //WebDriver driver = new FirefoxDriver();
        WebDriverManager.edgedriver().setup();
        WebDriver driver = new EdgeDriver();
        //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.get("https://automationintesting.online/#/");
        driver.manage().window().maximize();

        String filePath = System.getProperty("user.dir")+"\\src\\main\\resources\\testdata\\GuestsReservations.xlsx";

        int rows = ExcelUtils.getRowCount(filePath,"Info");
        System.out.println(rows);

        for(int i=1;i<=rows;i++){
            // a) read data from Excel
            String name=ExcelUtils.getCellData(filePath,"Info",i,0);
            String email=ExcelUtils.getCellData(filePath,"Info",i,1);
            String phone=ExcelUtils.getCellData(filePath,"Info",i,2);
            String subject=ExcelUtils.getCellData(filePath,"Info",i,3);
            String messageFromGust=ExcelUtils.getCellData(filePath,"Info",i,4);

            // scroll down action
            WebElement submitButton = driver.findElement(By.xpath(".//button[@id='submitContact']"));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", submitButton);

           // b) pass data into application
            driver.findElement(By.xpath(".//input[@id='name']")).sendKeys(name);
            driver.findElement(By.xpath(".//input[@id='email']")).sendKeys(email);
            driver.findElement(By.xpath(".//input[@id='phone']")).sendKeys(phone);
            driver.findElement(By.xpath(".//input[@id='subject']")).sendKeys(subject);
            driver.findElement(By.xpath(".//textarea[@id='description']")).sendKeys(messageFromGust);

            submitButton.click();
            Thread.sleep(2000);

            // c) verification section
            String noteDisplayed = driver.findElement(By.xpath("//h2[normalize-space()='Thanks for getting in touch "+name+"!']")).getText();

            if(noteDisplayed.equals(ExcelUtils.getCellData(filePath,"Info",i,5))){
                System.out.println("Test Passed...");
                ExcelUtils.setCellData(filePath,"Info",i,6,"Reservation Passed");
                ExcelUtils.fillGreenColor(filePath,"Info",i,6);
            }else{
                System.out.println("Test Failed...");
                ExcelUtils.setCellData(filePath,"Info",i,6,"Reservation Failed");
                ExcelUtils.fillRedColor(filePath,"Info",i,6);
            }


            driver.navigate().refresh();
            Thread.sleep(2000);

        }
        System.out.println("Sent all reservations...");
        driver.close();
    }

}
