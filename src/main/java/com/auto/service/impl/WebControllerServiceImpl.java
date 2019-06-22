package com.auto.service.impl;

import com.auto.common.Constants;
import com.auto.service.WebControllerService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.*;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

public class WebControllerServiceImpl implements WebControllerService {

    private static final String XPATH_LOGIN = "/html/body/div[1]/div[1]/div/div/div/span/span/a[1]/span";
    private static final String XPATH_LOGIN_BUTTON = "//*[@id=\"y_auth\"]";
    private static final String XPATH_ACCOUNT_BUTTON = "/html/body/div[1]/div[1]/div/div/div/div";
    private static final String XPATH_LOGOUT_BUTTON = "/html/body/div[1]/div[1]/div/div/div/div/div/div[2]/div/div/span/a[5]";


    private static final String YAHOO_WEB_URL = "https://vn.yahoo.com/?p=us";
    private static final String YAHOO_XPATH_LOGIN = "//*[@id=\"uh-signin\"]";
    private static final String YAHOO_XPATH_USERNAME = "//*[@id=\"login-username\"]";
    private static final String YAHOO_XPATH_PASSWORD = "//*[@id=\"login-passwd\"]";
    private static final String YAHOO_XPATH_NEXT = "//*[@id=\"login-signin\"]";
    private static final String YAHOO_XPATH_LOGOUT = "//*[@id=\"uh-signout\"]";
    private static final String YAHOO_XPATH_AVATAR = "//*[@id=\"uh-avatar\"]";


    public WebControllerServiceImpl() {
        //Init Web driver
        WebDriverManager.chromedriver().setup();
    }


    @Override
    public void startAutoLogin(String username, String password) throws IOException {
        ClassLoader classLoader = new WebControllerServiceImpl().getClass().getClassLoader();
        ChromeOptions options = new ChromeOptions();
        options.addExtensions(new File(classLoader.getResource("config/driver/Adblock-Plus-free-ad-blocker_v3.5.2.crx").getPath()));

        WebDriver driver = new ChromeDriver(options);
        try {
            loginWeb(driver, username, password);
            writeLog(username + " login success______!");
        } catch (Exception e) {
            e.printStackTrace();
            writeLog(username + " login fail______!");
        } finally {
            driver.close();
            driver.quit();
        }
    }

    private static void loginWeb(WebDriver driver, String user, String password) throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        System.out.println("========Login account : " + user);
        driver.get(YAHOO_WEB_URL);

        //Login Yahoo
        System.out.println("========Start login to Yahoo");
        driver.findElement(By.xpath(YAHOO_XPATH_LOGIN)).click();
        driver.findElement(By.xpath(YAHOO_XPATH_USERNAME)).sendKeys(user);
        WebElement webElement = driver.findElement(By.xpath(YAHOO_XPATH_NEXT));
        webElement.click();
        driver.findElement(By.xpath(YAHOO_XPATH_PASSWORD)).sendKeys(password);
        webElement = driver.findElement(By.xpath(YAHOO_XPATH_NEXT));
        Thread.sleep(Constants.sleepingDuration);
        webElement.click();
        Thread.sleep(Constants.sleepingDuration);
        System.out.println("========Finish login to Yahoo");
        driver.manage().window().maximize();


//        //Go to Web
//        System.out.println("========Start login to web");
//        driver.get(Constants.WEB_URL);
//        driver.findElement(By.xpath(XPATH_LOGIN)).click();
//        driver.findElement(By.xpath(XPATH_LOGIN_BUTTON)).click();
//        System.out.println("========Login to web success");
//        driver.get(Constants.WEB_URL);
//        Thread.sleep(Constants.sleepingDuration *5);
//        driver.findElement(By.xpath(XPATH_ACCOUNT_BUTTON)).click();
//        driver.findElement(By.xpath(XPATH_LOGOUT_BUTTON)).click();
//        System.out.println("========Logout web success");
//        Thread.sleep(Constants.sleepingDuration * 5);

        //Logout Yahoo
        System.out.println("========Start logout to yahoo");
        driver.get(YAHOO_WEB_URL);
        Thread.sleep(Constants.sleepingDuration);
        driver.findElement(By.xpath(YAHOO_XPATH_AVATAR)).click();
        Thread.sleep(Constants.sleepingDuration);
        driver.findElement(By.xpath(YAHOO_XPATH_LOGOUT)).click();

        Thread.sleep(Constants.sleepingDuration);
        System.out.println("========Finish logout to yahoo");
        System.out.printf("========Login account : %s Finished!", user);
        System.out.printf("====================================");
    }

    private void writeLog(String log) {
        LocalDate localDate = LocalDate.now();
        try {
            File file = new File(Constants.LOG_DIR + localDate);
            if (!file.exists()) {
                File directory = new File(Constants.LOG_DIR);
                if (!directory.exists()) {
                    directory.mkdir();
                    file.getParentFile().mkdir();
                    file.createNewFile();
                } else {
                    file.createNewFile();
                }
            }

            FileOutputStream writer = new FileOutputStream(Constants.LOG_DIR + localDate);
            writer.write((log).getBytes());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
