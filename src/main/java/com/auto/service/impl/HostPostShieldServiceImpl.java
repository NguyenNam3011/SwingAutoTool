package com.auto.service.impl;

import com.auto.common.Constants;
import com.auto.service.HostPostShieldService;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;

import java.net.URL;

public class HostPostShieldServiceImpl implements HostPostShieldService {
    public static final String WINDOW_TITLE_BAR_NAME_CLOSE = "./*[@Name='HotspotShield']/*[@Name='WindowTitleBar']/*[@Name='close']";
    private final String DISCONNECT_BUTTON = "./*[@Name='HotspotShield']/*[@Name='Shell']/*[@Name='Main']/*[@AutomationId='disconnectButton']";
    private final String CONNECTION_BUTTON = "./*[@Name='HotspotShield']/*[@Name='Shell']/*[@Name='Main']/*[@AutomationId='connectionButton']";
    public static WiniumDriver hpsDriver;
    public static String hsscpPath = null;

    public String runHostPostShield(String hsscpPath) {
        try {
            this.hsscpPath = hsscpPath;
            DesktopOptions option = new DesktopOptions();
            option.setApplicationPath(hsscpPath);
            hpsDriver = new WiniumDriver(new URL(Constants.HTTP_LOCALHOST_9999), option);
            Thread.sleep(Constants.sleepingDuration * 3);
            WebElement stopElement = null;
            WebElement startElement = null;
            if (checkStart(startElement)) return Constants.SUCCESS;
            try {
                System.out.println("Disconnect Hostspost!");
                stopElement = hpsDriver.findElement(By.xpath(DISCONNECT_BUTTON));
            } catch (Exception e) {

                e.printStackTrace();
            }
            System.out.println("Disconnect Hostspost stop!");
            if (stopElement != null) {
                stopElement.click();
                Thread.sleep(Constants.sleepingDuration);
            }
            System.out.println("Connect Hostspost!");
            if (checkStart(startElement)) return Constants.SUCCESS;
            return Constants.FAIL;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }


    }

    private boolean checkStart(WebElement startElement) throws InterruptedException {
        int time = 0;
        while (time < Constants.WAITING_TIME) {
            try {
                startElement = hpsDriver.findElement(By.xpath(CONNECTION_BUTTON));
            } catch (NoSuchElementException nse) {
                nse.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (startElement != null) {
                return true;
            }
            Thread.sleep(Constants.sleepingDuration);
            time++;
        }
        return false;
    }

    public String startHostPostShield() {
        try {
            WebElement startElement = hpsDriver.findElement(By.xpath(CONNECTION_BUTTON));
            startElement.click();
            int time = 0;
            WebElement stopElement = null;
            while (time < Constants.WAITING_TIME) {
                try {
                    stopElement = hpsDriver.findElement(By.xpath(DISCONNECT_BUTTON));
                } catch (NoSuchElementException nse) {
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (stopElement != null) {
                    return Constants.SUCCESS;
                }
                Thread.sleep(Constants.sleepingDuration);
            }
        } catch (NoSuchElementException e) {
            return e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            runHostPostShield(hsscpPath);
            return e.getMessage();
        }
        return Constants.SUCCESS;
    }


    public String stopHostPostShield() {
        try {

            System.out.println("Stop Hostspost start!");
            WebElement stopElement = hpsDriver.findElement(By.xpath(DISCONNECT_BUTTON));
            Thread.sleep(Constants.sleepingDuration);
            stopElement.click();
            Thread.sleep(Constants.sleepingDuration);
            int time = 0;
            WebElement startElement = null;
            while (time < Constants.WAITING_TIME) {
                try {
                    startElement = hpsDriver.findElement(By.xpath(CONNECTION_BUTTON));
                } catch (NoSuchElementException nse) {
                    System.out.println(time);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (startElement != null) {
                    return Constants.SUCCESS;
                }
                Thread.sleep(Constants.sleepingDuration);
            }
            return Constants.FAIL;
        } catch (Exception e) {
            e.printStackTrace();
            runHostPostShield(hsscpPath);
            return e.getMessage();
        }
    }


    public String closeHostPostShield() {
        WebElement stopElement = null;
        try {
            stopElement = hpsDriver.findElement(By.xpath(DISCONNECT_BUTTON));
        } catch (NoSuchElementException nse) {

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (stopElement != null) {
            stopHostPostShield();
        }
        try {
            WebElement closeElement = hpsDriver.findElement(By.xpath(WINDOW_TITLE_BAR_NAME_CLOSE));
            closeElement.click();
        } catch (NoSuchElementException e) {
            return Constants.FAIL;
        } catch (Exception e) {
            e.printStackTrace();
            return Constants.FAIL;
        }
        return Constants.SUCCESS;
    }


}
