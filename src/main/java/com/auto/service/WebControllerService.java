package com.auto.service;

import java.io.IOException;

public interface WebControllerService {
    void startAutoLogin(String username, String password) throws IOException;
}
