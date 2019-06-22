package com.auto.service.impl;

import com.auto.common.Constants;
import com.auto.service.CCleanerService;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;

import java.net.URL;

public class CCleanerServiceImpl implements CCleanerService {
    private final String CLOSE = "./*[@Name='CCleaner - FREE FOR HOME USE']/*[@Name='CCleaner - FREE FOR HOME USE']/*[@Name='Close']";
    private final String RUN_CLEANER = "./*[@Name='CCleaner - FREE FOR HOME USE']/*[@Name='Run Cleaner']";
    private final String CUSTOM_CLEAN = "./*[@Name='CCleaner - FREE FOR HOME USE']/*[@Name='Custom Clean']";
    public static WiniumDriver ccDriver;

    public String autoCCleaner(long cleaningDuration, String cclearerPath){
        try {
            DesktopOptions option = new DesktopOptions();
            option.setApplicationPath(cclearerPath);
            ccDriver = new WiniumDriver(new URL(Constants.HTTP_LOCALHOST_9999), option);
            WebElement elementCusClean = null;
            int time = 0;
            while (time < Constants.WAITING_TIME){
                try {
                    elementCusClean = ccDriver.findElement(By.xpath(CUSTOM_CLEAN));
                }catch (NoSuchElementException e){

                }
                if (elementCusClean != null){
                    break;
                }
            }
            if (elementCusClean  == null){
                return Constants.FAIL;
            }
            elementCusClean.click();
            Thread.sleep(Constants.sleepingDuration);
            WebElement cleanElement = null;
            while (time < Constants.WAITING_TIME){
                try {
                    cleanElement = ccDriver.findElement(By.xpath(RUN_CLEANER));
                }catch (NoSuchElementException e){

                }
                if (cleanElement != null){
                    break;
                }
            }
            if (cleanElement  == null){
                return Constants.FAIL;
            }
            cleanElement.click();
            Thread.sleep(cleaningDuration * 1000);
            WebElement closeElement = ccDriver.findElement(By.xpath(CLOSE));
            closeElement.click();
        }catch (Exception e){
            e.printStackTrace();
            return Constants.SUCCESS;
        }
        return Constants.SUCCESS;
    }

}
