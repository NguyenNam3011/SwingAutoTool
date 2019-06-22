package com.auto.service;

import java.net.MalformedURLException;

public interface HostPostShieldService {
    String runHostPostShield(String hsscpPath) throws MalformedURLException, InterruptedException;
    String startHostPostShield() throws MalformedURLException, InterruptedException;
    String stopHostPostShield() throws MalformedURLException, InterruptedException;
    String closeHostPostShield() throws MalformedURLException, InterruptedException;

}
