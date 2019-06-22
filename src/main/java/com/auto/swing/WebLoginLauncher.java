package com.auto.swing;

import com.auto.service.impl.WebControllerServiceImpl;

import java.io.IOException;

public class WebLoginLauncher {

    public static void main(String[] args) {
        WebControllerServiceImpl webControllerService = new WebControllerServiceImpl();
        try {
            webControllerService.startAutoLogin("shirlfairy106873@yahoo.com", "BErwkfe3624");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
