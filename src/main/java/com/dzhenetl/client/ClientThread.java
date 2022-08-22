package com.dzhenetl.client;

import com.dzhenetl.util.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ClientThread extends Thread {

    private final SocketChannel socketChannel;
    private final ByteBuffer buffer;

    private final Logger logger = Logger.getInstance();

    public ClientThread(SocketChannel socketChannel, ByteBuffer buffer) {
        this.socketChannel = socketChannel;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            int bytesCount = 0;
            try {
                bytesCount = socketChannel.read(buffer);
            } catch (ClosedByInterruptException e) {
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (bytesCount == -1) continue;

            String msg = new String(buffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim();
            logger.log(msg);
            System.out.println(new String(buffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim());
            buffer.clear();
        }
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
