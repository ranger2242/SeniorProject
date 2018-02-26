package com.ANZR.Ergo.io;

import com.ANZR.Ergo.Ergo;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.socket.client.IO;
import io.socket.client.Socket;

import static com.ANZR.Ergo.io.Logger.slog;

import java.net.*;


public class Pipeline {

    private static Socket socket;
    private static String ip = "localhost";
    private static String port = "2242";
    private static String serverAddress = "http://" + ip + ":" + port;
    private static boolean serverIsReady = false;
    private Ergo ergo;


    enum Codes {
        CONNECTED("CONNECTED"),
        SENDCLIENT("SEND-CLIENT"),
        ID("ID"),
        SENDPYTHON("SEND-PYTHON"),
        MSG("msg");
        private final String text;

        Codes(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    enum ReceivedCodes {
        RESULTS("RESULTS"),
        RECEIVED("RECEIVED");
        private final String text;

        ReceivedCodes(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }


    /**
     * Create a new pipeline and attempt to connect to the Ergo Server
     *
     * @param ergo A reference to Ergo to return the data when the server sends the results back
     */
    public Pipeline(Ergo ergo) {
        this.ergo = ergo;

        try {
            slog("Connecting to " + serverAddress);
            connectSocket();
        } catch (URISyntaxException e) {
            slog("Failed to connect");
            e.printStackTrace();
        }
    }


    /**
     * Attempt to send message to server, will timeout after 15 seconds
     *
     * @param data The data to send to the server in JSON format
     */
    public static void sendToServer(String data) {

        for (int i = 0; i < 15; i++) {
            if (serverIsReady) {
                socket.emit(Codes.SENDPYTHON.toString(), data);
                slog("Data Sent To Server");
                return;
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    slog("Error happened while waiting for server to connect...");
                    ex.printStackTrace();
                }
            }
        }
        slog("Message to server timed out...");
    }

    private void connectSocket() throws URISyntaxException {
        socket = IO.socket(serverAddress);

        //on connect
        socket.on(io.socket.client.Socket.EVENT_CONNECT, args -> {
        });

        socket.on(Codes.CONNECTED.toString(), args -> {
            slog("Connected to server");
            serverIsReady = true;
        });

        socket.on(Codes.ID.toString(), args -> socket.emit("ID", 0));

        socket.on(Codes.SENDCLIENT.toString(), args -> {
            Gson gson = new Gson();
            JsonElement element = gson.fromJson((String) args[0], JsonElement.class);
            JsonObject jsonObj = element.getAsJsonObject();

            String key = jsonObj.keySet().iterator().next();
            JsonElement data = jsonObj.get(key);

            if (ReceivedCodes.RECEIVED.toString().equals(key)) {
                if (data.getAsString().toLowerCase().equals("true")) {
                    slog("Server Received data...");
                } else {
                    slog("Error: Server failed to received data...");
                }
            } else if (ReceivedCodes.RESULTS.toString().equals(key)) {
                ergo.interpretData(data);
                socket.disconnect();
            } else {
                slog("UNKNOWN RECEIVE CODE FOUND...");
            }
        });

        socket.on(io.socket.client.Socket.EVENT_DISCONNECT, args -> {
            slog("Disconnected from server");
            serverIsReady = false;
        });

        socket.connect();
    }

    public static void setShutdownOperations() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                slog("Disconnected from server");
                socket.disconnect();
            }
        });
    }
}
