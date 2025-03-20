package com.chenpu.backend.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.sql.Connection;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class KeyWordCrawer {

    public static List<String> getBirdList(String input) throws Exception{
        Connection conn = JdbcUtil.getConnection();
        try {
            SslUtils.ignoreSsl();
        } catch (Exception e1) {
            throw new RuntimeException("获取SSL证书异常");
        }
        return search(conn,input);
    }

    public static List<String> search(Connection conn, String input){
        String url = "https://www.huaniao8.com/";
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver_132.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // 开启无头模式
        options.addArguments("--disable-gpu");  // 关闭 GPU
        options.addArguments("--window-size=1920x1080");  // 设置窗口大小
        WebDriver driver = new ChromeDriver(options);
        List<String> answer = new ArrayList<>();
        try {
            driver.get(url);
            int waitTimeInSeconds = 10;
            Duration duration = Duration.ofSeconds(waitTimeInSeconds);
            WebDriverWait wait = new WebDriverWait(driver, duration);

            WebElement birdNameInput = driver.findElement(By.xpath("/html/body/div[1]/main/div[1]/div/div/div[2]/div[1]/form/div[2]/input"));
            WebElement searchButton = driver.findElement(By.xpath("/html/body/div[1]/main/div[1]/div/div/div[2]/div[1]/form/div[2]/button"));

            birdNameInput.sendKeys(input);
            searchButton.click();

            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("img.logo.regular[src='https://www.huaniao8.com/wp-content/themes/rizhuti-v2/assets/img/logo.png']")
            ));
            WebElement divElement = driver.findElement(By.className("posts-wrapper"));
            List<WebElement> columnDivs = divElement.findElements(By.className("col-lg-5ths"));
            for (WebElement columnDiv : columnDivs) {
                WebElement linkElement = columnDiv.findElement(By.xpath(".//header//h2/a"));
                answer.add(linkElement.getText());
                System.out.println(linkElement.getText());
            }
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
        return null;
    }
}
