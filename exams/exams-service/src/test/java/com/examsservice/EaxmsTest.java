package com.examsservice;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

@SpringBootTest
public class EaxmsTest {
    private WebDriver driver;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "G:\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(chromeOptions);
        driver.get("http://localhost:5173/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @ParameterizedTest
    @CsvSource("登录.png")
    public void test_login(String login) throws InterruptedException {
        //1.点击并输入账号
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/form/div[2]/div/div/div/input")).sendKeys("2515100224");
        Thread.sleep(1000);
        //2.点击并输入密码
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/form/div[3]/div/div/div/input")).sendKeys("123456");
        Thread.sleep(1000);
        //3.点击登录
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/form/div[5]/div/button")).click();
        Thread.sleep(1000);
        //4.截图保存效果
        takeScreenshot(login);
    }

    //查询试题详情
    @ParameterizedTest
    @CsvSource(
            {"登录Linux 字符界面,登录Linux 字符界面题目详情.png","在稳定的直流电路中,在稳定的直流电路中题目详情.png"}

    )
    public void test_getQuestion(String question,String questionParticulars) throws InterruptedException {
        //1.点击并输入账号
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/form/div[2]/div/div/div/input")).sendKeys("2515100224");
        Thread.sleep(1000);
        //2.点击并输入密码
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/form/div[3]/div/div/div/input")).sendKeys("123456");
        Thread.sleep(1000);
        //3.点击登录
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/form/div[5]/div/button")).click();
        Thread.sleep(1000);
        //4.点击题库管理
        driver.findElement(By.xpath("/html/body/div[1]/div/section/aside/ul/li[3]")).click();
        Thread.sleep(800);
        //5.查找题目
        driver.findElement(By.xpath("/html/body/div[1]/div/section/section/main/div/div/div/div/div[1]/div[1]/div/input")).sendKeys(question);
        Thread.sleep(1000);
        //6.点击对应题目详情
        driver.findElement(By.xpath("/html/body/div[1]/div/section/section/main/div/div/div/div/div[2]/div[1]/div[3]/div/div[1]/div/table/tbody/tr/td[8]/div/temlate/button[2]/span")).click();
        Thread.sleep(1000);
        //7.截图保存效果
        takeScreenshot(questionParticulars);
    }
    //新增题目
    @ParameterizedTest
    @CsvSource(
            {"辩证唯物主义认为 认识的本质是( ),主体对客体的能动反映,主体对客体的直观反映,主体头脑中固有的,绝对观念在头脑中的显现,1,新增1.png",
                    "早期中华文化圈或文化上的早期中国开始萌芽，代表性的文化是,裴李岗文化,彭头山文化,良渚文化,红山文化,1,新增2.png"}
    )
    public void test_insert(String question,String chooseA,String chooseB,String chooseC,String chooseD,int sum,String testName) throws InterruptedException {
        //1.点击并输入账号
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/form/div[2]/div/div/div/input")).sendKeys("2515100224");
        Thread.sleep(1000);
        //2.点击并输入密码
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/form/div[3]/div/div/div/input")).sendKeys("123456");
        Thread.sleep(1000);
        //3.点击登录
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/form/div[5]/div/button")).click();
        Thread.sleep(1000);
        //4.点击题库管理
        driver.findElement(By.xpath("/html/body/div[1]/div/section/aside/ul/li[3]")).click();
        Thread.sleep(800);
        //5.点击新增
        driver.findElement(By.xpath("/html/body/div[1]/div/section/section/main/div/div/div/div/div[1]/div[3]/button[1]/span")).click();
        Thread.sleep(2000);
        //6.点击单选题
        driver.findElement(By.xpath("/html/body/div[1]/div/section/section/main/div/div/div/div[2]/div/div[1]/div/label[1]")).click();
        Thread.sleep(1000);
        //7.输入题目、选项、正确答案
        driver.findElement(By.xpath("/html/body/div[1]/div/section/section/main/div/div/div/div[2]/div/div[2]/div[1]/div/div[1]/div[2]/textarea")).sendKeys(question);
        Thread.sleep(800);
        driver.findElement(By.xpath("/html/body/div[1]/div/section/section/main/div/div/div/div[2]/div/div[2]/div[1]/div/div[2]/div[2]/div[2]/textarea")).sendKeys(chooseA);
        Thread.sleep(800);
        driver.findElement(By.xpath("/html/body/div[1]/div/section/section/main/div/div/div/div[2]/div/div[2]/div[1]/div/div[2]/div[3]/div[2]/textarea")).sendKeys(chooseB);
        Thread.sleep(800);
        driver.findElement(By.xpath("/html/body/div[1]/div/section/section/main/div/div/div/div[2]/div/div[2]/div[1]/div/div[2]/div[4]/div[2]/textarea")).sendKeys(chooseC);
        Thread.sleep(800);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 5000);");
        driver.findElement(By.xpath("/html/body/div[1]/div/section/section/main/div/div/div/div[2]/div/div[2]/div[1]/div/div[2]/div[5]/div[2]/textarea")).sendKeys(chooseD);
        Thread.sleep(800);
        String xpath = String.format("/html/body/div[1]/div/section/section/main/div/div/div/div[2]/div/div[2]/div[1]/div/div[2]/div[7]/div[2]/label[%d]", sum);
        driver.findElement(By.xpath(xpath)).click();
        Thread.sleep(800);
        driver.findElement(By.xpath("/html/body/div[1]/div/section/section/main/div/div/div/div[2]/div/div[2]/div[1]/div/div[2]/button")).click();
        Thread.sleep(1000);
        //8.截图
        takeScreenshot(testName);
    }

    //删除题目
    @ParameterizedTest
    @CsvSource(
            {"辩证唯物主义认为,认识的本质是( ),删除1.png",
                    "早期中华文化圈或文化上的早期中国开始萌芽,删除2.png"}
    )
    public void deleteQuestion(String question,String testName) throws InterruptedException {
        //1.点击并输入账号
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/form/div[2]/div/div/div/input")).sendKeys("2515100224");
        Thread.sleep(1000);
        //2.点击并输入密码
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/form/div[3]/div/div/div/input")).sendKeys("123456");
        Thread.sleep(1000);
        //3.点击登录
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/form/div[5]/div/button")).click();
        Thread.sleep(1000);
        //4.点击题库管理
        driver.findElement(By.xpath("/html/body/div[1]/div/section/aside/ul/li[3]")).click();
        Thread.sleep(800);
        //5.查找题目
        driver.findElement(By.xpath("/html/body/div[1]/div/section/section/main/div/div/div/div/div[1]/div[1]/div/input")).sendKeys(question);
        Thread.sleep(1000);
        //6.点击删除
        driver.findElement(By.xpath("/html/body/div[1]/div/section/section/main/div/div/div/div/div[2]/div[1]/div[3]/div/div[1]/div/table/tbody/tr/td[1]/div")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("/html/body/div[1]/div/section/section/main/div/div/div/div/div[1]/div[3]/button[2]/span")).click();
        Thread.sleep(1000);
        //7.截图
        takeScreenshot(testName);
    }

    @AfterEach
    public void teardown() {
        this.driver.quit();
    }
    // 截图
    private void takeScreenshot(String fileName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmssddSSS");
        String timestamp = dateFormat.format(new Date());
        String timestampedFileName = timestamp + "_" + fileName;
        File screenshotsDir = new File("screenshots");
        if (!screenshotsDir.exists()) {
            screenshotsDir.mkdirs();
        }
        String screenshotFilePath = screenshotsDir.getAbsolutePath() + File.separator + timestampedFileName;
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshotFile, new File(screenshotFilePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



