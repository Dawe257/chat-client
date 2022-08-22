package com.dzhenetl;

import com.dzhenetl.client.Client;

import java.io.IOException;

public class ApplicationClient {

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.start();
    }
}
