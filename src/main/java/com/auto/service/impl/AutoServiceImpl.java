package com.auto.service.impl;

import com.auto.common.Constants;
import com.auto.service.AutoService;
import com.auto.service.CCleanerService;
import com.auto.service.HostPostShieldService;
import com.auto.service.WebControllerService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;

public class AutoServiceImpl implements AutoService {


    public String autoLogin(String path) {
        CCleanerService cCleanerService = new CCleanerServiceImpl();
        HostPostShieldService hostPostShieldService = new HostPostShieldServiceImpl();
        WebControllerService webControllerService = new WebControllerServiceImpl();
        ClassLoader classLoader = new WebControllerServiceImpl().getClass().getClassLoader();
        String pathDriver = classLoader.getResource("config/driver/Winium.Desktop.Driver.exe").getPath();
        File file = new File(pathDriver);
        if (! file.exists()) {
            throw new IllegalArgumentException("The file " + pathDriver + " does not exist");
        }

        Process p = null;
        try {
            p = Runtime.getRuntime().exec(file.getAbsolutePath());
        } catch (Exception e) {
            return "Start driver error: " + e.getMessage();
        }
        try {
            BufferedReader objReader = new BufferedReader(new FileReader(path));
            String strCurrentLine;
            String runHostPostResult = hostPostShieldService.runHostPostShield(Constants.HSSCP_PATH);
            if(!Constants.SUCCESS.equals(runHostPostResult)){
                return "runHostPostResult error: " + runHostPostResult;
            }
            while ((strCurrentLine = objReader.readLine()) != null) {
                String[] accountInfo = strCurrentLine.split("\\t");
                System.out.println("Start Hostspost!");

                if (!Constants.SUCCESS.equals(hostPostShieldService.startHostPostShield())) {
                    System.out.println("Start Hostspost Fail!");
                    continue;
                }
                System.out.println("Start Hostspost Success!");

                System.out.println("Start Login web");
                webControllerService.startAutoLogin(accountInfo[0], accountInfo[1]);
                System.out.println("Login web success!");

                Thread.sleep(Constants.sleepingDuration);
                //TODO web handler
                if (!Constants.SUCCESS.equals(hostPostShieldService.stopHostPostShield())) {
                    System.out.println("Stop Hostspost fail!");
                    continue;
                }
                System.out.println("Stop Hostspost Success!");
                if (!Constants.SUCCESS.equals(cCleanerService.autoCCleaner(Constants.CLEANING_DURATION, Constants.CCLEANER_PATH))) {
                    break;
                }
                Thread.sleep(Constants.sleepingDuration);
            }
            String closeHostPostResult = hostPostShieldService.closeHostPostShield();
            if(!Constants.SUCCESS.equals(closeHostPostResult)){
                return "closeHostPostResult error: " + closeHostPostResult;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Process fail: " +  e.getMessage();
        }finally {
            p.destroy();
        }
        return Constants.SUCCESS;
    }
}
