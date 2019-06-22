package com.auto.service;

import java.net.MalformedURLException;

public interface CCleanerService {

    String autoCCleaner(long cleaningDuration, String cclearerPath) throws MalformedURLException, InterruptedException;

}
