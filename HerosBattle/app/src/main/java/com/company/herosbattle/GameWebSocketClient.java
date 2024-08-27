package com.company.herosbattle;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class GameWebSocketClient extends WebSocketClient {
    public GameWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        // Connection Successful
        System.out.println("Connected to server");
    }

    @Override
    public void onMessage(String message) {
        // Receive a message
        System.out.println("Received message: " + message);

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

        // Connection closed
        System.out.println("Disconnected from server: " + reason);

    }

    @Override
    public void onError(Exception ex) {
        // Error occurred
        ex.printStackTrace();

    }
}
