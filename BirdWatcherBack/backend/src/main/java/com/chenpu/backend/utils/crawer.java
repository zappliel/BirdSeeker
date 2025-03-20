package com.chenpu.backend.utils;

import com.chenpu.backend.domain.Bird;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.sql.Connection;
import java.time.Duration;

public class crawer {

    public static Bird getBirdSql(String input) throws Exception{
        Connection conn = JdbcUtil.getConnection();
        try {
            SslUtils.ignoreSsl();
        } catch (Exception e1) {
            throw new RuntimeException("获取SSL证书异常");
        }
        return crawBird(conn,input);
    }

    public static Bird crawBird(Connection conn, String input){
        String url = "https://www.huaniao8.com/";
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver_132.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // 开启无头模式
        options.addArguments("--disable-gpu");  // 关闭 GPU
        options.addArguments("--window-size=1920x1080");  // 设置窗口大小
        WebDriver driver = new ChromeDriver(options);
        Bird bird_info = new Bird();
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
            WebElement firstHtmlLink = driver.findElement(By.cssSelector("a[href$='.html']"));
            String href = firstHtmlLink.getAttribute("href");

            driver.get(href);
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("img.logo.regular[src='https://www.huaniao8.com/wp-content/themes/rizhuti-v2/assets/img/logo.png']")
            ));
//            String pageSource = driver.getPageSource();
//            Document document = Jsoup.parse(pageSource);
            WebElement entryContent = driver.findElement(By.cssSelector("div.entry-content.u-text-format.u-clearfix"));

            WebElement englishNameParagraph = entryContent.findElement(By.xpath(".//p[contains(text(), '英文名')]"));
            String Name = englishNameParagraph.getText();
            String[] parts = Name.split("\n");
            bird_info.setChinese(parts[0]);
            bird_info.setName(parts[1]);
            bird_info.setProtonym(parts[2]);

            WebElement imageElement = entryContent.findElement(By.tagName("img"));
            String imgSrc = imageElement.getAttribute("src");
            bird_info.setPicture(imgSrc);
            System.out.println(imgSrc);

            WebElement fourthParagraph = entryContent.findElement(By.xpath("./p[4]"));
            String paragraph = fourthParagraph.getText();
            bird_info.setIntroduce(paragraph);
            System.out.println(paragraph);

            WebElement geoParagraph = entryContent.findElement(By.xpath("./p[5]"));
            String geogra = geoParagraph.getText();
            bird_info.setGeo(geogra);
            System.out.println(geogra);

            return bird_info;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
        return null;
    }
}
