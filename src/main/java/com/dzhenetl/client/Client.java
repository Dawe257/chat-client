package com.dzhenetl.client;

import com.dzhenetl.util.Logger;
import com.dzhenetl.util.PropertiesUtil;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    private final InetSocketAddress socketAddress = new InetSocketAddress(
            PropertiesUtil.get("host"),
            Integer.parseInt(PropertiesUtil.get("port")));

    private SocketChannel socketChannel;

    private final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

    public void start() throws IOException {

        socketChannel = SocketChannel.open();
        socketChannel.connect(socketAddress);

        try (Scanner scanner = new Scanner(System.in)) {
            // Печатаем требование ввести имя
            receiveMessage();
            // Указываем и отправляем на сервер свое имя
            String name = scanner.nextLine();
            socketChannel.write(ByteBuffer.wrap(name.getBytes(StandardCharsets.UTF_8)));
            // Печатаем приветственное сообщение
            receiveMessage();

            // Делегируем прием и вывод новых сообщений отдельному потоку, чтобы вывод не блокировался вводом
            ClientThread clientThread = new ClientThread(socketChannel, inputBuffer);
            clientThread.start();

            String msg;
            while (true) {
                msg = scanner.nextLine();
                if ("/exit".equals(msg)) {
                    clientThread.interrupt();
                    break;
                }

                sendMessage(msg);
            }
        } finally {
            socketChannel.close();
        }
    }

    public void receiveMessage() throws IOException {
        int bytesCount = socketChannel.read(inputBuffer);
        System.out.println(new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim());
        inputBuffer.clear();
    }

    public void sendMessage(String msg) throws IOException {
        socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
    }
}
