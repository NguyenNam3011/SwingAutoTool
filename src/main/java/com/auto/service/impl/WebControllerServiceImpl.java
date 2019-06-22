package com.auto.service.impl;

import com.auto.service.WebControllerService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

public class WebControllerServiceImpl implements WebControllerService {

    private static final String ACCOUNT_FILE_URL = "config/account/yahoo_account.txt";
    private static final String LOG_DIR = "D:/auto_log/";

    private static final String WEB_URL = "http://kynu.net/";
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


    public WebControllerServiceImpl() {
        //Init Web driver
        WebDriverManager.chromedriver().setup();
    }


    @Override
    public void startAutoLogin(String username, String password) throws IOException {
        ClassLoader classLoader = new WebControllerServiceImpl().getClass().getClassLoader();

        BufferedReader objReader = new BufferedReader(new FileReader(classLoader.getResource(ACCOUNT_FILE_URL).getPath()));
        String strCurrentLine;
        WebDriver driver = new ChromeDriver();
        try {
            loginWeb(driver, username, password);
            writeLog(username + " login success______!");
        } catch (Exception e) {
            e.printStackTrace();
            writeLog(username + " login fail______!");
        } finally {
            driver.close();
        }
    }

    private static void loginWeb(WebDriver driver, String user, String password) throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        System.out.println("========Login account : " + user);
        driver.get(YAHOO_WEB_URL);
//        driver.manage().window().maximize();

        //Login Yahoo
        System.out.println("========Start login to Yahoo");
        driver.findElement(By.xpath(YAHOO_XPATH_LOGIN)).click();
        driver.findElement(By.xpath(YAHOO_XPATH_USERNAME)).sendKeys(user);
        driver.findElement(By.xpath(YAHOO_XPATH_NEXT)).click();
        driver.findElement(By.xpath(YAHOO_XPATH_PASSWORD)).sendKeys(password);
        driver.findElement(By.xpath(YAHOO_XPATH_NEXT)).click();
        Thread.sleep(5000);
        System.out.println("========Finish login to Yahoo");


        //Go to Web
        System.out.println("========Start login to web");
        driver.get(WEB_URL);
        driver.findElement(By.xpath(XPATH_LOGIN)).click();
        driver.findElement(By.xpath(XPATH_LOGIN_BUTTON)).click();
        System.out.println("========Login to web success");
        driver.get(WEB_URL);
        Thread.sleep(5000);
        driver.findElement(By.xpath(XPATH_ACCOUNT_BUTTON)).click();
        driver.findElement(By.xpath(XPATH_LOGOUT_BUTTON)).click();
        System.out.println("========Logout web success");
        Thread.sleep(5000);

        //Logout Yahoo
        System.out.println("========Start logout to yahoo");
        driver.get(YAHOO_WEB_URL);
        driver.findElement(By.id("uh-profile")).click();
        driver.findElement(By.xpath(YAHOO_XPATH_LOGOUT)).click();
        Thread.sleep(5000);
        System.out.println("========Finish logout to yahoo");
        System.out.printf("========Login account : %s Finished!", user);
        System.out.printf("====================================");
    }

    private void writeLog(String log) {
        LocalDate localDate = LocalDate.now();
        try {
            File file = new File(LOG_DIR + localDate);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream writer = new FileOutputStream(LOG_DIR + localDate);
            writer.write((log).getBytes());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
